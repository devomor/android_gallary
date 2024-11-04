package com.photo.photography.duplicatephotos.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class CustomZoomableImageView extends androidx.appcompat.widget.AppCompatImageView {
    static final int CLICK = 3;
    static final int DRAG = 1;
    static final int NONE = 0;
    static final int ZOOM = 2;
    float bmHeight;
    float bmWidth;
    float bottom;
    Context context;
    float height;
    PointF last = new PointF();
    float[] m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix = new Matrix();
    float maxScale = 4.0f;
    float minScale = 1.0f;
    int mode = 0;
    float origHeight;
    float origWidth;
    float redundantXSpace;
    float redundantYSpace;
    float right;
    float saveScale = 1.0f;
    PointF start = new PointF();
    float width;

    public CustomZoomableImageView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        super.setClickable(true);
        this.context = context2;
        this.mScaleDetector = new ScaleGestureDetector(context2, new ScaleListener());
        this.matrix.setTranslate(1.0f, 1.0f);
        this.m = new float[9];
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomZoomableImageView.this.mScaleDetector.onTouchEvent(motionEvent);
                CustomZoomableImageView.this.matrix.getValues(CustomZoomableImageView.this.m);
                float f = CustomZoomableImageView.this.m[2];
                float f2 = CustomZoomableImageView.this.m[5];
                PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
                switch (motionEvent.getAction()) {
                    case 0:
                        CustomZoomableImageView.this.last.set(motionEvent.getX(), motionEvent.getY());
                        CustomZoomableImageView.this.start.set(CustomZoomableImageView.this.last);
                        CustomZoomableImageView.this.mode = 1;
                        break;
                    case 1:
                        CustomZoomableImageView.this.mode = 0;
                        int abs = (int) Math.abs(pointF.x - CustomZoomableImageView.this.start.x);
                        int abs2 = (int) Math.abs(pointF.y - CustomZoomableImageView.this.start.y);
                        if (abs < 3 && abs2 < 3) {
                            CustomZoomableImageView.this.performClick();
                            break;
                        }
                    case 2:
                        if (CustomZoomableImageView.this.mode == 2 || (CustomZoomableImageView.this.mode == 1 && CustomZoomableImageView.this.saveScale > CustomZoomableImageView.this.minScale)) {
                            float f3 = pointF.x - CustomZoomableImageView.this.last.x;
                            float f4 = pointF.y - CustomZoomableImageView.this.last.y;
                            float round = (float) Math.round(CustomZoomableImageView.this.origHeight * CustomZoomableImageView.this.saveScale);
                            if (((float) Math.round(CustomZoomableImageView.this.origWidth * CustomZoomableImageView.this.saveScale)) < CustomZoomableImageView.this.width) {
                                float f5 = f2 + f4;
                                if (f5 > 0.0f) {
                                    f4 = -f2;
                                    f3 = 0.0f;
                                } else if (f5 < (-CustomZoomableImageView.this.bottom)) {
                                    f4 = -(f2 + CustomZoomableImageView.this.bottom);
                                    f3 = 0.0f;
                                } else {
                                    f3 = 0.0f;
                                }
                            } else if (round < CustomZoomableImageView.this.height) {
                                float f6 = f + f3;
                                if (f6 > 0.0f) {
                                    f3 = -f;
                                    f4 = 0.0f;
                                } else if (f6 < (-CustomZoomableImageView.this.right)) {
                                    f3 = -(f + CustomZoomableImageView.this.right);
                                    f4 = 0.0f;
                                } else {
                                    f4 = 0.0f;
                                }
                            } else {
                                float f7 = f + f3;
                                if (f7 > 0.0f) {
                                    f3 = -f;
                                } else if (f7 < (-CustomZoomableImageView.this.right)) {
                                    f3 = -(f + CustomZoomableImageView.this.right);
                                }
                                float f8 = f2 + f4;
                                if (f8 > 0.0f) {
                                    f4 = -f2;
                                } else if (f8 < (-CustomZoomableImageView.this.bottom)) {
                                    f4 = -(f2 + CustomZoomableImageView.this.bottom);
                                }
                            }
                            CustomZoomableImageView.this.matrix.postTranslate(f3, f4);
                            CustomZoomableImageView.this.last.set(pointF.x, pointF.y);
                            break;
                        }
                    case 5:
                        CustomZoomableImageView.this.last.set(motionEvent.getX(), motionEvent.getY());
                        CustomZoomableImageView.this.start.set(CustomZoomableImageView.this.last);
                        CustomZoomableImageView.this.mode = 2;
                        break;
                    case 6:
                        CustomZoomableImageView.this.mode = 0;
                        break;
                }
                CustomZoomableImageView zoomableImageView = CustomZoomableImageView.this;
                zoomableImageView.setImageMatrix(zoomableImageView.matrix);
                CustomZoomableImageView.this.invalidate();
                return true;
            }
        });
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            super.setImageBitmap(bitmap);
            this.bmWidth = (float) bitmap.getWidth();
            this.bmHeight = (float) bitmap.getHeight();
        }
    }

    public void setMaxZoom(float f) {
        this.maxScale = f;
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.width = (float) MeasureSpec.getSize(i);
        this.height = (float) MeasureSpec.getSize(i2);
        float min = Math.min(this.width / this.bmWidth, this.height / this.bmHeight);
        this.matrix.setScale(min, min);
        setImageMatrix(this.matrix);
        this.saveScale = 1.0f;
        this.redundantYSpace = this.height - (this.bmHeight * min);
        this.redundantXSpace = this.width - (min * this.bmWidth);
        this.redundantYSpace /= 2.0f;
        this.redundantXSpace /= 2.0f;
        this.matrix.postTranslate(this.redundantXSpace, this.redundantYSpace);
        float f = this.width;
        float f2 = this.redundantXSpace;
        this.origWidth = f - (f2 * 2.0f);
        float f3 = this.height;
        float f4 = this.redundantYSpace;
        this.origHeight = f3 - (f4 * 2.0f);
        float f5 = this.saveScale;
        this.right = ((f * f5) - f) - ((f2 * 2.0f) * f5);
        this.bottom = ((f3 * f5) - f3) - ((f4 * 2.0f) * f5);
        setImageMatrix(this.matrix);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            CustomZoomableImageView.this.mode = 2;
            return true;
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f = CustomZoomableImageView.this.saveScale;
            CustomZoomableImageView.this.saveScale *= scaleFactor;
            if (CustomZoomableImageView.this.saveScale > CustomZoomableImageView.this.maxScale) {
                CustomZoomableImageView zoomableImageView = CustomZoomableImageView.this;
                zoomableImageView.saveScale = zoomableImageView.maxScale;
                scaleFactor = CustomZoomableImageView.this.maxScale / f;
            } else if (CustomZoomableImageView.this.saveScale < CustomZoomableImageView.this.minScale) {
                CustomZoomableImageView zoomableImageView2 = CustomZoomableImageView.this;
                zoomableImageView2.saveScale = zoomableImageView2.minScale;
                scaleFactor = CustomZoomableImageView.this.minScale / f;
            }
            CustomZoomableImageView zoomableImageView3 = CustomZoomableImageView.this;
            zoomableImageView3.right = ((zoomableImageView3.width * CustomZoomableImageView.this.saveScale) - CustomZoomableImageView.this.width) - ((CustomZoomableImageView.this.redundantXSpace * 2.0f) * CustomZoomableImageView.this.saveScale);
            CustomZoomableImageView zoomableImageView4 = CustomZoomableImageView.this;
            zoomableImageView4.bottom = ((zoomableImageView4.height * CustomZoomableImageView.this.saveScale) - CustomZoomableImageView.this.height) - ((CustomZoomableImageView.this.redundantYSpace * 2.0f) * CustomZoomableImageView.this.saveScale);
            if (CustomZoomableImageView.this.origWidth * CustomZoomableImageView.this.saveScale <= CustomZoomableImageView.this.width || CustomZoomableImageView.this.origHeight * CustomZoomableImageView.this.saveScale <= CustomZoomableImageView.this.height) {
                CustomZoomableImageView.this.matrix.postScale(scaleFactor, scaleFactor, CustomZoomableImageView.this.width / 2.0f, CustomZoomableImageView.this.height / 2.0f);
                if (scaleFactor >= 1.0f) {
                    return true;
                }
                CustomZoomableImageView.this.matrix.getValues(CustomZoomableImageView.this.m);
                float f2 = CustomZoomableImageView.this.m[2];
                float f3 = CustomZoomableImageView.this.m[5];
                if (scaleFactor >= 1.0f) {
                    return true;
                }
                if (((float) Math.round(CustomZoomableImageView.this.origWidth * CustomZoomableImageView.this.saveScale)) < CustomZoomableImageView.this.width) {
                    if (f3 < (-CustomZoomableImageView.this.bottom)) {
                        CustomZoomableImageView.this.matrix.postTranslate(0.0f, -(f3 + CustomZoomableImageView.this.bottom));
                        return true;
                    } else if (f3 <= 0.0f) {
                        return true;
                    } else {
                        CustomZoomableImageView.this.matrix.postTranslate(0.0f, -f3);
                        return true;
                    }
                } else if (f2 < (-CustomZoomableImageView.this.right)) {
                    CustomZoomableImageView.this.matrix.postTranslate(-(f2 + CustomZoomableImageView.this.right), 0.0f);
                    return true;
                } else if (f2 <= 0.0f) {
                    return true;
                } else {
                    CustomZoomableImageView.this.matrix.postTranslate(-f2, 0.0f);
                    return true;
                }
            } else {
                CustomZoomableImageView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                CustomZoomableImageView.this.matrix.getValues(CustomZoomableImageView.this.m);
                float f4 = CustomZoomableImageView.this.m[2];
                float f5 = CustomZoomableImageView.this.m[5];
                if (scaleFactor >= 1.0f) {
                    return true;
                }
                if (f4 < (-CustomZoomableImageView.this.right)) {
                    CustomZoomableImageView.this.matrix.postTranslate(-(f4 + CustomZoomableImageView.this.right), 0.0f);
                } else if (f4 > 0.0f) {
                    CustomZoomableImageView.this.matrix.postTranslate(-f4, 0.0f);
                }
                if (f5 < (-CustomZoomableImageView.this.bottom)) {
                    CustomZoomableImageView.this.matrix.postTranslate(0.0f, -(f5 + CustomZoomableImageView.this.bottom));
                    return true;
                } else if (f5 <= 0.0f) {
                    return true;
                } else {
                    CustomZoomableImageView.this.matrix.postTranslate(0.0f, -f5);
                    return true;
                }
            }
        }
    }
}
