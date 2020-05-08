package com.example.stessier.ihabit;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {

    private List<String> habitsList;
    private List<String>colorList;
    private LinearLayout recyclerLayout;
    private onClickHabitListener mOnHabitListener;


    public RecyclerAdapter(List<String>habits, List<String> colors, onClickHabitListener onHabitListener, LinearLayout l)
    {
        this.habitsList = habits;
        this.colorList = colors;
        this.mOnHabitListener = onHabitListener;
        this.recyclerLayout = l;
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int i) {
        //Creates each object of the view holder
        TextView textView = (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_layout, parent, false);
        viewHolder myViewHolder = new viewHolder(textView, mOnHabitListener, recyclerLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
    //Assign data to the view component
        viewHolder.habitView.setText(habitsList.get(position));
        switch(colorList.get(position))
        {
            case "blue": viewHolder.habitView.setBackgroundResource(R.drawable.blue_rectangle); break;
            case "orange": viewHolder.habitView.setBackgroundResource(R.drawable.orange_rectangle);break;
            case "lime": viewHolder.habitView.setBackgroundResource(R.drawable.lime_rectangle);break;
            case "red": viewHolder.habitView.setBackgroundResource(R.drawable.red_rectangle);break;
            case "yellow": viewHolder.habitView.setBackgroundResource(R.drawable.yellow_rectangle);break;
            case "pink": viewHolder.habitView.setBackgroundResource(R.drawable.pink_rectangle);break;
            case "purple":viewHolder.habitView.setBackgroundResource(R.drawable.purple_rectangle);break;
            case "green":viewHolder.habitView.setBackgroundResource(R.drawable.green_rectangle);break;
            case "cyan":viewHolder.habitView.setBackgroundResource(R.drawable.cyan_rectangle);break;
            case "white":viewHolder.habitView.setBackgroundResource(R.drawable.white_rectangle);break;
        }
    }

    @Override
    public int getItemCount() {
        //Uses to find out how many items available on the list
        return habitsList.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView habitView;
        LinearLayout habit_layout;
        onClickHabitListener onHabitListener;

            public viewHolder(TextView itemView, onClickHabitListener habitListener, LinearLayout l) {
            super(itemView);
            itemView.setOnClickListener(this);
            habitView = itemView;
            this.onHabitListener = habitListener;
            this.habit_layout = l;
        }

        @Override
        public void onClick(View v) {
            onHabitListener.onHabitClick(getAdapterPosition());
        }
    }
    public interface onClickHabitListener{
        void onHabitClick(int position);
    }
}


