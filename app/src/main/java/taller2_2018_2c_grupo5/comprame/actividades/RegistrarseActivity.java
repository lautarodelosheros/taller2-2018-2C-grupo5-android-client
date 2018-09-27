package taller2_2018_2c_grupo5.comprame.actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import taller2_2018_2c_grupo5.comprame.App;
import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.dominio.Session;
import taller2_2018_2c_grupo5.comprame.dominio.User;
import taller2_2018_2c_grupo5.comprame.servicios.listeners.RegistrarseListener;

public class RegistrarseActivity extends AppCompatActivity {
    private static final String TAG = "RegistrarseActivity";

    private EditText campo_nombreUsuario;
    private EditText campo_nombre;
    private EditText campo_apellido;
    private EditText campo_email;
    private EditText campo_password;
    private Button boton_registrarse;

    private ProgressDialog progressDialog;

    public RegistrarseActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        boton_registrarse = findViewById(R.id.btn_signup);
        boton_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        findViewById(R.id.link_login)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Ir a Login
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void signup() {
        Log.d(TAG, "Registrarse");

        if (!validate()) {
            return;
        }

        boton_registrarse.setEnabled(false);

        progressDialog = new ProgressDialog(RegistrarseActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registrando nuevo User...");
        progressDialog.show();

        campo_nombreUsuario = findViewById(R.id.input_userName);
        campo_nombre = findViewById(R.id.input_name);
        campo_apellido = findViewById(R.id.input_apellido);
        campo_email = findViewById(R.id.input_email);
        campo_password = findViewById(R.id.input_password);

        String nombreUsuario = campo_nombreUsuario.getText().toString();
        String nombre = campo_nombre.getText().toString();
        String apellido = campo_apellido.getText().toString();
        String email = campo_email.getText().toString();
        String password = campo_password.getText().toString();

        App.services.signUp.post(new User(nombreUsuario, nombre, apellido, password, email)
                , new RegistrarseListener(this)
                , Session.class);

    }


    public void onSignupSuccess(String session) {
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        progressDialog.dismiss();
        boton_registrarse.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        campo_nombreUsuario = findViewById(R.id.input_userName);
        campo_nombre = findViewById(R.id.input_name);
        campo_apellido = findViewById(R.id.input_apellido);
        campo_email = findViewById(R.id.input_email);
        campo_password = findViewById(R.id.input_password);

        String nombreUsuario = campo_nombreUsuario.getText().toString();
        String nombre = campo_nombre.getText().toString();
        String apellido = campo_apellido.getText().toString();
        String email = campo_email.getText().toString();
        String password = campo_password.getText().toString();

        if (nombreUsuario.isEmpty() || nombreUsuario.length() < 2) {
            campo_nombreUsuario.setError(getString(R.string.nombre_error));
            valid = false;
        } else {
            campo_nombreUsuario.setError(null);
        }

        if (nombre.isEmpty() || nombre.length() < 2) {
            campo_nombre.setError(getString(R.string.nombre_error));
            valid = false;
        } else {
            campo_nombre.setError(null);
        }

        if (apellido.isEmpty() || apellido.length() < 2) {
            campo_apellido.setError(getString(R.string.apellido_error));
            valid = false;
        } else {
            campo_apellido.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campo_email.setError(getString(R.string.email_error));
            valid = false;
        } else {
            campo_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            campo_password.setError(getString(R.string.password_error));
            valid = false;
        } else {
            campo_password.setError(null);
        }

        return valid;
    }

}