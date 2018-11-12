package com.comprame.sell;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.comprame.App;
import com.comprame.R;
import com.comprame.databinding.SellFragmentBinding;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.Bindings;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.search.SearchFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.comprame.Config.CLOUDINARY_CLOUD_NAME;
import static com.comprame.Config.CLOUDINARY_UPLOAD_PRESET;
import static com.comprame.MainActivity.PLACE_PICKER_REQUEST;


public class SellFragment extends Fragment {

    private SellViewModel model;
    private static final int FILE_PATH_REQUEST_CODE = 0;
    private ProgressPopup progressPopupCloudinary;
    private SellFragmentBinding sellFragmentBinding;

    private GridLayout imagesGrid;

    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        sellFragmentBinding = SellFragmentBinding.inflate(inflater, container, false);
        sellFragmentBinding.setFragment(this);
        model = ViewModelProviders.of(this).get(SellViewModel.class);
        sellFragmentBinding.setData(model);

        imagesGrid = sellFragmentBinding.getRoot().findViewById(R.id.sell_item_images_grid);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                progressPopupCloudinary.dismiss();
                String imageUrl = (String) message.obj;

                ImageView imageView = new ImageView(sellFragmentBinding.getRoot().getContext());
                Bindings.loadImage(imageView, imageUrl);
                imagesGrid.addView(imageView);
            }
        };

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
                InputStream fileInputStream = Objects.requireNonNull(getContext()).getContentResolver().
                        openInputStream((Uri) objects[0]);
                Map uploadResult = cloudinary.uploader().unsignedUpload(fileInputStream,
                        CLOUDINARY_UPLOAD_PRESET, null);

                model.addImageUrl(String.valueOf(uploadResult.get("url")));

                Message message = mHandler.obtainMessage(0, String.valueOf(uploadResult.get("url")));
                message.sendToTarget();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void uploadImage(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        startActivityForResult(chooseFile, FILE_PATH_REQUEST_CODE);
    }

    public void sell(View view) {
        ProgressPopup progressDialog = new ProgressPopup("Procesando...", this.getContext());
        progressDialog.show();
        App.appServer.post("/item/"
                , model.asSellItem()
                , SellItem.class, new Headers().authorization(Session.getInstance().getSessionToken()))
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
        Objects.requireNonNull(getActivity())
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SearchFragment())
                .addToBackStack(null)
                .commit();
    }

    public void openPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            getActivity().startActivityForResult(builder.build(Objects.requireNonNull(getActivity())), PLACE_PICKER_REQUEST);
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
                    Place place = PlacePicker.getPlace(Objects.requireNonNull(getActivity()), data);
                    LatLng latLng = place.getLatLng();
                    String placeName = String.format("%s", place.getAddress());
                    model.setGeolocation(new Geolocation(latLng.latitude, latLng.longitude, placeName));
                    addAddress(placeName);
                    break;
                case FILE_PATH_REQUEST_CODE:
                    Uri uri = data.getData();
                    progressPopupCloudinary = new ProgressPopup("Cargando imágenes...", getContext());
                    progressPopupCloudinary.show();
                    new UploadToCloudinary().execute(uri);
                    break;
            }
        }
    }

    private void addAddress(String placeName) {
        LinearLayout linearLayout = sellFragmentBinding.getRoot().findViewById(R.id.list_geolocation_sell);
        linearLayout.removeAllViews();
        TextView textView = new TextView(sellFragmentBinding.getRoot().getContext());
        textView.setText(placeName);
        linearLayout.addView(textView);
    }
}
