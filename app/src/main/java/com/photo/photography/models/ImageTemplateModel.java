package com.photo.photography.models;

import android.os.Parcel;
import android.os.Parcelable;


public class ImageTemplateModel extends ItemInfoModel {
    public static final Parcelable.Creator<ImageTemplateModel> CREATOR
            = new Parcelable.Creator<ImageTemplateModel>() {
        public ImageTemplateModel createFromParcel(Parcel in) {
            return new ImageTemplateModel(in);
        }

        public ImageTemplateModel[] newArray(int size) {
            return new ImageTemplateModel[size];
        }
    };
    private LanguageModel[] mNames;
    private long mPackageId;
    private String mPreview;
    private String mTemplate;
    private String mChild;

    public ImageTemplateModel() {

    }

    protected ImageTemplateModel(Parcel in) {
        super(in);
        int len = in.readInt();
        if (len > 0) {
            mNames = new LanguageModel[len];
            in.readTypedArray(mNames, LanguageModel.CREATOR);
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

    public LanguageModel[] getLanguages() {
        return mNames;
    }

    public void setLanguages(LanguageModel[] names) {
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
