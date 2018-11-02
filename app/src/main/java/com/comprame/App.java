package com.comprame;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.security.NetworkSecurityPolicy;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.comprame.library.rest.RestService;

class Config {
    private enum Env {
        PROD, DEV_EMULATOR, DEV_PHONE
    }

    private static final Env environment = Env.DEV_PHONE;
    private static final String EMULATOR_HOST_ADDRESS = "10.0.2.2";
    private static final String NETWORK_ADDRESS = "192.168.0.15";

    static String compraMeUrl() {
        switch (environment) {
            case DEV_EMULATOR:
                return "http://" + EMULATOR_HOST_ADDRESS + ":5000";
            case DEV_PHONE:
                return "http://" + NETWORK_ADDRESS + ":5000";
            default:
                return "https://grupo5-application-server.herokuapp.com";
        }
    }

    @SuppressLint("NewApi")
    static void setupNetwork() {
        switch (environment) {
            case DEV_EMULATOR:
                NetworkSecurityPolicy
                        .getInstance()
                        .isCleartextTrafficPermitted(EMULATOR_HOST_ADDRESS);
                return;
            case DEV_PHONE:
                NetworkSecurityPolicy
                        .getInstance()
                        .isCleartextTrafficPermitted(NETWORK_ADDRESS);
                return;
            case PROD:
        }
    }
}

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
