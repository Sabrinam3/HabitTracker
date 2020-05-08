package com.example.stessier.ihabit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewHabitActivity extends AppCompatActivity {
    ImageButton blueBtn;
    ImageButton limeBtn;
    ImageButton orangeBtn;
    ImageButton redBtn;
    ImageButton yellowBtn;
    ImageButton pinkBtn;
    ImageButton purpleBtn;
    ImageButton whiteBtn;
    ImageButton greenBtn;
    ImageButton cyanBtn;
    ImageButton addAnother;
    ImageButton returnHome;
    private TextView habitNameView;
    private HabitManager habitManager;
    private int selectedButtonId;
    private String selectedColor;
    ArrayList<ImageButton> colorButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        Toolbar mTopToolbar = findViewById(R.id.standard_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        blueBtn = findViewById(R.id.imageBtnColorBlue);
        orangeBtn = findViewById(R.id.imageBtnColorOrange);
        redBtn = findViewById(R.id.imageButtonColorRed);
        limeBtn = findViewById(R.id.imageBtnColorLime);
        yellowBtn = findViewById(R.id.imageBtnColorYellow);
        pinkBtn = findViewById(R.id.imageBtnColorPink);
        purpleBtn = findViewById(R.id.imageBtnColorPurple);
        whiteBtn = findViewById(R.id.imageBtnColorWhite);
        greenBtn = findViewById(R.id.imageBtnColorGreen);
        cyanBtn = findViewById(R.id.imageBtnColorCyan);
        addAnother = findViewById(R.id.btnAddAnotherHabit);
        returnHome = findViewById(R.id.imageBtnReturnHome);
        habitNameView = findViewById(R.id.newHabitName);
        colorButtons = new ArrayList<>();
        colorButtons.add(blueBtn);
        colorButtons.add(orangeBtn);
        colorButtons.add(redBtn);
        colorButtons.add(limeBtn);
        colorButtons.add(yellowBtn);
        colorButtons.add(pinkBtn);
        colorButtons.add(purpleBtn);
        colorButtons.add(whiteBtn);
        colorButtons.add(greenBtn);
        colorButtons.add(cyanBtn);
        habitManager = new HabitManager(this);
    }

    private void setSelectedButton(int id) {
        selectedButtonId = id;
        for (int i = 0; i < colorButtons.size(); i++) {
            if (selectedButtonId == colorButtons.get(i).getId()) {
                colorButtons.get(i).setBackgroundResource(R.drawable.inactive_dots);
                switch (i) {
                    case 0:
                        selectedColor = "blue";
                        break;
                    case 1:
                        selectedColor = "orange";
                        break;
                    case 2:
                        selectedColor = "red";
                        break;
                    case 3:
                        selectedColor = "lime";
                        break;
                    case 4:
                        selectedColor = "yellow";
                        break;
                    case 5:
                        selectedColor = "pink";
                        break;
                    case 6:
                        selectedColor = "purple";
                        break;
                    case 7:
                        selectedColor = "white";
                        break;
                    case 8:
                        selectedColor = "green";
                        break;
                    case 9:
                        selectedColor = "cyan";
                        break;
                    default:
                        selectedColor = "";
                }
            } else {
                colorButtons.get(i).setBackgroundColor(Color.TRANSPARENT);
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

    public void onClickColor(View view) {
        setSelectedButton(view.getId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences settings = getSharedPreferences("newhabit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("habitName", habitNameView.getText().toString());
        editor.putInt("selectedColorId", selectedButtonId);
        editor.apply();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("newhabit", Context.MODE_PRIVATE);
        habitNameView.setText(settings.getString("habitName", ""));
        setSelectedButton(settings.getInt("selectedColorId", 0));
    }

    public void onClickAddAnother(View view) {
        Habit newHabit = createHabit();
        if (!newHabit.getName().isEmpty() && !newHabit.getColor().isEmpty()) {
            habitManager.writeNewHabitJSON(newHabit);
            Toast.makeText(this, "Habit recorded", Toast.LENGTH_LONG).show();
        }
        setSelectedButton(0);
        habitNameView.setText("");
    }

    public void onClickHome(View view) {
        Habit newHabit = createHabit();
        if (!newHabit.getName().isEmpty() && !newHabit.getColor().isEmpty()) {
            habitManager.writeNewHabitJSON(newHabit);
        }

        habitNameView.setText("");
        setSelectedButton(0);
        finish();
    }

    public Habit createHabit() {
       return habitManager.createHabit(habitNameView.getText().toString(), selectedColor);
    }
}