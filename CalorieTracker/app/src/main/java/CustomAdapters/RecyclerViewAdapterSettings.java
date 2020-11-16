package CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calorietracker.R;

import java.util.ArrayList;

public class RecyclerViewAdapterSettings extends RecyclerView.Adapter<RecyclerViewAdapterSettings.customViewHolder> {

    private static final String TAG = "bind";
    private ArrayList<String> itemList;
    private Context applicationContext;

    public static class customViewHolder extends RecyclerView.ViewHolder {

        public TextView settingItem;
        public RelativeLayout parentLayout;


        public customViewHolder(View itemView) {
            super(itemView);
            settingItem = itemView.findViewById(R.id.rv_item);
            parentLayout = itemView.findViewById((R.id.rvsettingsitems));
        }

    }
    
    // Default Constructor
    public RecyclerViewAdapterSettings(ArrayList<String> data, Context aContext) {
        itemList = data;
        applicationContext = aContext;
        }


    @Override
    public customViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

          // Creating a new view
          View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_settings_items, parent, false);
          RecyclerViewAdapterSettings.customViewHolder viewHolder = new RecyclerViewAdapterSettings.customViewHolder(v1);
          return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull customViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.settingItem.setText(itemList.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(applicationContext, itemList.get(position), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}