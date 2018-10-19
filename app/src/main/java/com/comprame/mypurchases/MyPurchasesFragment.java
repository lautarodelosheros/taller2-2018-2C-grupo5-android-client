package com.comprame.mypurchases;

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
import com.comprame.buy.BuyItem;
import com.comprame.buy.Purchase;
import com.comprame.library.rest.Query;
import com.comprame.library.view.ProgressPopup;
import com.comprame.overview.OverviewFragment;
import com.comprame.overview.OverviewViewModel;
import com.comprame.search.SearchFilterPopUp;
import com.comprame.search.SearchItem;
import com.comprame.search.SearchItemsAdapter;
import com.comprame.search.SearchViewModel;

import java.util.Arrays;
import java.util.List;

public class MyPurchasesFragment extends Fragment {
    private static final int ITEM_OFFSET = 5;

    private String session;
    private MyPurchasesViewModel myPurchasesViewModel;
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
        myPurchasesViewModel = ViewModelProviders.of(this)
                .get(MyPurchasesViewModel.class);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.my_purchases_fragment
                        , container
                        , false);

        recyclerView = view.getRoot().findViewById(R.id.my_purchases_items);
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext()
                , DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MyPurchasesItemsAdapter(myPurchasesViewModel, this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BuyItem buyItem = new BuyItem("ididid"
                , "Articulo mockeado", "Desc"
                , 120.20, "Pepito"
                , "Buenos Aires", null
                , "Cat");
        buyItem.addImage("https://i.pinimg.com/236x/1c/23/cc/1c23cce271fbc3c6b83c7870ac9eb62e.jpg");
        myPurchasesViewModel.addItem(buyItem);

        //TODO: Borrar Mock
        //fetchMyPurchases(view);
    }

    public void fetchMyPurchases(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Cargando Mis Compras...", getContext());
        progressPopup.show();

        App.appServer.get("/purchase/" + session
                , Purchase[].class)
                .onDone((i, ex) -> progressPopup.dismiss())
                .run(
                        (Purchase[] purchases) -> {
                            if (purchases.length == 0)
                                Toast.makeText(view.getContext()
                                        , "Sin Resultados"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            else
                                for (Purchase purchase: purchases)
                                    fetchItem(purchase.itemId);
                        }
                        , (Exception ex) -> {
                            Log.d("MyPurchasesListener", "Error al buscar compras", ex);
                            Toast.makeText(getActivity()
                                    , "Error al buscar las compras en el servidor"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    private void fetchItem(String itemId) {
        App.appServer.get("/item/" + itemId,
                BuyItem.class)
                .run((item) -> myPurchasesViewModel.addItem(item), (ex) -> {
                    Log.d("MyPurchasesListener", "Recuperando Item", ex);
                    Toast.makeText(getActivity()
                            , "Error al buscar la compra"
                            , Toast.LENGTH_LONG)
                            .show();
                });
    }

    public void openChat(BuyItem buyItem) {
        //TODO: Chat con vendedor en Firebase
        Toast.makeText(getContext(), "Chat con vendedor", Toast.LENGTH_LONG).show();
    }
}
