package com.photo.photography.collage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Field names are used in Gson. So Don't rename if file 'package.json' doesn't change field names.
 *
 * @author vanhu_000
 */
public class DataLanguage implements Parcelable {
    public static final Parcelable.Creator<DataLanguage> CREATOR
            = new Parcelable.Creator<DataLanguage>() {
        public DataLanguage createFromParcel(Parcel in) {
            return new DataLanguage(in);
        }

        public DataLanguage[] newArray(int size) {
            return new DataLanguage[size];
        }
    };
    private String mName;
    private String mValue;

    public DataLanguage() {

    }

    private DataLanguage(Parcel in) {
        mName = in.readString();
        mValue = in.readString();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mValue);
    }
}
