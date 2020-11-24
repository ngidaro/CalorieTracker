package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import CustomAdapters.RecyclerViewAdapter;
import CustomAdapters.RecyclerViewAdapterHome;
import CustomAdapters.RecyclerViewAdapterHomeExercise;
import callbacks.IVolleyRequestCallback;

public class HomeActivity extends AppCompatActivity {

    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.Adapter mAdapterExercise;

    protected RecyclerView.LayoutManager layoutManager;
    protected RecyclerView rvConcat;
    protected ConcatAdapter concatAdapter;

    protected double dCalorieGoal;
    protected double dProteinGoal;
    protected double dCarbsGoal;
    protected double dFatGoal;

    // Macro Nutrient ratios:
    double dProteinRatio = 0.15;
    double dCarbsRatio = 0.05;
    double dFatRatio = 0.80;

    // Quantity
    double dEnergy = 0;
    double dProtein = 0;
    double dCarbs = 0;
    double dFat = 0;

    TextView tvDiaryDate;
    ImageView ivDateBack;
    ImageView ivDateForward;
    ImageView ivDateToday;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    Button btnAddBurntCalories;

    ProgressBar pbEnergy;
    ProgressBar pbProtein;
    ProgressBar pbCarbs;
    ProgressBar pbFat;

    TextView tvEnergyValue;
    TextView tvProteinValue;
    TextView tvCarbsValue;
    TextView tvFatValue;

    TextView tvEnergyGoal;
    TextView tvProteinGoal;
    TextView tvCarbsGoal;
    TextView tvFatGoal;

