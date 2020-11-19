package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.example.calorietracker.navigator.ActivityNavigator;
import com.example.calorietracker.volley.VolleyRequestContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomAdapters.RecyclerViewAdapter;
import callbacks.IVolleyRequestCallback;

public class ScaleInputActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_input);

        final String user_id = getIntent().getStringExtra("_id");

        // Code to Switch Activities

        LinearLayout llHomeTab    = findViewById(R.id.tbar_home);
        LinearLayout llFoodTab    = findViewById(R.id.tbar_food);
        LinearLayout llSettingsTab = findViewById(R.id.tbar_settings);
        LinearLayout llRecipeTab  = findViewById(R.id.tbar_recipe);
        FloatingActionButton llFloatingButton = findViewById(R.id.floating_action_button);

        ActivityNavigator.changeActivity(this, user_id, llHomeTab, llFoodTab, llSettingsTab, llRecipeTab, llFloatingButton);

        // Setting the RecyclerView
        recyclerView = findViewById(R.id.isa_search_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        etSearch = findViewById(R.id.isa_search);

        displaySearchFields(user_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.addfoodmenu, menu);
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


    public void displaySearchFields(final String user_id){

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {

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
                        ScaleInputActivity.this,
                        new IVolleyRequestCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
//                                        System.out.println(result.toString());
                                    JSONArray res = result.getJSONArray("foods");

                                    mAdapter = new RecyclerViewAdapter(res, ScaleInputActivity.this, user_id);
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