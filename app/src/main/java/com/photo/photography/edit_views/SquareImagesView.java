package com.photo.photography.edit_views;

/**
 * Created on 30-03-2018, 11:04:34 AM.
 */

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class SquareImagesView extends AppCompatImageView {

    public SquareImagesView(Context context) {
        super(context);
    }

    public SquareImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImagesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}