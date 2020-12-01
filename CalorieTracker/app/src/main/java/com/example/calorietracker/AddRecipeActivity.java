package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import callbacks.IVolleyRequestCallback;

public class AddRecipeActivity extends AppCompatActivity {

    protected Button btnContinue;
    protected EditText etRecipeName;
    protected ImageView ivExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        final String user_id = getIntent().getStringExtra("_id");

        btnContinue= findViewById(R.id.ar_continue);
        etRecipeName = findViewById(R.id.ar_recipe_name);
        ivExit = findViewById(R.id.ar_exit);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonRecipe = new JSONObject();

                // Check if the Recipe name field is populated
                if(etRecipeName.getText().toString().equals(""))
                {
                    // Empty fields
                    Toast.makeText(AddRecipeActivity.this,"Empty Field", Toast.LENGTH_LONG);
                }
                else
                {
                    try
                    {
                        jsonRecipe.put("recipename", etRecipeName.getText().toString());
                        jsonRecipe.put("user_id", user_id);

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                    // Store data in database:

                    VolleyRequestContainer.request(
                            Request.Method.POST,
                            "/recipe/addrecipe",
                            jsonRecipe,
                            AddRecipeActivity.this,
                            new IVolleyRequestCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {


                                    try {
                                        System.out.println(result.getString("_id"));

                                        Intent intent = new Intent(AddRecipeActivity.this, AddIngredientActivity.class);
                                        intent.putExtra("_id",user_id);
                                        intent.putExtra("recipe_id", result.getString("_id"));
                                        startActivity(intent);

                                    }catch (JSONException e)
                                    {
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
        });

    }
}