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
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar mTopToolbar;
    private Switch notificationsSwitch;
    private Spinner spinner;
    private HabitManager habitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mTopToolbar = findViewById(R.id.standard_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        habitManager = new HabitManager(this);
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
        boolean enabled = sharedPrefs.getBoolean("notificationsEnabled", false);
        if (enabled) {
            notificationsSwitch.setChecked(true);
        } else {
            notificationsSwitch.setChecked(false);
        }
        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("notificationsEnabled", true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Notifications enabled", Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("notificationsEnabled", false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Notifications disabled", Toast.LENGTH_LONG).show();
                }
            }
        });
         spinner = findViewById(R.id.spinnerNumber);
        sharedPrefs=getApplicationContext().getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
        String num = sharedPrefs.getString("numHabitsPerDay", "0");
        int index = habitManager.returnNumberHabitsGoal(num);
        if(index > 0)
        {
            spinner.setSelection(index);
        }
        else
        {
            spinner.setSelection(0);
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
                saveSharedPref();
                this.finish();
                break;
            }
            case R.id.action_settings: {
                this.finish();
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

    public void onClickBtnHome(View view) {
        saveSharedPref();
        finish();
    }
    private void saveSharedPref()
    {
        SharedPreferences sharedPrefs = getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("numHabitsPerDay", spinner.getSelectedItem().toString());
        editor.apply();
    }
}
