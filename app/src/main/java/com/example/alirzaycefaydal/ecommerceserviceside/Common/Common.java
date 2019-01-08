package com.example.alirzaycefaydal.ecommerceserviceside.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.alirzaycefaydal.ecommerceserviceside.Models.Request;
import com.example.alirzaycefaydal.ecommerceserviceside.Remote.IGeoCoordinates;
import com.example.alirzaycefaydal.ecommerceserviceside.Remote.RetrofitClient;

public class Common {

    public static Request currentRequest;


    public static final String baseUrl = "https://maps.googleapis.com";

    public static IGeoCoordinates getGeoCodeService() {
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotx = 0, pivotY = 0;

        Matrix scaleMatrix=new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotx,pivotY);

        Canvas canvas=new Canvas(bitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }
}
