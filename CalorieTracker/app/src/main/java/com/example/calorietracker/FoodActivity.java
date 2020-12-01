package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.calorietracker.navigator.ActivityNavigator;
import com.example.calorietracker.volley.VolleyRequestContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomAdapters.RecyclerViewAdapter;
import callbacks.IVolleyRequestCallback;

public class FoodActivity extends AppCompatActivity {

    EditText etSearch;
    TextView tvRecentResult;

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llSettingsTab, llRecipeTab, llFloatingButton);

        etSearch = findViewById(R.id.fa_search);
        tvRecentResult = findViewById(R.id.fa_rec_res);

        // Setting the RecyclerView
        recyclerView = findViewById(R.id.fa_search_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {

                // When food is being searched, change text from recent to results
                tvRecentResult.setText("Results");

                JSONObject jsonSearch = new JSONObject();
                try
                {
                    jsonSearch.put("search", etSearch.getText().toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

                VolleyRequestContainer.request(
                        Request.Method.POST,
                        "/food/",
                        jsonSearch,
                        FoodActivity.this,
                        new IVolleyRequestCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
//                                        System.out.println(result.toString());
                                    JSONArray res = result.getJSONArray("foods");

                                    mAdapter = new RecyclerViewAdapter(res, FoodActivity.this, user_id, "");
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
        });
    }
}