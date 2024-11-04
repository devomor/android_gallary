package com.photo.photography.duplicatephotos.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapLoaderUtil {
    public static int getScale(int i, int i2, int i3, int i4) {
        if (i <= i3 && i2 <= i4) {
            return 1;
        }
        if (i < i2) {
            return Math.round(((float) i) / ((float) i3));
        }
        return Math.round(((float) i2) / ((float) i4));
    }

    public static BitmapFactory.Options getOptions(String str, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = getScale(options.outWidth, options.outHeight, i, i2);
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Bitmap loadBitmap(String str, int i, int i2) {
        return BitmapFactory.decodeFile(str, getOptions(str, i, i2));
    }
}
