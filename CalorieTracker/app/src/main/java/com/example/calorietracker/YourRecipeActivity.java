package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class YourRecipeActivity extends AppCompatActivity {

    protected Button stepButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_recipe);

        stepButton = findViewById(R.id.yra_view_steps);

    }
}