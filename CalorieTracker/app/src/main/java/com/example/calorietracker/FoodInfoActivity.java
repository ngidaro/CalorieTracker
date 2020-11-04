package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomAdapters.RecyclerViewAdapter;
import callbacks.IVolleyRequestCallback;

public class FoodInfoActivity extends AppCompatActivity {

    ImageView ivExit;
    TextView tvFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        ivExit = findViewById(R.id.fia_exit);
        tvFood = findViewById(R.id.fia_food);

        String fdcId = getIntent().getStringExtra("fdcId");

        getFoodData(fdcId);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodInfoActivity.super.onBackPressed();
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
                          tvFood.setText(result.getString("description"));

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
}