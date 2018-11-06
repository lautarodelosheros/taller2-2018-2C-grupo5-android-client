package com.comprame.mypurchases;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.databinding.OverviewMyPurchaseFragmentBinding;
import com.comprame.library.view.GlideSliderView;
import com.daimajia.slider.library.SliderLayout;

import java.util.Objects;

public class OverviewMyPurchaseFragment extends Fragment {

    private MyPurchase purchase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        OverviewMyPurchaseFragmentBinding binding = OverviewMyPurchaseFragmentBinding
                .inflate(inflater, container, false);
        OverviewMyPurchaseViewModel overviewMyPurchaseViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity())).get(OverviewMyPurchaseViewModel.class);
        binding.setModel(overviewMyPurchaseViewModel);
        binding.setFragment(this);

        SliderLayout mSlider = binding.slider;

        if (overviewMyPurchaseViewModel.item.getImages() != null) {

            for (String url : overviewMyPurchaseViewModel.item.getImages()) {
                GlideSliderView sliderView = new GlideSliderView(getContext());
                sliderView
                        .image(url);
                mSlider.addSlider(sliderView);
            }
            mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSlider.setDuration(8000);
        } else {
            mSlider.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    public void setPurchase(MyPurchase purchase) {
        this.purchase = purchase;
    }

    public void openChat(View view) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setPurchase(this.purchase);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, chatFragment, "ChatFragment")
                .commit();
    }

}