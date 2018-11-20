package com.comprame.categories;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.buy.BuyItem;
import com.comprame.library.rest.Headers;
import com.comprame.library.rest.Query;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.overview.OverviewFragment;
import com.comprame.overview.OverviewViewModel;
import com.comprame.search.SearchFilterPopUp;
import com.comprame.search.SearchFragment;
import com.comprame.search.SearchItem;
import com.comprame.search.SearchItemsAdapter;
import com.comprame.search.SearchViewModel;
import com.comprame.sell.Geolocation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.comprame.MainActivity.PLACE_PICKER_REQUEST;

public class CategoriesFragment extends Fragment {
    private static final int NUMBER_OF_COLUMNS = 3;

    private CategoriesViewModel categoriesViewModel;
    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        categoriesViewModel = ViewModelProviders.of(this)
                .get(CategoriesViewModel.class);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.categories_fragment
                        , container
                        , false);

        recyclerView = view.getRoot().findViewById(R.id.categories_recycler);
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext()
                , DividerItemDecoration.VERTICAL));
        categoriesAdapter = new CategoriesAdapter(categoriesViewModel, this);
        recyclerView.setAdapter(categoriesAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        categoriesViewModel.removeAllItems();
        fetchCategories(view);
    }

    public void fetchCategories(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Buscando categorías...", getContext());
        progressPopup.show();

        App.appServer.get("/item/categories/"
                , String[].class
                , Headers.Authorization(Session.getInstance()))
                .onDone((i, ex) -> progressPopup.dismiss())
                .run(
                        (String[] categories) -> {
                            if (categories.length == 0)
                                Toast.makeText(view.getContext()
                                        , "Sin Resultados"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            categoriesViewModel.addItems(Arrays.asList(categories));
                        }
                        , (Exception ex) -> {
                            Log.d("CategoriesListener", "Recuperando categorias", ex);
                            Toast.makeText(getActivity()
                                    , "Error al buscar las categorias en el servidor"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    public void goSearch(String category) {
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setCategory(category);
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, searchFragment)
                .addToBackStack(null)
                .commit();
    }
}
