package com.comprame.mysellings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.databinding.OverviewMySellFragmentBinding;
import com.comprame.library.view.GlideSliderView;
import com.comprame.mypurchases.ChatFragment;
import com.comprame.mypurchases.MyPurchase;
import com.daimajia.slider.library.SliderLayout;

import java.util.Objects;

public class OverviewMySellFragment extends Fragment {

    private MyPurchase sell;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        OverviewMySellFragmentBinding binding = OverviewMySellFragmentBinding.inflate(inflater, container, false);
        OverviewMySellViewModel overviewMySellViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(OverviewMySellViewModel.class);
        binding.setModel(overviewMySellViewModel);
        binding.setFragment(this);

        SliderLayout mSlider = binding.slider;

        if (overviewMySellViewModel.item.getImages() != null) {

            for (String url : overviewMySellViewModel.item.getImages()) {
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

    public void setSell(MyPurchase sell) {
        this.sell = sell;
    }

    public void openChat(View view) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setPurchase(this.sell);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, chatFragment, "ChatFragement")
                .addToBackStack(null)
                .commit();
    }

}