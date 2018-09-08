package com.chandora.androidy.instantsearchrxjava.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chandora.androidy.instantsearchrxjava.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        whiteNotificationBar(toolbar);
    }



    @OnClick(R.id.btn_local_search)
    public void onLocalSearch(){
        startActivity(new Intent(this,LocalSearchActivity.class));
    }

    @OnClick(R.id.btn_remote_search)
    public void onRemoteSearch(){
        startActivity(new Intent(this,RemoteSearchActivity.class));
    }

    private void whiteNotificationBar(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            int flags  = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);

            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
}
