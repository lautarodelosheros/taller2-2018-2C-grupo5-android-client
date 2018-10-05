package com.comprame.login;

import android.app.ProgressDialog;
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
import com.comprame.R;
import com.comprame.databinding.LoginFragmentBinding;
import com.comprame.domain.Session;
import com.comprame.domain.User;
import com.comprame.MainActivity;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        LoginFragmentBinding binding = DataBindingUtil.inflate(inflater
                , R.layout.login_fragment
                , container
                , false);
        binding.setLoginModel(ViewModelProviders.of(this)
                .get(LoginViewModel.class));
        binding.setFragment(this);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    public void signUp(View view) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_activity, new SignUpFragment())
                .commit();
    }

    public void logIn(View view) {
        LoginViewModel model = ViewModelProviders.of(this)
                .get(LoginViewModel.class);
        ProgressDialog progressDialog = new ProgressDialog(view.getContext()
                , R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Ingresando...");
        progressDialog.show();
        App.login.post(new User(model.name.getValue()
                        , null
                        , null
                        , model.password.getValue()
                        , null)
                , Session.class)
                .onDone((s, ex) -> progressDialog.dismiss())
                .run(s -> search(model, s)
                        , this::showToastError);

    }

    public void search(LoginViewModel model, Session session) {
        model.session.setValue(session);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("session", session.getSession());
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