package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.widget.AdapterView;




public class MainActivity extends AppCompatActivity implements CustomBottomSheetDialogFragment.OnBottomSheetDismissListener{

    private ArrayList<String> habitArray;
    private ArrayAdapter adapter;
    private ListView habitList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // i dont think this does anything I just added it from hw 2
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("onCreate");

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

            }
        });


    }
    private void showBottomSheetDialog() {
        CustomBottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("onResume");

        habitArray.clear();


       // SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
      //  String savedInput1 = sharedPreferences.getString("input1", "");

        // Iterate through the set

        SharedPreferences forsets = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        Set<String> habitNames = forsets.getStringSet("habitName", new HashSet<>());
       // habitNames.add(savedInput1);

        SharedPreferences.Editor editor = forsets.edit();
        editor.putStringSet("habitName", habitNames);
        editor.apply();

        for (String element : habitNames) {
            // System.out.println(element);
            habitArray.add(element);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("onPause");
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

        // saving the habit description set
        Set<String> habitDescriptions = forsets.getStringSet("habitDescription", new HashSet<>());
        SharedPreferences.Editor editor2 = forsets.edit();
        editor2.putStringSet("habitDescription", habitDescriptions);
        editor2.remove("habitDescription");
        editor2.apply();

        editor2.putStringSet("habitDescription", habitDescriptions);
        editor2.apply();



        // saving the habitNumber set
        Set<String> habitNumber = forsets.getStringSet("habitNumber", new HashSet<>());
        SharedPreferences.Editor editor3 = forsets.edit();
        editor3.putStringSet("habitNumber", habitNumber);
        editor3.remove("habitNumber");
        editor3.apply();

        editor3.putStringSet("habitNumber", habitNumber);
        editor3.apply();




    }

    public void onDismiss() {
        // Your code to be executed when the bottom sheet is dismissed
        // For example, update something in the main activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("onDismiss");
        habitArray.clear();


        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String savedInput1 = sharedPreferences.getString("input1", "");
        String savedInput2 = sharedPreferences.getString("input2", "");
        String savedInput3 = sharedPreferences.getString("input3", "");
        // Iterate through the set

        SharedPreferences forsets = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        Set<String> habitNames = forsets.getStringSet("habitName", new HashSet<>());
        habitNames.add(savedInput1);
        SharedPreferences.Editor editor = forsets.edit();
        editor.putStringSet("habitName", habitNames);
        editor.apply();


        Set<String> habitDescriptions = forsets.getStringSet("habitDescription", new HashSet<>());
        habitDescriptions.add(savedInput2);
        SharedPreferences.Editor editor2 = forsets.edit();
        editor2.putStringSet("habitDescription", habitDescriptions);
        editor2.apply();


        Set<String> habitNumber = forsets.getStringSet("habitNumber", new HashSet<>());
        habitNumber.add(savedInput3);
        SharedPreferences.Editor editor3 = forsets.edit();
        editor3.putStringSet("habitNumber", habitNumber);
        editor3.apply();

        for (String element : habitNames) {
            habitArray.add(element);
            adapter.notifyDataSetChanged();
        }
    }


}