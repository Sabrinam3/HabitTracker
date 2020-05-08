package com.example.stessier.ihabit;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SummaryPagerController extends FragmentPagerAdapter {
    int tabCount;

    public SummaryPagerController(FragmentManager fm, int tabCount)
    {
       super(fm);
       this.tabCount = tabCount;
    }


    @Override
    public Fragment getItem(int i) {
        switch(i)
        {
            case 0: return new MonthlyView();
            case 1: return new SummaryByHabit();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
