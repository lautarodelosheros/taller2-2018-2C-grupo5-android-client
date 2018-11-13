package com.comprame.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.IntRange;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class QRCodeHelper {
    private static QRCodeHelper qrCodeHelper = null;
    private ErrorCorrectionLevel mErrorCorrectionLevel;
    private int mMargin;
    private String mContent;
    private int mWidth, mHeight;

    private QRCodeHelper(Context context) {
        mHeight = (int) (context.getResources().getDisplayMetrics().heightPixels / 2.4);
        mWidth = (int) (context.getResources().getDisplayMetrics().widthPixels / 1.3);
    }

    public static QRCodeHelper newInstance(Context context) {
        if (qrCodeHelper == null) {
            qrCodeHelper = new QRCodeHelper(context);
        }
        return qrCodeHelper;
    }

    public Bitmap getQRCode() {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(mContent, BarcodeFormat.QR_CODE, mWidth, mHeight);
            Bitmap bmp = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
            for (int x = 0; x < mWidth; ++x){
                for (int y = 0; y < mHeight; ++y){
                    bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public QRCodeHelper setErrorCorrectionLevel(ErrorCorrectionLevel level) {
        mErrorCorrectionLevel = level;
        return this;
    }

    public QRCodeHelper setContent(String content) {
        mContent = content;
        return this;
    }

    public QRCodeHelper setWidthAndHeight(@IntRange(from = 1) int width, @IntRange(from = 1) int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    public QRCodeHelper setMargin(@IntRange(from = 0) int margin) {
        mMargin = margin;
        return this;
    }

}