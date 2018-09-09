package taller2_2018_2c_grupo5.comprame.servicios.listeners;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import taller2_2018_2c_grupo5.comprame.actividades.LoginActivity;
import taller2_2018_2c_grupo5.comprame.servicios.ResponseListener;

public class LoginListener implements ResponseListener {

    private final LoginActivity context;

    public LoginListener(LoginActivity context) {
        this.context = context;
    }

    @Override
    public void onRequestCompleted(Object response) {

        Log.d("LoginListener", response.toString());

        JSONObject jsonObject = (JSONObject) response;
        String session;

        try {
            session = jsonObject.getString("session");
            context.onLoginSuccess(session);
        } catch (JSONException e) {
            e.printStackTrace();
            context.onLoginFailed();
        }

    }

    @Override
    public void onRequestError(int codError, String errorMessage) {
        Log.d("LoginListener", errorMessage + codError);

        switch (codError) {
            case 404:
                Toast.makeText(context, "El Usuario ingresado no existe. Si aún no está registrado ingrese al link debajo", Toast.LENGTH_LONG).show();
                break;
            case 406:
                Toast.makeText(context, "La contraseña es incorrecta, inténtelo nuevamente", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, "Error en el Login, reintente en unos minutos", Toast.LENGTH_LONG).show();
        }

        context.onLoginFailed();
    }


}
