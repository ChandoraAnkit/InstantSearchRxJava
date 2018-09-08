package com.chandora.androidy.instantsearchrxjava.view;

import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.chandora.androidy.instantsearchrxjava.R;
import com.chandora.androidy.instantsearchrxjava.adapter.ContactsAdapterFilterable;
import com.chandora.androidy.instantsearchrxjava.network.ApiClient;
import com.chandora.androidy.instantsearchrxjava.network.ApiService;
import com.chandora.androidy.instantsearchrxjava.network.model.Contact;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LocalSearchActivity extends AppCompatActivity implements ContactsAdapterFilterable.OnClickContactListener {

    private final static String TAG = LocalSearchActivity.class.getSimpleName();

    private CompositeDisposable mCompositeDisposable;
    private ApiService mApiService;
    private ContactsAdapterFilterable mAdapter;
    private ArrayList<Contact> contactsList = new ArrayList<>();

    @BindView(R.id.input_search)
    EditText inputSearchEd;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);

        unbinder = ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();

        Toolbar  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAdapter = new ContactsAdapterFilterable(this,contactsList,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        whiteNotificationBar(recyclerView);

        mApiService = ApiClient.getClient().create(ApiService.class);

        mCompositeDisposable.add(RxTextView.textChangeEvents(inputSearchEd)
        .skipInitialValue()
        .debounce(300, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(searchContacts()));

        fetchContacts("gmail");

    }

    private void fetchContacts(String source) {

        mCompositeDisposable.add(mApiService.getContacts(source,null)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSingleObserver<List<Contact>>(){

            @Override
            public void onSuccess(List<Contact> contacts) {

                contactsList.clear();
                contactsList.addAll(contacts);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }
        }));
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        unbinder.unbind();
        super.onDestroy();
    }

    private DisposableObserver<TextViewTextChangeEvent> searchContacts() {

        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {

                mAdapter.getFilter().filter(textViewTextChangeEvent.text());
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };
    }

    private void whiteNotificationBar(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    @Override
    public void onContactSelected(Contact contact) {
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
