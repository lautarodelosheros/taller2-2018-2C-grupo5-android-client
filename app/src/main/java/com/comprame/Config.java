package com.comprame;


import android.annotation.SuppressLint;
import android.security.NetworkSecurityPolicy;

public class Config {
    private enum Env {
        PROD, DEV_EMULATOR, DEV_PHONE
    }
    public static final String CLOUDINARY_CLOUD_NAME = "dsivfsfcd";
    public static final String CLOUDINARY_UPLOAD_PRESET = "in08vb9h";
    public static final String CLOUDINARY_FIXED_IMAGE_PATH =
            "/storage/emulated/0/Download/download.jpeg";
    public static final String CLOUDINARY_FIXED_IMAGE_NAME = "test_image.jpeg";

    private static final Env environment = Env.DEV_EMULATOR;
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