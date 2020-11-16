package com.example.calorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private Context recipeContext;
    ArrayList<Recipe> recipeList;
    int resources;

    public RecipeAdapter(Context context, int resource, ArrayList recipes) {
        super(context, resource,recipes);
        recipeContext = context;
        recipeList = recipes;
        resources = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String recipeName = getItem(position).getRecipeName();

        String recipeTime = getItem(position).getRecipeTime();

        LayoutInflater inflater = LayoutInflater.from(recipeContext);
        convertView = inflater.inflate(resources,parent,false);

        TextView textRecipeName = (TextView) convertView.findViewById(R.id.ri_recipe_name);
        TextView textRecipeTime = (TextView) convertView.findViewById(R.id.ri_timer);

        textRecipeName.setText(recipeName);
        textRecipeTime.setText(recipeTime);


        return convertView;

    }
}
