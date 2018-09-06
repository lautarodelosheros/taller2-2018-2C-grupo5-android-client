package taller2_2018_2c_grupo5.comprame.servicios;

import android.support.v4.util.Pair;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;


public class CustomJsonObjectRequest extends JsonObjectRequest {

    public CustomJsonObjectRequest(String url, JSONObject jsonRequest, final ResponseListener listener) {
        super
                (
                        url,
                        jsonRequest,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                listener.onRequestCompleted(response);
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Pair<Integer, String> errorDetail;

                                errorDetail = RequestHelper.getError(error);

                                listener.onRequestError(errorDetail.first, errorDetail.second);

                            }
                        }
                );
        setShouldCache(false);

    }

    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest, final ResponseListener listener) {
        super
                (
                        method,
                        url,
                        jsonRequest,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                listener.onRequestCompleted(response);
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Pair<Integer, String> errorDetail;

                                errorDetail = RequestHelper.getError(error);

                                listener.onRequestError(errorDetail.first, errorDetail.second);

                            }
                        }
                );
        setShouldCache(false);

    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        if (response.data == null || response.data.length == 0) {
            Log.d("CustomJsonObjetRequest", "response is null");
            return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
        } else {
            return super.parseNetworkResponse(response);
        }
    }

    @Override
    public Map getHeaders() throws AuthFailureError {

        return RequestHelper.getHeaders();
    }

}
