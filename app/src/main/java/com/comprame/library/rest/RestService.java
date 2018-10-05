package com.comprame.library.rest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.comprame.library.fun.Async;

public class RestService {
    private final String url;
    private final RequestQueue queue;

    public RestService(String url, RequestQueue queue) {
        this.url = url;
        this.queue = queue;
    }

    public <T> Async<T> post(Object request
            , Class<T> responseClass) {
        return new VolleyJsonRequest<>(queue
                , Request.Method.POST
                , url
                , request
                , responseClass
        );
    }

    public <T> Async<T> put(String id
            , Object request
            , Class<T> responseClass) {
        return new VolleyJsonRequest<>(queue
                , Request.Method.PUT
                , url + "/" + id
                , request
                , responseClass
        );
    }

    public <T> Async<T> get(Object id
            , Class<T> responseClass) {
        return new VolleyJsonRequest<>(queue
                , Request.Method.GET
                , url + "/" + id
                , null
                , responseClass
        );
    }

    public <T> Async<T> get(Query queryParams
            , Class<T> responseClass) {
        return new VolleyJsonRequest<>(queue
                , Request.Method.GET
                , url + "?" + queryParams.string
                , null
                , responseClass
        );
    }
}

