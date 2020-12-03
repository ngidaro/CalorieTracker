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
//    double dProteinRatio = 0.15;
//    double dCarbsRatio = 0.05;
//    double dFatRatio = 0.80;

    double dProteinRatio = 0;
    double dCarbsRatio = 0;
    double dFatRatio = 0;

    // Quantity
    double dEnergy = 0;
    double dProtein = 0;
    double dCarbs = 0;
    double dFat = 0;

    String sGender;
    String sActivityLevel;
    double dWeight;
    double dHeight;
    double BMR=0;
    double dWeightLossWeekly;
    int iAge;

    TextView tvDiaryDate;
    ImageView ivDateBack;
    ImageView ivDateForward;
    ImageView ivDateToday;
    Calendar calendar;
    SimpleDateFormat dateFormat;

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
        LinearLayout llExerciseTab    = findViewById(R.id.tbar_exercise);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llExerciseTab, llSettingsTab, llRecipeTab, llFloatingButton);

        // Concat Adapter to combine foods and exercise adapters
        layoutManager = new LinearLayoutManager(this);
        rvConcat = findViewById(R.id.ha_diary_rv);
        rvConcat.setLayoutManager(layoutManager);

        tvDiaryDate = findViewById(R.id.ha_diary_date);
        ivDateBack = findViewById(R.id.ha_diary_date_back);
        ivDateForward = findViewById(R.id.ha_diary_date_forward);
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

        // Populate Page and Display the foods added by the user in a list
        getUser(user_id);

        ivDateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the date by -1
                calendar.add(Calendar.DATE, -1);
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
                getUser(user_id);
            }
        });

        ivDateForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the date by +1
                calendar.add(Calendar.DATE, 1);
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
                getUser(user_id);
            }
        });

        ivDateToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());
                tvDiaryDate.setText(date);
                getUser(user_id);
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

    // Prevents user from pressing back into the login screen
    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    public void getUser(final String user_id){

        JSONObject userData = new JSONObject();
        try
        {
            userData.put("_id", user_id);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(Request.Method.POST,
                "/users/getuser/",
                userData,
                HomeActivity.this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println(result);

                        try {
                            JSONObject user = result.getJSONObject("userdata");

                            sGender = user.getString("gender");
                            iAge = user.getInt("age");
                            dHeight = user.getDouble("height");
                            dWeight = user.getDouble("weight");
                            dWeightLossWeekly = user.getDouble("weightlossweekly");
                            sActivityLevel = user.getString("activitylevel");

                            dProteinRatio = Double.parseDouble(user.getString("proteinratio"));
                            dCarbsRatio = Double.parseDouble(user.getString("carbsratio"));
                            dFatRatio = Double.parseDouble(user.getString("fatratio"));


                            // Set Daily Calorie Limit
                            dCalorieGoal = 0;

                    //        sGender = "Male";
                    //        sActivityLevel = "hard exercise";
                    //        iAge = 23;
                    //        dWeight = 74.84; // in kilos
                    //        dHeight = 178; // in cm

                            // Formula for calorie limit obtained from: http://www.checkyourhealth.org/eat-healthy/cal_calculator.php

                            // Determining calorie limit for male
                            if (sGender.equals("Male"))
                            {
                                // Formula https://en.wikipedia.org/wiki/Harris–Benedict_equation
                                BMR = (10 * dWeight) + (6.25 * dHeight) - (5 * iAge) + 5;
                            }

                            // Determining calorie limit for female
                            else if (sGender.equals("Female"))
                            {
                                BMR = (10 * dWeight) + (6.25 * dHeight) - (5 * iAge) - 161;
                            }

                            if (sActivityLevel.equals("Little to Non-Active"))
                                dCalorieGoal = BMR*1.2- dWeightLossWeekly *1100;
                            if (sActivityLevel.equals("Lightly Active"))
                                dCalorieGoal = BMR*1.375- dWeightLossWeekly *1100;
                            if (sActivityLevel.equals("Moderately Active"))
                                dCalorieGoal = BMR*1.55- dWeightLossWeekly *1100;
                            if (sActivityLevel.equals("Very Active"))
                                dCalorieGoal = BMR*1.725- dWeightLossWeekly *1100;
                            if (sActivityLevel.equals("Extremely Active"))
                                dCalorieGoal = BMR*1.9- dWeightLossWeekly *1100;

                            getFoodDiary(user_id);

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }


    public void getFoodDiary(final String user_id)
    {

        dEnergy = 0;
        dProtein = 0;
        dCarbs = 0;
        dFat = 0;

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
