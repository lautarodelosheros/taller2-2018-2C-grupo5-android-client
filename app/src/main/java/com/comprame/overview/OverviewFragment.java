package com.comprame.overview;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.buy.BuyFragment;
import com.comprame.buy.BuyViewModel;
import com.comprame.databinding.OverviewFragmentBinding;
import com.comprame.library.rest.Headers;
import com.comprame.library.rest.Query;
import com.comprame.library.view.GlideSliderView;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.login.User;
import com.daimajia.slider.library.SliderLayout;

import java.util.Arrays;
import java.util.Objects;

public class OverviewFragment extends Fragment {

    private OverviewViewModel overviewViewModel;
    private QuestionsList questionsList;
    private NewQuestionPopupViewModel newQuestionPopupViewModel;
    public AnswerQuestionPopupViewModel answerQuestionPopupViewModel;

    private SliderLayout mSlider;

    private User user;

    ProgressPopup progressPopup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        newQuestionPopupViewModel = ViewModelProviders.of(this)
                .get(NewQuestionPopupViewModel.class);
        answerQuestionPopupViewModel = ViewModelProviders.of(this)
                .get(AnswerQuestionPopupViewModel.class);

        OverviewFragmentBinding binding = OverviewFragmentBinding.inflate(inflater, container, false);
        overviewViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(OverviewViewModel.class);
        binding.setModel(overviewViewModel);
        binding.setFragment(this);

        mSlider = binding.slider;

        if (overviewViewModel.item.getImages() != null) {

            for (String url : overviewViewModel.item.getImages()) {
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        questionsList = new QuestionsList(Objects.requireNonNull(getView())
                .findViewById(R.id.questions_list), this);
        loadOverview();
    }

    private void loadQuestions() {
        App.appServer.get(
                Query.query("/question/")
                        .and("item_id", overviewViewModel.item.getId())
                , Question[].class
                , Headers.Authorization(Session.getInstance()))
                .onDone((i, ex) -> progressPopup.dismiss())
                .run((Question[] questions) -> questionsList
                                .setQuestions(Arrays.asList(questions)
                                        , user.getName().equals(overviewViewModel.item.getSeller()))
                        , (Exception ex) -> {
                            Log.d("QuestionsListener", "Error al buscar las preguntas", ex);
                            Toast.makeText(getActivity()
                                    , "Error al buscar las preguntas en el servidor"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        });
    }

    private void loadOverview() {
        progressPopup = new ProgressPopup("Cargando publicación...", getContext());
        progressPopup.show();

        String path = "/user/" + Session.getInstance().getSessionToken();

        App.appServer.get(path, User.class
                , Headers.Authorization(Session.getInstance()))
                .run(
                        (User user) -> {
                            if (user.getName().isEmpty()) {
                                Toast.makeText(getContext()
                                        , "No se encuentra el usuario"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                this.user = user;
                                loadQuestions();
                            }
                        }
                        , (Exception ex) -> {
                            Log.d("ProfileListener", "Error al recuperar el perfil", ex);
                            Toast.makeText(getContext()
                                    , "Error al cargar el perfil"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    public void buy(View view) {
        BuyFragment buyFragment = new BuyFragment();
        BuyViewModel buyViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity())).get(BuyViewModel.class);
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
                , newQuestionPopupViewModel
                , this::createNewQuestion);
        newQuestionPopup.show();
    }

    public void editQuestion(View view) {
        ProgressPopup progressDialog = new ProgressPopup("Cargando respuesta...", this.getContext());
        progressDialog.show();
        Question question = answerQuestionPopupViewModel.asQuestion();
        question.item_id = overviewViewModel.item.getId();
        question.responder = Session.getInstance().getSessionToken();
        App.appServer.put("/question/" + question.id
                , question
                , Session.class
                , Headers.Authorization(Session.getInstance()))
                .onDone((s, ex) -> progressDialog.dismiss())
                .run(s -> loadQuestions()
                        , ex -> Toast.makeText(this.getContext()
                                , "Error creando la respuesta. Reintente en unos minutos"
                                , Toast.LENGTH_LONG).show());
    }

    private void createNewQuestion(View view) {
        ProgressPopup progressDialog = new ProgressPopup("Cargando pregunta...", this.getContext());
        progressDialog.show();
        Question question = newQuestionPopupViewModel.asQuestion();
        question.item_id = overviewViewModel.item.getId();
        question.questioner = Session.getInstance().getSessionToken();
        App.appServer.post("/question/"
                , question
                , Session.class, new Headers().authorization(Session.getInstance().getSessionToken()))
                .onDone((s, ex) -> progressDialog.dismiss())
                .run(s -> loadQuestions()
                        , ex -> Toast.makeText(this.getContext()
                                , "Error creando la pregunta. Reintente en unos minutos"
                                , Toast.LENGTH_LONG).show());
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mSlider.stopAutoCycle();
        super.onStop();
    }

}