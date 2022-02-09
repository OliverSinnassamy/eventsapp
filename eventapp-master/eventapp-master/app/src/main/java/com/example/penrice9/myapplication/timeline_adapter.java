package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Penrice9 on 09/10/2018.
 */

public class timeline_adapter extends RecyclerView.Adapter<timeline_adapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<TimeLineItem> events;

    public timeline_adapter(ArrayList<TimeLineItem> items) {
        this.events = items;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView event,date;
        ConstraintLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            event = itemView.findViewById(R.id.EventName);
            date = itemView.findViewById(R.id.Date);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.event.setText(events.get(position).getName());
        Log.d(TAG, events.get(position).getName());
        holder.date.setText(String.valueOf(formatDate(events.get(position).getDate())));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                Log.d(TAG, events.get(position) + " clicked");
                Intent intent = new Intent(c, eventScreen.class);
                intent.putExtra("name", events.get(position).getName());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    private String formatDate(Integer date){
        String dateString = date.toString();
        String days = dateString.substring(6,8);
        String months = dateString.substring(4,6);
        String years = dateString.substring(0,4);

        String fDate = days + "/" + months + "/" + years;

        return fDate;
    }
}



