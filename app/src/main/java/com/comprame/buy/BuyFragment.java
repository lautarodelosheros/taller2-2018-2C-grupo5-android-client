package com.comprame.buy;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.databinding.BuyFragmentBinding;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.login.User;
import com.comprame.overview.QuestionsList;
import com.comprame.search.SearchFragment;

import java.util.Objects;

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
        return binding.getRoot();
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
}