package com.comprame.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.buy.BuyItem;
import com.comprame.library.rest.Query;
import com.comprame.library.view.ProgressPopup;
import com.comprame.overview.OverviewFragment;
import com.comprame.overview.OverviewViewModel;
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

public class SearchFragment extends Fragment {
    private static final int ITEM_OFFSET = 5;

    private SearchViewModel searchViewModel;
    private SearchFilterPopUp searchFilterPopUp;
    private RecyclerView recyclerView;

    private boolean moreItemsLeft = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this)
                .get(SearchViewModel.class);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.search_fragment
                        , container
                        , false);

        recyclerView = view.getRoot().findViewById(R.id.search_items);
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext()
                , DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new SearchItemsAdapter(searchViewModel, this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int numberOfItems = linearLayoutManager.getItemCount();
                int visibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (moreItemsLeft && visibleItem >= numberOfItems - 1 /* hasMoreItems */) {
                    searchViewModel.incOffset(ITEM_OFFSET);
                    fetchItems(recyclerView);
                }
            }
        });

        searchFilterPopUp = new SearchFilterPopUp(this
                , searchViewModel
                , this::filterItems);
        return view.getRoot();
    }

    private void filterItems(View view) {
        recyclerView.scrollToPosition(1);
        searchViewModel.setOffset(0);
        searchViewModel.removeAllItems();
        fetchItems(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filtro)
            searchFilterPopUp.show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fetchItems(view);
    }

    public void fetchItems(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Buscando publicaciones...", getContext());
        progressPopup.show();

        SearchViewModel.SearchFilter filter = searchViewModel
                .filter();
        App.appServer.get(
                Query.query("/item/")
                        .and("limit", filter.size)
                        .and("offset", filter.offset)
                        .and("name", filter.name)
                        .and("description", filter.description)
                        .and("latitude", filter.geolocation != null ? filter.geolocation.getLatitude() : null)
                        .and("longitude", filter.geolocation != null ? filter.geolocation.getLongitude() : null)
                        .and("kilometers", filter.kilometers)

                , SearchItem[].class)
                .onDone((i, ex) -> progressPopup.dismiss())
                .run(
                        (SearchItem[] searchItems) -> {
                            if (searchItems.length == 0)
                                Toast.makeText(view.getContext()
                                        , "Sin Resultados"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            searchViewModel.addItems(Arrays.asList(searchItems));
                            moreItemsLeft = searchItems.length >= ITEM_OFFSET;
                        }
                        , (Exception ex) -> {
                            Log.d("BuscarItemsListener", "Recuperando Items", ex);
                            Toast.makeText(getActivity()
                                    , "Error al buscar las publicaciones en el servidor"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    public void overviewItem(SearchItem item) {
        ProgressPopup progressPopup = new ProgressPopup("Cargando publicación...", getContext());
        progressPopup.show();
        App.appServer.get("/item/" + item.id,
                BuyItem.class)
                .run((ok) -> {
                    progressPopup.dismiss();
                    OverviewFragment overviewFragment = new OverviewFragment();
                    OverviewViewModel overviewViewModel = ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
                    overviewViewModel.item = ok;
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, overviewFragment)
                            .commit();
                }, (ex) -> {
                    progressPopup.dismiss();
                    Log.d("BuscarItemsListener", "Recuperando Item", ex);
                    Toast.makeText(getActivity()
                            , "Error al buscar el item"
                            , Toast.LENGTH_LONG)
                            .show();
                });
    }

    public void openPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            getActivity().startActivityForResult(builder.build(Objects.requireNonNull(getActivity())), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(Objects.requireNonNull(getActivity()), data);
                    LatLng latLng = place.getLatLng();
                    String placeName = String.format("%s", place.getAddress());
                    searchViewModel.filter().geolocation = new Geolocation(latLng.latitude, latLng.longitude, placeName);
            }
        }
    }

}
