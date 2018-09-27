package taller2_2018_2c_grupo5.comprame.library;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;


public class RestService {
    private final String url;
    private final RequestQueue queue;

    public RestService(String url, Context context) {
        this.url = url;
        this.queue = Volley.newRequestQueue(context);
    }

    public <T> void post(Object request
            , final Continuation<T> listener
            , Class<T> responseClass) {
        pushRequest(new GenericJsonRequest<>(responseClass
                , Request.Method.POST
                , url
                , request
                , new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                listener.onSuccess(response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        }));
    }

    public <T> void put(String id
            , Object request
            , final Continuation<T> listener
            , Class<T> responseClass) {
        pushRequest(new GenericJsonRequest<>(responseClass
                , Request.Method.PUT
                , url + "/" + id
                , request
                , new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                listener.onSuccess(response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        }));
    }

    public <T> void get(Map<String, Object> queryParams
            , final Continuation<T> listener
            , Class<T> responseClass) {
        pushRequest(new GenericJsonRequest<>(responseClass
                , Request.Method.GET
                , url + queryString(queryParams)
                , null
                , new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                listener.onSuccess(response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        }));
    }

    private String queryString(Map<String, Object> queryParams) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = queryParams.entrySet().iterator();
        if (iterator.hasNext()) {
            sb.append("?");
            do {
                Map.Entry<String, Object> pair = iterator.next();
                if (pair.getValue() != null)
                    sb.append(pair.getKey())
                            .append("=")
                            .append(pair.getValue());
                if (iterator.hasNext()) {
                    sb.append("&");
                }
            } while (iterator.hasNext());
        }
        return sb.toString();
    }

    private void pushRequest(GenericJsonRequest<?> json) {
        json.setRetryPolicy(new DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(json);
    }
}

class GenericJsonRequest<T> extends JsonRequest<T> {
    private static final Gson gson = new Gson();
    private final Class<T> clazz;

    GenericJsonRequest(Class<T> clazz
            , int method
            , String url
            , Object requestBody
            , Response.Listener<T> listener
            , @Nullable Response.ErrorListener errorListener) {
        super(method, url, gson.toJson(requestBody), listener, errorListener);
        this.clazz = clazz;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            T result = deserialize(response.data);
            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
            return Response.success(result, entry);
        } catch (Exception e) {
            return Response.error(null);
        }
    }

    private T deserialize(byte[] data) {
        InputStreamReader byteArrayInputStream =
                new InputStreamReader(new ByteArrayInputStream(data));
        return gson.fromJson(byteArrayInputStream, clazz);
    }
}