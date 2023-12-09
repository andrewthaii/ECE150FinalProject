package com.example.habittracker;



import android.content.Context;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.ListenableWorker.Result;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MyWorkerClass extends Worker {
    private String param1;
    private String param2;
    public MyWorkerClass(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Perform your task here
        Log.d("MyWorker", "Task executed at 6pm");

       // Toast.makeText(getApplicationContext(), "PLEASE", Toast.LENGTH_SHORT).show();
        // Return success
        if(loadHabitList() != null){
            ArrayList<Habit>  CurHabit = loadHabitList();
            for (Habit habit : CurHabit) {
                param1 = habit.getName();
                param2 = habit.getDescription();
                updateInfo(param1, param2);
            }

        }


        return Result.success();
    }

    private ArrayList<Habit> loadHabitList() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("HabitPrefs", Context.MODE_PRIVATE);

        // Retrieve the JSON string from SharedPreferences
        String habitsJson = prefs.getString("habitList", "");

        // Convert the JSON string back to ArrayList<Habit>

        Type type = new TypeToken<ArrayList<Habit>>(){}.getType();
        return new Gson().fromJson(habitsJson, type);
    }
    void updateInfo(String name, String des){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StreakBool",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean condition = sharedPreferences.getBoolean("theKey:"+ name, false);

        SharedPreferences pref= getApplicationContext().getSharedPreferences("StreakInt",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref.edit();
        int CurStreak = sharedPreferences.getInt("theKey:"+ des, 0);

        SharedPreferences pref2= getApplicationContext().getSharedPreferences("CheckedCount",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = pref2.edit();
        int cCount = sharedPreferences.getInt(des, 0);

        if(condition == false){
            CurStreak = 0;
            editor2.putInt("theKey:" + des, CurStreak);
            editor2.apply();
        }
        if (condition == true){
            condition = false;
            editor.putBoolean("theKey:" + name, condition);
            editor.apply();
            // save
        }

        cCount = 0;
        editor3.putInt(des, cCount);
        editor3.apply();
    }

}
