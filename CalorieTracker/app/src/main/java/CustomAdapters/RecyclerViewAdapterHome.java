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

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.customViewHolder> {

    private JSONArray mData;
    private Context applicationContext;
    private String user_id;

    public static class customViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFoodName;
        public TextView tvFoodServing;
        public TextView tvFoodCalorie;

        public customViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.rv_home_food);
            tvFoodServing = itemView.findViewById(R.id.rv_home_serving);
            tvFoodCalorie = itemView.findViewById(R.id.rv_home_calories);

        }
    }

    // Default Constructor
    public RecyclerViewAdapterHome(JSONArray data, Context applicationContext, String _id) {
        this.mData = data;
        this.applicationContext = applicationContext;
        this.user_id = _id;

    }

    @Override
    public RecyclerViewAdapterHome.customViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // Creating a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_home_diary, parent, false);

        RecyclerViewAdapterHome.customViewHolder viewHolder = new RecyclerViewAdapterHome.customViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(customViewHolder holder, int position) {

        JSONObject foodObj = null;
        String sFoodInfo = "TODO";
        double dFoodQuantity = 0;
        String sServingUnits = "";
        double dCalories = 0;

        try {
            foodObj = (JSONObject) mData.get(position);

            System.out.println(foodObj);

            if(!foodObj.has("brandowner")){
                sFoodInfo = foodObj.getString("fooddescription") + ", No Brand Owner";
            }
            else
                sFoodInfo = foodObj.getString("fooddescription") + ", " + foodObj.getString("brandowner");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            assert foodObj != null;
            dFoodQuantity = Double.parseDouble(foodObj.getString("amount")) *
                            Double.parseDouble(foodObj.getString("servingsize"));
            sServingUnits = foodObj.getString("servingunits");
            dCalories = Double.parseDouble(foodObj.getString("energy"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tvFoodName.setText(sFoodInfo);
        holder.tvFoodServing.setText((int) dFoodQuantity + " " + sServingUnits);
        holder.tvFoodCalorie.setText((int) dCalories + " kcal");

        final JSONObject finalFoodObj = foodObj;
        holder.tvFoodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(applicationContext, FoodInfoActivity.class);
                try {
                    intent.putExtra("fdcId", finalFoodObj.getString("fdcId"));
                    intent.putExtra("_id", user_id);
                    applicationContext.startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // Return the size of mData
    @Override
    public int getItemCount() {
        return mData.length();
    }

}
