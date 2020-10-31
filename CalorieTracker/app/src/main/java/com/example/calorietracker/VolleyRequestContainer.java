package com.example.calorietracker;

import android.content.Context;
import android.widget.Toast;

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

    private static String CALORIE_TRACKER_SERVER = "http://10.0.0.226:3333";

    public static void request(int method,
                                  String SERVER_URL,
                                  JSONObject data,
                                  Context applicationContext,
                                  final IVolleyRequestCallback callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, CALORIE_TRACKER_SERVER + SERVER_URL, data.length() != 0 ? data : null, new Response.Listener<JSONObject>() {

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
                    }
                });

        Volley.newRequestQueue(applicationContext).add(jsonObjectRequest);

    };
}
