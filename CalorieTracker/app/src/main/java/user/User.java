package user;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.calorietracker.HomeActivity;
import com.example.calorietracker.PopulateAccountActivity;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

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
                            intent.putExtra("_id", result.getString("_id"));
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

    public static void createUser(final String username,
                                  String password,
                                  String email,
                                  final Context applicationContext)
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

                        System.out.println(result);
                        String user_id = null;

                        try{
                            user_id = result.getString("_id");
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(applicationContext, PopulateAccountActivity.class);
                        intent.putExtra( "_id", user_id);
                        applicationContext.startActivity(intent);

                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                        System.out.println(result);
                        Toast.makeText(applicationContext, "Username already taken", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
