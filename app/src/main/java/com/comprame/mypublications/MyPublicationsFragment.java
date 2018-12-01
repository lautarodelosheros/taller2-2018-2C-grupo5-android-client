package com.comprame.mypublications;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyPublicationsFragment extends Fragment {

    private MyPublicationsViewModel myPublicationsViewModel;
    private MyPublicationsItemsAdapter myPublicationsItemsAdapter;

    private List<Publication> myPublications = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        myPublicationsViewModel = ViewModelProviders.of(this)
                .get(MyPublicationsViewModel.class);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.my_publications_fragment
                        , container
                        , false);

        RecyclerView recyclerView = view.getRoot().findViewById(R.id.my_publications_items);
        Objects.requireNonNull(recyclerView.getItemAnimator()).setAddDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext()
                , DividerItemDecoration.VERTICAL));
        myPublicationsItemsAdapter = new MyPublicationsItemsAdapter(myPublicationsViewModel, this);
        recyclerView.setAdapter(myPublicationsItemsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        myPublicationsItemsAdapter.removeAllItems();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fetchMyPublications(view);
    }

    public void fetchMyPublications(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Cargando Mis Publicaciones...", getContext());
        progressPopup.show();

        App.appServer.get("/item/?seller=true"
                , Publication[].class
                , Headers.Authorization(Session.getInstance()))
                .onDone((i, ex) -> progressPopup.dismiss())
                .run(
                        (Publication[] publications) -> {
                            if (publications.length == 0)
                                Toast.makeText(view.getContext()
                                        , "Sin Resultados"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            else
                                for (Publication publication: publications)
                                    myPublicationsViewModel.addItem(publication);
                        }
                        , (Exception ex) -> {
                            Log.d("MyPublicationsListener", "Error al buscar publicaciones", ex);
                            Toast.makeText(getActivity()
                                    , "Error al buscar las publicaciones en el servidor"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    public void overviewMyPublication(Publication publication) {
        EditPublicationFragment editPublicationFragment = new EditPublicationFragment();
        editPublicationFragment.setPublication(publication);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, editPublicationFragment, "EditPublicationFragment")
                .addToBackStack(null)
                .commit();
    }
}
