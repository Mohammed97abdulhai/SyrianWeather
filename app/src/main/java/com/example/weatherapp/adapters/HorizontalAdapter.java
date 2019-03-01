package com.example.weatherapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.api.RecyclerViewClickListener;
import com.example.weatherapp.R;
import com.example.weatherapp.models.ParentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    ArrayList<ParentModel> items;

    private RecyclerViewClickListener mListener;

    public HorizontalAdapter(Context context, ArrayList<ParentModel> items , RecyclerViewClickListener listener) {
        this.context = context;
        this.items = items;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.horizontal_row_layout,viewGroup,false);
        Item item = new Item(v, mListener);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((Item)viewHolder).textView.setText(items.get(i).temparature);
        ((Item) viewHolder).time.setText(items.get(i).hour);
        Picasso.with(context).load(context.getString(R.string.imageLink )+ items.get(i).image+context.getString(R.string.pngSuffex)).into(((Item) viewHolder).imageView);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class Item extends RecyclerView.ViewHolder implements View.OnClickListener {


        private RecyclerViewClickListener mListener;

        TextView textView;
        ImageView imageView;
        TextView time;
        public Item(@NonNull View itemView , RecyclerViewClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.degree);
            imageView = itemView.findViewById(R.id.mini_icon);
            time = itemView.findViewById(R.id.time);

            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mListener.onClick(v, getAdapterPosition());
        }
    }
}
