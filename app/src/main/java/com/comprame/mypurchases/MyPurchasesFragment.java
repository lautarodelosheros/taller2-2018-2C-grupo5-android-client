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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.buy.BuyItem;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyPurchasesFragment extends Fragment {

    private MyPurchasesViewModel myPurchasesViewModel;

    private List<MyPurchase> myPurchases = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        myPurchasesViewModel = ViewModelProviders.of(this)
                .get(MyPurchasesViewModel.class);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.my_purchases_fragment
                        , container
                        , false);

        RecyclerView recyclerView = view.getRoot().findViewById(R.id.my_purchases_items);
        Objects.requireNonNull(recyclerView.getItemAnimator()).setAddDuration(1000);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fetchMyPurchases(view);
    }

    public void fetchMyPurchases(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Cargando Mis Compras...", getContext());
        progressPopup.show();

        App.appServer.get("/purchase/?buyer"
                , MyPurchase[].class
                , Headers.Authorization(Session.getInstance()))
                .onDone((i, ex) -> progressPopup.dismiss())
                .run(
                        (MyPurchase[] purchases) -> {
                            if (purchases.length == 0)
                                Toast.makeText(view.getContext()
                                        , "Sin Resultados"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            else

                                for (MyPurchase purchase: purchases)
                                    fetchItem(purchase, purchase.itemId);
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

    private void fetchItem(MyPurchase purchase, String itemId) {
        App.appServer.get("/item/" + itemId,
                BuyItem.class
                , Headers.Authorization(Session.getInstance()))
                .run((item) -> {
                    myPurchasesViewModel.addItem(item);
                    myPurchases.add(purchase);
                }, (ex) -> {
                    Log.d("MyPurchasesListener", "Recuperando Item", ex);
                    Toast.makeText(getActivity()
                            , "Error al buscar la compra"
                            , Toast.LENGTH_LONG)
                            .show();
                });
    }

    public void overviewMyPurchase(BuyItem buyItem) {
        OverviewMyPurchaseViewModel overviewMyPurchaseViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity())).get(OverviewMyPurchaseViewModel.class);
        overviewMyPurchaseViewModel.item = buyItem;
        OverviewMyPurchaseFragment overviewMyPurchaseFragment = new OverviewMyPurchaseFragment();
        overviewMyPurchaseFragment.setPurchase(myPurchases.get(myPurchasesViewModel.positionOf(buyItem)));
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, overviewMyPurchaseFragment, "OverviewMyPurchaseFragment")
                .commit();
    }
}
