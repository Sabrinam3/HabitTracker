package com.example.stessier.ihabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class listViewAdapter extends ArrayAdapter<String> {
    private int resourceLayout;
    private Context context;
    public listViewAdapter(Context ctx, List<String> habits)
    {
        super(ctx, R.layout.text_view_layout, habits);
        //this.resourceLayout = resource;
        //this.context = ctx;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.text_view_layout, parent, false);
        //reference to items in list
        String habitItem = getItem(position);
        //reference to text element
        TextView habitView = customView.findViewById(R.id.textViewHabit);
        habitView.setText(habitItem);
        return customView;
    }
}
