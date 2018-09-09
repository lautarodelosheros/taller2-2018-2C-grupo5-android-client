package taller2_2018_2c_grupo5.comprame.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.dominio.Usuario;
import taller2_2018_2c_grupo5.comprame.servicios.RequestSender;
import taller2_2018_2c_grupo5.comprame.servicios.ResponseListener;
import taller2_2018_2c_grupo5.comprame.servicios.listeners.RegistrarseListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Usuario usuario = new Usuario( "Pep55", "abc123","Pepe", "Gonzalez", "pepe@gmail.com");
            registrarUsuario(usuario);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void registrarUsuario(Usuario usuario) {
        Map<String,String> parametros;
        parametros = new HashMap<>();
        RequestSender requestSender = new RequestSender(this);
        parametros.put("nombreUsuario", usuario.getNombreUsuario());
        parametros.put("password", usuario.getPassword());
        parametros.put("nombre", usuario.getNombre());
        parametros.put("apellido", usuario.getApellido());
        parametros.put("email", usuario.getEmail());

        JSONObject jsonObject = new JSONObject(parametros);

        String url = getString(R.string.urlAppServer) + "comprame/registrarse";

        ResponseListener listener = new RegistrarseListener(this);

        requestSender.doPost(listener, url, jsonObject);
    }

    /*
    @Override
    public void onRequestCompleted(Object response) {

            JSONObject jsonObject = (JSONObject) response;
            Log.d("RegisterResponse", jsonObject.toString());
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestError(int errorCode, String errorMessage) {

        if (errorCode == 500) {
            Log.d("RegisterResponse", "Llega el error 500");
            Toast.makeText(this, "Llega el error 500", Toast.LENGTH_LONG).show();
        }
    }*/
}

