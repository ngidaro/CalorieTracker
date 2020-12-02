package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import callbacks.IVolleyRequestCallback;

public class PopulateAccountActivity extends AppCompatActivity {

    protected Spinner spinGender;
    protected EditText etAge;
    protected EditText etHeight;
    protected EditText etWeight;
    protected EditText etWeightLossWeekly;
    protected Spinner spinActivityLevel;
    protected Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate_account);

        spinGender = findViewById(R.id.paa_spin_gender);
        etAge = findViewById(R.id.paa_age);
        etHeight = findViewById(R.id.paa_height);
        etWeight = findViewById(R.id.paa_weight);
        etWeightLossWeekly = findViewById(R.id.paa_weight_loss_weekly);
        spinActivityLevel = findViewById(R.id.paa_spin_activity_level);

        btnContinue = findViewById(R.id.paa_continue);

        populateServingSizeSpinner();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                String sGender = spinGender.getSelectedItem().toString();
                String sHeight = etHeight.getText().toString();
                String sWeight = etWeight.getText().toString();
                String sWeightLossWeekly = etWeightLossWeekly.getText().toString();
                String sActivityLevel = spinActivityLevel.getSelectedItem().toString();

                double dProteinRatio = 0.15;
                double dCarbsRatio = 0.05;
                double dFatRatio = 0.80;

                final String user_id = getIntent().getStringExtra("_id");

                if (sHeight.equals("") || sWeight.equals("") || sWeightLossWeekly.equals("") || etAge.getText().toString().equals(""))
                {
                    Toast.makeText(PopulateAccountActivity.this, "One or more Empty Fields.", Toast.LENGTH_LONG);
                }
                else{

                    int iAge = Integer.parseInt(etAge.getText().toString());
                    double dHeight = Double.parseDouble(sHeight);
                    double dWeight = Double.parseDouble(sWeight);
                    double dWeightLossWeekly = Double.parseDouble(sWeightLossWeekly);

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
                            PopulateAccountActivity.this,
                            new IVolleyRequestCallback() {
                                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                                @Override
                                public void onSuccess(JSONObject result) {

                                    Intent intent = new Intent(PopulateAccountActivity.this, InitiateBluetooth.class);
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
        });

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
}