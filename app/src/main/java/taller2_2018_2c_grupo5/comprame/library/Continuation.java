package taller2_2018_2c_grupo5.comprame.library;

import com.android.volley.VolleyError;

public interface Continuation<T> {

    void onSuccess(T response);

    void onError(VolleyError ex);

}
