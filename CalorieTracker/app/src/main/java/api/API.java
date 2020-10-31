package api;

import com.android.volley.Request;
import com.example.calorietracker.VolleyRequestContainer;

import org.json.JSONObject;

import callbacks.IVolleyRequestCallback;

public class API {

    public static void getAPI()
    {
        VolleyRequestContainer.request(Request.Method.GET, "/api", null,
                null,
                new IVolleyRequestCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                //
            }

            @Override
            public void onFailure(String result) {
                //
            }
        });
    }

}
