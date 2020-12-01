package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.calorietracker.navigator.ActivityNavigator;
import com.example.calorietracker.volley.VolleyRequestContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import callbacks.IVolleyRequestCallback;

public class SettingsActivity extends AppCompatActivity {


    protected Spinner spinGender;
    protected EditText etAge;
    protected EditText etHeight;
    protected EditText etWeight;
    protected EditText etWeightLossWeekly;
    protected Spinner spinActivityLevel;
    protected EditText etProteinRatio;
    protected EditText etCarbsRatio;
    protected EditText etFatRatio;

    protected String sGender;
    protected int iAge;
    protected double dHeight;
    protected double dWeight;
    protected double dWeightLossWeekly;
    protected String sActivityLevel;
    protected int iProteinRatio;
    protected int iCarbsRatio;
    protected int iFatRatio;

    protected Button btnSave;
    protected Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab = findViewById(R.id.tbar_food);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llSettingsTab, llRecipeTab, llFloatingButton);

        spinGender = findViewById(R.id.sa_spin_gender);
        etAge = findViewById(R.id.sa_age);
        etHeight = findViewById(R.id.sa_height);
        etWeight = findViewById(R.id.sa_weight);
        etWeightLossWeekly = findViewById(R.id.sa_weight_loss_weekly);
        spinActivityLevel = findViewById(R.id.sa_spin_activity_level);
        etProteinRatio = findViewById(R.id.sa_protein_ratio);
        etCarbsRatio = findViewById(R.id.sa_carbs_ratio);
        etFatRatio = findViewById(R.id.sa_fat_ratio);

        btnSave = findViewById(R.id.sa_save);
        btnLogout = findViewById(R.id.sa_logout);

        getUser(user_id);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update database
                updateUser(user_id);

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getUser(final String user_id) {

        JSONObject userData = new JSONObject();
        try {
            userData.put("_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestContainer.request(Request.Method.POST,
                "/users/getuser/",
                userData,
                SettingsActivity.this,
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
                            iProteinRatio = (int) (user.getDouble("proteinratio") * 100);
                            iCarbsRatio = (int) (user.getDouble("carbsratio") * 100);
                            iFatRatio = (int) (user.getDouble("fatratio") * 100);

                            populatePage();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    public void populatePage() {
        populateServingSizeSpinner();

        List<String> genderOptions;
        List<String> activityLevelOptions;

        genderOptions = Arrays.asList((getResources().getStringArray(R.array.gender)));

        activityLevelOptions = Arrays.asList((getResources().getStringArray(R.array.activityLevel)));

        spinGender.setSelection(genderOptions.indexOf(sGender));
        spinActivityLevel.setSelection(activityLevelOptions.indexOf(sActivityLevel));

        etAge.setText(Integer.toString(iAge));
        etHeight.setText(Double.toString(dHeight));
        etWeight.setText(Double.toString(dWeight));
        etWeightLossWeekly.setText(Double.toString(dWeightLossWeekly));

        etProteinRatio.setText(Integer.toString(iProteinRatio));
        etCarbsRatio.setText(Integer.toString(iCarbsRatio));
        etFatRatio.setText(Integer.toString(iFatRatio));

    }

    public void populateServingSizeSpinner() {

        ArrayAdapter<CharSequence> spinAdapterGender = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);

        spinAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(spinAdapterGender);

        ArrayAdapter<CharSequence> spinAdapterActivity = ArrayAdapter.createFromResource(this,
                R.array.activityLevel, android.R.layout.simple_spinner_item);

        spinAdapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinActivityLevel.setAdapter(spinAdapterActivity);

    }

    public void updateUser(final String user_id)
    {
        String sGender = spinGender.getSelectedItem().toString();
        String sHeight = etHeight.getText().toString();
        String sWeight = etWeight.getText().toString();
        String sWeightLossWeekly = etWeightLossWeekly.getText().toString();
        String sActivityLevel = spinActivityLevel.getSelectedItem().toString();

        String sProteinRatio = etProteinRatio.getText().toString();
        String sCarbsRatio = etCarbsRatio.getText().toString();
        String sFatRatio = etFatRatio.getText().toString();

        if (sHeight.equals("") || sWeight.equals("") || sWeightLossWeekly.equals("") || etAge.getText().toString().equals("") ||
            sProteinRatio.equals("") || sCarbsRatio.equals("") || sFatRatio.equals(""))
        {
            Toast.makeText(SettingsActivity.this, "One or more Empty Fields.", Toast.LENGTH_LONG);
        }
        else{

            int iAge = Integer.parseInt(etAge.getText().toString());
            double dHeight = Double.parseDouble(sHeight);
            double dWeight = Double.parseDouble(sWeight);
            double dWeightLossWeekly = Double.parseDouble(sWeightLossWeekly);

            double dProteinRatio = (double) Integer.parseInt(sProteinRatio) / 100;
            double dCarbsRatio = (double) Integer.parseInt(sCarbsRatio) / 100;
            double dFatRatio = (double) Integer.parseInt(sFatRatio) / 100;

            JSONObject jsonUpdatedUser = new JSONObject();

            try
            {
                jsonUpdatedUser.put("_id", user_id);
                jsonUpdatedUser.put("gender", sGender);
                jsonUpdatedUser.put("age", iAge);
                jsonUpdatedUser.put("height", dHeight);
                jsonUpdatedUser.put("weight", dWeight);
                jsonUpdatedUser.put("weightlossweekly", dWeightLossWeekly);
                jsonUpdatedUser.put("activitylevel", sActivityLevel);
                jsonUpdatedUser.put("proteinratio", dProteinRatio);
                jsonUpdatedUser.put("carbsratio", dCarbsRatio);
                jsonUpdatedUser.put("fatratio", dFatRatio);
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            VolleyRequestContainer.request(
                    Request.Method.POST,
                    "/users/updateaccount",
                    jsonUpdatedUser,
                    SettingsActivity.this,
                    new IVolleyRequestCallback() {
                        @SuppressLint({"SetTextI18n", "DefaultLocale"})
                        @Override
                        public void onSuccess(JSONObject result) {

                            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                            intent.putExtra("_id",user_id);
                            startActivity(intent);

                        }

                        @Override
                        public void onFailure(String result) {
                            // Failed
                        }
                    });

        }

    }
}
