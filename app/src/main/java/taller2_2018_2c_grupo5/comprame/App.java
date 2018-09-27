package taller2_2018_2c_grupo5.comprame;

import android.app.Application;
import android.content.Context;

import taller2_2018_2c_grupo5.comprame.library.RestService;


public class App extends Application {
    public static class Comprame {
        public final RestService login;
        public final RestService items;
        public final RestService signUp;

        Comprame(Context context) {
            String baseUrl = context.getString(R.string.urlAppServer);
            this.login = new RestService(baseUrl + "users/login"
                    , context);
            this.items = new RestService(baseUrl + "/articulos/"
                    , context);
            this.signUp = new RestService(baseUrl + "users/signup"
                    , context);
        }
    }

    public static Comprame services;

    @Override
    public void onCreate() {
        super.onCreate();
        services = new Comprame(getApplicationContext());
    }
}
