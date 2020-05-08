package com.example.stessier.ihabit;

import java.util.ArrayList;
import java.util.Date;

public class Habit {
    public Habit(String name, String color) {
        this.name = name;
        this.color = color;
        //set streak properties to 0
        this.currentStreak = 0;
        this.longestStreak = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    private String name;
    private String color;
    private int longestStreak;
    private int currentStreak;
}
