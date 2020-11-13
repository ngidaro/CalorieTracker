package com.example.calorietracker.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.calorietracker.BarcodeActivity;
import com.example.calorietracker.FoodActivity;
import com.example.calorietracker.HomeActivity;
import com.example.calorietracker.R;
import com.example.calorietracker.RecipeActivity;

public class ActivityNavigator extends Activity {

    public static void changeActivity(final Context applicationContext,
                                      final String user_id,
                                      LinearLayout llHomeTab,
                                      LinearLayout llFoodTab,
                                      LinearLayout llBarcodeTab,
                                      LinearLayout llRecipeTab) {

        llHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(applicationContext, HomeActivity.class);
                intent.putExtra("_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        llFoodTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(applicationContext, FoodActivity.class);
                intent.putExtra("_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        llBarcodeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(applicationContext, BarcodeActivity.class);
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
    }
}