package com.example.calorietracker;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Recipe {

    private String recipeName;
    private ArrayList<String> recipeIngredients;
    private ArrayList<Step> recipeSteps;
    private String recipeTime;

    public Recipe(String name, ArrayList<String> ingredients) {
        recipeName = name;
        recipeIngredients = ingredients;
    }

    public void setRecipeSteps(ArrayList<Step> steps)
    {
        recipeSteps = steps;
        int hour=0;
        int minute=0;
        int second=0;

        for (int i=0; i<recipeSteps.size();i++) {
            hour += recipeSteps.get(i).getStepHour();
            minute += recipeSteps.get(i).getStepMinute();
            second += recipeSteps.get(i).getStepSecond();
        }

        int minuteSecond = second/60;
        int secondSecond = second % 60;

        second = secondSecond;
        minute += minuteSecond;

        int hourMinute = minute/60;
        int minuteMinute = minute % 60;

        minute = minuteMinute;
        hour += hourMinute;

        if (hour/10 == 0)
        {
            if(minute/10 == 0)
            {
                if(second/10 == 0)
                    recipeTime = "0"+hour+":0"+minute+":0"+second;
                recipeTime = "0"+hour+":0"+minute+":"+second;
            }

            else if(second/10 == 0)
                recipeTime = "0"+hour+":"+minute+":0"+second;
            else
                recipeTime = "0"+hour+":"+minute+":"+second;
        }

        else
        {
            if(minute/10 == 0)
            {
                if(second/10 == 0)
                    recipeTime = ""+hour+":0"+minute+":0"+second;
                recipeTime = ""+hour+":0"+minute+":"+second;
            }

            else if(second/10 == 0)
                recipeTime = ""+hour+":"+minute+":0"+second;
            else
                recipeTime = ""+hour+":"+minute+":"+second;
        }

    }

    public String getRecipeName() {return recipeName;}
    public String getRecipeTime() {return recipeTime;}
}
