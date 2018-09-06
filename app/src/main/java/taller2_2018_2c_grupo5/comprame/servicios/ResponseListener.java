package taller2_2018_2c_grupo5.comprame.servicios;

public interface ResponseListener {

    void onRequestCompleted(Object response);

    void onRequestError(int errorCode, String errorMessage);

}
