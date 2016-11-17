package com.example.dhirenchandnani.fuelo;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.dhirenchandnani.fuelo.R.id.button;

/**
 * Created by kunal4 on 11/5/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<ListItem> items;
    Context mcon;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView1, mTextView2, mTextView3;
        public Button mButton_points;
        public ViewHolder(View v) {
            super(v);
            // Find the TextView in the LinearLayout
            mTextView1 = (TextView)v.findViewById(R.id.card_view_id);
            mTextView2 = (TextView)v.findViewById(R.id.card_view_name);
            mTextView3 = (TextView)v.findViewById(R.id.card_view_title);
            mButton_points = (Button)v.findViewById(R.id.button_points);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context con, String[] name, String[] title, String[] id, String[] point, String[] turl) {
        super();
        items = new ArrayList<ListItem>();
        mcon = con;
        for(int i =0; i < (name.length); i++){
            ListItem item = new ListItem();
            item.setName(name[i]);
            item.setTitle(title[i]);
            item.setId(id[i]);
            item.setPoint(point[i]);
            item.setTUrl(turl[i]);
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
        final ListItem list = items.get(position);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextView1.setText(list.getId());
        holder.mTextView2.setText(list.getName());
        holder.mTextView3.setText(list.getTitle());
        holder.mButton_points.setText(list.getPoint());

        holder.mButton_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcon,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alertDialogBuilder.setMessage("This coupon costs "+list.getPoint()+" FueloPoints. Continue?");
                        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(mcon,WebViewActivity.class);
                                        intent.putExtra("TURL",list.getTUrl());
                                        Log.d("ABCD",list.getTUrl());
                                        mcon.startActivity(intent);
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }


            });




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}