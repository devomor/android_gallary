package com.photo.photography.models;

/**
 * Field names are used in Gson. So Don't rename if file 'package.json' doesn't change field names.
 *
 * @author vanhu_000
 */
public class ShadeInfo extends ItemInfoModel {
    private LanguageModel[] mNames;
    private String mImage;
    private String mType;
    private long mPackageId;

    public String getForeground() {
        return mImage;
    }

    public void setForeground(String foreground) {
        mImage = foreground;
    }

    public String getShadeType() {
        return mType;
    }

    public void setShadeType(String type) {
        mType = type;
    }

    public long getPackageId() {
        return mPackageId;
    }

    public void setPackageId(long packageId) {
        mPackageId = packageId;
    }

    public LanguageModel[] getLanguages() {
        return mNames;
    }
}
