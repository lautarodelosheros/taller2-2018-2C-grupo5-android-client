package com.comprame.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.comprame.App;
import com.comprame.MainActivity;
import com.comprame.R;
import com.comprame.databinding.LoginFragmentBinding;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.ProgressPopup;

public class LoginFragment extends Fragment {

    private LoginViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(this)
                .get(LoginViewModel.class);
        LoginFragmentBinding binding = DataBindingUtil.inflate(inflater
                , R.layout.login_fragment
                , container
                , false);
        binding.setLoginModel(model);
        binding.setFragment(this);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    public void logIn(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Ingresando...", getContext());
        progressPopup.show();
        App.appServer.post(
                "/user/login"
                , model.asUser(), SessionToken.class
                , new Headers())
                .onDone((s, ex) -> progressPopup.dismiss())
                .run(this::search
                        , this::showToastError);

    }

    public void search(SessionToken session) {
        Session.getInstance().setSessionToken(session.getSession());
        Intent intent = new Intent(getContext(), MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("name", model.name.getValue());
        intent.putExtras(bundle);

        startActivity(intent);

    }

    private void showToastError(Exception ex) {
        if (ex instanceof ServerError) {
            switch (((ServerError) ex).networkResponse.statusCode) {
                case 404:
                    Toast.makeText(this.getContext()
                            , "El User ingresado no existe. Si aún no está registrado ingrese al link debajo"
                            , Toast.LENGTH_LONG).show();
                    break;
                case 406:
                    Toast.makeText(this.getContext()
                            , "La contraseña es incorrecta, inténtelo nuevamente"
                            , Toast.LENGTH_LONG).show();
                    break;
            }
        } else
            Toast.makeText(this.getContext()
                    , "Error en el Login, reintente en unos minutos"
                    , Toast.LENGTH_LONG).show();

    }


}