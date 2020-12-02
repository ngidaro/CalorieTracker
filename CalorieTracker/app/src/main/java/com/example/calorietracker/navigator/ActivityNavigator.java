package com.example.calorietracker.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.calorietracker.BarcodeActivity;
import com.example.calorietracker.CaloriesBurnedActivity;
import com.example.calorietracker.FoodActivity;
import com.example.calorietracker.HomeActivity;
import com.example.calorietracker.RecipeActivity;
import com.example.calorietracker.ScaleInputActivity;
import com.example.calorietracker.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityNavigator extends Activity {

    public static void changeActivity(final Context applicationContext,
                                      final String user_id,
                                      LinearLayout llHomeTab,
                                      LinearLayout llExerciseTab,
                                      LinearLayout llSettingsTab,
                                      LinearLayout llRecipeTab,
                                      FloatingActionButton fabtnFoodInput) {

        llHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(applicationContext, HomeActivity.class);
                intent.putExtra("_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        llExerciseTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(applicationContext, CaloriesBurnedActivity.class);
                intent.putExtra("_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        llSettingsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(applicationContext, SettingsActivity.class);
                intent.putExtra("_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        llRecipeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(applicationContext, RecipeActivity.class);
                intent.putExtra("_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        fabtnFoodInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(applicationContext, FoodActivity.class);
                intent.putExtra("_id", user_id);
                v.getContext().startActivity(intent);
            }
        });
    }
}
