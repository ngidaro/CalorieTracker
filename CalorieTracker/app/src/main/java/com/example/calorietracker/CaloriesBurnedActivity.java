package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import CustomAdapters.RecyclerViewAdapterHome;
import callbacks.IVolleyRequestCallback;

public class CaloriesBurnedActivity extends AppCompatActivity {

    TextView noteCalorieDeficit;
    TextView unitCalorieDeficit;

    EditText inputCalorieDeficit;
    EditText etExerciseName;
    Button addCalorieDeficit;
    ImageView ivExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_burned);

        final String user_id = getIntent().getStringExtra("_id");

        SetupUI();

        addCalorieDeficit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputCalorieDeficit.getText().toString().equals("") ||
                    inputCalorieDeficit.getText().toString().equals("0") ||
                    etExerciseName.getText().toString().equals(""))
                {
                    Toast.makeText(CaloriesBurnedActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
                else {
                    Integer iCaloriesBurned = Integer.parseInt(inputCalorieDeficit.getText().toString());
                    // Save the iCalories to the Database

                    JSONObject jsonExercise = new JSONObject();
                    Calendar calendar = Calendar.getInstance();

                    try {
                        jsonExercise.put("user_id", user_id);
                        jsonExercise.put("date", calendar.getTime());
                        jsonExercise.put("caloriesburned", iCaloriesBurned);
                        jsonExercise.put("exercisename", etExerciseName.getText().toString());
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                    VolleyRequestContainer.request(
                            Request.Method.POST,
                            "/exercise/addexercise",
                            jsonExercise,
                            CaloriesBurnedActivity.this,
                            new IVolleyRequestCallback() {
                                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                                @Override
                                public void onSuccess(JSONObject result) {
                                    Intent intent = new Intent(CaloriesBurnedActivity.this, HomeActivity.class);
                                    intent.putExtra("_id",user_id);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(String result) {
                                    // Failed
                                }
                            });
                }
            }
        });

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void SetupUI(){

        ivExit = findViewById(R.id.cba_exit);
        etExerciseName = findViewById(R.id.cba_exercise_name);
        noteCalorieDeficit = findViewById(R.id.caldef_note);
        inputCalorieDeficit = findViewById(R.id.cal_burned);
        addCalorieDeficit = findViewById(R.id.caldef_confirm);
    }
}