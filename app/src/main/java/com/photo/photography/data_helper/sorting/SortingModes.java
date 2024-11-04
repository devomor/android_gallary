package com.photo.photography.data_helper.sorting;

import android.provider.MediaStore;


public enum SortingModes {
    NAME(0, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME),
    DATE_DAY(11, MediaStore.MediaColumns.DATE_MODIFIED, "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")"),
    DATE_MONTH(12, MediaStore.MediaColumns.DATE_MODIFIED, "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")"),
    DATE_YEAR(13, MediaStore.MediaColumns.DATE_MODIFIED, "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")"),
    SIZE(2, MediaStore.MediaColumns.SIZE, "count(*)"),
    TYPE(3, MediaStore.MediaColumns.MIME_TYPE),
    NUMERIC(4, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);

    int value;
    String mediaColumn;
    String albumsColumn;

    SortingModes(int value, String mediaColumn) {
        this.value = value;
        this.mediaColumn = mediaColumn;
        this.albumsColumn = mediaColumn;
    }

    SortingModes(int value, String mediaColumn, String albumsColumn) {
        this.value = value;
        this.mediaColumn = mediaColumn;
        this.albumsColumn = albumsColumn;
    }

    public static SortingModes fromValue(int value) {
        switch (value) {
            case 0:
                return NAME;
            case 11:
            default:
                return DATE_DAY;
            case 12:
                return DATE_MONTH;
            case 13:
                return DATE_YEAR;
            case 2:
                return SIZE;
            case 3:
                return TYPE;
            case 4:
                return NUMERIC;
        }
    }

    public String getMediaColumn() {
        return mediaColumn;
    }

    public String getAlbumsColumn() {
        return albumsColumn;
    }

    public int getValue() {
        return value;
    }
}
