package taller2_2018_2c_grupo5.comprame.servicios.listeners;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import taller2_2018_2c_grupo5.comprame.actividades.LoginActivity;
import taller2_2018_2c_grupo5.comprame.dominio.Session;
import taller2_2018_2c_grupo5.comprame.library.Continuation;

public class LoginListener implements Continuation<Session> {

    private final LoginActivity context;

    public LoginListener(LoginActivity context) {
        this.context = context;
    }

    @Override
    public void onSuccess(Session response) {
        Log.d("LoginListener", response.toString());
        try {
            context.onLoginSuccess(response.getSession());
        } catch (Exception e) {
            e.printStackTrace();
            context.onLoginFailed();
        }
    }

    @Override
    public void onError(VolleyError ex) {
        Log.d("LoginListener", ex.getMessage());
        if (ex instanceof ServerError) {
            switch (ex.networkResponse.statusCode) {
                case 404:
                    Toast.makeText(context, "El User ingresado no existe. Si aún no está registrado ingrese al link debajo", Toast.LENGTH_LONG).show();
                    break;
                case 406:
                    Toast.makeText(context, "La contraseña es incorrecta, inténtelo nuevamente", Toast.LENGTH_LONG).show();
                    break;
                default:
            }
        }
        Toast.makeText(context, "Error en el Login, reintente en unos minutos", Toast.LENGTH_LONG).show();
    }
}
