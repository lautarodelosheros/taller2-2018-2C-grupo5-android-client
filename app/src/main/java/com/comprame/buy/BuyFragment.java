package com.comprame.buy;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.databinding.BuyFragmentBinding;
import com.comprame.library.view.ProgressPopup;
import com.comprame.search.SearchFragment;

public class BuyFragment extends Fragment {

    private BuyViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        BuyFragmentBinding binding = BuyFragmentBinding.inflate(inflater, container, false);
        model = ViewModelProviders.of(getActivity()).get(BuyViewModel.class);
        binding.setData(model);
        binding.setFragment(this);
        return binding.getRoot();
    }

    public void buy(View item) {
        ProgressPopup popupWindow = new ProgressPopup("Procesando Pago", getContext());
        popupWindow.show();
        App.appServer.post("/purchase/"
                , model.asPuchase()
                , Object.class)
                .onDone((ok, error) -> popupWindow.dismiss())
                .run(
                        (ok) ->
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_container, new SearchFragment())
                                        .commit()
                        , (ex) -> {
                            Log.d("BuyFragment", "Comprando Item", ex);
                            Toast.makeText(getActivity()
                                    , "Error al procesar el pago"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        });
    }
}