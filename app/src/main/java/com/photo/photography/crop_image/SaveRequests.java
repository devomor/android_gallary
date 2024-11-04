package com.photo.photography.crop_image;

import android.graphics.Bitmap;
import android.net.Uri;

import com.photo.photography.crop_image.listener.SaveListener;

import io.reactivex.Single;

public class SaveRequests {

    private final CropImageViews cropImageView;
    private final Bitmap image;
    private Bitmap.CompressFormat compressFormat;
    private int compressQuality = -1;

    public SaveRequests(CropImageViews cropImageView, Bitmap image) {
        this.cropImageView = cropImageView;
        this.image = image;
    }

    public SaveRequests compressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public SaveRequests compressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
        return this;
    }

    private void build() {
        if (compressFormat != null) {
            cropImageView.setCompressFormat(compressFormat);
        }
        if (compressQuality >= 0) {
            cropImageView.setCompressQuality(compressQuality);
        }
    }

    public void execute(Uri saveUri, SaveListener callback) {
        build();
        cropImageView.saveAsync(saveUri, image, callback);
    }

    public Single<Uri> executeAsSingle(Uri saveUri) {
        build();
        return cropImageView.saveAsSingle(image, saveUri);
    }
}
