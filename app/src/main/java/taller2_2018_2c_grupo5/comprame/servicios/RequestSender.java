package taller2_2018_2c_grupo5.comprame.servicios;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RequestSender {

    private final RequestQueue queue;

    public RequestSender(Activity context) {
        queue = Volley.newRequestQueue(context);
    }

    private void doRequest(JsonRequest request){
        queue.add(request);
    }

    public void doPost(final ResponseListener listener, String url, final JSONObject jsonObject){
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, jsonObject, listener);
        Log.d("RequestSender", "Sending post: " + request.toString());
        doRequest(request);

    }

}
