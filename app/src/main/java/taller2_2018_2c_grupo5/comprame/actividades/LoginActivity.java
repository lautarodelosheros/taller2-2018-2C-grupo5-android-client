package taller2_2018_2c_grupo5.comprame.actividades;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.servicios.RequestSender;
import taller2_2018_2c_grupo5.comprame.servicios.listeners.LoginListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText campo_nombreUsuario;
    private EditText campo_password;
    private Button boton_login;
    private TextView link_registrarse;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boton_login = findViewById(R.id.btn_login);

        boton_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        link_registrarse = findViewById(R.id.link_signup);

        link_registrarse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Ir a Registrarse
                Intent intent = new Intent(getApplicationContext(), RegistrarseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            return;
        }

        boton_login.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Ingresando...");
        progressDialog.show();

        campo_nombreUsuario = findViewById(R.id.input_userName);
        campo_password = findViewById(R.id.input_password);

        String nombreUsuario = campo_nombreUsuario.getText().toString();
        String password = campo_password.getText().toString();

        loginUsuario(nombreUsuario, password);

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String session) {
        progressDialog.dismiss();
        finish();
    }

    public void onLoginFailed() {
        progressDialog.dismiss();
        boton_login.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        campo_nombreUsuario = findViewById(R.id.input_userName);
        campo_password = findViewById(R.id.input_password);

        String nombreUsuario = campo_nombreUsuario.getText().toString();
        String password = campo_password.getText().toString();

        if (nombreUsuario.isEmpty() || nombreUsuario.length() < 2) {
            campo_nombreUsuario.setError(getString(R.string.nombre_error));
            valid = false;
        } else {
            campo_nombreUsuario.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            campo_password.setError(getString(R.string.password_error));
            valid = false;
        } else {
            campo_password.setError(null);
        }

        return valid;
    }

    private void loginUsuario(String nombreUsuario, String password) {
        Map<String,String> parametros;
        parametros = new HashMap<>();
        RequestSender requestSender = new RequestSender(this);
        parametros.put("name", nombreUsuario);
        parametros.put("password", password);

        JSONObject jsonObject = new JSONObject(parametros);

        String url = getString(R.string.urlAppServer) + "users/login";

        LoginListener listener = new LoginListener(this);

        requestSender.doPost(listener, url, jsonObject);
    }
}