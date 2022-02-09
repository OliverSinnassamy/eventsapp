package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.master.glideimageview.GlideImageView;

import java.util.ArrayList;

/**
 * Created by Penrice9 on 09/10/2018.
 */

/**
 * Creates view from card_layout xml file.
 * Inflater instantiates card_layout template as view objects.
 * Attributes are set to values of parameters.
 * Image and name attribute set to values in the respective arrays at index of 'position'
 * New activity started in OnClickListener method.
 * ViewHolder class inherits RecyclerView.ViewHolder
 * Values retrieved and set using element ids from xml file.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    public static String EXTRA_MESSAGE = "com.example.penrice9.myapplication.BrowseScreen";
    private ArrayList<BrowseElement> eventArray = new ArrayList<>();
    private Context context;
    public LoginScreen ls;
    public Database db;
    public int resource;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout to be copied is assigned here, uses 'resource' attribute to set file to get the object.
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        //creates new viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public RecyclerViewAdapter(ArrayList<BrowseElement> elements, Context context, int resourceFile) {
        this.eventArray = elements; //Sets values of parameters to attributes of the class
        this.context = context;
        this.resource = resourceFile;//used to allow for this class to be used in different scenarios.
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        Glide.with(context).load(eventArray.get(position).getImage()).into(holder.image);

        holder.name.setText(eventArray.get(position).getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                Log.d(TAG, eventArray.get(position).getName() + " clicked");
                Intent intent = new Intent(c, eventScreen.class);
                intent.putExtra("name", eventArray.get(position).getName());
                c.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return eventArray.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        RelativeLayout layout;
        GlideImageView image;
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            card = itemView.findViewById(R.id.card);
        }
    }


    }
