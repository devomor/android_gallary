package com.photo.photography.util.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.photo.photography.frames.EntityFrame;
import com.photo.photography.util.multi_touch.helper.MultiTouchEntityHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultContainers {
    public static final String IMAGES_KEY = "imagesKey";
    public static final String PHOTO_BACKGROUND_IMAGE_KEY = "photoBgKey";
    public static final String FRAME_STICKER_IMAGES_KEY = "frameStickerKey";
    public static final String FRAME_BACKGROUND_IMAGE_KEY = "frameBackgroundKey";
    public static final String FRAME_IMAGES_KEY = "frameImageKey";

    private static ResultContainers instance = null;
    private final HashMap<String, Bitmap> mDecodedImageMap = new HashMap<>();
    private ArrayList<MultiTouchEntityHelper> mImages = new ArrayList<MultiTouchEntityHelper>();
    private Uri mPhotoBackgroundImage = null;
    // frame
    private ArrayList<MultiTouchEntityHelper> mFrameStickerImages = new ArrayList<MultiTouchEntityHelper>();
    private Uri mFrameBackgroundImage = null;
    private ArrayList<EntityFrame> mFrameImages = new ArrayList<EntityFrame>();

    private ResultContainers() {

    }

    public static ResultContainers getInstance() {
        if (instance == null) {
            instance = new ResultContainers();
        }

        return instance;
    }

    public void removeImageEntity(MultiTouchEntityHelper entity) {
        mImages.remove(entity);
    }

    public void putImageEntities(ArrayList<MultiTouchEntityHelper> images) {
        mImages.clear();
        for (MultiTouchEntityHelper entity : images) {
            mImages.add(entity);
        }
    }

    public void putImage(String key, Bitmap bitmap) {
        mDecodedImageMap.put(key, bitmap);
    }

    public Bitmap getImage(String key) {
        return mDecodedImageMap.get(key);
    }

    public ArrayList<MultiTouchEntityHelper> copyImageEntities() {
        ArrayList<MultiTouchEntityHelper> result = new ArrayList<MultiTouchEntityHelper>();
        for (MultiTouchEntityHelper entity : mImages) {
            result.add(entity);
        }

        return result;
    }

    public ArrayList<MultiTouchEntityHelper> getImageEntities() {
        return mImages;
    }

    public Uri getPhotoBackgroundImage() {
        return mPhotoBackgroundImage;
    }

    public void setPhotoBackgroundImage(Uri photoBackgroundImage) {
        mPhotoBackgroundImage = photoBackgroundImage;
    }

    public Uri getFrameBackgroundImage() {
        return mFrameBackgroundImage;
    }

    public void setFrameBackgroundImage(Uri frameBackgroundImage) {
        mFrameBackgroundImage = frameBackgroundImage;
    }

    public void putFrameImage(EntityFrame entity) {
        mFrameImages.add(entity);
    }

    public ArrayList<EntityFrame> copyFrameImages() {
        ArrayList<EntityFrame> result = new ArrayList<EntityFrame>();
        for (EntityFrame uri : mFrameImages) {
            result.add(uri);
        }

        return result;
    }

    public void putFrameSticker(MultiTouchEntityHelper entity) {
        mFrameStickerImages.add(entity);
    }

    public void putFrameStickerImages(ArrayList<MultiTouchEntityHelper> images) {
        mFrameStickerImages.clear();
        for (MultiTouchEntityHelper entity : images) {
            mFrameStickerImages.add(entity);
        }
    }

    public ArrayList<MultiTouchEntityHelper> copyFrameStickerImages() {
        ArrayList<MultiTouchEntityHelper> result = new ArrayList<MultiTouchEntityHelper>();
        for (MultiTouchEntityHelper entity : mFrameStickerImages) {
            result.add(entity);
        }

        return result;
    }

    public void removeFrameSticker(MultiTouchEntityHelper entity) {
        mFrameStickerImages.remove(entity);
    }

    /**
     * Clear all frame image uri
     */
    public void clearFrameImages() {
        mFrameImages.clear();
    }

    public void clearAll() {
        mImages.clear();
        mPhotoBackgroundImage = null;
        mFrameStickerImages.clear();
        mFrameImages.clear();
        mFrameBackgroundImage = null;
        mDecodedImageMap.clear();
    }

    public void clearAllImageInFrameCreator() {
        mFrameStickerImages.clear();
        mFrameImages.clear();
        mFrameBackgroundImage = null;
    }

    public void saveToBundle(Bundle bundle) {
        bundle.putParcelableArrayList(IMAGES_KEY, mImages);
        bundle.putParcelable(PHOTO_BACKGROUND_IMAGE_KEY, mPhotoBackgroundImage);
        bundle.putParcelableArrayList(FRAME_STICKER_IMAGES_KEY, mFrameStickerImages);
        bundle.putParcelable(FRAME_BACKGROUND_IMAGE_KEY, mFrameBackgroundImage);
        bundle.putParcelableArrayList(FRAME_IMAGES_KEY, mFrameImages);
    }

    public void restoreFromBundle(Bundle bundle) {
        mImages = bundle.getParcelableArrayList(IMAGES_KEY);
        if (mImages == null) {
            mImages = new ArrayList<MultiTouchEntityHelper>();
        }
        mPhotoBackgroundImage = bundle.getParcelable(PHOTO_BACKGROUND_IMAGE_KEY);
        mFrameStickerImages = bundle.getParcelableArrayList(FRAME_STICKER_IMAGES_KEY);
        if (mFrameStickerImages == null) {
            mFrameStickerImages = new ArrayList<MultiTouchEntityHelper>();
        }
        mFrameBackgroundImage = bundle.getParcelable(FRAME_BACKGROUND_IMAGE_KEY);
        mFrameImages = bundle.getParcelableArrayList(FRAME_IMAGES_KEY);
        if (mFrameImages == null) {
            mFrameImages = new ArrayList<EntityFrame>();
        }
    }
}
