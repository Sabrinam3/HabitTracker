package com.example.stessier.ihabit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowCompleteActivity extends AppCompatActivity {
private List<String> completedHabits;
private ListView completedHabitsList;
private ArrayAdapter<String> adapter;
private TextView onDateView;
private TextView heading;
private Button recordHabitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complete);
        Toolbar mTopToolbar = findViewById(R.id.standard_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        onDateView = findViewById(R.id.textViewOnDate);
        heading = findViewById(R.id.completedHeading);
        recordHabitBtn = findViewById(R.id.buttonStartRecording);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            completedHabits = extras.getStringArrayList("completedHabits");
            String date = extras.getString("completedDate");
            if(completedHabits.size() > 0)
            {
                recordHabitBtn.setText(R.string.back_to_summary);
                onDateView.setText(getApplicationContext().getString(R.string.completed_date, date));
                heading.setText(R.string.congratulations);
                completedHabitsList = findViewById(R.id.completedHabitsList);
                adapter = new listViewAdapter(this, completedHabits);
                completedHabitsList.setAdapter(adapter);
            }
            else
            {
                onDateView.setText(getApplicationContext().getString(R.string.no_habits_recorded, date));
                DateFormat dateFormat = new SimpleDateFormat("MMMM-dd-yyyy");
                String today = dateFormat.format(new Date());
                if(date.equals(today))
                {
                    recordHabitBtn.setText(R.string.record_habit);
                }
                else
                {
                    recordHabitBtn.setText(R.string.back_to_summary);
                }
            }
        }
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

    public void onClickRecordHabit(View view) {
        if(recordHabitBtn.getText().toString().equals("Record Habit"))
        {
            finish();
            startActivity(new Intent(this, RecordHabitActivity.class));
        }
        else
        {
            finish();
        }

    }

}
