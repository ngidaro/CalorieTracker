package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewRecipeActivity extends AppCompatActivity {

    protected ListView recipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        Bundle bundle = getIntent().getExtras();
        String recipename = bundle.getString("KEY");

        recipeSteps = findViewById(R.id.recipeSteps);
        loadSteps();

    }

    public void loadSteps()
    {
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

        StepAdapter adapter = new StepAdapter(this,R.layout.activity_step_info,steps);
        recipeSteps.setAdapter(adapter);
    }
}