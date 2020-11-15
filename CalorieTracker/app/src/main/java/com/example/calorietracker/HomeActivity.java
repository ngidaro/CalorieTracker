package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.calorietracker.navigator.ActivityNavigator;
import com.example.calorietracker.volley.VolleyRequestContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomAdapters.RecyclerViewAdapter;
import CustomAdapters.RecyclerViewAdapterHome;
import callbacks.IVolleyRequestCallback;

public class HomeActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    protected TextView calorieAmount;

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

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llBarcodeTab, llRecipeTab);

        // Setting the RecyclerView
        recyclerView = findViewById(R.id.ha_diary_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Display the foods added by the user in a list
        getFoodDiary(user_id);

        // Buton to go to scale input
        llFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goToScaleInput();
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
    protected void goToScaleInput()
    {
        Intent intent = new Intent(this,scaleInput.class);
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



                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }
}
