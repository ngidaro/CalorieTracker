package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.calorietracker.navigator.ActivityNavigator;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llBarcodeTab = findViewById(R.id.tbar_barcode);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llBarcodeTab, llRecipeTab);
    }
}