package com.example.stessier.ihabit;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HabitManager {

    public HabitManager(Context context){
        userHabits = new JSONArray();
        this.context = context;
    }

    private String readJSONFile()
    {
        //Read in the data
        String json;
        try {
            File path = context.getFilesDir();
            File file = new File(path, "habits.json");
            if(!file.exists())
            {
                FileOutputStream stream = new FileOutputStream(file);
                try{
                    stream.write("[]".getBytes());
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    stream.close();
                }
            }
            InputStream is = context.openFileInput("habits.json");
            InputStreamReader isReader = new InputStreamReader(is);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            return json;
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            return null;
        }
    }

    public void loadHabitsArray()
    {
        try
        {
            String json = readJSONFile();
            JSONArray habitsArray = new JSONArray(json);
            this.userHabits = habitsArray;

        }catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void writeNewHabitJSON(Habit habit)
    {
        loadHabitsArray();
        JSONObject obj = new JSONObject();
        try
        {
            if(habit.getName() != " " && habit.getColor() != " ")
            {
                obj.put("name", habit.getName());
                obj.put("color",habit.getColor());
                obj.put("currentStreak", habit.getCurrentStreak());
                obj.put("longestStreak", habit.getLongestStreak());
                JSONObject mainObj = new JSONObject();
                mainObj.put("habit", obj);
                userHabits.put(mainObj);
                //Write to file
                OutputStreamWriter os = new OutputStreamWriter(context.openFileOutput("habits.json", Context.MODE_PRIVATE));
                os.write(userHabits.toString());
                os.close();
            }
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void updateHabitsJSON()
    {
        try
        {
            //Write to file
            OutputStreamWriter os = new OutputStreamWriter(context.openFileOutput("habits.json", Context.MODE_PRIVATE));
            os.write(userHabits.toString());
            os.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    //Check if a habit has been completed on a given day
    public boolean habitDoneToday(String habitName, String date)
    {
        String dt = date.split(" ")[0];
        for(int i = 0; i < userHabits.length(); i++) {
            JSONObject obj;
            try {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                if (habit.getString("name").equals(habitName)) {
                    if (habit.has("dates")) {
                        JSONArray datesCompleted = habit.getJSONArray("dates");
                        for(int j =0; j < datesCompleted.length(); j++)
                        {
                            String dateCompleted = datesCompleted.getString(j);
                            if(dt.equals(dateCompleted.split(" ")[0]))
                                return true;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public JSONArray getUserHabits(){
        loadHabitsArray();
        return userHabits;
    }

    public void recordHabitCompleted(String habitName, String dateCompleted)
    {
        for(int i = 0; i < userHabits.length(); i++)
        {
            JSONObject obj;
            try
            {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                if(habit.getString("name") == habitName )
                {
                    if(habit.has("dates"))
                    {
                        JSONArray datesCompleted = habit.getJSONArray("dates");
                        datesCompleted.put(dateCompleted);
                        updateHabitsJSON();
                    }
                    else
                    {
                        JSONArray datesCompleted = new JSONArray("[]");
                        datesCompleted.put(dateCompleted);
                        habit.put("dates", datesCompleted);
                        updateHabitsJSON();
                    }

                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void removeHabitCompleted(String habitName, String date)
    {
        String dt = date.split(" ")[0];
        for(int i = 0; i < userHabits.length(); i++) {
            JSONObject obj;
            try {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                if (habit.getString("name").equals(habitName)) {
                    if (habit.has("dates")) {
                        JSONArray datesCompleted = habit.getJSONArray("dates");
                        for(int j =0; j < datesCompleted.length(); j++)
                        {
                            String dateCompleted = datesCompleted.getString(j);
                            if(dt.equals(dateCompleted.split(" ")[0]))
                            {
                                datesCompleted.remove(j);
                                updateHabitsJSON();
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void removeHabit(String habitName)
    {
        for(int i = 0; i < userHabits.length(); i++)
        {
            try
            {
                JSONObject obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                if(habit.getString("name").equals(habitName))
                {
                    userHabits.remove(i);
                    updateHabitsJSON();
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
    public Habit createHabit(String name, String selectedColor)
    {
        return new Habit(name, selectedColor);
    }

    public int numberOfHabitsCompletedToday()
    {
        DateFormat dateFormat = new SimpleDateFormat("MMMM-dd-yyyy");
        String todaysDate = dateFormat.format(new Date());
        List<String> habits = returnHabitsCompletedOnDay(todaysDate);
        return habits.size();
    }
    public List<String>returnHabitsCompletedOnDay(String date)
    {
        List<String> habitsCompleted = new ArrayList<>();
        for(int i = 0; i < userHabits.length(); i++)
        {
            JSONObject obj;
            try
            {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");

                    if(habit.has("dates"))
                    {
                        JSONArray datesCompleted = habit.getJSONArray("dates");
                        for(int j = 0; j < datesCompleted.length(); j++)
                        {
                            String habitCompleted = datesCompleted.getString(j).split(" ")[0];
                            if(habitCompleted.equals(date))
                            {
                                habitsCompleted.add(habit.getString("name"));
                            }
                        }
                    }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        return habitsCompleted;
    }

    public ArrayList<String>  returnDatesHabitCompleted(String name)
    {
        ArrayList<String> datesCompleted = new ArrayList<>();
        for(int i = 0; i < userHabits.length(); i++)
        {
            JSONObject obj;
            try
            {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                if(habit.getString("name").equals(name))
                {
                    if(habit.has("dates"))
                    {
                        JSONArray dates = habit.getJSONArray("dates");
                        for(int j = 0; j < dates.length(); j++)
                        {
                            datesCompleted.add(dates.getString(j));
                        }
                    }
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        return datesCompleted;
    }

    public void deleteAllHabits()
    {
        try {
            File path = context.getFilesDir();
            File file = new File(path, "habits.json");
            FileOutputStream stream = new FileOutputStream(file);
            try{
                stream.write("[]".getBytes());
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                stream.close();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void updateStreakData(String habitName)
    {
        for(int i = 0; i < userHabits.length(); i++)
        {
            JSONObject obj;
            try
            {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");

                if(habit.has("dates"))
                {
                    JSONArray dates = habit.getJSONArray("dates");
                    int currentStreak = Integer.parseInt(habit.getString("currentStreak"));
                    int longestStreak = Integer.parseInt(habit.getString("longestStreak"));
                    if(dates.length() == 0)
                    {
                        //reset streak data
                        habit.put("currentStreak", 0);
                        habit.put("longestStreak", 0);
                    }else
                    {
                        List<Date> dateList = new ArrayList<>();
                        //Convert the dates to LocalDate
                        for(int j =0; j < dates.length(); j++)
                        {
                            String dt = dates.getString(j).split(" ")[0];
                            try
                            {
                                dateList.add(new SimpleDateFormat("MMMM-dd-yyyy").parse(dt));
                            }catch(ParseException e)
                            {
                                e.printStackTrace();
                            }

                        }

                        habit.put("currentStreak", findCurrentStreak(dateList));
                        habit.put("longestStreak", findLongestStreak(dateList));
                        updateHabitsJSON();
                    }
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public int findCurrentStreak(List<Date> dates)
    {
        Date today = new Date();
        String dateString =  new SimpleDateFormat("MMMM-dd-yyyy").format(today);
        try
        {
            today = new SimpleDateFormat("MMMM-dd-yyyy").parse(dateString);
        }catch(ParseException e)
        {
            e.printStackTrace();
        }
        Date lastRecordedDate = dates.get(dates.size()-1);
        long diffInMilli = Math.abs(lastRecordedDate.getTime() - today.getTime());
        long diffDays = TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS);
        //If the current date is not in the list and the last recorded date is longer than one day ago, the current streak is 0
        if(lastRecordedDate.compareTo(today) != 0 && diffDays != 1)
        {
            return 0;
        }
        //else if there is only one date in the list and it is today's date, the current streak is 1
       else if(dates.size() == 1 && lastRecordedDate.compareTo(today) == 0)
        {
            return 1;
        }
        //else if there is only one date in the list and the date is yesterday, the current streak is 1
       else if(dates.size() == 1 && diffDays == 1)
        {
        return 1;
         }
      //else, loop backwards until date is not one less
        else {
            int consecutive = 1;
            for (int i = dates.size() - 1; i > 0; i--) {
                Date firstDate = dates.get(i);
                Date secondDate = dates.get(i - 1);
                diffInMilli = Math.abs(secondDate.getTime() - firstDate.getTime());
                diffDays = TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS);
                if (diffDays == 1) {
                    consecutive++;
                } else {
                    break;
                }
            }
            return consecutive;
        }
    }

    public int findLongestStreak(List<Date>dates)
    {
        int longest = 1;
        int current = 1;
        if(dates.size() == 1)
            return 1;

        for(int i = 0; i < dates.size()-1; i++)
        {
            Date firstDate = dates.get(i);
            Date secondDate = dates.get(i+1);
            long diffInMilli = Math.abs(secondDate.getTime() - firstDate.getTime());
            long diffDays = TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS);
            if(diffDays == 1)
            {
                current++;
            }
            else
            {
                if(current > longest)
                    longest = current;
                current = 1;
            }
        }
        return current > longest ? current : longest;
    }

    public int getCurrentStreakData(String name)
    {
        for(int i = 0; i < userHabits.length(); i++)
        {
            JSONObject obj;
            try
            {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                if(habit.getString("name").equals(name))
                    return habit.getInt("currentStreak");
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getLongestStreakData(String name)
    {
        for(int i = 0; i < userHabits.length(); i++)
        {
            JSONObject obj;
            try
            {
                obj = userHabits.getJSONObject(i);
                JSONObject habit = obj.getJSONObject("habit");
                if(habit.getString("name").equals(name))
                    return habit.getInt("longestStreak");
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int returnNumberHabitsGoal(String number)
    {
        switch (number)
        {
            case "None":return -1;
            case "One": return 1;
            case "Two": return 2 ;
            case "Three": return 3;
            case "Four": return 4;
            case "Five": return 5;
            case "Six": return 6;
            case "Seven": return 7;
            case "Eight": return 8;
            case "Nine": return 9;
            case "Ten": return 10;
            default: return 0;
        }
    }
    static JSONArray userHabits;
    private Context context;
}
