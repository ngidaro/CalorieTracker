package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomAdapters.RecyclerViewAdapter;
import CustomAdapters.RecyclerViewAdapterIngredients;
import callbacks.IVolleyRequestCallback;

public class AddIngredientActivity extends AppCompatActivity {

    protected LinearLayout llAddIngredient;
    protected Button btnSaveRecipe;
    protected ImageView ivExit;
    protected TextView tvRecipeName;

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        final String recipe_id = getIntent().getStringExtra("recipe_id");
        final String user_id = getIntent().getStringExtra("_id");

        llAddIngredient = findViewById(R.id.ar_add_ingredient);
        btnSaveRecipe = findViewById(R.id.ar_save_button);
        ivExit = findViewById(R.id.ar_exit);
        tvRecipeName = findViewById(R.id.aia_recipe_name);

        // Setting the RecyclerView
        recyclerView = findViewById(R.id.ra_ingredients_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get recipe
        getRecipe(recipe_id);

        llAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddIngredientActivity.this, AddFoodActivity.class);
                intent.putExtra("_id", user_id);
                intent.putExtra("recipe_id", recipe_id);
                startActivity(intent);

            }
        });

        btnSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddIngredientActivity.this, RecipeActivity.class);
                intent.putExtra("_id", user_id);
                startActivity(intent);

            }
        });
    }

    public void getRecipe(final String recipe_id)
    {
        JSONObject jsonRecipe = new JSONObject();
        try
        {
            jsonRecipe.put("recipe_id", recipe_id);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/recipe/getrecipe",
                jsonRecipe,
                AddIngredientActivity.this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {

                            JSONObject jsonRecipe = result.getJSONObject("recipe");

                            tvRecipeName.setText(jsonRecipe.getString("recipename"));

                            //Populate the recycler view with the ingredients
                            getIngredients(recipe_id);

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

    public void getIngredients(String recipe_id)
    {
        JSONObject jsonRecipe = new JSONObject();
        try
        {
            jsonRecipe.put("recipe_id", recipe_id);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/ingredient/getingredients",
                jsonRecipe,
                AddIngredientActivity.this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
//                                        System.out.println(result.toString());
                            JSONArray res = result.getJSONArray("ingredients");

                            //Populate recycler view:
                            mAdapter = new RecyclerViewAdapterIngredients(res, AddIngredientActivity.this);
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