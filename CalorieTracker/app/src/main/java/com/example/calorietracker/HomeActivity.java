package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.calorietracker.navigator.ActivityNavigator;
import com.example.calorietracker.volley.VolleyRequestContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import CustomAdapters.RecyclerViewAdapterHome;
import callbacks.IVolleyRequestCallback;

public class HomeActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    protected TextView calorieAmount;
    protected TextView calorieLimitView;
    protected double calorieLimit;

    TextView tvDiaryDate;
    ImageView ivDateBack;
    ImageView ivDateForward;
    ImageView ivSettings;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    Button btnAddBurntCalories;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llBarcodeTab = findViewById(R.id.tbar_barcode);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llBarcodeTab, llRecipeTab, llFloatingButton);

        // Setting the RecyclerView
        recyclerView = findViewById(R.id.ha_diary_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tvDiaryDate = findViewById(R.id.ha_diary_date);
        ivDateBack = findViewById(R.id.ha_diary_date_back);
        ivDateForward = findViewById(R.id.ha_diary_date_forward);
        ivSettings = findViewById(R.id.ha_settings);
        btnAddBurntCalories = findViewById(R.id.ha_add_burnt_calories);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE MMM d, yyyy");
        String date = dateFormat.format(calendar.getTime());
        tvDiaryDate.setText(date);

        // Display the foods added by the user in a list
        getFoodDiary(user_id);

        // Set Daily Calorie Limit
        String gender;
        String activityLevel;
        double weight;
        double height;
        double BMR=0;
        double weightLossWeekly;
        int age;
        calorieLimit=0;

        gender = "Male";

        activityLevel = "hard exercise";

        age = 21;

        weight = 168;

        height = 70;

        weightLossWeekly = 1.5;

        // Formula for calorie limit obtained from: http://www.checkyourhealth.org/eat-healthy/cal_calculator.php

        // Determining calorie limit for male
        if (gender.equals("Male"))
        {
            BMR = 66 + (6.3*weight) + 12.9*height - 6.8*age;

            if (activityLevel.equals("little or no exercise"))
                calorieLimit = BMR*1.2-weightLossWeekly*500;
            else if (activityLevel.equals("light exercise"))
                calorieLimit = BMR*1.375-weightLossWeekly*500;
            else if (activityLevel.equals("moderate exercise"))
                calorieLimit = BMR*1.55-weightLossWeekly*500;
            else if (activityLevel.equals("hard exercise"))
                calorieLimit = BMR*1.725-weightLossWeekly*500;
            else if (activityLevel.equals("very hard exercise"))
                calorieLimit = BMR*1.9-weightLossWeekly*500;
        }

        // Determining calorie limit for female
        if (gender.equals("Female"))
        {
            BMR = 665 + (4.3*weight) + 4.7*height - 4.7*age;

            if (activityLevel.equals("little or no exercise"))
                calorieLimit = BMR*1.2-weightLossWeekly*500;
            if (activityLevel.equals("light exercise"))
                calorieLimit = BMR*1.375-weightLossWeekly*500;
            if (activityLevel.equals("moderate exercise"))
                calorieLimit = BMR*1.55-weightLossWeekly*500;
            if (activityLevel.equals("hard exercise"))
                calorieLimit = BMR*1.725-weightLossWeekly*500;
            if (activityLevel.equals("very hard exercise"))
                calorieLimit = BMR*1.9-weightLossWeekly*500;
        }

        calorieLimitView = findViewById(R.id.ha_limit);
        calorieLimitView.setText(""+calorieLimit);


        // Buton to go to scale input
        llFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goToScaleInput(user_id);
            }
        });

        ivDateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the date by -1
//                calendar.add();
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
            }
        });

        ivDateForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the date by +1
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
            }
        });

        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                intent.putExtra("_id",user_id);
                startActivity(intent);
            }
        });

        btnAddBurntCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CaloriesBurnedActivity.class);
                intent.putExtra("_id",user_id);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
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

    // Takes user to scaleInput
    protected void goToScaleInput(String user_id)
    {
        Intent intent = new Intent(this, ScaleInputActivity.class);
        intent.putExtra("_id", user_id);
        startActivity(intent);
    }

    // Takes user to BarcodeActivity
    protected void goToBarcodeActivity()
    {
        Intent intent = new Intent(this,BarcodeActivity.class);
        startActivity(intent);
    }

    // Prevents user from pressing back into the login screen
    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }


    public void getFoodDiary(final String user_id)
    {

        JSONObject jsonFood = new JSONObject();

        try
        {
            jsonFood.put("user_id", user_id);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/food/getdiary",
                jsonFood,
                this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println(result.toString());
                        JSONArray jsonArrayFoods = null;
                        double calorie=0;
                        JSONObject foodObj = null;
                        try {

                            jsonArrayFoods = result.getJSONArray("foodDiary");
                            for (int i=0; i<jsonArrayFoods.length(); i++) {
                                foodObj = (JSONObject) jsonArrayFoods.get(i);
                                calorie += Double.parseDouble(foodObj.getString("energy"));
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mAdapter = new RecyclerViewAdapterHome(jsonArrayFoods, HomeActivity.this, user_id);
                        recyclerView.setAdapter(mAdapter);

                        calorieAmount = findViewById(R.id.ha_amount);
                        calorieAmount.setText(""+calorie);
                        if (calorie>calorieLimit)
                        {
                            calorieAmount.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }
}
