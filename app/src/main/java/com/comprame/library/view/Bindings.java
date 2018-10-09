package com.comprame.library.view;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.comprame.library.fun.Consumer;
import com.comprame.library.fun.Provider;

import java.util.List;

public class Bindings {


    @BindingAdapter("validator")
    public static void editTextValidation(EditText editText, Function<CharSequence, String> validator) {
        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        editText.setError(validator.apply(editable));
                    }
                }
        );
    }

    @BindingAdapter("validation")
    public static void editTextValidation(EditText editText, Provider<String> validator) {
        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        editText.setError(validator.get());
                    }
                }
        );
    }

    @BindingAdapter({"btn_enable"})
    public static void enableByObservable(Button button
            , LiveData<Boolean> liveData) {
        liveData.observeForever(button::setEnabled);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @BindingAdapter({"read"})
    public static <T> void readWriteText(TextView editText,  MutableLiveData<T> readIn) {
        if (readIn != null)
            readIn.observeForever(data -> editText.setText(data.toString()));
    }

    @BindingAdapter("onItemSelected")
    public static void setItemSelectedListener(Spinner spinner, AdapterView.OnItemSelectedListener itemSelectedListener) {
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

}