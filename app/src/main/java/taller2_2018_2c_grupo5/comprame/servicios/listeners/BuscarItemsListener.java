package taller2_2018_2c_grupo5.comprame.servicios.listeners;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taller2_2018_2c_grupo5.comprame.actividades.BuscarItemsFragment;
import taller2_2018_2c_grupo5.comprame.dominio.Item;
import taller2_2018_2c_grupo5.comprame.servicios.ResponseListener;

public class BuscarItemsListener implements ResponseListener {

    private final BuscarItemsFragment context;

    public BuscarItemsListener(BuscarItemsFragment context) {
        this.context = context;
    }

    @Override
    public void onRequestCompleted(Object response) {

        Log.d("BuscarItemsListener", response.toString());

        JSONArray jsonArray = (JSONArray) response;

        Gson gson = new Gson();
        List<Item> items = new ArrayList<>();

        if (jsonArray.length() > 0) {
            items = Arrays.asList(gson.fromJson(jsonArray.toString(), Item[].class));
        }

        context.onSearchSuccess(items);

    }

    @Override
    public void onRequestError(int codError, String errorMessage) {
        Log.d("BuscarItemsListener", errorMessage + codError);
        Toast.makeText(context.getActivity(), "Error al buscar las publicaciones en el servidor", Toast.LENGTH_LONG).show();
        context.onSearchFailed();
    }


}
