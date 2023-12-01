package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        //setting up the Addhabit button
        Button openDialogButton = findViewById(R.id.habitButton);
        openDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        // setting up the array in onCreate
        habitArray = new ArrayList<>();
        habitList = findViewById(R.id.habitListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, habitArray);
        habitList.setAdapter(adapter);
    }
    private void showBottomSheetDialog() {
        CustomBottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}