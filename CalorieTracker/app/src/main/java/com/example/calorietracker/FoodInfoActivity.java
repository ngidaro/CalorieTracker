package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import callbacks.IVolleyRequestCallback;

public class FoodInfoActivity extends AppCompatActivity {

    ImageView ivExit;
    TextView tvFood;
    TextView tvServingSizeUnits;
    Spinner spinServingSize;
    EditText etAmount;
    Button btnAddToDiary;
    JSONObject jsonFoodObj;

    @SuppressLint("SetTextI18n")
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
        final String prevApplicationContext = getIntent().getStringExtra("prevViewName");


        assert prevApplicationContext != null;
        if(prevApplicationContext.equals("AddFoodActivity"))
        {
            btnAddToDiary.setText("Add Ingredient");
        }

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
                    double dAmount = 0;
                    double dEnergy = 0; // id 1008 - "name": "Energy"
                    double dProtein = 0;    // id 1003 - "name": "Protein"
                    double dCarbs = 0;      // id 1005 - "name": "Carbohydrate, by difference"
                    double dFat = 0;        // id 1004 - "name": "Total lipid (fat)"
                    double dFiber = 0;      // id 1079 - "name": "Fiber, total dietary"

                    if ( etAmount.getText().toString().equals("")){
                        Toast.makeText(FoodInfoActivity.this, "Invalid Amount", Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        dAmount = Double.parseDouble(etAmount.getText().toString());

                        // FORMULA FOR CALCULATING NUTRIENT RATIOS:
                        // (Nutrient Amount)*(Amount entered by user)*(Serving Size option chosen by user) / (Serving Size) = x Serving Size Units

                        if(prevApplicationContext.equals("AddFoodActivity"))
                            addToIngredient(dAmount, dEnergy, dProtein, dCarbs, dFat, dServingSize, user_id, fdcId);
                        else
                            addToDiary(dAmount, dEnergy, dProtein, dCarbs, dFat, dServingSize, user_id, fdcId);

                    }

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

    public void addToDiary(double dAmount, double dEnergy, double dProtein, double dCarbs, double dFat, double dServingSize, final String user_id, String fdcId)
    {
        JSONObject jsonFood = new JSONObject();

        try
        {

//                            JSONArray arrTemp = jsonFoodObj.getJSONArray("foodNutrients");
//                            JSONObject jsonFoodNutrientObj = null;
//                            JSONObject jsonEnergyObj;
//
//                            for (int i = 0; i < arrTemp.length(); i++){
//                                jsonFoodNutrientObj = (JSONObject) arrTemp.get(i);
//                                jsonEnergyObj = jsonFoodNutrientObj.getJSONObject("nutrient"); // returns nutrient Object
//
//                                System.out.println(jsonEnergyObj.getString("id"));
//
//                                switch(jsonEnergyObj.getString("id")) {
//                                    case "1008":
//                                        dEnergy = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1003":
//                                        dProtein = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1005":
//                                        dCarbs = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1004":
//                                        dFat = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1079":
//                                        dFiber = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }

            JSONObject jsonLabelNutrientObj = jsonFoodObj.getJSONObject("labelNutrients");

            JSONObject jsonNutrient = jsonLabelNutrientObj.getJSONObject("calories");
            dEnergy = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("protein");
            dProtein = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("carbohydrates");
            dCarbs = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("fat");
            dFat = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonFood.put("fdcId", fdcId);
            jsonFood.put("date", Calendar.getInstance().getTime());
            jsonFood.put("user_id", user_id);
            jsonFood.put("amount", dAmount);
            jsonFood.put("servingsize", dServingSize);
            jsonFood.put("servingunits", jsonFoodObj.getString("servingSizeUnit"));
            jsonFood.put("description", jsonFoodObj.getString("description"));
            jsonFood.put("brandowner", jsonFoodObj.getString("brandOwner"));

            jsonFood.put("energy", (dEnergy*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize")))); // Nutrient Id for Energy is 1008 -> see nutrient.csv
            jsonFood.put("protein", (dProtein*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonFood.put("carbs", (dCarbs*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonFood.put("fat", (dFat*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
//                            jsonFood.put("fiber", (dFiber*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
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

    public void addToIngredient(double dAmount, double dEnergy, double dProtein, double dCarbs, double dFat, double dServingSize, final String user_id, String fdcId)
    {
        final String recipeId = getIntent().getStringExtra("recipe_id");
        // Save the recipe to the database

        JSONObject jsonIngredient = new JSONObject();

        try
        {

            JSONObject jsonLabelNutrientObj = jsonFoodObj.getJSONObject("labelNutrients");

            JSONObject jsonNutrient = jsonLabelNutrientObj.getJSONObject("calories");
            dEnergy = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("protein");
            dProtein = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("carbohydrates");
            dCarbs = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("fat");
            dFat = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonIngredient.put("fdcId", fdcId);
            jsonIngredient.put("recipe_id", recipeId);
            jsonIngredient.put("user_id", user_id);
            jsonIngredient.put("amount", dAmount);
            jsonIngredient.put("servingsize", dServingSize);
            jsonIngredient.put("servingunits", jsonFoodObj.getString("servingSizeUnit"));
            jsonIngredient.put("description", jsonFoodObj.getString("description"));
            jsonIngredient.put("brandowner", jsonFoodObj.getString("brandOwner"));

            jsonIngredient.put("energy", (dEnergy*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize")))); // Nutrient Id for Energy is 1008 -> see nutrient.csv
            jsonIngredient.put("protein", (dProtein*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonIngredient.put("carbs", (dCarbs*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonIngredient.put("fat", (dFat*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
//                            jsonFood.put("fiber", (dFiber*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        // Store data in database:

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/ingredient/addingredient",
                jsonIngredient,
                FoodInfoActivity.this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        Intent intent = new Intent(FoodInfoActivity.this, AddIngredientActivity.class);
                        intent.putExtra("_id",user_id);
                        intent.putExtra("recipe_id",recipeId);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });

    }
}