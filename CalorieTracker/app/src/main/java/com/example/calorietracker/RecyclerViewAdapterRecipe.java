package CustomAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calorietracker.AddIngredientActivity;
import com.example.calorietracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerViewAdapterRecipe extends RecyclerView.Adapter<RecyclerViewAdapterRecipe.customViewHolder> {

    private JSONArray mData;
    private Context applicationContext;

    public static class customViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llRecipeView;
        public TextView tvRecipeName;

        public customViewHolder(View itemView) {
            super(itemView);
            llRecipeView = itemView.findViewById(R.id.rv_recipe_container);
            tvRecipeName = itemView.findViewById(R.id.rv_recipe_name);
        }
    }

    // Default Constructor
    public RecyclerViewAdapterRecipe(JSONArray data, Context applicationContext) {
        this.mData = data;
        this.applicationContext = applicationContext;
    }

    @Override
    public customViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // Creating a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_recipes, parent, false);

        customViewHolder viewHolder = new customViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(customViewHolder holder, int position) {

        JSONObject foodObj = null;

        try {
            foodObj = (JSONObject) mData.get(position);

            holder.tvRecipeName.setText(foodObj.getString("recipename"));

            final JSONObject finalFoodObj = foodObj;
            holder.llRecipeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Go to the recipe ingredients
                    Intent intent = new Intent(applicationContext, AddIngredientActivity.class);
                    try {
                        intent.putExtra("_id",finalFoodObj.getString("userid"));
                        intent.putExtra("recipe_id", finalFoodObj.getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    applicationContext.startActivity(intent);
                }
            });

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
