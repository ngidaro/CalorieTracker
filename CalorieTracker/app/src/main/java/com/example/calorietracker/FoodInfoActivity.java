package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import callbacks.IVolleyRequestCallback;

public class FoodInfoActivity extends AppCompatActivity {

    ImageView ivExit;
    TextView tvFood;
    TextView tvServingSizeUnits;
    Spinner spinServingSize;
    EditText etAmount;
    Button btnAddToDiary;
    JSONObject jsonFoodObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        ivExit = findViewById(R.id.fia_exit);
        tvFood = findViewById(R.id.fia_food);
        spinServingSize = findViewById(R.id.fia_spin_serving_size);
        tvServingSizeUnits = findViewById(R.id.fia_serving_size_units);
        etAmount = findViewById(R.id.fia_amount);
        btnAddToDiary = findViewById(R.id.fia_add_to_diary);

        final String fdcId = getIntent().getStringExtra("fdcId");
        final String user_id = getIntent().getStringExtra("_id");

        System.out.println(user_id);

        getFoodData(fdcId);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodInfoActivity.super.onBackPressed();
            }
        });

        btnAddToDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsonFoodObj != null){

                    double dServingSize = Double.parseDouble(spinServingSize.getSelectedItem().toString());
                    double dEnergy;

                    double dAmount = Double.parseDouble(etAmount.getText().toString());

                    // Will have to perform calculation to get exact energy:
                    // FORMULA FOR CALCULATING NUTRIENT RATIOS:
                    // (Nutrient Amount)*(Amount entered by user)*(Serving Size option chosen by user) / (Serving Size) = x Serving Size Units

                    JSONObject jsonFood = new JSONObject();

                    try
                    {

                        JSONArray arrTemp = jsonFoodObj.getJSONArray("foodNutrients");
                        String key = "Energy";
                        JSONObject jsonFoodNutrientObj = null;
                        JSONObject jsonEnergyObj;

                        for (int i = 0; i < arrTemp.length(); i++){
                            jsonFoodNutrientObj = (JSONObject) arrTemp.get(i);
                            jsonEnergyObj = jsonFoodNutrientObj.getJSONObject("nutrient"); // returns nutrient Object

                            if (jsonEnergyObj.get("name").toString().equals( key )){
                                break;
                            }
                        }

                        // jsonFoodNutrientObj:
                        /*{
                            "type":"FoodNutrient",
                             "nutrient":
                                {
                                    "id":1008,
                                    "number":"208",
                                    "name":"Energy",
                                    "rank":300,
                                    "isNutrientLabel":false,
                                    "indentLevel":1,
                                    "shortestName":"Energy",
                                    "nutrientUnit":
                                        {
                                            "name":"kcal",
                                             "aliases":[]
                                        }
                                },
                            "foodNutrientDerivation":
                                {
                                    "id":70,
                                    "code":"LCCS",
                                    "description":"Calculated from value per serving size measure"
                                },
                            "id":5502949,
                            "amount":242,
                            "percentDailyValue":0
                        }*/

                        assert jsonFoodNutrientObj != null;
                        dEnergy = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size

                        jsonFood.put("fdcId", fdcId);
                        jsonFood.put("date", Calendar.getInstance().getTime());
                        jsonFood.put("user_id", user_id);
                        jsonFood.put("amount", dAmount);
                        jsonFood.put("servingsize", dServingSize);
                        jsonFood.put("servingunits", jsonFoodObj.getString("servingSizeUnit"));
                        jsonFood.put("description", jsonFoodObj.getString("description"));
                        jsonFood.put("brandowner", jsonFoodObj.getString("brandOwner"));
                        jsonFood.put("energy", (dEnergy*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize")))); // Nutrient Id for Energy is 1008 -> see nutrient.csv
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                    // Store data in database:

                    VolleyRequestContainer.request(
                            Request.Method.POST,
                            "/food/savediary",
                            jsonFood,
                            FoodInfoActivity.this,
                            new IVolleyRequestCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    System.out.println(result.toString());
                                    Intent intent = new Intent(FoodInfoActivity.this, HomeActivity.class);
                                    intent.putExtra("_id",user_id);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(String result) {
                                    // Failed
                                }
                            });
                }
                else {
                    // Error Occurred
                }
            }
        });

    }

    protected void getFoodData(String fdcId){

        JSONObject jsonFood = new JSONObject();
        try
        {
            jsonFood.put("fdcId", fdcId);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/food/fooddata",
                jsonFood,
                this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                          System.out.println(result.toString());
                          jsonFoodObj = result;
                          tvFood.setText(result.getString("description"));

                          try {
                            populateServingSizeSpinner(result);

                          }catch (JSONException e){
                              e.printStackTrace();
                          }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

    public void populateServingSizeSpinner(JSONObject result) throws JSONException {

        ArrayList<String> alServingSizes = new ArrayList<>();

        if(result.has("servingSize"))
        {
            alServingSizes.add(result.getString("servingSize"));
        }

        alServingSizes.add("1");
        tvServingSizeUnits.setText(result.getString("servingSizeUnit"));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, alServingSizes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinServingSize.setAdapter(spinnerAdapter);

    }
}