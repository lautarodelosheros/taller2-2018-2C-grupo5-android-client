package com.comprame.overview;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comprame.R;
import com.comprame.buy.BuyFragment;
import com.comprame.buy.BuyViewModel;
import com.comprame.databinding.OverviewFragmentBinding;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {

    private OverviewViewModel model;

    private SliderLayout mSlider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        OverviewFragmentBinding binding = OverviewFragmentBinding.inflate(inflater, container, false);
        model = ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
        binding.setModel(model);
        binding.setFragment(this);

        mSlider = binding.slider;

        if (model.item.getImages() != null) {

            for (String url : model.item.getImages()) {
                DefaultSliderView sliderView = new DefaultSliderView(getContext());
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

    public void buy(View view) {
        BuyFragment buyFragment = new BuyFragment();
        BuyViewModel buyViewModel = ViewModelProviders.of(getActivity()).get(BuyViewModel.class);
        buyViewModel.item = model.item;
        buyViewModel.total.setValue(model.item.getUnitPrice());
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, buyFragment)
                .commit();
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mSlider.stopAutoCycle();
        super.onStop();
    }

}