package taller2_2018_2c_grupo5.comprame.actividades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import taller2_2018_2c_grupo5.comprame.R;

public class BuscarItemsFragment extends Fragment {
    private static final String ARG_PARAM1 = "session";

    private String session;

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
        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Agregar Item", Toast.LENGTH_LONG).show();
            }
        });
    }

}