    TextView tvEnergyPercent;
    TextView tvProteinPercent;
    TextView tvCarbsPercent;
    TextView tvFatPercent;

    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llSettingsTab, llRecipeTab, llFloatingButton);

        // Concat Adapter to combine foods and exercise adapters
        layoutManager = new LinearLayoutManager(this);
        rvConcat = findViewById(R.id.ha_diary_rv);
        rvConcat.setLayoutManager(layoutManager);

        tvDiaryDate = findViewById(R.id.ha_diary_date);
        ivDateBack = findViewById(R.id.ha_diary_date_back);
        ivDateForward = findViewById(R.id.ha_diary_date_forward);
        btnAddBurntCalories = findViewById(R.id.ha_add_burnt_calories);
        ivDateToday = findViewById(R.id.ha_today);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE MMM d, yyyy");
        String date = dateFormat.format(calendar.getTime());
        tvDiaryDate.setText(date);

        // Initialize id for the progress bar:

        pbEnergy = findViewById(R.id.pbm_energy);
        pbProtein = findViewById(R.id.pbm_protein);
        pbCarbs = findViewById(R.id.pbm_carbs);
        pbFat = findViewById(R.id.pbm_fat);

        tvEnergyValue = findViewById(R.id.pbm_energy_value);
        tvProteinValue = findViewById(R.id.pbm_protein_value);
        tvCarbsValue = findViewById(R.id.pbm_carbs_value);
        tvFatValue = findViewById(R.id.pbm_fat_value);

        tvEnergyGoal = findViewById(R.id.pbm_energy_goal);
        tvProteinGoal = findViewById(R.id.pbm_protein_goal);
        tvCarbsGoal = findViewById(R.id.pbm_carbs_goal);
        tvFatGoal = findViewById(R.id.pbm_fat_goal);

        tvEnergyPercent = findViewById(R.id.pbm_energy_percent);
        tvProteinPercent = findViewById(R.id.pbm_protein_percent);
        tvCarbsPercent = findViewById(R.id.pbm_carbs_percent);
        tvFatPercent = findViewById(R.id.pbm_fat_percent);

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
        dCalorieGoal =0;

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
                dCalorieGoal = BMR*1.2-weightLossWeekly*500;
            else if (activityLevel.equals("light exercise"))
                dCalorieGoal = BMR*1.375-weightLossWeekly*500;
            else if (activityLevel.equals("moderate exercise"))
                dCalorieGoal = BMR*1.55-weightLossWeekly*500;
            else if (activityLevel.equals("hard exercise"))
                dCalorieGoal = BMR*1.725-weightLossWeekly*500;
            else if (activityLevel.equals("very hard exercise"))
                dCalorieGoal = BMR*1.9-weightLossWeekly*500;
        }

        // Determining calorie limit for female
        if (gender.equals("Female"))
        {
            BMR = 665 + (4.3*weight) + 4.7*height - 4.7*age;

            if (activityLevel.equals("little or no exercise"))
                dCalorieGoal = BMR*1.2-weightLossWeekly*500;
            if (activityLevel.equals("light exercise"))
                dCalorieGoal = BMR*1.375-weightLossWeekly*500;
            if (activityLevel.equals("moderate exercise"))
                dCalorieGoal = BMR*1.55-weightLossWeekly*500;
            if (activityLevel.equals("hard exercise"))
                dCalorieGoal = BMR*1.725-weightLossWeekly*500;
            if (activityLevel.equals("very hard exercise"))
                dCalorieGoal = BMR*1.9-weightLossWeekly*500;
        }

        setMacros(dCalorieGoal);

        // Button to go to scale input
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
                calendar.add(Calendar.DATE, -1);
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
                getFoodDiary(user_id);
            }
        });

        ivDateForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the date by +1
                calendar.add(Calendar.DATE, 1);
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
                getFoodDiary(user_id);
            }
        });

        ivDateToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
                getFoodDiary(user_id);
            }
        });

        llSettingsTab.setOnClickListener(new View.OnClickListener() {
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
            jsonFood.put("date", calendar.getTime());
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
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println(result.toString());
                        JSONArray jsonArrayFoods = null;
                        JSONObject foodObj = null;

                        try {

                            jsonArrayFoods = result.getJSONArray("foodDiary");

                            for (int i=0; i<jsonArrayFoods.length(); i++) {
                                foodObj = (JSONObject) jsonArrayFoods.get(i);

                                // Sum the total macros and calories of all foods in the list for that day
                                dEnergy += Double.parseDouble(foodObj.getString("energy"));
                                dProtein += Double.parseDouble(foodObj.getString("protein"));
                                dCarbs += Double.parseDouble(foodObj.getString("carbohydrates"));
                                dFat += Double.parseDouble(foodObj.getString("fat"));
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mAdapter = new RecyclerViewAdapterHome(jsonArrayFoods, HomeActivity.this, user_id);

                        populateProgressBar();

                        getExerciseDiary(user_id);

                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

    public void setMacros( double calGoal )
    {
        // Calculate the macronutrients based on the ratios given
        dCalorieGoal = calGoal;
        dProteinGoal = (dCalorieGoal * dProteinRatio) / 4;
        dCarbsGoal = (dCalorieGoal * dCarbsRatio) / 4;
        dFatGoal = (dCalorieGoal * dFatRatio) / 9;

        // Set the maximum for the Progress Bars
        tvEnergyGoal.setText(String.format("%.1f", dCalorieGoal));
        pbEnergy.setMax((int)(dCalorieGoal));

        tvProteinGoal.setText(String.format("%.1f", dProteinGoal));
        pbProtein.setMax((int)(dProteinGoal));

        tvCarbsGoal.setText(String.format("%.1f", dCarbsGoal));
        pbCarbs.setMax((int)(dCarbsGoal));

        tvFatGoal.setText(String.format("%.1f", dFatGoal));
        pbFat.setMax((int)(dFatGoal));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void populateProgressBar(){

        tvEnergyValue.setText(String.format("%.1f", dEnergy));
        pbEnergy.setProgress((int)dEnergy);
        tvEnergyPercent.setText(String.format("%.0f", (dEnergy/ dCalorieGoal)*100) + " %");

        tvProteinValue.setText(String.format("%.1f", dProtein));
        pbProtein.setProgress((int)dProtein);
        tvProteinPercent.setText(String.format("%.0f", (dProtein/ dProteinGoal)*100) + " %");

        tvCarbsValue.setText(String.format("%.1f", dCarbs));
        pbCarbs.setProgress((int)(dCarbs));
        tvCarbsPercent.setText(String.format("%.0f", ((dCarbs)/ dCarbsGoal)*100) + " %");

        tvFatValue.setText(String.format("%.1f", dFat));
        pbFat.setProgress((int)dFat);
        tvFatPercent.setText(String.format("%.0f", (dFat/ dFatGoal)*100) + " %");

    }

    public void getExerciseDiary(final String user_id)
    {

        JSONObject jsonExercise = new JSONObject();

        try
        {
            jsonExercise.put("user_id", user_id);
            jsonExercise.put("date", calendar.getTime());
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/exercise/getexercise",
                jsonExercise,
                this,
                new IVolleyRequestCallback() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println(result.toString());

                         JSONArray jsonArrayExercises = null;
                         int iCaloriesBurned = 0;

                         try {
                             jsonArrayExercises = result.getJSONArray("exercise");

                             for (int i = 0; i < jsonArrayExercises.length(); i++)
                             {
                                 JSONObject jsonExercise = (JSONObject) jsonArrayExercises.get(i);
                                 iCaloriesBurned += jsonExercise.getInt("caloriesburned");
                             }

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                         System.out.println(iCaloriesBurned);

                         setMacros(dCalorieGoal+iCaloriesBurned);

                        mAdapterExercise = new RecyclerViewAdapterHomeExercise(jsonArrayExercises);

                        concatAdapter = new ConcatAdapter(mAdapter, mAdapterExercise);
                        rvConcat.setAdapter(concatAdapter);

                        populateProgressBar();

                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

}
