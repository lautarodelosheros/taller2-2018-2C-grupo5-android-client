package taller2_2018_2c_grupo5.comprame.actividades;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.actividades.comunes.RecyclerFragment;
import taller2_2018_2c_grupo5.comprame.dominio.Item;
import taller2_2018_2c_grupo5.comprame.servicios.RequestSender;
import taller2_2018_2c_grupo5.comprame.servicios.listeners.BuscarItemsListener;
import taller2_2018_2c_grupo5.comprame.vista.ItemsAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class BuscarItemsFragment extends RecyclerFragment {
    private static final String ARG_PARAM1 = "session";

    private String session;
    private ArrayList<Item> items = new ArrayList<>();

    private ItemsAdapter mAdapter;

    private ProgressDialog progressDialog;

    private FloatingActionButton fab;

    public BuscarItemsFragment() {
        // Required empty public constructor
    }

    public static BuscarItemsFragment newInstance(String session) {
        BuscarItemsFragment fragment = new BuscarItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, session);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected void configureAdapter() {
        this.mAdapter = new ItemsAdapter(items);
        this.setConfiguredAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            session = getArguments().getString(ARG_PARAM1);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.filtro:
                mostrarPopUpFiltro();
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarPopUpFiltro() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_filtro, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);

        final EditText nombreFiltro = popupView.findViewById(R.id.input_nombre_articulo);
        final EditText descripcionFiltro = popupView.findViewById(R.id.input_descripcion_articulo);
        final EditText ubicacionFiltro = popupView.findViewById(R.id.input_ubicacion_articulo);
        Button botonFiltrar = popupView.findViewById(R.id.boton_filtrar);

        // dismiss the popup window when touched
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                traerItems(nombreFiltro.getText().toString(),
                        descripcionFiltro.getText().toString(),
                        ubicacionFiltro.getText().toString());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Buscando publicaciones...");
        progressDialog.show();

        traerItems("", "", "");

    }

    private void traerItems(String filtroNombre, String filtroDescripcion, String filtroUbicacion) {

        String url = getString(R.string.urlAppServer) + "articulos?limite=1&offset=1";

        if (!filtroNombre.isEmpty())
            url += "&nombre=" + filtroNombre;
        if (!filtroDescripcion.isEmpty())
            url += "&descripcion=" + filtroDescripcion;
        if (!filtroUbicacion.isEmpty())
            url += "&ubicacion_geografica=" + filtroUbicacion;

        BuscarItemsListener listener = new BuscarItemsListener(this);

        RequestSender requestSender = new RequestSender(getActivity());
        requestSender.doGet_expectArray(listener, url);
    }

    /*private void mockearItems() {
        final MetodoDePago metodoDePago = new MetodoDePago("Tarjeta", "Visa", 6);
        final MetodoDePago metodoDePago1 = new MetodoDePago("Tarjeta", "MasterCard", 3);
        ArrayList<MetodoDePago> metodosDePago = new ArrayList<MetodoDePago>() {{
            add(metodoDePago);
            add(metodoDePago1);
        }};
        ArrayList<String> categorias1 = new ArrayList<String>() {{
            add("Zapatillas");
            add("Ropa");
        }};
        Item item1 = new Item("Zapatillas solo 6 meses de uso OFERTA",
                "Son zapatillas, el talle no me lo acuerdo...",
                120.5, "Juancito5", "Temperley", metodosDePago, categorias1);
        ArrayList<String> categorias2 = new ArrayList<String>() {{
            add("Remeras");
            add("Ropa");
        }};
        Item item2 = new Item("Remeras lavadas listas para usar",
                "Las lavo cuando la compra está confirmada, NO ANTES",
                300, "LaMerchu", "Lanús", metodosDePago, categorias2);
        item1.addFoto("https://cdn.pixabay.com/photo/2017/04/09/18/54/shoes-2216498_960_720.jpg");
        item2.addFoto("https://cdn.pixabay.com/photo/2016/11/21/15/45/heartache-1846050_960_720.jpg");
        items.add(item1);
        items.add(item2);
    }*/

    public void onSearchFailed() {
        progressDialog.dismiss();
    }

    public void onSearchSuccess(List<Item> items) {
        progressDialog.dismiss();
        this.items = new ArrayList<>(items);
        mAdapter.refreshData(this.items);
    }
}
