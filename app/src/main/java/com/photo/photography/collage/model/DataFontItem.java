package com.photo.photography.collage.model;

/**
 * Created by Sira on 2/29/2018.
 */
public class DataFontItem {
    private final String mFontName;
    private final String mFontPath;

    public DataFontItem(String fontName, String fontPath) {
        mFontName = fontName;
        mFontPath = fontPath;
    }

    public String getFontName() {
        return mFontName;
    }

    public String getFontPath() {
        return mFontPath;
    }
}
