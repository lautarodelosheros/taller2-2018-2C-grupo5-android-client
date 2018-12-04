package com.comprame.overview;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.comprame.qrcode.QRCodeHelper;
import com.comprame.sell.Geolocation;
import com.daimajia.slider.library.SliderLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.comprame.MainActivity.PLACE_PICKER_REQUEST;

public class OverviewFragment extends Fragment {

    private OverviewViewModel overviewViewModel;
    private QuestionsList questionsList;
    private NewQuestionPopupViewModel newQuestionPopupViewModel;
    public AnswerQuestionPopupViewModel answerQuestionPopupViewModel;
    private OverviewFragmentBinding binding;
    private AsyncTask qrTask;

    private SliderLayout mSlider;

    private User user;
    private User seller;

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

        binding = OverviewFragmentBinding.inflate(inflater, container, false);
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
        qrTask = new QRLoader().execute(this.overviewViewModel.item.getId());
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
                                        , user.getName().equals(seller.getName()))
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

        App.appServer.get("/user/", User.class
                , Headers.Authorization(Session.getInstance()))
                .run(
                        (User user) -> {
                            if (user.getName().isEmpty()) {
                                progressPopup.dismiss();
                                Toast.makeText(getContext()
                                        , "No se encuentra el usuario"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                this.user = user;
                                loadSeller();
                            }
                        }
                        , (Exception ex) -> {
                            Log.d("ProfileListener", "Error al recuperar el perfil", ex);
                            progressPopup.dismiss();
                            Toast.makeText(getContext()
                                    , "Error al cargar el perfil"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    private void loadSeller() {
        String url = "/user/" + overviewViewModel.item.getSellerId();
        App.appServer.get(url, User.class
                , Headers.Authorization(Session.getInstance()))
                .run(
                        (User user) -> {
                            if (user.getName().isEmpty()) {
                                progressPopup.dismiss();
                                Toast.makeText(getContext()
                                        , "No se encuentra el vendedor"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                this.seller = user;
                                overviewViewModel.setSeller(user.getName());
                                addSeller(user.getName());
                                loadQuestions();
                            }
                        }
                        , (Exception ex) -> {
                            Log.d("ProfileListener", "Error al recuperar el perfil del vendedor", ex);
                            progressPopup.dismiss();
                            Toast.makeText(getContext()
                                    , "Error al cargar el perfil del vendedor"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    private void addSeller(String name) {
        ((TextView) binding.getRoot()
                .findViewById(R.id.overview_item_seller)).setText("Vendedor: " + name);
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
                .addToBackStack(null)
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
        App.appServer.put("/question/" + question.id
                , question
                , Question.class
                , new Headers().authorization(Session.getInstance().getSessionToken()))
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
        App.appServer.post("/question/"
                , question
                , Question.class
                , new Headers().authorization(Session.getInstance().getSessionToken()))
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
        if (qrTask != null && qrTask.getStatus() == AsyncTask.Status.RUNNING)
            qrTask.cancel(true);
        super.onStop();
    }

    public void printQRCode(View view) {
        ImageView imageView = view.getRootView().findViewById(R.id.qrCodeImageView);
        PrintHelper printHelper = new PrintHelper(Objects.requireNonNull(getContext()));
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        printHelper.printBitmap("print", ((BitmapDrawable) imageView.getDrawable()).getBitmap());
    }

    private class QRLoader extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return QRCodeHelper
                    .newInstance(getContext())
                    .setContent(params[0])
                    .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                    .setMargin(2)
                    .getQRCode();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            TextView textView = Objects.requireNonNull(getView()).findViewById(R.id.qrTextExplanation);
            textView.setVisibility(View.VISIBLE);
            ProgressBar progressBar = getView().findViewById(R.id.qrProgressBar);
            progressBar.setVisibility(View.GONE);
            ImageView qrCodeImageView = Objects.requireNonNull(getView()).findViewById(R.id.qrCodeImageView);
            qrCodeImageView.setImageBitmap(bitmap);
        }
    }

    public void estimate(View item) {
        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(getContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    try {
                        overviewViewModel.deliveryDate.setValue(new Date(year, month, dayOfMonth));
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        getActivity()
                                .startActivityForResult(builder.build(OverviewFragment.this.getActivity()), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        Log.e("Estimate Delivery", "Open location", e);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(Objects.requireNonNull(getActivity()), data);
                    overviewViewModel.geolocation = new Geolocation(
                            place.getLatLng().latitude,
                            place.getLatLng().longitude,
                            String.format("%s", place.getAddress()));
                    ProgressPopup progressDialog = new ProgressPopup("Cargando pregunta...", this.getContext());
                    progressDialog.show();
                    App.appServer
                            .post("/delivery-estimate/",
                                    new Estimate(overviewViewModel.geolocation,
                                            overviewViewModel.item.getId(),
                                            1,
                                            new SimpleDateFormat()
                                                    .format(overviewViewModel.deliveryDate.getValue())),
                                    DeliveryEstimate.class,
                                    Headers.Authorization(Session.getInstance()))
                            .onDone((ok, error) -> progressDialog.dismiss())
                            .run(
                                    (ok) ->
                                            overviewViewModel.setDelivery(ok),
                                    (error) ->
                                            Toast.makeText(this.getContext()
                                                    , "Error estimando el envio. Reintente en unos minutos"
                                                    , Toast.LENGTH_LONG).show()
                            );
                    break;
            }
        }
    }

}