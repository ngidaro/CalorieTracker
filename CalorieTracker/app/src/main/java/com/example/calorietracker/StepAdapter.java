package com.example.calorietracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StepAdapter extends ArrayAdapter<Step> {

    private Context stepContext;
    ArrayList<Step> stepList;
    int resources;

    public StepAdapter(Context context, int resource, ArrayList<Step> steps)
    {
        super(context,resource,steps);
        stepContext = context;
        stepList = steps;
        resources = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String stepInstruction = getItem(position).getStepInstruction();
        int hour = getItem(position).getStepHour();
        int minute = getItem(position).getStepMinute();
        int second = getItem(position).getStepSecond();

        String recipeTime="";

        if (hour/10 == 0)
        {
            if(minute/10 == 0)
            {
                if(second/10 == 0 && second != 0)
                    recipeTime = "0"+hour+":0"+minute+":0"+second;
                else if(second == 0)
                    recipeTime = "0"+hour+":0"+minute+":00";
                else
                    recipeTime = "0"+hour+":0"+minute+":"+second;
            }

            else if(second == 0)
                recipeTime = "0"+hour+":"+minute+":00";
            else if(second/10 == 0 && second != 0)
                recipeTime = "0"+hour+":"+minute+":0"+second;
            else
                recipeTime = "0"+hour+":"+minute+":"+second;

        }

        else
        {
            if(minute/10 == 0)
            {
                if(second == 0)
                recipeTime = ""+hour+":0"+minute+":00";
                else if(second/10 == 0 && second != 0)
                    recipeTime = ""+hour+":0"+minute+":0"+second;
                else
                    recipeTime = ""+hour+":0"+minute+":"+second;
            }

            else if(second == 0)
                recipeTime = ""+hour+":"+minute+":00";
            else if(second/10 == 0 && second != 0)
                recipeTime = ""+hour+":"+minute+":0"+second;
            else
                recipeTime = ""+hour+":"+minute+":"+second;

        }

        LayoutInflater inflater = LayoutInflater.from(stepContext);
        convertView = inflater.inflate(resources,parent,false);

        TextView textInstruction = (TextView) convertView.findViewById(R.id.aci_instruction);
        TextView textTime = (TextView) convertView.findViewById(R.id.aci_timer);

        textInstruction.setText(stepInstruction);
        textTime.setText(recipeTime);

        return convertView;
    }

}
