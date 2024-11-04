package com.photo.photography.collage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sira on 3/17/2018.
 */
public class DataImageTemplate extends DataItemInfo {
    public static final Parcelable.Creator<DataImageTemplate> CREATOR
            = new Parcelable.Creator<DataImageTemplate>() {
        public DataImageTemplate createFromParcel(Parcel in) {
            return new DataImageTemplate(in);
        }

        public DataImageTemplate[] newArray(int size) {
            return new DataImageTemplate[size];
        }
    };
    private DataLanguage[] mNames;
    private long mPackageId;
    private String mPreview;
    private String mTemplate;
    private String mChild;

    public DataImageTemplate() {

    }

    protected DataImageTemplate(Parcel in) {
        super(in);
        int len = in.readInt();
        if (len > 0) {
            mNames = new DataLanguage[len];
            in.readTypedArray(mNames, DataLanguage.CREATOR);
        }
        mPackageId = in.readLong();
        mPreview = in.readString();
        mTemplate = in.readString();
        mChild = in.readString();
    }

    public long getPackageId() {
        return mPackageId;
    }

    public void setPackageId(long packageId) {
        mPackageId = packageId;
    }

    public String getPreview() {
        return mPreview;
    }

    public void setPreview(String preview) {
        mPreview = preview;
    }

    public String getTemplate() {
        return mTemplate;
    }

    public void setTemplate(String template) {
        mTemplate = template;
    }

    public String getChild() {
        return mChild;
    }

    public void setChild(String child) {
        mChild = child;
    }

    public DataLanguage[] getLanguages() {
        return mNames;
    }

    public void setLanguages(DataLanguage[] names) {
        mNames = names;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        int len = 0;
        if (mNames != null && mNames.length > 0) {
            len = mNames.length;
        }
        dest.writeInt(len);
        if (mNames != null && len > 0) {
            dest.writeTypedArray(mNames, flags);
        }

        dest.writeLong(mPackageId);
        dest.writeString(mPreview);
        dest.writeString(mTemplate);
        dest.writeString(mChild);
    }
}
