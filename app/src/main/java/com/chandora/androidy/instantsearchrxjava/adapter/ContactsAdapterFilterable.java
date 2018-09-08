package com.chandora.androidy.instantsearchrxjava.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chandora.androidy.instantsearchrxjava.R;
import com.chandora.androidy.instantsearchrxjava.network.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapterFilterable extends RecyclerView.Adapter<ContactsAdapterFilterable.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Contact> contactsList;
    private ArrayList<Contact> filterdContactsList;

    private OnClickContactListener onClickContactListener;


    public ContactsAdapterFilterable(Context context, ArrayList<Contact> contactsList, OnClickContactListener onClickContactListener) {

        this.context = context;
        this.contactsList = contactsList;
        this.filterdContactsList = contactsList;
        this.onClickContactListener = onClickContactListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {

        holder.bind(filterdContactsList.get(pos));
    }

    @Override
    public int getItemCount() {

        return filterdContactsList == null ? 0 : filterdContactsList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String query = charSequence.toString();

                if (query.isEmpty()){
                    filterdContactsList = contactsList;
                }else {

                    ArrayList<Contact> filteredList = new ArrayList<>();

                    for (Contact row : contactsList){

                        if (row.getName().toLowerCase().contains(query.toLowerCase()) || row.getPhone().contains(query)){
                            filteredList.add(row);
                        }

                    }

                    filterdContactsList = filteredList;
                }

                FilterResults results = new FilterResults();
                results.values = filterdContactsList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filterdContactsList = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        } ;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name, phone;
        private ImageView thumbnail;

        public MyViewHolder(@NonNull View view) {
            super(view);

            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickContactListener.onContactSelected(filterdContactsList.get(getAdapterPosition()));
        }

        public void bind(Contact contact){

            name.setText(contact.getName());
            phone.setText(contact.getPhone());

            Glide.with(context)
                    .load(contact.getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail);

        }
    }

    public interface OnClickContactListener {
        void onContactSelected(Contact contact);
    }
}
