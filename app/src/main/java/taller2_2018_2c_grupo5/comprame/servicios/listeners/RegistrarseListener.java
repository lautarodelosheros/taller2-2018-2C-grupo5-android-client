package taller2_2018_2c_grupo5.comprame.servicios.listeners;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;


import taller2_2018_2c_grupo5.comprame.actividades.RegistrarseActivity;
import taller2_2018_2c_grupo5.comprame.dominio.Session;
import taller2_2018_2c_grupo5.comprame.library.Continuation;

public class RegistrarseListener implements Continuation<Session> {

    private final RegistrarseActivity context;

    public RegistrarseListener(RegistrarseActivity context) {
        this.context = context;
    }

    @Override
    public void onSuccess(Session response) {
        Log.d("RegistrarseListener", response.toString());
        try {
            context.onSignupSuccess(response.getSession());
        } catch (Exception e) {
            Toast.makeText(context, "Hubo un error inesperado al intentar registrar su usuario, intente hacer un Login o registrarse nuevamente", Toast.LENGTH_LONG).show();
            context.onSignupFailed();
        }

    }

    @Override
    public void onError(VolleyError ex) {
        Log.d("RegistrarseListener", ex.getMessage());
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        context.onSignupFailed();
    }

}
