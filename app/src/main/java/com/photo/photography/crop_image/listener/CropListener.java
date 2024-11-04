package com.photo.photography.crop_image.listener;

import android.graphics.Bitmap;

public interface CropListener extends Callback {
    void onSuccess(Bitmap cropped);
}
