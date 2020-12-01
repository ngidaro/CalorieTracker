package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomAdapters.RecyclerViewAdapterRecipe;
import callbacks.IVolleyRequestCallback;

public class MyRecipesActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    protected ImageView ivExit;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_recipe);

        final String user_id = getIntent().getStringExtra("_id");

        recyclerView = findViewById(R.id.yra_recipes);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ivExit = findViewById(R.id.yra_exit);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getRecipes(user_id);

    }

    public void getRecipes(final String user_id)
    {
        JSONObject jsonRecipe = new JSONObject();
        try
        {
            jsonRecipe.put("user_id", user_id);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/recipe/getuserrecipes",
                jsonRecipe,
                MyRecipesActivity.this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {

                            JSONArray jsonRecipes = result.getJSONArray("recipes");

                            //Populate the recycler view with the ingredients
                            mAdapter = new RecyclerViewAdapterRecipe(jsonRecipes, MyRecipesActivity.this);
                            recyclerView.setAdapter(mAdapter);

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