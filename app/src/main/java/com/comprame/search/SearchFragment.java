package com.comprame.search;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.domain.Item;
import com.comprame.library.rest.Query;

import java.util.Arrays;

public class SearchFragment extends Fragment {
    private static final int ITEM_OFFSET = 5;

    private String session;
    private SearchViewModel searchViewModel;
    private SearchFilterPopUp searchFilterPopUp;
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        this.session = getActivity().
                getIntent()
                .getExtras()
                .getString("session");
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
        recyclerView.setAdapter(new SearchItemsAdapter(searchViewModel));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int numberOfItems = linearLayoutManager.getItemCount();
                int visibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (visibleItem >= numberOfItems - 1 /* hasMoreItems */) {
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
        ProgressDialog progressDialog = new ProgressDialog(getActivity()
                , R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Buscando publicaciones...");
        progressDialog.show();

        SearchViewModel.SearchFilter filter = searchViewModel
                .filter();
        App.items.get(Query
                        .query("limite", filter.size)
                        .and("offset", filter.offset)
                        .and("nombre", filter.name)
                        .and("description", filter.description)
                        .and("ubicacion_geografica", filter.location)

                , Item[].class)
                .onDone((i, ex) -> progressDialog.dismiss())
                .run(
                        (Item[] items) -> {
                            if (items.length == 0)
                                Toast.makeText(view.getContext()
                                        , "Sin Resultados"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            searchViewModel.addItems(Arrays.asList(items));
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


}
