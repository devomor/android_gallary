package com.photo.photography.collage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.photo.photography.R;

public class OptionBordersView extends View {

    private final Paint mPaint = new Paint();
    private final RectF mRectF = new RectF();
    private float mBorderSize = 0;
    private int mForegroundColor = 0xA6033E;

    public OptionBordersView(Context context) {
        super(context);
        init();
    }

    public OptionBordersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OptionBordersView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public float getBorderSize() {
        return mBorderSize;
    }

    public void setBorderSize(float borderSize) {
        mBorderSize = borderSize;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() < 10 || getHeight() < 10) {
            return;
        }
        mRectF.set(mBorderSize, mBorderSize, getWidth() - mBorderSize,
                getHeight() - mBorderSize);
        canvas.drawRect(mRectF, mPaint);
    }

    private void init() {
        mForegroundColor = getResources().getColor(
                R.color.border_view_foreground);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mForegroundColor);
    }
}
