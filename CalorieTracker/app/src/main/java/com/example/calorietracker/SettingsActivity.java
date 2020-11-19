package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calorietracker.navigator.ActivityNavigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llSettingsTab, llRecipeTab, llFloatingButton);

        initSettingsOptions();

    }

    private void initSettingsOptions(){

        itemList.add("Edit Profile");
        itemList.add("Change Password");
        itemList.add("Push Notifications");
        itemList.add("Delete Account");
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