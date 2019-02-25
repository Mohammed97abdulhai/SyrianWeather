package com.example.weatherapp.adapters;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.APIs.RecyclerViewClickListener;
import com.example.weatherapp.R;
import com.example.weatherapp.activites.ForecastActivity;
import com.example.weatherapp.activites.MainActivity;
import com.example.weatherapp.fragments.WeatherDetialsFragment;
import com.example.weatherapp.models.DialogModel;
import com.example.weatherapp.models.ParentModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;


public class MainAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder>{
    Context context;

    //an array list that  contain the temparature and the image address and the hour of each node
    ArrayList<ParentModel> items1;

    //anarray list that conatin the names of the days
    ArrayList<String> namesOfDays;

    // arraylist to store the data that we want to sent to each element in the child recycler view
    ArrayList<ParentModel> subItems;


    ArrayList<ArrayList<ParentModel>> lists;


    int endDay =0;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    public MainAdapter(Context context, ArrayList<String> days , ArrayList<ParentModel> items1 )
    {
        this.context =context;
        this.namesOfDays = days;
        this.items1 = items1;
        lists = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.main_row_layout, viewGroup, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull XRecyclerView.ViewHolder viewHolder, int i) {
        ((Item)viewHolder).textView.setText(namesOfDays.get(i));
        LinearLayoutManager childlayout = new LinearLayoutManager(context, LinearLayout.HORIZONTAL
                ,false);

        childlayout.setInitialPrefetchItemCount(4);
        ((Item) viewHolder).recyclerView.setLayoutManager(childlayout);
        ((Item) viewHolder).recyclerView.setRecycledViewPool(viewPool);

        ((Item) viewHolder).recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));



        if( i==0)
        {
            for(int index =0 ; index< items1.size(); index++)
            {

                if(items1.get(index).hour.equalsIgnoreCase("11:00 pm"))
                {
                    Log.i("hello",items1.get(index).hour);
                    endDay = index+1;
                    break;
                }
            }

            subItems = new ArrayList<>(items1.subList(0,endDay));
            lists.add(subItems);
        }
        else {

            subItems = new ArrayList<>(items1.subList(endDay,endDay+8));
            lists.add(subItems);
            endDay = endDay + 8;
        }

        RecyclerViewClickListener listener = (view, position) -> {

            FragmentManager manager = ((ForecastActivity)context).getSupportFragmentManager();

            FragmentTransaction ft = manager.beginTransaction();
            Fragment prev = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment dialogFragment = WeatherDetialsFragment.newInstance(lists.get(i).get(position));
            dialogFragment.show(ft, "dialog");
        };

        HorizontalAdapter adapter = new HorizontalAdapter(context,subItems, listener);
        ((Item) viewHolder).recyclerView.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return namesOfDays.size();
    }

    class Item extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textView;

        public Item(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.hours_list);
            textView = itemView.findViewById(R.id.day_name);

        }
    }
}

