package com.photo.photography.crop_image.listener;

import android.net.Uri;

public interface SaveListener extends Callback {
    void onSuccess(Uri uri);
}
