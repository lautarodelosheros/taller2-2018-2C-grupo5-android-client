package taller2_2018_2c_grupo5.comprame.servicios;

import android.app.Activity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RequestSender {
    private static final int MY_SOCKET_TIMEOUT_MS = 15000;

    private final RequestQueue queue;

    public RequestSender(Activity context) {
        queue = Volley.newRequestQueue(context);
    }

    private void doRequest(JsonRequest request){
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    public void doPing(final ResponseListener listener) {
        Request request = new CustomJsonObjectRequest(Request.Method.GET, "http://192.168.0.15:5000/comprame/ping", null, listener);
        Log.d("RequestSender", "Ping to App-Server");
        queue.add(request);
    }

    public void doPost(final ResponseListener listener, String url, final JSONObject jsonObject){
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, jsonObject, listener);
        Log.d("RequestSender", "Sending post: " + request.toString());
        doRequest(request);
    }

    public void doGet_expectArray(final ResponseListener listener, String url){
        Log.d("RequestSender", "Sending get to " + url );

        doRequest(new CustomJsonArrayRequest(url, listener));

    }

}
