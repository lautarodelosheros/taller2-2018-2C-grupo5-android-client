package com.comprame;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.comprame.library.rest.RestService;


public class App extends Application {
    public static final String BASE_URL = Config.compraMeUrl();
    public static RestService appServer;

    static {
        Config.setupNetwork();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        RequestQueue volleyQueue = Volley.newRequestQueue(context);
        appServer = new RestService(BASE_URL, volleyQueue);

    }
}
