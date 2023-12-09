package com.example.habittracker;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;


public class HabitInfo extends AppCompatActivity {

    String param1;
    String param2;
    String param3;
    int intValue;
    int checkedCount;
    boolean streak;
    int cur_streak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_info);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        checkedCount = 0;
        streak = false;
        cur_streak = 0;
        LinearLayout container = findViewById(R.id.container);
        // Retrieve the intent that started this activity
        TextView textView1 = findViewById(R.id.paragraphTextView);
        TextView textViewStreak = findViewById(R.id.streak);
        //TextView textView2 = findViewById(R.id.textView2);
       // TextView textView3 = findViewById(R.id.textView3);
        Bundle extras = getIntent().getExtras();

        // Check if extras are not null to avoid potential crashes
        if (extras != null) {
            // Retrieve the string parameters
            param1 = extras.getString("Name");
            param2 = extras.getString("Description");
            param3 = extras.getString("Number");

            // Do something with the parameters, for example, display them in TextViews


            textView1.setText(param2);
            toolbar.setTitle(param1);

         /*   getSupportActionBar().setTitle(param1);
            TextView paragraphTextView = findViewById(R.id.paragraphTextView);
            paragraphTextView.setText(param2);*/
        }
        checkedCount = loadCheckedCount(param2);
        cur_streak = loadCurrentStreak();
        textViewStreak.setText(String.valueOf(cur_streak));

        streak = loadStreak();
        //textView1.setText(String.valueOf(cur_streak));
        //ok so the number of checked boxes are saved I think. But i need to keep those boxes checked when I come back
        try {
            intValue = Integer.parseInt(param3);
            System.out.println("Converted int value: " + intValue);
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format");
        }
        for (int i = 0; i < intValue; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(View.generateViewId()); // Generate a unique ID for each checkbox

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Update the checked count based on checkbox state
                    checkedCount += isChecked ? 1 : -1;
                    // Save the checkbox state
                    String stringValue = String.valueOf(checkedCount);
                   // toolbar.setTitle(stringValue);//BIG SHIT DONE HERE, COUNTER WORKS PERFECTLY
                    Context context = HabitInfo.this;
                    if(checkedCount == intValue){
                        if(streak == false){

                           // toolbar.setTitle("streak just added");
                            Toast.makeText(context, "Nice Job! You're Streak Increased!", Toast.LENGTH_SHORT).show();
                            streak = true;
                            cur_streak++; //and then save it
                          //  textView1.setText(String.valueOf(cur_streak));
                            textViewStreak.setText(String.valueOf(cur_streak));
                        }
                    }

                }
            });
            LayoutParams layoutParams = new LayoutParams(
                    0,  // Set width to 0 to allow even distribution
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            layoutParams.weight = 1;  // Set weight to evenly distribute checkboxes
            // Add margin if needed
            layoutParams.topMargin = 100; // Adjust as needed
            // Apply layout parameters to the checkbox
            checkBox.setLayoutParams(layoutParams);
            container.addView(checkBox);

        }
        precheckCheckboxes(checkedCount);
        //WAKE UP FIRST STEP(GET THIS TO WORK) YOU HAVE THE SAVEDPREFERENCE SAVING OF CHECKEDCOUNT WORKING
        //YOU GOTTA GET PRECHECK WORKING CORRECTLY, SOME WEIRD DOUBLING STILL HAPPENING FROM IT.
    }
    private void saveCheckedCount(int count, String habitKey) {
        SharedPreferences preferences = getSharedPreferences("CheckedCount",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(habitKey, count);
        editor.apply();
    }
    private void saveStreakBool(boolean streak) {
        SharedPreferences preferences = getSharedPreferences("StreakBool",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("theKey:" + param1, streak);
        editor.apply();
    }
    private void saveStreakInt(int streak) {
        SharedPreferences preferences = getSharedPreferences("StreakInt",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theKey:" + param2, streak);
        editor.apply();
    }
    private int loadCheckedCount(String key) {
        SharedPreferences preferences = getSharedPreferences("CheckedCount",Context.MODE_PRIVATE);
        // Use the key "checkedCount" to retrieve the saved count, and default to 0 if it doesn't exist
        return preferences.getInt(key, 0);
    }
    private int loadCurrentStreak() {
        SharedPreferences preferences = getSharedPreferences("StreakInt",Context.MODE_PRIVATE);
        // Use the key "checkedCount" to retrieve the saved count, and default to 0 if it doesn't exist
        return preferences.getInt("theKey:"+ param2, 0);
    }
    private boolean loadStreak() {
        SharedPreferences preferences = getSharedPreferences("StreakBool",Context.MODE_PRIVATE);
        // Use the key "checkedCount" to retrieve the saved count, and default to 0 if it doesn't exist
        return preferences.getBoolean("theKey:"+ param1, false);
    }
    private void precheckCheckboxes(int countToPrecheck) {
        LinearLayout container = findViewById(R.id.container);

        // Iterate through the checkboxes and precheck them up to the specified count
        for (int i = 0; i < countToPrecheck; i++) {
            CheckBox checkBox = (CheckBox) container.getChildAt(i);
            if (checkBox != null) {
                checkedCount--;
                checkBox.setChecked(true);

            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible
        // Perform any cleanup or save state if needed
        saveCheckedCount(checkedCount, param2);

        saveStreakBool(streak);
        saveStreakInt(cur_streak);
    }
    @Override
    public void onBackPressed() {
        // Perform custom actions when the back button is pressed
        // For example, save the current state before finishing the activity
        saveCheckedCount(checkedCount, param2);
        saveStreakBool(streak);
        saveStreakInt(cur_streak);

        // Call the superclass implementation to allow the default back button behavior
        super.onBackPressed();
    }


}