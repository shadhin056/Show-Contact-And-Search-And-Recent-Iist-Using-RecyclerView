package com.example.contactlistshowandsearchinlistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Contact> contactList;
    private List<Contact> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone,count,tvTitle;
        public ImageView thumbnail;
        public RelativeLayout rl;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);
            count = view.findViewById(R.id.count);
            tvTitle = view.findViewById(R.id.tvTitle);
            rl = view.findViewById(R.id.rl);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Contact contact = contactListFiltered.get(position);
        if(!contact.getName().equals("all") && !contact.getName().equals("recent")){
            holder.tvTitle.setVisibility(View.GONE);
           /* holder.name.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.VISIBLE);
            holder.count.setVisibility(View.VISIBLE);
            holder.thumbnail.setVisibility(View.VISIBLE);*/
            holder.rl.setVisibility(View.VISIBLE);


            holder.name.setText(contact.getName());
            holder.phone.setText(contact.getPhone());
            holder.count.setBackgroundResource(0);
            if ((contact.getImage()==null)||contact.getImage().equals("")){
                holder.thumbnail.setImageResource(R.drawable.avater);
            }else {
                Glide.with(context)
                        .load(contact.getImage())
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.thumbnail);
            }
        }
       else if(contact.getName().equals("all")){
            holder.tvTitle.setVisibility(View.VISIBLE);
            /*holder.name.setVisibility(View.GONE);
            holder.phone.setVisibility(View.GONE);
            holder.count.setVisibility(View.GONE);
            holder.thumbnail.setVisibility(View.GONE);*/
            holder.rl.setVisibility(View.GONE);
            holder.tvTitle.setText("All Contact");
        }else if(contact.getName().equals("recent")){
           /* holder.name.setVisibility(View.GONE);
            holder.phone.setVisibility(View.GONE);
            holder.count.setVisibility(View.GONE);
            holder.thumbnail.setVisibility(View.GONE);*/
            holder.rl.setVisibility(View.GONE);
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText("Recent Contact");
        }

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);
    }

}
