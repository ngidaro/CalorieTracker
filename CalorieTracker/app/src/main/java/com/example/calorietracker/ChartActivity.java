package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.calorietracker.navigator.ActivityNavigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llSettingsTab, llRecipeTab, llFloatingButton);
    }
}