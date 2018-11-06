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

import com.comprame.App;
import com.comprame.MainActivity;
import com.comprame.R;
import com.comprame.databinding.LoginSignupFragmentBinding;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.ProgressPopup;

public class SignUpFragment extends Fragment {

    private SignUpViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        LoginSignupFragmentBinding inflate = DataBindingUtil.inflate(inflater
                , R.layout.login_signup_fragment
                , container
                , false);
        model = ViewModelProviders.of(this)
                .get(SignUpViewModel.class);
        inflate.setSignUpModel(model);
        inflate.setFragment(this);
        return inflate.getRoot();
    }

    public void signUp(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Registrando nuevo usuario...", getContext());
        progressPopup.show();
        App.appServer.post(
                "/user/signup"
                , model.asUser()
                , SessionToken.class, new Headers())
                .onDone((s, ex) -> progressPopup.dismiss())
                .run(this::search
                        , ex -> Toast.makeText(getContext()
                                , "Hubo un error inesperado al intentar registrar su usuario, intente hacer un Login o registrarse nuevamente"
                                , Toast.LENGTH_LONG).show());
    }

    public void search(SessionToken s) {
        Session.getInstance().setSessionToken(s.getSession());
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}