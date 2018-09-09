package taller2_2018_2c_grupo5.comprame.servicios.listeners;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import taller2_2018_2c_grupo5.comprame.actividades.RegistrarseActivity;
import taller2_2018_2c_grupo5.comprame.servicios.ResponseListener;

public class RegistrarseListener implements ResponseListener {

    private final RegistrarseActivity context;

    public RegistrarseListener(RegistrarseActivity context) {
        this.context = context;
    }

    @Override
    public void onRequestCompleted(Object response) {

        Log.d("RegistrarseListener", response.toString());

            JSONObject jsonObject = (JSONObject) response;

            String session;

            try {
                session = jsonObject.getString("session");
                context.onSignupSuccess(session);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Hubo un error inesperado al intentar registrar su usuario, intente hacer un Login o registrarse nuevamente", Toast.LENGTH_LONG).show();
                context.onSignupFailed();
            }

    }

    @Override
    public void onRequestError(int codError, String errorMessage) {
        Log.d("RegistrarseListener", errorMessage + codError);
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        context.onSignupFailed();
    }

}
