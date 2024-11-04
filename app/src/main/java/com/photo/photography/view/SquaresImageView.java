package com.photo.photography.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class SquaresImageView extends ImageView {

    public SquaresImageView(Context context) {
        super(context);
    }

    public SquaresImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaresImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}