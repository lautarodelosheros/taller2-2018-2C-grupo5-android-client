package com.comprame.sell;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.databinding.SellFragmentBinding;
import com.comprame.domain.Session;
import com.comprame.library.view.ProgressPopup;
import com.comprame.search.SearchFragment;

public class SellFragment extends Fragment {

    private SellViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        SellFragmentBinding sellFragmentBinding = SellFragmentBinding.inflate(inflater, container, false);
        sellFragmentBinding.setFragment(this);
        model = ViewModelProviders.of(this).get(SellViewModel.class);
        sellFragmentBinding.setData(model);
        return sellFragmentBinding.getRoot();
    }


    public void sell(View view) {
        ProgressPopup progressDialog = new ProgressPopup("Procesando...", this.getContext());
        progressDialog.show();
        App.appServer.post("/sell"
                , model.asSellItem()
                , Session.class)
                .onDone((s, ex) -> progressDialog.dismiss())
                .run(s -> search()
                        , this::showToastError);
    }

    private void showToastError(Exception e) {
        Toast.makeText(this.getContext()
                , "Error publicando la venta por favor reintente en unos minutos"
                , Toast.LENGTH_LONG).show();
    }

    private void search() {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SearchFragment())
                .commit();
    }
}