package CustomAdapters;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.os.Build;
        import android.provider.ContactsContract;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.RequiresApi;
        import androidx.recyclerview.widget.RecyclerView;

        import com.android.volley.Request;
        import com.example.calorietracker.AddIngredientActivity;
        import com.example.calorietracker.R;
        import com.example.calorietracker.volley.VolleyRequestContainer;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import callbacks.IVolleyRequestCallback;

public class RecyclerViewAdapterIngredients extends RecyclerView.Adapter<RecyclerViewAdapterIngredients.customViewHolder> {

    private JSONArray mData;
    private Context applicationContext;

    public static class customViewHolder extends RecyclerView.ViewHolder {

        public TextView tvIngredientName;
        public TextView tvIngredientSize;
        public ImageView ivDeleteIngredient;

        public customViewHolder(View itemView) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.rv_ri_food_name);
            tvIngredientSize = itemView.findViewById(R.id.rv_ri_food_quantity);
            ivDeleteIngredient = itemView.findViewById(R.id.rv_ri_delete);
        }
    }

    // Default Constructor
    public RecyclerViewAdapterIngredients(JSONArray data, Context applicationContext) {
        this.mData = data;
        this.applicationContext = applicationContext;
    }

    @Override
    public customViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // Creating a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_recipe_ingredients, parent, false);

        customViewHolder viewHolder = new customViewHolder(v);
        return viewHolder;
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(customViewHolder holder, final int position) {

        JSONObject foodObj = null;

        try {
            foodObj = (JSONObject) mData.get(position);

            int iServings = (int) (foodObj.getDouble("amount") * foodObj.getDouble("servingsize"));

            holder.tvIngredientName.setText(foodObj.getString("fooddescription"));
            holder.tvIngredientSize.setText(iServings + " " + foodObj.getString("servingunits"));

            final JSONObject finalFoodObj = foodObj;
            holder.ivDeleteIngredient.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    JSONObject jsonRecipe = new JSONObject();

//                    mData.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position,mData.length());

                    try
                    {
                        jsonRecipe.put("ingredient_id", finalFoodObj.getString("_id"));
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                    VolleyRequestContainer.request(
                            Request.Method.POST,
                            "/ingredient/deleteingredient",
                            jsonRecipe,
                            applicationContext,
                            new IVolleyRequestCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {

                                }

                                @Override
                                public void onFailure(String result) {
                                    // Failed
                                }
                            });
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
