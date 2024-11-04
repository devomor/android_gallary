package com.photo.photography.crop_image;

import android.graphics.Bitmap;
import android.net.Uri;

import com.photo.photography.crop_image.listener.CropListener;

import io.reactivex.Single;

public class CropRequests {

    private final CropImageViews cropImageView;
    private final Uri sourceUri;
    private int outputWidth;
    private int outputHeight;
    private int outputMaxWidth;
    private int outputMaxHeight;

    public CropRequests(CropImageViews cropImageView, Uri sourceUri) {
        this.cropImageView = cropImageView;
        this.sourceUri = sourceUri;
    }

    public CropRequests outputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
        this.outputHeight = 0;
        return this;
    }

    public CropRequests outputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
        this.outputWidth = 0;
        return this;
    }

    public CropRequests outputMaxWidth(int outputMaxWidth) {
        this.outputMaxWidth = outputMaxWidth;
        return this;
    }

    public CropRequests outputMaxHeight(int outputMaxHeight) {
        this.outputMaxHeight = outputMaxHeight;
        return this;
    }

    private void build() {
        if (outputWidth > 0) cropImageView.setOutputWidth(outputWidth);
        if (outputHeight > 0) cropImageView.setOutputHeight(outputHeight);
        cropImageView.setOutputMaxSize(outputMaxWidth, outputMaxHeight);
    }

    public void execute(CropListener cropCallback) {
        build();
        cropImageView.cropAsync(sourceUri, cropCallback);
    }

    public Single<Bitmap> executeAsSingle() {
        build();
        return cropImageView.cropAsSingle(sourceUri);
    }
}
