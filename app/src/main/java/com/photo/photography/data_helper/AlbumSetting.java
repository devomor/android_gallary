package com.photo.photography.data_helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.photo.photography.data_helper.filters_mode.FilterModes;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;

import java.io.Serializable;

public class AlbumSetting implements Serializable, Parcelable {

    /**
     * It is a non-null static field that must be in parcelable.
     */
    public static final Parcelable.Creator<AlbumSetting> CREATOR = new Parcelable.Creator<AlbumSetting>() {

        @Override
        public AlbumSetting createFromParcel(Parcel source) {
            return new AlbumSetting(source);
        }

        @Override
        public AlbumSetting[] newArray(int size) {
            return new AlbumSetting[size];
        }
    };
    String coverPath;
    int sortingMode, sortingOrder;
    boolean pinned;
    FilterModes filterMode = FilterModes.ALL;

    AlbumSetting(String cover, int sortingMode, int sortingOrder, int pinned) {
        this.coverPath = cover;
        this.sortingMode = sortingMode;
        this.sortingOrder = sortingOrder;
        this.pinned = pinned == 1;
    }

    /**
     * This is the constructor used by CREATOR.
     */
    protected AlbumSetting(Parcel in) {
        this.coverPath = in.readString();
        this.sortingMode = in.readInt();
        this.sortingOrder = in.readInt();
        this.pinned = in.readByte() != 0;
        int tmpFilterMode = in.readInt();
        this.filterMode = tmpFilterMode == -1 ? null : FilterModes.values()[tmpFilterMode];
    }

    public static AlbumSetting getDefaults() {
        return new AlbumSetting(null, SortingModes.DATE_DAY.getValue(), 0, 0);
    }

    public SortingModes getSortingMode() {
        return SortingModes.fromValue(sortingMode);
    }

    public SortingOrders getSortingOrder() {
        return SortingOrders.fromValue(sortingOrder);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coverPath);
        dest.writeInt(this.sortingMode);
        dest.writeInt(this.sortingOrder);
        dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
        dest.writeInt(this.filterMode == null ? -1 : this.filterMode.ordinal());
    }
}