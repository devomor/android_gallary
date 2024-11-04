package com.photo.photography.models;

import java.util.ArrayList;
import java.util.List;


public class GalleryAlbumModel {
    private final List<String> mImageList = new ArrayList<>();
    private String mAlbumName;
    private long mAlbumId;
    private String mTakenDate;

    public GalleryAlbumModel(long albumId, String albumName) {
        mAlbumId = albumId;
        mAlbumName = albumName;
    }

    public List<String> getImageList() {
        return mImageList;
    }

    public long getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(long albumId) {
        mAlbumId = albumId;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }

    public String getTakenDate() {
        return mTakenDate;
    }

    public void setTakenDate(String takenDate) {
        mTakenDate = takenDate;
    }
}
