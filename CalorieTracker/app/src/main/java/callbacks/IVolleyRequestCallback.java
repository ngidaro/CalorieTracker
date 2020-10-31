package callbacks;

import org.json.JSONObject;

public interface IVolleyRequestCallback {
    void onSuccess(JSONObject result);
    void onFailure(String result);
}
