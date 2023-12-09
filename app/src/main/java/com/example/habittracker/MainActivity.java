package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.widget.AdapterView;

import com.example.habittracker.Habit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.work.Data;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CustomBottomSheetDialogFragment.OnBottomSheetDismissListener {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    private ArrayList<String> habitArray;
    private ArrayAdapter adapter;
    private ListView habitList;

    private ArrayList<Habit> userHabit;
    public static final String HABIT_PREFS = "HabitPrefs"; // SharedPreferences name
    public static final String HABIT_LIST_KEY = "habitList"; // Key for habitList in SharedPreferences

    public static final String CUSTOM_ALARM_ACTION = "com.example.habittracker.CUSTOM_ALARM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for firebase login and register
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        // i dont think this does anything I just added it from hw 2
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("GoodHabit");

        //setting up the Addhabit button
        Button openDialogButton = findViewById(R.id.habitButton);
        openDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showBottomSheetDialog();
                CustomBottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
                bottomSheetDialogFragment.setOnBottomSheetDismissListener(MainActivity.this);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });
        //intitalizing userHabit
        userHabit = new ArrayList<>();

        // setting up the array in onCreate
        habitArray = new ArrayList<>();
        habitList = findViewById(R.id.habitListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitArray);
        habitList.setAdapter(adapter);


        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected habit
                String selectedHabit = habitArray.get(position);
                Habit chosenHabit = findHabitByName(selectedHabit);
               // toolbar.setTitle(chosenHabit.getNumber());
                Intent intent = new Intent(MainActivity.this, HabitInfo.class);
                intent.putExtra("Name", chosenHabit.getName());
                intent.putExtra("Description", chosenHabit.getDescription());
                intent.putExtra("Number", chosenHabit.getNumber());
                startActivity(intent);

            }
        });
        //scheduleAlarm();
        setDailyTask();

      /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }*/

    }
    private void showBottomSheetDialog() {
        CustomBottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  Toolbar toolbar = findViewById(R.id.toolbar);
      //  toolbar.setTitle("onResume");

        habitArray.clear();

        if(userHabit == null || userHabit.isEmpty()){

            if(loadHabitList() != null){
                userHabit = loadHabitList();
            }
        }

        //get habitName list and push it back in shared preferences
        SharedPreferences forsets = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        Set<String> habitNames = forsets.getStringSet("habitName", new HashSet<>());
        SharedPreferences.Editor editor = forsets.edit();
        editor.putStringSet("habitName", habitNames);
        editor.apply();
        //display the list of names again(data was lost when onResume was called)
        for (String element : habitNames) {
            // System.out.println(element);
            habitArray.add(element);
            adapter.notifyDataSetChanged();
        }


       //saves userHabit list if its not empty
        if(!userHabit.isEmpty()){
        saveHabitList(userHabit);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // toolbar.setTitle("GoodHabits");
        habitArray.clear();



        // saving the habit names set
        SharedPreferences forsets = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        Set<String> habitNames = forsets.getStringSet("habitName", new HashSet<>());
        SharedPreferences.Editor editor = forsets.edit();
        editor.putStringSet("habitName", habitNames);
        editor.remove("habitName");
        editor.apply();
        editor.putStringSet("habitName", habitNames);
        editor.apply();




        saveHabitList(userHabit);
        saveHabitList(userHabit); //Just added this 2


    }

    public void onDismiss() {
        // Your code to be executed when the bottom sheet is dismissed
        // For example, update something in the main activity
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  toolbar.setTitle("GoodHabit");
        habitArray.clear();
        Log.d("MyAlarmReceiver", "Alarm received!");
        //get the user's input
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String savedInput1 = sharedPreferences.getString("input1", "");
        String savedInput2 = sharedPreferences.getString("input2", "");
        String savedInput3 = sharedPreferences.getString("input3", "");



        //save Haibtnames in a list
        SharedPreferences forsets = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        Set<String> habitNames = forsets.getStringSet("habitName", new HashSet<>());
        habitNames.add(savedInput1);
        SharedPreferences.Editor editor = forsets.edit();
        editor.putStringSet("habitName", habitNames);
        editor.apply();

        //display habit name on List
        for (String element : habitNames) {
            habitArray.add(element);
            adapter.notifyDataSetChanged();
        }


        //add all habit information in Habit list's Shared Preferences
        Habit newHabit = new Habit(savedInput1, savedInput2, savedInput3);
        userHabit.add(newHabit);
        saveHabitList(userHabit);
    }
    private void saveHabitList(ArrayList<Habit> habits) {
        SharedPreferences prefs = getSharedPreferences(HABIT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Convert habitList to a JSON string and save it
        String habitsJson = new Gson().toJson(habits);
        editor.putString(HABIT_LIST_KEY, habitsJson);

        editor.apply();
    }

    private ArrayList<Habit> loadHabitList() {
        SharedPreferences prefs = getSharedPreferences(HABIT_PREFS, Context.MODE_PRIVATE);

        // Retrieve the JSON string from SharedPreferences
        String habitsJson = prefs.getString(HABIT_LIST_KEY, "");

        // Convert the JSON string back to ArrayList<Habit>
        Type type = new TypeToken<ArrayList<Habit>>(){}.getType();
        return new Gson().fromJson(habitsJson, type);
    }
    public Habit findHabitByName(String habitName) {
        for (Habit habit : userHabit) {
            if (habit.getName().equals(habitName)) {
                return habit; // Return the matching Habit object
            }
        }
        return null; // Return null if no match is found
    }

    private void setDailyTask() {

        OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(MyWorkerClass.class)
                        .setInitialDelay(getMillisUntil5AM(), TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }

    private long getMillisUntil5AM() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 1);

        long currentTime = System.currentTimeMillis();
        long scheduledTime = calendar.getTimeInMillis();

        // If the scheduled time is in the past, schedule for the next day
        if (currentTime > scheduledTime) {
            scheduledTime += TimeUnit.DAYS.toMillis(1);
        }

        return scheduledTime - currentTime;
    }
    }
