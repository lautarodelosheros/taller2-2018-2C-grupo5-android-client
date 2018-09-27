package taller2_2018_2c_grupo5.comprame.actividades;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.actividades.comunes.RecyclerFragment;
import taller2_2018_2c_grupo5.comprame.dominio.Item;
import taller2_2018_2c_grupo5.comprame.servicios.RequestSender;
import taller2_2018_2c_grupo5.comprame.servicios.listeners.BuscarItemsListener;
import taller2_2018_2c_grupo5.comprame.vista.ItemsAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class BuscarItemsFragment extends RecyclerFragment {
    private static final String ARG_PARAM1 = "session";
    public static final int LIMITE_CANTIDAD_ARTICULOS = 5;

    private String session;
    private ArrayList<Item> items = new ArrayList<>();

    private ItemsAdapter mAdapter;

    private ProgressDialog progressDialog;

    private FloatingActionButton fab;

    private String nombreFiltroAnterior = "";
    private String descripcionFiltroAnterior = "";
    private String ubicacionFiltroAnterior = "";

    private int totalItemCount;
    private int ultimoItemVisible;
    private boolean cargando;

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

        configurarLoadMore();
    }

    private void configurarLoadMore() {

        RecyclerView recyclerView = mList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    ultimoItemVisible = linearLayoutManager.findLastVisibleItemPosition();
                    if (!cargando && totalItemCount <= ultimoItemVisible + 1) {
                        // Se llego al final de la lista y hay que cargar mas articulos
                        mAdapter.agregarProgressBar();
                        traerItems(nombreFiltroAnterior, descripcionFiltroAnterior, ubicacionFiltroAnterior,
                                LIMITE_CANTIDAD_ARTICULOS, mAdapter.getItemCount() - 1);
                        cargando = true;
                    }
                }
            });
        }
    }

    public void setCargado() {
        cargando = false;
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
        View popupView = Objects.requireNonNull(inflater).inflate(R.layout.popup_filtro, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);

        final EditText nombreFiltro = popupView.findViewById(R.id.input_nombre_articulo);
        final EditText descripcionFiltro = popupView.findViewById(R.id.input_descripcion_articulo);
        final EditText ubicacionFiltro = popupView.findViewById(R.id.input_ubicacion_articulo);
        Button botonFiltrar = popupView.findViewById(R.id.boton_filtrar);

        nombreFiltro.setText(nombreFiltroAnterior);
        descripcionFiltro.setText(descripcionFiltroAnterior);
        ubicacionFiltro.setText(ubicacionFiltroAnterior);

        // dismiss the popup window when touched
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                nombreFiltroAnterior = nombreFiltro.getText().toString();
                descripcionFiltroAnterior = descripcionFiltro.getText().toString();
                ubicacionFiltroAnterior = ubicacionFiltro.getText().toString();
                limpiarListaItems();

                progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Buscando publicaciones...");
                progressDialog.show();

                traerItems(nombreFiltroAnterior, descripcionFiltroAnterior, ubicacionFiltroAnterior,
                        LIMITE_CANTIDAD_ARTICULOS, 0);
            }
        });
    }

    private void limpiarListaItems() {
        this.items.clear();
        this.mAdapter.clearItems();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Buscando publicaciones...");
        progressDialog.show();

        traerItems("", "", "",
                LIMITE_CANTIDAD_ARTICULOS, 0);
    }

    private void traerItems(String filtroNombre, String filtroDescripcion, String filtroUbicacion,
                            int limite, int offset) {

        String url = getString(R.string.urlAppServer)
                + "articulos/?limite=" + String.format(Locale.getDefault(), "%d", limite)
                + "&offset=" + String.format(Locale.getDefault(), "%d", offset);

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
        setCargado();
    }

    public void onSearchSuccess(List<Item> items) {
        progressDialog.dismiss();
        this.items.addAll(items);
        mAdapter.quitarProgressBar();
        mAdapter.addItems(this.items);
        this.items.clear();
        setCargado();
    }
}
