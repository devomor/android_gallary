package com.photo.photography.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Field names are used in Gson. So Don't rename if file 'package.json' doesn't change field names.
 *
 * @author vanhu_000
 */
public class LanguageModel implements Parcelable {
    public static final Parcelable.Creator<LanguageModel> CREATOR
            = new Parcelable.Creator<LanguageModel>() {
        public LanguageModel createFromParcel(Parcel in) {
            return new LanguageModel(in);
        }

        public LanguageModel[] newArray(int size) {
            return new LanguageModel[size];
        }
    };
    private String mName;
    private String mValue;

    public LanguageModel() {

    }

    private LanguageModel(Parcel in) {
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
