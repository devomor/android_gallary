package com.photo.photography.collage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Field names are used in Gson. So Don't rename if file 'package.json' doesn't
 * change field names.
 *
 * @author vanhu_000
 */
public class DataItemPackageInfo extends DataItemInfo {
    public static final Parcelable.Creator<DataItemPackageInfo> CREATOR
            = new Parcelable.Creator<DataItemPackageInfo>() {
        public DataItemPackageInfo createFromParcel(Parcel in) {
            return new DataItemPackageInfo(in);
        }

        public DataItemPackageInfo[] newArray(int size) {
            return new DataItemPackageInfo[size];
        }
    };
    private String m_id;
    private String mType;
    private String mFolder;

    public DataItemPackageInfo() {

    }

    protected DataItemPackageInfo(Parcel in) {
        super(in);
        m_id = in.readString();
        mType = in.readString();
        mFolder = in.readString();
    }

    public String getFolder() {
        return mFolder;
    }

    public void setFolder(String folder) {
        mFolder = folder;
    }

    public String getIdString() {
        return m_id;
    }

    public void setIdString(String idStr) {
        m_id = idStr;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(m_id);
        dest.writeString(mType);
        dest.writeString(mFolder);
    }
}
