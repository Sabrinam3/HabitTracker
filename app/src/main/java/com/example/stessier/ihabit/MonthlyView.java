package com.example.stessier.ihabit;


import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyView extends Fragment {
    private CalendarView calendarView;
    private Button chooseDate;
    private String chosenDate;
    private HabitManager habitManager;

    public MonthlyView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chosenDate = "";
        calendarView = getView().findViewById(R.id.calendarView);
        chooseDate = getView().findViewById(R.id.buttonChooseDate);
        habitManager = new HabitManager(view.getContext());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                chosenDate = new DateFormatSymbols().getMonths()[month] + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth )+ "-" + year;
            }
        });
        chooseDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("MMMM-dd-yyyy");
                habitManager.loadHabitsArray();
                if(chosenDate.equals(""))
                {
                    chosenDate = dateFormat.format(calendarView.getDate());
                }
                List<String> completedHabits= habitManager.returnHabitsCompletedOnDay(chosenDate);
                Intent intent = new Intent(getActivity(), ShowCompleteActivity.class);
                intent.putStringArrayListExtra("completedHabits", (ArrayList<String>)completedHabits);
                intent.putExtra("completedDate", chosenDate);
                startActivity(intent);
            }
        });
    }
}
