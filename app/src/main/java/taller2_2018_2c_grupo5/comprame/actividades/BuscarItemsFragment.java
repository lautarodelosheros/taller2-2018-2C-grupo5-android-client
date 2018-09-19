package taller2_2018_2c_grupo5.comprame.actividades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.dominio.Item;
import taller2_2018_2c_grupo5.comprame.dominio.MetodoDePago;
import taller2_2018_2c_grupo5.comprame.vista.ItemsAdapter;

public class BuscarItemsFragment extends RecyclerFragment {
    private static final String ARG_PARAM1 = "session";

    private String session;
    private ArrayList<Item> items = new ArrayList<>();

    private RecyclerView recyclerView;
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
        ItemsAdapter itemsAdapter = new ItemsAdapter(items);
        this.setConfiguredAdapter(itemsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            session = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buscar_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recycler_view_items);

        //TODO: Reemplazar por un llamado al server
        mockearItems();

        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Agregar Item", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mockearItems() {
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
    }

}
