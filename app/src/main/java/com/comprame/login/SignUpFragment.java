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

import com.comprame.App;
import com.comprame.R;
import com.comprame.databinding.LoginSignupFragmentBinding;
import com.comprame.domain.Session;
import com.comprame.MainActivity;

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
        ProgressDialog progressDialog = new ProgressDialog(view.getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registrando nuevo User...");
        progressDialog.show();
        App.signUp.post(model.asUser()
                , Session.class)
                .onDone((s, ex) -> progressDialog.dismiss())
                .run(s -> search(model, s)
                        , ex -> {
                            Toast.makeText(getContext()
                                    , "Hubo un error inesperado al intentar registrar su usuario, intente hacer un Login o registrarse nuevamente"
                                    , Toast.LENGTH_LONG).show();
                        });
    }

    public void search(SignUpViewModel model, Session s) {
        model.session.setValue(s);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("session", s.getSession());
        startActivity(intent);
    }
}