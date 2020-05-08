package com.example.stessier.ihabit;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Toolbar mTopToolbar;
    private TextView dateView;
    private TextView yearView;
    private TextView hubMessage;
    private ImageButton addNewBtn;
    private ImageButton recordBtn;
    private ImageButton manageBtn;
    private ImageButton summaryBtn;
    private HabitManager habitManager;
    private TextView addHabitView;
    private TextView manageHabitView;
    private TextView summaryHabitView;
    private TextView recordHabitView;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopToolbar = findViewById(R.id.my_toolbar);
        dateView = findViewById(R.id.textViewDate);
        yearView = findViewById(R.id.textViewYear);
        hubMessage = findViewById(R.id.textViewAddHabit);
        addNewBtn = findViewById(R.id.imageBtnAddNew);
        recordBtn = findViewById(R.id.imageBtnRecordHabit);
        manageBtn = findViewById(R.id.imageBtnManage);
        summaryBtn = findViewById(R.id.imageBtnSummary);
        addHabitView = findViewById(R.id.textViewAdd);
        manageHabitView = findViewById(R.id.textViewManage);
        summaryHabitView = findViewById(R.id.textViewSummary);
        recordHabitView = findViewById(R.id.textViewRecord);
        mainLayout = findViewById(R.id.mainLayout);
        setDate();
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get the existing user habits
        habitManager = new HabitManager(this);
        JSONArray habits = habitManager.getUserHabits();
        if(habits.length() == 0)
        {
            //User must add habits
            hubMessage.setText(R.string.start_habit);
            recordBtn.setVisibility(View.INVISIBLE);
            manageBtn.setVisibility(View.INVISIBLE);
            summaryBtn.setVisibility(View.INVISIBLE);
            manageHabitView.setVisibility(View.INVISIBLE);
            summaryHabitView.setVisibility(View.INVISIBLE);
            recordHabitView.setVisibility(View.INVISIBLE);
        }
        else
        {
            hubMessage.setVisibility(View.INVISIBLE);
            recordBtn.setVisibility(View.VISIBLE);
            manageBtn.setVisibility(View.VISIBLE);
            summaryBtn.setVisibility(View.VISIBLE);
            manageHabitView.setVisibility(View.VISIBLE);
            summaryHabitView.setVisibility(View.VISIBLE);
            recordHabitView.setVisibility(View.VISIBLE);
        }
        //Ask the user about notifications if first time launching activity
        SharedPreferences sharedPrefs = getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
        if(!sharedPrefs.contains("notificationsEnabled"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Would you like to enable notifications? You can turn this off later in Settings").setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPrefs=getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("notificationsEnabled", true);
                    editor.apply();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPrefs=getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("notificationsEnabled", false);
                    editor.apply();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            sharedPrefs=getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
            boolean enabled = sharedPrefs.getBoolean("notificationsEnabled", true);
            Log.i("TEST", "onCreate: " + enabled);
        }

        //Retrieve the number of habits user would like to perform per day
        sharedPrefs=getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
        String num = sharedPrefs.getString("numHabitsPerDay", "0");
        int numberOfHabits = habitManager.returnNumberHabitsGoal(num);

        if(numberOfHabits > 0)
        {
            int habitsToday = habitManager.numberOfHabitsCompletedToday();
            if(habitsToday < numberOfHabits)
            {
                String remainder = Integer.toString(numberOfHabits - habitsToday);
                Snackbar snackbar = Snackbar.make(mainLayout, "Complete " + remainder + " more habits to complete your daily goal!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else
            {
                Snackbar snackbar = Snackbar.make(mainLayout, "Congratulations! You have met your daily habit goal!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    private void setDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("MMMM-dd", Locale.CANADA);
        Date date = new Date();
        dateView.setText(dateFormat.format(date));
        dateFormat = new SimpleDateFormat("yyyy", Locale.CANADA);
        yearView.setText(dateFormat.format(date));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

   /* @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("Notification Text", "some text");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ledgerId, intent, )
    }
*/
    public void onClickAddNew(View view) {
        startActivity(new Intent(this,NewHabitActivity.class));
    }

    public void onClickRecordHabit(View view) {
        startActivity(new Intent(this, RecordHabitActivity.class));
    }

    public void onClickManage(View view) {
        startActivity(new Intent(this, ManageHabitsActivity.class));
    }

    public void onClickViewSummary(View view) {
        startActivity(new Intent(this, SummaryActivity.class));
    }
}
