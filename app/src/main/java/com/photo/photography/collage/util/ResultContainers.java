package com.photo.photography.collage.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.photo.photography.collage.frames.FrameEntitys;
import com.photo.photography.collage.multipletouch.control.MultiTouchEntitys;

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
    private ArrayList<MultiTouchEntitys> mImages = new ArrayList<MultiTouchEntitys>();
    private Uri mPhotoBackgroundImage = null;
    // frame
    private ArrayList<MultiTouchEntitys> mFrameStickerImages = new ArrayList<MultiTouchEntitys>();
    private Uri mFrameBackgroundImage = null;
    private ArrayList<FrameEntitys> mFrameImages = new ArrayList<FrameEntitys>();

    private ResultContainers() {

    }

    public static ResultContainers getInstance() {
        if (instance == null) {
            instance = new ResultContainers();
        }

        return instance;
    }

    public void removeImageEntity(MultiTouchEntitys entity) {
        mImages.remove(entity);
    }

    public void putImageEntities(ArrayList<MultiTouchEntitys> images) {
        mImages.clear();
        for (MultiTouchEntitys entity : images) {
            mImages.add(entity);
        }
    }

    public void putImage(String key, Bitmap bitmap) {
        mDecodedImageMap.put(key, bitmap);
    }

    public Bitmap getImage(String key) {
        return mDecodedImageMap.get(key);
    }

    public ArrayList<MultiTouchEntitys> copyImageEntities() {
        ArrayList<MultiTouchEntitys> result = new ArrayList<MultiTouchEntitys>();
        for (MultiTouchEntitys entity : mImages) {
            result.add(entity);
        }

        return result;
    }

    public ArrayList<MultiTouchEntitys> getImageEntities() {
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

    public void putFrameImage(FrameEntitys entity) {
        mFrameImages.add(entity);
    }

    public ArrayList<FrameEntitys> copyFrameImages() {
        ArrayList<FrameEntitys> result = new ArrayList<FrameEntitys>();
        for (FrameEntitys uri : mFrameImages) {
            result.add(uri);
        }

        return result;
    }

    public void putFrameSticker(MultiTouchEntitys entity) {
        mFrameStickerImages.add(entity);
    }

    public void putFrameStickerImages(ArrayList<MultiTouchEntitys> images) {
        mFrameStickerImages.clear();
        for (MultiTouchEntitys entity : images) {
            mFrameStickerImages.add(entity);
        }
    }

    public ArrayList<MultiTouchEntitys> copyFrameStickerImages() {
        ArrayList<MultiTouchEntitys> result = new ArrayList<MultiTouchEntitys>();
        for (MultiTouchEntitys entity : mFrameStickerImages) {
            result.add(entity);
        }

        return result;
    }

    public void removeFrameSticker(MultiTouchEntitys entity) {
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
            mImages = new ArrayList<MultiTouchEntitys>();
        }
        mPhotoBackgroundImage = bundle.getParcelable(PHOTO_BACKGROUND_IMAGE_KEY);
        mFrameStickerImages = bundle.getParcelableArrayList(FRAME_STICKER_IMAGES_KEY);
        if (mFrameStickerImages == null) {
            mFrameStickerImages = new ArrayList<MultiTouchEntitys>();
        }
        mFrameBackgroundImage = bundle.getParcelable(FRAME_BACKGROUND_IMAGE_KEY);
        mFrameImages = bundle.getParcelableArrayList(FRAME_IMAGES_KEY);
        if (mFrameImages == null) {
            mFrameImages = new ArrayList<FrameEntitys>();
        }
    }
}
