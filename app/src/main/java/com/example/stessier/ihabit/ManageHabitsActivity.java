package com.example.stessier.ihabit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageHabitsActivity extends AppCompatActivity implements RecyclerAdapter.onClickHabitListener{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private Toolbar mTopToolbar;
    private JSONArray userHabits;
    private HabitManager habitManager;
    private Button homeBtn;
    private LinearLayout recycler_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_habits);
        mTopToolbar = findViewById(R.id.standard_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recycler_layout = findViewById(R.id.recycler_layout);
        homeBtn = findViewById(R.id.btnHome);
        recyclerView = findViewById(R.id.habitsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        habitManager = new HabitManager(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_standard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back: {
                this.finish();
                break;
            }
            case R.id.action_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
            case R.id.about: {
                startActivity(new Intent(this, AboutActivity.class));
                break;
            }

            case R.id.help: {
                SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
                sharedPref.edit().remove("priorLaunch").apply();
                this.finish();
                startActivity(new Intent(this, WelcomeActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHabitClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String name = returnHabitsList(userHabits).get(position);
        deleteListener listener = new deleteListener(name, this);
        builder.setMessage("Are you sure you want to delete " + name + "?").setCancelable(false);
        builder.setPositiveButton("Yes", listener);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onClickBtnHome(View view) {
        finish();
    }
    public void onClickDeleteAll(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        deleteAllListener listener = new deleteAllListener(this);
        builder.setMessage("Are you sure you want to delete all habits?").setCancelable(false);
        builder.setPositiveButton("Yes", listener);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class deleteListener implements DialogInterface.OnClickListener
    {
        String habitToDelete;
        Context context;

        public deleteListener(String h, Context ctx)
        {
            this.habitToDelete = h;
            this.context = ctx;
        }
        @Override
        public void onClick(DialogInterface h, int p) {
            habitManager.removeHabit(habitToDelete);
            finish();
            startActivity(new Intent(context, ManageHabitsActivity.class));
        }
    }

    public class deleteAllListener implements DialogInterface.OnClickListener
    {
        Context context;
        public deleteAllListener(Context ctx)
        {
            this.context = ctx;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            habitManager.deleteAllHabits();
            finish();
            startActivity(new Intent(context, MainActivity.class));
        }
    }
}
