package CustomAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calorietracker.FoodInfoActivity;
import com.example.calorietracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerViewAdapterHomeExercise extends RecyclerView.Adapter<RecyclerViewAdapterHomeExercise.customViewHolder> {

    private JSONArray mData;

    public static class customViewHolder extends RecyclerView.ViewHolder {

        public TextView tvExerciseName;
        public TextView tvBurnedCalories;

        public customViewHolder(View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.rv_home_exercise_name);
            tvBurnedCalories = itemView.findViewById(R.id.rv_home_exercise_cal);

        }
    }

    // Default Constructor
    public RecyclerViewAdapterHomeExercise(JSONArray data) {
        this.mData = data;

    }

    @Override
    public RecyclerViewAdapterHomeExercise.customViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // Creating a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_home_exercise, parent, false);

        RecyclerViewAdapterHomeExercise.customViewHolder viewHolder = new RecyclerViewAdapterHomeExercise.customViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerViewAdapterHomeExercise.customViewHolder holder, int position) {

        JSONObject jsonExercise = null;

        try {
            jsonExercise = (JSONObject) mData.get(position);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tvExerciseName.setText("Exercise");

        try {
            assert jsonExercise != null;
            holder.tvExerciseName.setText(jsonExercise.getString("exercisename"));
            holder.tvBurnedCalories.setText(jsonExercise.getString("caloriesburned") + " kcal");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Return the size of mData
    @Override
    public int getItemCount() {
        return mData.length();
    }
}
