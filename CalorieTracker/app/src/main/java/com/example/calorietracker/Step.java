package com.example.calorietracker;

import java.sql.Time;
import java.util.ArrayList;

public class Step {

    private Recipe stepRecipe;
    private ArrayList<String> stepIngredients;
    private int stepMinute;
    private int stepHour;
    private int stepSecond;
    private String stepInstruction;

    public Step(Recipe recipe, ArrayList<String> ingredients, int hour, int minute, int second, String instruction)
    {
        stepRecipe = recipe;
        stepIngredients = ingredients;
        stepInstruction = instruction;

        stepHour = hour;
        stepMinute = minute;
        stepSecond = second;
    }

    public int getStepMinute() {return  stepMinute;}
    public int getStepHour() {return stepHour;}
    public int getStepSecond() {return stepSecond;}

    public Recipe getStepRecipe() {return stepRecipe;}
    public ArrayList<String> getStepIngredients() {return stepIngredients;}
    public String getStepInstruction() {return stepInstruction;}
}
