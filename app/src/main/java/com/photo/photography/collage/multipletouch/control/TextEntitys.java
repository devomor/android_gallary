package com.photo.photography.collage.multipletouch.control;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;

/**
 * Created by Sira on 2/26/2018.
 */
public class TextEntitys extends ImageEntitys {
    public static final Parcelable.Creator<TextEntitys> CREATOR = new Parcelable.Creator<TextEntitys>() {
        @Override
        public TextEntitys createFromParcel(Parcel in) {
            return new TextEntitys(in);
        }

        @Override
        public TextEntitys[] newArray(int size) {
            return new TextEntitys[size];
        }
    };
    private String mText;
    private int mTextColor = Color.BLACK;
    private float mTextSize = 18;
    private String mTypefacePath = "";
    private float mCurrentScale = 0;

    public TextEntitys(String text, Resources res) {
        super(-1, res);
        mText = text;
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                TextDrawables.DEFAULT_TEXT_SIZE, res.getDisplayMetrics());
    }

    private TextEntitys(Parcel in) {
        super(in);
        mText = in.readString();
        mTextColor = in.readInt();
        mTextSize = in.readFloat();
        mTypefacePath = in.readString();
    }

    @Override
    protected Drawable createDrawableFromPrimaryInfo(Context context) {
        final TextDrawables textDrawable = new TextDrawables(mTextSize, mText);
        textDrawable.setTextSize(mTextSize);
        textDrawable.setTextColor(mTextColor);
        textDrawable.setTypefacePath(mTypefacePath);
        return textDrawable;
    }

    @Override
    public boolean isNull() {
        return mText == null || mText.trim().length() == 0;
    }

    public void setText(String text) {
        mText = text;
        if (getDrawable() != null)
            ((TextDrawables) getDrawable()).setText(mText);
    }

    public void setTextColor(int color) {
        mTextColor = color;
        if (getDrawable() != null)
            ((TextDrawables) getDrawable()).setTextColor(mTextColor);
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        if (getDrawable() != null)
            ((TextDrawables) getDrawable()).setTextSize(mTextSize);
    }

    public void setTypefacePath(String typefacePath) {
        mTypefacePath = typefacePath;
        if (getDrawable() != null)
            ((TextDrawables) getDrawable()).setTypefacePath(mTypefacePath);
    }

    @Override
    public void draw(Canvas canvas, float scale) {
        if (scale == 1) {
            super.draw(canvas, scale);
        } else {
            canvas.save();
            final TextDrawables textDrawable = (TextDrawables) getDrawable();
            float dx = scale * (mMaxX + mMinX) / 2;
            float dy = scale * (mMaxY + mMinY) / 2;
            float textSize = textDrawable.findSuitableTextSize(scale * mMaxX - scale * mMinX, scale * mMaxY - scale * mMinY, scale < 1);
            float oldTextSize = textDrawable.getTextSize();
            textDrawable.setTextSize(textSize);
            textDrawable.setBounds((int) (scale * mMinX), (int) (scale * mMinY), (int) (scale * mMaxX), (int) (scale * mMaxY));
            canvas.translate(dx, dy);
            canvas.rotate(mAngle * 180.0f / (float) Math.PI);
            canvas.translate(-dx, -dy);
            textDrawable.draw(canvas);
            canvas.restore();
            textDrawable.setTextSize(oldTextSize);
        }
    }

    @Override
    protected float[] calculateHalfDrawableSize(float scaleX, float scaleY) {
        TextDrawables drawable = (TextDrawables) getDrawable();
        if (mCurrentScale > 0) {
            float ts = 50 * (scaleX / mCurrentScale - 1);
            mTextSize = Math.max(12, mTextSize + ts);
            drawable.setTextSize(mTextSize);
        }
        mCurrentScale = scaleX;
        float[] size = new float[2];
        size[0] = drawable.getIntrinsicWidth() / 2;
        size[1] = drawable.getIntrinsicHeight() / 2;
        return size;
    }

    // parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mText);
        dest.writeInt(mTextColor);
        dest.writeFloat(mTextSize);
        dest.writeString(mTypefacePath);
    }
}
