package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class YourRecipeActivity extends AppCompatActivity {

    protected Button stepButton;

    protected TextView recipeName;
    protected TextView cookingTime;
    protected TextView ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recipe);

        recipeName = findViewById(R.id.yra_recipe_name);
        cookingTime = findViewById(R.id.yra_cooking_time);
        ingredients = findViewById(R.id.yra_recipe_ingredients);

        String recipe_name = "Fruit Salad";
        ArrayList<String> recipeIngredients = new ArrayList<String>();

        recipeIngredients.add("Apple");
        recipeIngredients.add("Banana");
        recipeIngredients.add("Mango");



        final Recipe recipe = new Recipe(recipe_name,recipeIngredients);

        String instruction1 = "Cut Apple";
        ArrayList<String> step1Ingredients = new ArrayList<String>();
        step1Ingredients.add("Apple");

        String instruction2 = "Cut Banana";
        ArrayList<String> step2Ingredients = new ArrayList<String>();
        step2Ingredients.add("Banana");

        String instruction3 = "Cut Mango";
        ArrayList<String> step3Ingredients = new ArrayList<String>();
        step3Ingredients.add("Mango");

        String instruction4 = "Mix together";
        ArrayList<String> step4Ingredients = new ArrayList<String>();
        step4Ingredients.add("Apple");
        step4Ingredients.add("Banana");
        step4Ingredients.add("Mango");


        Step step = new Step(recipe,step1Ingredients,0,5,0,instruction1);
        Step step2 = new Step(recipe,step2Ingredients,0,1,0,instruction2);
        Step step3 = new Step(recipe,step3Ingredients,0,3,0,instruction3);
        Step step4 = new Step(recipe,step4Ingredients,0,1,0,instruction4);

        ArrayList<Step> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step2);
        steps.add(step3);
        steps.add(step4);

        recipe.setRecipeSteps(steps);

        recipeName.setText(recipe.getRecipeName());
        cookingTime.setText(recipe.getRecipeTime());

        int size = recipeIngredients.size();

        for (int i=0; i<size;i++)
            ingredients.append("\n"+recipeIngredients.get(i));

        stepButton = findViewById(R.id.yra_view_steps);

        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewRecipeActivity(recipe.getRecipeName());
            }
        });
    }

    public  void goToViewRecipeActivity(String recipeName)
    {
        Intent intent = new Intent(this,ViewRecipeActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("KEY",recipeName);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}