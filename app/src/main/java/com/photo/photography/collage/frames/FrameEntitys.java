package com.photo.photography.collage.frames;

import android.graphics.Matrix;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class FrameEntitys implements Parcelable {

    public static final Parcelable.Creator<FrameEntitys> CREATOR = new Parcelable.Creator<FrameEntitys>() {
        @Override
        public FrameEntitys createFromParcel(Parcel in) {
            return new FrameEntitys(in);
        }

        @Override
        public FrameEntitys[] newArray(int size) {
            return new FrameEntitys[size];
        }
    };
    private final Matrix mMatrix = new Matrix();
    private Uri mImage;

    public FrameEntitys() {

    }

    private FrameEntitys(Parcel in) {
        float[] values = new float[9];
        in.readFloatArray(values);
        mMatrix.setValues(values);
        mImage = in.readParcelable(Uri.class.getClassLoader());
    }

    public Uri getImage() {
        return mImage;
    }

    public void setImage(Uri image) {
        mImage = image;
    }

    public Matrix getMatrix() {
        return new Matrix(mMatrix);
    }

    public void setMatrix(Matrix matrix) {
        mMatrix.set(matrix);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        float[] values = new float[9];
        mMatrix.getValues(values);
        dest.writeFloatArray(values);
        dest.writeParcelable(mImage, flags);

    }
}
