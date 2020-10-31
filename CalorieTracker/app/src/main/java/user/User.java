package user;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.example.calorietracker.HomeActivity;
import com.example.calorietracker.MainActivity;
import com.example.calorietracker.VolleyRequestContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import callbacks.IVolleyRequestCallback;

public class User {

    public static void getUser(final String username,
                               String password,
                               final Context applicationContext)
    {
        JSONObject userData = new JSONObject();
        try
        {
            userData.put("username", username);
            userData.put("password", password);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(Request.Method.POST,
                "/users/login/",
                userData,
                applicationContext,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try
                        {
                            System.out.println(result);
                            String token = result.getString("token");

                            Intent intent = new Intent(applicationContext, HomeActivity.class);
                            intent.putExtra("username", username);
                            applicationContext.startActivity(intent);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

    public static void createUser(String username,
                                  String password,
                                  String email,
                                  Context applicationContext)
    {

        JSONObject postUser = new JSONObject();
        try{
            postUser.put("username", username);
            postUser.put("password", password);
            postUser.put("email", email);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/users/createaccount",
                postUser,
                applicationContext,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        //
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

}
