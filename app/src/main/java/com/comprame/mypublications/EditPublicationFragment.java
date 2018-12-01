package com.comprame.mypublications;

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
import com.comprame.databinding.EditPublicationFragmentBinding;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.Bindings;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.search.SearchFragment;
import com.comprame.sell.CategoryPopup;
import com.comprame.sell.CategoryPopupViewModel;
import com.comprame.sell.Geolocation;
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


public class EditPublicationFragment extends Fragment {

    private EditPublicationViewModel model;
    private static final int FILE_PATH_REQUEST_CODE = 0;
    private ProgressPopup progressPopupCloudinary;
    private EditPublicationFragmentBinding editPublicationFragmentBinding;
    private CategoryPopupViewModel categoryPopupViewModel;

    private LinearLayout imagesGrid;

    private Publication publication;

    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        editPublicationFragmentBinding = EditPublicationFragmentBinding.inflate(inflater, container, false);
        editPublicationFragmentBinding.setFragment(this);
        model = ViewModelProviders.of(this).get(EditPublicationViewModel.class);
        editPublicationFragmentBinding.setData(model);

        model.setPublication(publication);

        categoryPopupViewModel = ViewModelProviders.of(this)
                .get(CategoryPopupViewModel.class);

        imagesGrid = editPublicationFragmentBinding.getRoot().findViewById(R.id.publication_item_images_grid);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                progressPopupCloudinary.dismiss();
                String imageUrl = (String) message.obj;

                ImageView imageView = new ImageView(editPublicationFragmentBinding.getRoot().getContext());
                Bindings.loadImage(imageView, imageUrl);
                imagesGrid.addView(imageView);
            }
        };

        bindPublication();

        return editPublicationFragmentBinding.getRoot();
    }

    public void bindPublication() {
        addAddress(publication.getGeolocation().getAddress());
        for (String category: publication.getCategories()) {
            View categoryItem = getLayoutInflater().inflate(R.layout.category_item, null);
            TextView categoryName = categoryItem.findViewById(R.id.category_name);
            categoryName.setText(category);
            editPublicationFragmentBinding.categoryList.addView(categoryItem);
        }
        for (String imageUrl: publication.getImageUrls()) {
            ImageView imageView = new ImageView(editPublicationFragmentBinding.getRoot().getContext());
            Bindings.loadImage(imageView, imageUrl);
            imagesGrid.addView(imageView);
        }
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
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

    public void edit(View view) {
        ProgressPopup progressDialog = new ProgressPopup("Procesando...", this.getContext());
        progressDialog.show();
        Publication publication = model.asPublication();
        App.appServer.put("/item/" + publication.getId()
                , publication
                , Publication.class, new Headers().authorization(Session.getInstance().getSessionToken()))
                .onDone((s, ex) -> progressDialog.dismiss())
                .run(s -> search()
                        , this::showToastError);
    }

    private void showToastError(Exception e) {
        Toast.makeText(this.getContext()
                , "Error actualizando la publicación. Por favor reintente en unos minutos"
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

    public void openCategoryPopup(View view) {
        CategoryPopup categoryPopup = new CategoryPopup(this
                , categoryPopupViewModel
                , this::addCategory);
        categoryPopup.show();
    }

    private void addCategory(View view) {
        model.addCategory(categoryPopupViewModel.getCategory());
        GridLayout categoryList = editPublicationFragmentBinding.getRoot().findViewById(R.id.category_list);
        View categoryItem = getLayoutInflater().inflate(R.layout.category_item, null);
        TextView categoryName = categoryItem.findViewById(R.id.category_name);
        categoryName.setText(categoryPopupViewModel.getCategory());
        categoryList.addView(categoryItem);
    }

    private void addAddress(String placeName) {
        LinearLayout linearLayout = editPublicationFragmentBinding.getRoot().findViewById(R.id.publication_list_geolocation);
        linearLayout.removeAllViews();
        TextView textView = new TextView(editPublicationFragmentBinding.getRoot().getContext());
        textView.setText(placeName);
        linearLayout.addView(textView);
    }
}
