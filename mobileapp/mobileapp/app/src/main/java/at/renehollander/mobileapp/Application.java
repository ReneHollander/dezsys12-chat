package at.renehollander.mobileapp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Application extends android.app.Application {

    private static final String API_URL = "http://10.0.105.190:8080";

    private AsyncHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();

        client = new AsyncHttpClient();
    }

    public void register(String email, String password, Callback<Throwable, Boolean> cb) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);
            client.post(this, API_URL + "/register", new StringEntity(body.toString()), ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 201) {
                        cb.call(null, true);
                    } else {
                        cb.call(new Exception("invalid status code for success: " + statusCode), null);
                    }
                }

                public void onFailure(int statusCode, Header[] headers, String string, Throwable throwable) {
                    cb.call(throwable, null);
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                    if (statusCode == 400) {
                        cb.call(null, false);
                    } else {
                        cb.call(throwable, null);
                    }
                }
            });
        } catch (Exception e) {
            cb.call(e, null);
        }

    }

    public void login(String email, String password, Callback<Throwable, Boolean> cb) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);
            client.post(this, API_URL + "/login", new StringEntity(body.toString()), ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200) {
                        cb.call(null, true);
                    } else {
                        cb.call(new Exception("invalid status code for success: " + statusCode), null);
                    }
                }

                public void onFailure(int statusCode, Header[] headers, String string, Throwable throwable) {
                    cb.call(throwable, null);
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                    if (statusCode == 403) {
                        cb.call(null, false);
                    } else {
                        cb.call(throwable, null);
                    }
                }
            });
        } catch (Exception e) {
            cb.call(e, null);
        }
    }

}
