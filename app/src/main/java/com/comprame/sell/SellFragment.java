package com.comprame.sell;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.comprame.App;
import com.comprame.R;
import com.comprame.databinding.SellFragmentBinding;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.search.SearchFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.comprame.Config.CLOUDINARY_CLOUD_NAME;
import static com.comprame.Config.CLOUDINARY_FIXED_IMAGE_NAME;
import static com.comprame.Config.CLOUDINARY_FIXED_IMAGE_PATH;
import static com.comprame.Config.CLOUDINARY_UPLOAD_PRESET;
import static com.comprame.MainActivity.PLACE_PICKER_REQUEST;

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

    private class UploadToCloudinary extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};

            int permsRequestCode = 200;

            requestPermissions(perms, permsRequestCode);
            Map config = new HashMap<String, String>() {{
                put("cloud_name", CLOUDINARY_CLOUD_NAME);
            }};
            Cloudinary cloudinary = new Cloudinary(config);
            try {
                File file = new File((String) objects[0]);
                FileInputStream fileInputStream = new FileInputStream(file);
                Map uploadResult = cloudinary.uploader().unsignedUpload(fileInputStream, CLOUDINARY_UPLOAD_PRESET,
                        new HashMap<String, String>() {{
                            put("public_id", CLOUDINARY_FIXED_IMAGE_NAME);
                        }});

                model.addImageUrl(String.valueOf(uploadResult.get("url")));
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void uploadImage(View view) {
        new UploadToCloudinary().execute(CLOUDINARY_FIXED_IMAGE_PATH);
    }

    public void sell(View view) {
        ProgressPopup progressDialog = new ProgressPopup("Procesando...", this.getContext());
        progressDialog.show();
        App.appServer.post("/item/"
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

    public void openPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            getActivity().startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(getActivity(), data);
                    String placeName = String.format("%s", place.getAddress());
                    model.setLocation(placeName);
            }
        }
    }
}
