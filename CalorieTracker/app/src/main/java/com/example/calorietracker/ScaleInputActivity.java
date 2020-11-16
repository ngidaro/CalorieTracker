package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.calorietracker.navigator.ActivityNavigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class scaleInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_input);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llBarcodeTab = findViewById(R.id.tbar_barcode);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llBarcodeTab, llRecipeTab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.addfoodmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



}