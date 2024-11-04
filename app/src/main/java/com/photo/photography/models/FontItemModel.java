package com.photo.photography.models;


public class FontItemModel {
    private final String mFontName;
    private final String mFontPath;

    public FontItemModel(String fontName, String fontPath) {
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
