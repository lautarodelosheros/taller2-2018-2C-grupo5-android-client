package com.comprame.mypurchases;

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
import android.widget.Toast;

import com.comprame.App;
import com.comprame.MainActivity;
import com.comprame.R;
import com.comprame.buy.BuyFragment;
import com.comprame.buy.BuyItem;
import com.comprame.buy.BuyViewModel;
import com.comprame.databinding.OverviewMyPurchaseFragmentBinding;
import com.comprame.library.rest.Query;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.overview.AnswerQuestionPopupViewModel;
import com.comprame.overview.NewQuestionPopup;
import com.comprame.overview.NewQuestionPopupViewModel;
import com.comprame.overview.OverviewViewModel;
import com.comprame.overview.Question;
import com.comprame.overview.QuestionsList;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.Arrays;
import java.util.Objects;

public class OverviewMyPurchaseFragment extends Fragment {

    private OverviewMyPurchaseViewModel overviewMyPurchaseViewModel;

    private SliderLayout mSlider;

    private MyPurchase purchase;

    ProgressPopup progressPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        OverviewMyPurchaseFragmentBinding binding = OverviewMyPurchaseFragmentBinding.inflate(inflater, container, false);
        overviewMyPurchaseViewModel = ViewModelProviders.of(getActivity()).get(OverviewMyPurchaseViewModel.class);
        binding.setModel(overviewMyPurchaseViewModel);
        binding.setFragment(this);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.overview_my_purchase_fragment
                        , container
                        , false);

        mSlider = binding.slider;

        if (overviewMyPurchaseViewModel.item.getImages() != null) {

            for (String url : overviewMyPurchaseViewModel.item.getImages()) {
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

    public void setPurchase(MyPurchase purchase) {
        this.purchase = purchase;
    }

    public void openChat(View view) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setPurchase(this.purchase);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, chatFragment, "ChatFragement")
                .commit();
    }

}