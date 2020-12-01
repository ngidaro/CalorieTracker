package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomAdapters.RecyclerViewAdapter;
import callbacks.IVolleyRequestCallback;

public class AddFoodActivity extends AppCompatActivity {

    EditText etSearch;
    TextView tvRecentResult;

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        final String user_id = getIntent().getStringExtra("_id");
        final String recipe_id = getIntent().getStringExtra("recipe_id");

        etSearch = findViewById(R.id.afa_search);
        tvRecentResult = findViewById(R.id.afa_rec_res);

        // Setting the RecyclerView
        recyclerView = findViewById(R.id.afa_search_rv);
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
                        AddFoodActivity.this,
                        new IVolleyRequestCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
//                                        System.out.println(result.toString());
                                    JSONArray res = result.getJSONArray("foods");

                                    mAdapter = new RecyclerViewAdapter(res, AddFoodActivity.this, user_id, recipe_id);
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