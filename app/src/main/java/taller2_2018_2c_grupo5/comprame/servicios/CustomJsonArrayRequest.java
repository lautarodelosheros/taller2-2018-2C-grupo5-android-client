package taller2_2018_2c_grupo5.comprame.servicios;

import android.support.v4.util.Pair;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class CustomJsonArrayRequest extends JsonArrayRequest {

    public CustomJsonArrayRequest(String url, final ResponseListener listener) {
        super(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {

        if (response.data == null || response.data.length == 0) {
            Log.d("CustomJsonArrayRequest", "Response is null");
            return Response.success(new JSONArray(), HttpHeaderParser.parseCacheHeaders(response));
        } else {
            return null;
        }
    }
}
