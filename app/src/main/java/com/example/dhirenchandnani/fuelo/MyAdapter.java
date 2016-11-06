package com.example.dhirenchandnani.fuelo;

import android.app.LauncherActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal4 on 11/5/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<ListItem> items;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView1, mTextView2, mTextView3;
        public ViewHolder(View v) {
            super(v);
            // Find the TextView in the LinearLayout
            mTextView1 = (TextView)v.findViewById(R.id.card_view_id);
            mTextView2 = (TextView)v.findViewById(R.id.card_view_name);
            mTextView3 = (TextView)v.findViewById(R.id.card_view_payout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] name, String[] payout, String[] id) {
        super();
        items = new ArrayList<ListItem>();
        for(int i =0; i < (name.length); i++){
            ListItem item = new ListItem();
            item.setName(name[i]);
            item.setPayout(payout[i]);
            item.setId(id[i]);
            items.add(item);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // Give the view as it is
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem list = items.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextView1.setText(list.getId());
        holder.mTextView2.setText(list.getName());
        holder.mTextView3.setText(list.getPayout());



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}