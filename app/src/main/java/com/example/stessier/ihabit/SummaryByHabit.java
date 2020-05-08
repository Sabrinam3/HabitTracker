package com.example.stessier.ihabit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SummaryByHabit extends Fragment implements RecyclerAdapter.onClickHabitListener{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private Toolbar mTopToolbar;
    private JSONArray userHabits;
    private HabitManager habitManager;
    private LinearLayout recycler_layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.summary_by_habit_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        habitManager = new HabitManager(view.getContext());
        recycler_layout = view.findViewById(R.id.recycler_layout);
        recyclerView = view.findViewById(R.id.habitsRecyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        userHabits = habitManager.getUserHabits();

        adapter = new RecyclerAdapter(returnHabitsList(userHabits), returnColorList(userHabits), this, recycler_layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private List<String> returnColorList(JSONArray habitsArray)
    {
        List<String> colorList = new ArrayList<>();
        for(int i = 0; i < habitsArray.length(); i++)
        {
            try
            {
                JSONObject obj = habitsArray.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                colorList.add(habit.getString("color"));
            }catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return colorList;
    }
    private List<String> returnHabitsList(JSONArray habitsArray)
    {
        List<String> userHabitsList = new ArrayList<>();
        for(int i = 0; i < habitsArray.length(); i++)
        {
            try
            {
                JSONObject obj = habitsArray.getJSONObject(i);
                userHabitsList.add(obj.getJSONObject("habit").getString("name"));
            }catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return userHabitsList;
    }
    @Override
    public void onHabitClick(int position) {
            String name = returnHabitsList(userHabits).get(position);
            habitManager.updateStreakData(name);
            ArrayList<String> completedDates = habitManager.returnDatesHabitCompleted(name);
            int currentStreak = habitManager.getCurrentStreakData(name);
            int longestStreak = habitManager.getLongestStreakData(name);
            Intent intent = new Intent(getActivity(), DaysHabitCompletedActivity.class);
            intent.putStringArrayListExtra("completedDates", completedDates);
            intent.putExtra("habitName", name);
            intent.putExtra("currentStreak", Integer.toString(currentStreak));
            intent.putExtra("longestStreak", Integer.toString(longestStreak));
            startActivity(intent);
    }

}
