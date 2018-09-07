package taller2_2018_2c_grupo5.comprame.servicios.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;


import taller2_2018_2c_grupo5.comprame.servicios.ResponseListener;

public class RegistrarseListener implements ResponseListener {

    private final Context context;

    public RegistrarseListener(Context context) {
        this.context = context;
    }

    @Override
    public void onRequestCompleted(Object response) {

        Log.d("RegistrarseListener", response.toString());

            JSONObject jsonObject = (JSONObject) response;

            Log.d("RegistrarseListener", response.toString());

            Toast.makeText(context, "Respuesta: " + jsonObject.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestError(int codError, String errorMessage) {
        Log.d("RegistrarseListener", errorMessage + codError);
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }

}
