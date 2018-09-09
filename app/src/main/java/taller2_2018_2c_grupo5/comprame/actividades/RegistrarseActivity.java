package taller2_2018_2c_grupo5.comprame.actividades;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import taller2_2018_2c_grupo5.comprame.R;

public class RegistrarseActivity extends AppCompatActivity {
    private static final String TAG = "RegistrarseActivity";

    private EditText campo_nombre;
    private EditText campo_apellido;
    private EditText campo_email;
    private EditText campo_password;
    private Button boton_registrarse;
    private TextView link_login;

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

        link_login = findViewById(R.id.link_login);

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retornar al Login
                finish();
            }
        });
    }

    private void signup() {
        Log.d(TAG, "Registrarse");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        boton_registrarse.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegistrarseActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registrando nuevo Usuario...");
        progressDialog.show();

        campo_nombre = findViewById(R.id.input_name);
        campo_apellido = findViewById(R.id.input_apellido);
        campo_email = findViewById(R.id.input_email);
        campo_password = findViewById(R.id.input_password);

        String name = campo_nombre.getText().toString();
        String apellido = campo_apellido.getText().toString();
        String email = campo_email.getText().toString();
        String password = campo_password.getText().toString();

        // TODO: Registrar en el server

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    private void onSignupSuccess() {
        boton_registrarse.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registro fallido", Toast.LENGTH_LONG).show();

        boton_registrarse.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        campo_nombre = findViewById(R.id.input_name);
        campo_apellido = findViewById(R.id.input_apellido);
        campo_email = findViewById(R.id.input_email);
        campo_password = findViewById(R.id.input_password);

        String name = campo_nombre.getText().toString();
        String apellido = campo_apellido.getText().toString();
        String email = campo_email.getText().toString();
        String password = campo_password.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
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