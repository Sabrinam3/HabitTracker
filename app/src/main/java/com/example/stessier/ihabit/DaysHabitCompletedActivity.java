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

import java.util.List;

public class DaysHabitCompletedActivity extends AppCompatActivity {
    private List<String> completedDates;
    private ListView completedHabitsList;
    private ArrayAdapter<String> adapter;
    private TextView textViewHeading;
    private TextView textViewMessage;
    private TextView textViewCurrent;
    private TextView textViewLongest;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_habit_completed);
        textViewHeading = findViewById(R.id.textViewHeading);
        textViewMessage = findViewById(R.id.textViewMessage);
        returnButton = findViewById(R.id.btnReturnSummary);
        textViewCurrent = findViewById(R.id.textViewCurrentStreak);
        textViewLongest = findViewById(R.id.textViewLongestStreak);
        Toolbar mTopToolbar = findViewById(R.id.standard_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            completedDates = extras.getStringArrayList("completedDates");
            String habitName = extras.getString("habitName");

            if(completedDates.size() > 0)
            {
                textViewHeading.setText(R.string.congratulations);
                textViewMessage.setText(getApplicationContext().getString(R.string.you_completed_habit, habitName));
                completedHabitsList = findViewById(R.id.completedHabitsList);
                adapter = new listViewAdapter(this, completedDates);
                completedHabitsList.setAdapter(adapter);
                returnButton.setText(R.string.back_to_summary);
                textViewCurrent.setText(extras.getString("currentStreak"));
                textViewLongest.setText(extras.getString("longestStreak"));
            }
            else
            {
                textViewHeading.setText(null);
                textViewMessage.setText(getApplicationContext().getString(R.string.not_recorded, habitName));
                returnButton.setText(R.string.record_habit);
                textViewMessage.setPadding(0,0, 0,36);
                textViewCurrent.setText("0");
                textViewLongest.setText("0");
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

    public void onClickReturn(View view) {
        if(returnButton.getText().toString().equals("Record Habit"))
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
