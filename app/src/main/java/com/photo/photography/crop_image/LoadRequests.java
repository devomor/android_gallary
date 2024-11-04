package com.photo.photography.crop_image;

import android.graphics.RectF;
import android.net.Uri;

import com.photo.photography.crop_image.listener.LoadListener;

import io.reactivex.Completable;

public class LoadRequests {

    private final CropImageViews cropImageView;
    private final Uri sourceUri;
    private float initialFrameScale;
    private RectF initialFrameRect;
    private boolean useThumbnail;

    public LoadRequests(CropImageViews cropImageView, Uri sourceUri) {
        this.cropImageView = cropImageView;
        this.sourceUri = sourceUri;
    }

    public LoadRequests initialFrameScale(float initialFrameScale) {
        this.initialFrameScale = initialFrameScale;
        return this;
    }

    public LoadRequests initialFrameRect(RectF initialFrameRect) {
        this.initialFrameRect = initialFrameRect;
        return this;
    }

    public LoadRequests useThumbnail(boolean useThumbnail) {
        this.useThumbnail = useThumbnail;
        return this;
    }

    public void execute(LoadListener callback) {
        if (initialFrameRect == null) {
            cropImageView.setInitialFrameScale(initialFrameScale);
        }
        cropImageView.loadAsync(sourceUri, useThumbnail, initialFrameRect, callback);
    }

    public Completable executeAsCompletable() {
        if (initialFrameRect == null) {
            cropImageView.setInitialFrameScale(initialFrameScale);
        }
        return cropImageView.loadAsCompletable(sourceUri, useThumbnail, initialFrameRect);
    }
}
