package com.comprame.buy;

import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.databinding.BuyFragmentBinding;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.Format;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.login.User;
import com.comprame.overview.DeliveryEstimate;
import com.comprame.overview.Estimate;
import com.comprame.search.SearchFragment;
import com.comprame.sell.Geolocation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.comprame.MainActivity.PLACE_PICKER_REQUEST;

public class BuyFragment extends Fragment {

    private BuyViewModel model;

    private ProgressPopup progressPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        BuyFragmentBinding binding = BuyFragmentBinding.inflate(inflater, container, false);
        model = ViewModelProviders.of(getActivity()).get(BuyViewModel.class);
        binding.setData(model);
        binding.setFragment(this);
        model.deliveryDate.observeForever((d) -> tryCalculateDelivery());
        model.deliveryLocation.observeForever((l) -> tryCalculateDelivery());
        return binding.getRoot();
    }

    public void tryCalculateDelivery() {
        if (model.deliveryLocation.getValue() == null || model.deliveryDate.getValue() == null)
            return;
        ProgressPopup progressDialog = new ProgressPopup("Estimando Costo Envio...", this.getContext());
        progressDialog.show();
        App.appServer
                .post("/delivery-estimate/",
                        new Estimate(model.deliveryLocation.getValue(),
                                model.item.getId(),
                                1,
                                Format.iso(model.deliveryDate.getValue())),
                        DeliveryEstimate.class,
                        Headers.Authorization(Session.getInstance()))
                .onDone((ok, error) -> progressDialog.dismiss())
                .run(
                        (ok) ->
                                model.deliveryCost.setValue(ok.value),
                        (error) -> {
                            model.deliveryDate.setValue(null);
                            Toast.makeText(this.getContext()
                                    , "Error estimando el envio. Reintente en unos minutos"
                                    , Toast.LENGTH_LONG).show();
                        }
                );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadSeller();
    }

    private void loadSeller() {
        progressPopup = new ProgressPopup("Cargando...", getContext());
        progressPopup.show();

        String url = "/user/" + model.item.getSellerId();

        App.appServer.get(url, User.class
                , Headers.Authorization(Session.getInstance()))
                .onDone((u, ex) -> progressPopup.dismiss())
                .run(
                        (User user) -> {
                            if (user.getName().isEmpty()) {
                                Toast.makeText(getContext()
                                        , "No se encuentra el vendedor"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                model.setSeller(user.getName());
                            }
                        }
                        , (Exception ex) -> {
                            Log.d("ProfileListener", "Error al recuperar el perfil", ex);
                            Toast.makeText(getContext()
                                    , "Error al cargar el perfil"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    public void buy(View item) {
        ProgressPopup popupWindow = new ProgressPopup("Procesando Pago", getContext());
        popupWindow.show();
        App.appServer.post("/purchase/"
                , model.asPuchase()
                , Object.class
                , Headers.Authorization(Session.getInstance()))
                .onDone((ok, error) -> popupWindow.dismiss())
                .run(
                        (ok) ->
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_container, new SearchFragment())
                                        .addToBackStack(null)
                                        .commit()
                        , (ex) -> {
                            Log.d("BuyFragment", "Comprando Item", ex);
                            Toast.makeText(getActivity()
                                    , "Error al procesar el pago"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        });
    }

    public void pickGeoLocation(View item) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            getActivity()
                    .startActivityForResult(builder.build(this.getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(getActivity()
                    , "Error resolviendo la direccion, por favor intente nuevamente"
                    , Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void pickDate(View item) {
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(getContext(),
                (DatePicker view, int year, int month, int dayOfMonth) ->
                {
                    TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                            (v, hour, minute) -> {
                                model.deliveryDate.setValue(new Date(year - 1900,
                                        month,
                                        dayOfMonth,
                                        hour,
                                        minute));
                            }, 12, 0, false);
                    timePicker.setTitle("Horario de Entrega");
                    timePicker.show();
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Fecha de Entrega");
        datePickerDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(Objects.requireNonNull(getActivity()), data);
                    model.deliveryLocation.setValue(new Geolocation(
                            place.getLatLng().latitude,
                            place.getLatLng().longitude,
                            String.format("%s", place.getAddress())));
                    break;
            }
        }
    }
}