package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import CustomAdapters.RecyclerViewAdapter;
import CustomAdapters.RecyclerViewAdapterSettings;

public class SettingsActivity extends AppCompatActivity {

    private ArrayList<String> itemList = new ArrayList<>();
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        title = findViewById(R.id.u_settings);

        initSettingsOptions();

    }

    private void initSettingsOptions(){

        itemList.add("Edit Profile");
        itemList.add("Delete Account");
        itemList.add("Change Password");
        itemList.add("Push Notifications");
        itemList.add("Log Out");
        itemList.add("About Us");

        initRecylcerView();
    }

    private void initRecylcerView(){

        RecyclerView recyclerview = findViewById(R.id.rv_settings);
        RecyclerViewAdapterSettings adapter = new RecyclerViewAdapterSettings(itemList, this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }


}