package com.example.stessier.ihabit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity{
    private ViewPager helpPager;
    private int[] layouts = {R.layout.help_slide_1, R.layout.help_slide_2, R.layout.help_slide_3, R.layout.help_slide_4};
    private mPagerAdapter adapter;

    //for the dots
    private LinearLayout dots_layout;
    private ImageView[] dots;

    private Button skipButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
        if(sharedPref.contains("priorLaunch"))
        {
                loadHome();
        }
        setContentView(R.layout.activity_welcome);
        helpPager = findViewById(R.id.viewPager);
        adapter = new mPagerAdapter(layouts, this);
        helpPager.setAdapter(adapter);

        dots_layout = findViewById(R.id.dotsLayout);
        skipButton = findViewById(R.id.btnSkip);
        nextButton = findViewById(R.id.btnNext);

        createDots(0);
        helpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if(position == layouts.length -1)
                {
                    nextButton.setText(R.string.start);
                    skipButton.setVisibility(View.INVISIBLE);
                }
                else
                {
                    nextButton.setText(R.string.next);
                    skipButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void createDots(int currentPosition)
    {
        if(dots_layout != null)
        {
            dots_layout.removeAllViews();
        }

        dots = new ImageView[layouts.length];

        for(int i = 0; i < layouts.length; i++)
        {
            dots[i] = new ImageView(this);
            if(i ==currentPosition)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);

            dots_layout.addView(dots[i], params);
        }
    }

    public void onClickBtnSkip(View view) {
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("priorLaunch", true);
        editor.apply();
        loadHome();
    }

    public void onClickBtnNext(View view) {
        loadNextSlide();
    }
    private void loadHome()
    {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void loadNextSlide()
    {
        int nextSlide = helpPager.getCurrentItem() + 1;
        if(nextSlide < layouts.length)
        {
            helpPager.setCurrentItem(nextSlide);
        }
        else
        {
            SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.my_preference), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("priorLaunch", true);
            editor.apply();
            loadHome();
        }
    }
}
