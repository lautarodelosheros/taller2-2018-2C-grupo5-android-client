package com.comprame.qrcode;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.R;
import com.comprame.buy.BuyItem;
import com.comprame.library.rest.Headers;
import com.comprame.library.view.ProgressPopup;
import com.comprame.login.Session;
import com.comprame.overview.OverviewFragment;
import com.comprame.overview.OverviewViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BuyWithQRFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private ZXingScannerView qrCodeScanner;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {

        return inflater.inflate(R.layout.qr_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        qrCodeScanner = view.findViewById(R.id.qrCodeScanner);
        setScannerProperties();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Objects.requireNonNull(getContext()).checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity())
                        , new String[] {(Manifest.permission.CAMERA)}
                        , MY_CAMERA_REQUEST_CODE);
                return;
            }
        }
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    private void setScannerProperties() {
        qrCodeScanner.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
    }

    @Override
    public void handleResult(Result result) {
        if (result != null) {
            loadOverviewItem(result.getText());
            (new Handler()).postDelayed(this::restartCamera, 3000);
        }
    }

    private void restartCamera() {
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }

    private void loadOverviewItem(String itemId) {
        ProgressPopup progressPopup = new ProgressPopup("Cargando publicación...", getContext());
        progressPopup.show();
        App.appServer.get("/item/" + itemId,
                BuyItem.class
                , Headers.Authorization(Session.getInstance()))
                .run((item) -> {
                    progressPopup.dismiss();
                    OverviewFragment overviewFragment = new OverviewFragment();
                    OverviewViewModel overviewViewModel =
                            ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                                    .get(OverviewViewModel.class);
                    overviewViewModel.item = item;
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, overviewFragment)
                            .addToBackStack(null)
                            .commit();
                }, (ex) -> {
                    progressPopup.dismiss();
                    Log.d("BuyWithQRListener", "Recuperando Item", ex);
                    Toast.makeText(getActivity()
                            , "El código QR no corresponde a una publicación activa"
                            , Toast.LENGTH_LONG)
                            .show();
                });
    }
}
