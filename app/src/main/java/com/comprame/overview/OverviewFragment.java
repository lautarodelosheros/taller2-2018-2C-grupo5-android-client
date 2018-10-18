package com.comprame.overview;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.buy.BuyFragment;
import com.comprame.buy.BuyViewModel;
import com.comprame.databinding.OverviewFragmentBinding;
import com.comprame.library.rest.Query;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.search.SearchItem;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Arrays;

public class OverviewFragment extends Fragment {

    private OverviewViewModel overviewViewModel;
    private QuestionsList questionsList;
    private NewQuestionPopupViewModel questionPopupViewModel;

    private SliderLayout mSlider;

    ProgressPopup progressPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        questionPopupViewModel = ViewModelProviders.of(this)
                .get(NewQuestionPopupViewModel.class);

        OverviewFragmentBinding binding = OverviewFragmentBinding.inflate(inflater, container, false);
        overviewViewModel = ViewModelProviders.of(getActivity()).get(OverviewViewModel.class);
        binding.setModel(overviewViewModel);
        binding.setFragment(this);

        ViewDataBinding view =
                DataBindingUtil.inflate(inflater
                        , R.layout.overview_fragment
                        , container
                        , false);

        mSlider = binding.slider;

        if (overviewViewModel.item.getImages() != null) {

            for (String url : overviewViewModel.item.getImages()) {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        questionsList = new QuestionsList(getView().findViewById(R.id.questions_list));
        loadQuestions();
    }

    private void loadQuestions() {
        progressPopup = new ProgressPopup("Cargando preguntas...", getContext());
        progressPopup.show();
        App.appServer.get(
                Query.query("/question/")
                        .and("item_id", overviewViewModel.item.getId())
                , Question[].class)
                .onDone((i, ex) -> progressPopup.dismiss())
                .run((Question[] questions) -> questionsList.setQuestions(Arrays.asList(questions))
                        , (Exception ex) -> {
                            Log.d("QuestionsListener", "Error al buscar las preguntas", ex);
                            Toast.makeText(getActivity()
                                    , "Error al buscar las preguntas en el servidor"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        });
    }

    public void buy(View view) {
        BuyFragment buyFragment = new BuyFragment();
        BuyViewModel buyViewModel = ViewModelProviders.of(getActivity()).get(BuyViewModel.class);
        buyViewModel.item = overviewViewModel.item;
        buyViewModel.total.setValue(overviewViewModel.item.getUnitPrice());
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, buyFragment)
                .commit();
    }

    public void newQuestion(View view) {
        NewQuestionPopup newQuestionPopup = new NewQuestionPopup(this
                , questionPopupViewModel
                , this::createNewQuestion);
        newQuestionPopup.show();
    }

    private void createNewQuestion(View view) {
        ProgressPopup progressDialog = new ProgressPopup("Creando pregunta...", this.getContext());
        progressDialog.show();
        App.appServer.post("/question/"
                , questionPopupViewModel.asQuestion()
                , Session.class)
                .onDone((s, ex) -> progressDialog.dismiss())
                .run(s -> loadQuestions()
                        , ex -> Toast.makeText(this.getContext()
                                , "Error creando la pregunta"
                                , Toast.LENGTH_LONG).show());
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mSlider.stopAutoCycle();
        super.onStop();
    }

}