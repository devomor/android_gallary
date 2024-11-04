package com.photo.photography.collage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sira on 3/26/2018.
 */
public class DataGalleryAlbum {
    private final List<String> mImageList = new ArrayList<>();
    private String mAlbumName;
    private long mAlbumId;
    private String mTakenDate;

    public DataGalleryAlbum(long albumId, String albumName) {
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
