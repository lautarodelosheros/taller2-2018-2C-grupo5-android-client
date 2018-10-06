package com.comprame.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.comprame.R;

public class LoginActivity extends AppCompatActivity {
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_activity, new LoginFragment())
                .commit();

    }

    public void signUpFragment(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_activity, new SignUpFragment())
                .commit();
    }

    public void logInFragment(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_activity, new LoginFragment())
                .commit();

    }

}

