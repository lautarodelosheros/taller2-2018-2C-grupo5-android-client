package taller2_2018_2c_grupo5.comprame.servicios;

import android.support.v4.util.Pair;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

class RequestHelper {
    public static Pair<Integer, String> getError(VolleyError error) {
        String errorDesc;
        int codError = 0;

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

            errorDesc = "El servidor no responde o está lento";

        } else if (error instanceof AuthFailureError) {

            errorDesc = "Error de autenticación";

        } else if (error instanceof ServerError) {

            codError = error.networkResponse.statusCode;
            errorDesc = new String(error.networkResponse.data);

        } else if (error instanceof NetworkError) {

            errorDesc = "Error de red";

        } else if (error instanceof ParseError) {

            errorDesc = "Error de parser";
            error.printStackTrace();

        } else {

            errorDesc = "Error desconocido";

        }
        return new Pair<>(codError, errorDesc);
    }

}
