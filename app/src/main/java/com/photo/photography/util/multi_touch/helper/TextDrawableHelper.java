package com.photo.photography.util.multi_touch.helper;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.photo.photography.MyApp;
import com.photo.photography.util.utils.TextUtil;


public class TextDrawableHelper extends Drawable {
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_TEXT_SIZE = 18;

    private final Paint mPaint;
    private String mText;
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;
    private String mTypefacePath;
    private int mTextColor = DEFAULT_COLOR;
    private float mTextSize = 18;
    private Typeface mTypeface = Typeface.DEFAULT;
    private String[] mSentences;

    public TextDrawableHelper(float textSize, @NonNull String text) {
        mText = text;
        mSentences = mText.split("\n");
        mTextColor = DEFAULT_COLOR;
        mTextSize = textSize;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mTextColor);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setTextSize(mTextSize);
        mPaint.setTypeface(mTypeface);
        calculateIntrinsicSize();
    }

    private void calculateIntrinsicSize() {
        int[] size = TextUtil.findParagraphSize(mPaint, mText, mSentences);
        mIntrinsicWidth = size[0];
        mIntrinsicHeight = size[1];
    }

    public float findSuitableTextSize(float width, float height, boolean smaller) {
        final float delta = 1.0f;
        float maxTextSize = mTextSize;
        float minTextSize = mTextSize;
        if (smaller) {
            minTextSize = 12;
        } else {
            maxTextSize = 3 * mTextSize;
        }

        float textSize = minTextSize;
        while (textSize <= maxTextSize) {
            mPaint.setTextSize(textSize);
            int[] size = TextUtil.findParagraphSize(mPaint, mText, mSentences);
            if (size[0] > width || size[1] > height) {
                textSize = textSize - delta;
                break;
            } else {
                textSize += delta;
            }
        }
        //Restore old text size
        mPaint.setTextSize(mTextSize);
        return textSize;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float size) {
        mTextSize = size;
        mPaint.setTextSize(mTextSize);
        calculateIntrinsicSize();
        invalidateSelf();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mPaint.setColor(mTextColor);
        invalidateSelf();
    }

    public String getTypefacePath() {
        return mTypefacePath;
    }

    public void setTypefacePath(String typefacePath) {
        mTypefacePath = typefacePath;
        mTypeface = TextUtil.loadTypeface(MyApp.getInstance(), typefacePath);
        mPaint.setTypeface(mTypeface);
        calculateIntrinsicSize();
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        TextUtil.drawParagraph(canvas, mPaint, bounds.centerX(), bounds.centerY(), mText, mSentences);
    }

    public String getText() {
        return mText;
    }

    public void setText(@NonNull String text) {
        mText = text;
        mSentences = mText.split("\n");
        calculateIntrinsicSize();
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        mPaint.setColorFilter(filter);
    }
}
