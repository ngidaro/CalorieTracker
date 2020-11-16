package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddRecipeActivity extends AppCompatActivity {

    LinearLayout llAddIngredient;
    EditText etRecipeName;
    Button btnSaveRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        llAddIngredient = findViewById(R.id.ar_add_ingredient);
        etRecipeName = findViewById(R.id.ar_recipe_name);
        btnSaveRecipe = findViewById(R.id.ar_save_button);

        llAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Food search page
                // select food
                // Enter food quantity
                // Save the food
                // Food gets placed into listview in recipe page
            }
        });

        btnSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sRecipeName = etRecipeName.getText().toString();

                // What to store in DB:
                // Recipe Name
                // Object Array containing food id with serving size and amount

            }
        });

        final String user_id = getIntent().getStringExtra("_id");
    }
}