package com.comprame.mysellings;

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
import com.comprame.mypurchases.MyPurchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MySellingsFragment extends Fragment {

    private MySellingsViewModel mySellingsViewModel;

    private List<MyPurchase> mySellings = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {
        mySellingsViewModel = ViewModelProviders.of(this)
                .get(MySellingsViewModel.class);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.my_sellings_fragment
                        , container
                        , false);

        RecyclerView recyclerView = view.getRoot().findViewById(R.id.my_sellings_items);
        Objects.requireNonNull(recyclerView.getItemAnimator()).setAddDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext()
                , DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MySellingsItemsAdapter(mySellingsViewModel, this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fetchMySellings(view);
    }

    public void fetchMySellings(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Cargando Mis Ventas...", getContext());
        progressPopup.show();

        App.appServer.get("/purchase/?seller" + Session.getInstance().getSessionToken()
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
                            Log.d("MySellingsListener", "Error al buscar ventas", ex);
                            Toast.makeText(getActivity()
                                    , "Error al buscar las ventas en el servidor"
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
                    mySellingsViewModel.addItem(item);
                    mySellings.add(purchase);
                }, (ex) -> {
                    Log.d("MySellingsListener", "Recuperando Item", ex);
                    Toast.makeText(getActivity()
                            , "Error al buscar la venta"
                            , Toast.LENGTH_LONG)
                            .show();
                });
    }

    public void overviewMySelling(BuyItem buyItem) {
        OverviewMySellViewModel overviewMySellViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity())).get(OverviewMySellViewModel.class);
        overviewMySellViewModel.item = buyItem;
        OverviewMySellFragment overviewMySellFragment = new OverviewMySellFragment();
        overviewMySellFragment.setSell(mySellings.get(mySellingsViewModel.positionOf(buyItem)));
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, overviewMySellFragment, "OverviewMySellFragment")
                .commit();
    }
}
