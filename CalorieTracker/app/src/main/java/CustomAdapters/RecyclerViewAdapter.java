package CustomAdapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ListAdapter;
import androidx.core.widget.NestedScrollView;

import com.example.calorietracker.BarcodeActivity;
import com.example.calorietracker.FoodActivity;
import com.example.calorietracker.FoodInfoActivity;
import com.example.calorietracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.*;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.customViewHolder> {

    private JSONArray mData;
    private Context applicationContext;

    public static class customViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFoodInfo;

        public customViewHolder(View itemView) {
            super(itemView);
            tvFoodInfo = itemView.findViewById(R.id.rv_search_food);
        }
    }

    // Default Constructor
    public RecyclerViewAdapter(JSONArray data, Context applicationContext) {
        this.mData = data;
        this.applicationContext = applicationContext;
    }

    @Override
    public customViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // Creating a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_search_items, parent, false);

        customViewHolder viewHolder = new customViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(customViewHolder holder, int position) {

        JSONObject foodObj = null;
        String sFoodInfo = "";

        try {
            foodObj = (JSONObject) mData.get(position);

            if(!foodObj.has("brandOwner")){
                sFoodInfo = foodObj.getString("description") + ", No Brand Owner";
            }
            else
                sFoodInfo = foodObj.getString("description") + ", " + foodObj.getString("brandOwner");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tvFoodInfo.setText(sFoodInfo);

        final JSONObject finalFoodObj = foodObj;
        holder.tvFoodInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(applicationContext, FoodInfoActivity.class);
                try {
                    intent.putExtra("fdcId", finalFoodObj.getString("fdcId"));
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
