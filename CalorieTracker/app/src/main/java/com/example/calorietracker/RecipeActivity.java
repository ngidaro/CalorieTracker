package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calorietracker.navigator.ActivityNavigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeActivity extends AppCompatActivity {

    TextView tvAddRecipe;
    TextView tvYourRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llExerciseTab    = findViewById(R.id.tbar_exercise);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llExerciseTab, llSettingsTab, llRecipeTab, llFloatingButton);

        tvAddRecipe = findViewById(R.id.ra_add_recipe);
        tvYourRecipe = findViewById(R.id.ra_your_recipe);

        tvAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, AddRecipeActivity.class);
                intent.putExtra("_id", user_id);
                startActivity(intent);
            }
        });

        tvYourRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, MyRecipesActivity.class);
                intent.putExtra("_id", user_id);
                startActivity(intent);
            }
        });
    }
}