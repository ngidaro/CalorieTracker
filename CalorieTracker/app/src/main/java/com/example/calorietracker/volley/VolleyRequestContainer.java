package com.example.calorietracker.volley;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.ClosedFileSystemException;

import callbacks.IVolleyRequestCallback;

public class VolleyRequestContainer {

//    private static String CALORIE_TRACKER_SERVER = "http://192.168.2.55.:3333"; // Kevin
//    private static String CALORIE_TRACKER_SERVER = "http://10.0.0.226:3333";  // Nick
    private static String CALORIE_TRACKER_SERVER = "http://192.168.2.21:3333"; // Nico

    public static void request(int method,
                                  String SERVER_URL,
                                  JSONObject data,
                                  Context applicationContext,
                                  final IVolleyRequestCallback callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, CALORIE_TRACKER_SERVER + SERVER_URL, data, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("Something went wrong");
                        callback.onFailure(error.getMessage());
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(applicationContext).add(jsonObjectRequest);

    };
}
