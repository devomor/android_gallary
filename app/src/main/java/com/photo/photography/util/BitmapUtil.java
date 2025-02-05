package com.photo.photography.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;

import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;
import java.io.InputStream;


public class BitmapUtil {

    public static Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    public static Bitmap getCroppedBitmap(Bitmap srcBmp) {
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()) {
            dstBmp = Bitmap.createBitmap(srcBmp,
                    srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2, 0,
                    srcBmp.getHeight(), srcBmp.getHeight()
            );
        } else {
            dstBmp = Bitmap.createBitmap(srcBmp, 0,
                    srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                    srcBmp.getWidth(), srcBmp.getWidth()
            );
        }
        return dstBmp;
    }

    public static int getOrientation(Uri uri, Context ctx) {

        try (InputStream in = ctx.getContentResolver().openInputStream(uri)) {
            if (in == null) {
                return 0;
            }
            ExifInterface exif = new ExifInterface(in);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;//ORIENTATION_180;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;//ORIENTATION_90;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;//ORIENTATION_270;
                default:
                    return 0;//ORIENTATION_0;
            }
        } catch (IOException e) {
            return 0;
        }
    }
}
