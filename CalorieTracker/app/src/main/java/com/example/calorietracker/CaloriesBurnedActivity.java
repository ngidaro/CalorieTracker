package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CaloriesBurnedActivity extends AppCompatActivity {

    TextView titleCalorieDeficit;
    TextView noteCalorieDeficit;
    TextView unitCalorieDeficit;

    EditText inputCalorieDeficit;
    Button addCalorieDeficit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_burned);

        SetupUI();

        addCalorieDeficit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Not sure how to take the Numerical value from inputCalorieDeficit and then
                //make it so the onclick listener not only brings you to the food diary of the day
                //but also adds a negative value to the table with a food type being "Deficit"

                Intent intent = new Intent (CaloriesBurnedActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });


    }

    void SetupUI(){

        titleCalorieDeficit = findViewById(R.id.caldef_title);
        noteCalorieDeficit = findViewById(R.id.caldef_note);
        unitCalorieDeficit = findViewById(R.id.calunit);
        inputCalorieDeficit = findViewById(R.id.cal_burned);
        addCalorieDeficit = findViewById(R.id.caldef_confirm);


    }



}