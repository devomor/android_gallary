package com.photo.photography.templates;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.photo.photography.util.configs.AppLog;
import com.photo.photography.edit_views.MultiTouchHandlers;
import com.photo.photography.util.utils.ImageDecoder;
import com.photo.photography.util.utils.ImageUtil;
import com.photo.photography.util.utils.PhotoUtil;
import com.photo.photography.util.utils.ResultContainers;


public class ItemsImageView extends ImageView {
    private static final String TAG = ItemsImageView.class.getSimpleName();
    private final GestureDetector mGestureDetector;
    private final Paint mPaint;
    private final Matrix mImageMatrix;
    private final Matrix mScaleMatrix;
    private final Matrix mMaskMatrix;
    private final Matrix mScaleMaskMatrix;
    private final PhotosItem mPhotoItem;
    private MultiTouchHandlers mTouchHandler;
    private Bitmap mImage;
    private Bitmap mMaskImage;
    private float mViewWidth, mViewHeight;
    private float mOutputScale = 1;
    private OnImageClickListener mOnImageClickListener;
    private RelativeLayout.LayoutParams mOriginalLayoutParams;
    private boolean mEnableTouch = true;

    public ItemsImageView(Context context, PhotosItem photoItem) {
        super(context);
        mPhotoItem = photoItem;
        if (photoItem.imagePath != null && photoItem.imagePath.length() > 0) {
            mImage = ResultContainers.getInstance().getImage(photoItem.imagePath);
            if (mImage == null || mImage.isRecycled()) {
                mImage = ImageDecoder.decodeFileToBitmap(photoItem.imagePath);
                ResultContainers.getInstance().putImage(photoItem.imagePath, mImage);
                AppLog.d(TAG, "create ItemImageView, decode image");
            } else {
                AppLog.d(TAG, "create ItemImageView, use decoded image");
            }
        }

        if (photoItem.maskPath != null && photoItem.maskPath.length() > 0) {
            mMaskImage = ResultContainers.getInstance().getImage(photoItem.maskPath);
            if (mMaskImage == null || mMaskImage.isRecycled()) {
                mMaskImage = PhotoUtil.decodePNGImage(context, photoItem.maskPath);
                ResultContainers.getInstance().putImage(photoItem.maskPath, mMaskImage);
                AppLog.d(TAG, "create ItemImageView, decode mask image");
            } else {
                AppLog.d(TAG, "create ItemImageView, use decoded mask image");
            }
        }

        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        setScaleType(ScaleType.MATRIX);
        setLayerType(LAYER_TYPE_HARDWARE, mPaint);
        mImageMatrix = new Matrix();
        mScaleMatrix = new Matrix();
        mMaskMatrix = new Matrix();
        mScaleMaskMatrix = new Matrix();

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onLongClickImage(ItemsImageView.this);
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onDoubleClickImage(ItemsImageView.this);
                }
                return true;
            }
        });
    }

    public void swapImage(ItemsImageView view) {
        Bitmap temp = view.getImage();
        view.setImage(mImage);
        mImage = temp;

        String tmpPath = view.getPhotoItem().imagePath;
        view.getPhotoItem().imagePath = mPhotoItem.imagePath;
        mPhotoItem.imagePath = tmpPath;
        resetImageMatrix();
        view.resetImageMatrix();
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    public RelativeLayout.LayoutParams getOriginalLayoutParams() {
        if (mOriginalLayoutParams != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mOriginalLayoutParams.width, mOriginalLayoutParams.height);
            params.leftMargin = mOriginalLayoutParams.leftMargin;
            params.topMargin = mOriginalLayoutParams.topMargin;
            return params;
        } else {
            return (RelativeLayout.LayoutParams) getLayoutParams();
        }
    }

    public void setOriginalLayoutParams(RelativeLayout.LayoutParams originalLayoutParams) {
        mOriginalLayoutParams = new RelativeLayout.LayoutParams(originalLayoutParams.width, originalLayoutParams.height);
        mOriginalLayoutParams.leftMargin = originalLayoutParams.leftMargin;
        mOriginalLayoutParams.topMargin = originalLayoutParams.topMargin;
    }

    public PhotosItem getPhotoItem() {
        return mPhotoItem;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }

    public Bitmap getMaskImage() {
        return mMaskImage;
    }

    @Override
    public Matrix getImageMatrix() {
        return mImageMatrix;
    }

    public Matrix getMaskMatrix() {
        return mMaskMatrix;
    }

    public Matrix getScaleMatrix() {
        return mScaleMatrix;
    }

    public Matrix getScaleMaskMatrix() {
        return mScaleMaskMatrix;
    }

    public float getViewWidth() {
        return mViewWidth;
    }

    public float getViewHeight() {
        return mViewHeight;
    }

    public void init(final float viewWidth, final float viewHeight, final float scale) {
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mOutputScale = scale;
        if (mImage != null) {
            mImageMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(viewWidth, viewHeight, mImage.getWidth(), mImage.getHeight()));
            mScaleMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(scale * viewWidth, scale * viewHeight, mImage.getWidth(), mImage.getHeight()));
        }
        //mask
        if (mMaskImage != null) {
            mMaskMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(viewWidth, viewHeight, mMaskImage.getWidth(), mMaskImage.getHeight()));
            mScaleMaskMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(scale * viewWidth, scale * viewHeight, mMaskImage.getWidth(), mMaskImage.getHeight()));
        }

        mTouchHandler = new MultiTouchHandlers();
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        mTouchHandler.setScale(scale);
        mTouchHandler.setEnableRotation(true);
        invalidate();
    }

    public void setImagePath(String imagePath) {
        mPhotoItem.imagePath = imagePath;
        recycleMainImage();
        mImage = ImageDecoder.decodeFileToBitmap(imagePath);
        mImageMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(mViewWidth, mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mScaleMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(mOutputScale * mViewWidth, mOutputScale * mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        invalidate();
        ResultContainers.getInstance().putImage(mPhotoItem.imagePath, mImage);
    }

    public void resetImageMatrix() {
        mImageMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(mViewWidth, mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mScaleMatrix.set(ImageUtil.createMatrixToDrawImageInCenterView(mOutputScale * mViewWidth, mOutputScale * mViewHeight, mImage.getWidth(), mImage.getHeight()));
        mTouchHandler.setMatrices(mImageMatrix, mScaleMatrix);
        invalidate();
    }

    public void clearMainImage() {
        mPhotoItem.imagePath = null;
        recycleMainImage();
        invalidate();
    }

    private void recycleMainImage() {
        if (mImage != null && !mImage.isRecycled()) {
            mImage.recycle();
            mImage = null;
            System.gc();
        }
    }

    private void recycleMaskImage() {
        if (mMaskImage != null && !mMaskImage.isRecycled()) {
            mMaskImage.recycle();
            mMaskImage = null;
            System.gc();
        }
    }

    public void recycleImages(boolean recycleMainImage) {
        AppLog.d(TAG, "recycleImages, recycleMainImage=" + recycleMainImage);
        if (recycleMainImage) {
            recycleMainImage();
        }
        recycleMaskImage();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mImage != null && !mImage.isRecycled() && mMaskImage != null && !mMaskImage.isRecycled()) {
            canvas.drawBitmap(mImage, mImageMatrix, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(mMaskImage, mMaskMatrix, mPaint);
            mPaint.setXfermode(null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableTouch) {
            return super.onTouchEvent(event);
        } else {
            mGestureDetector.onTouchEvent(event);
            if (mTouchHandler != null && mImage != null && !mImage.isRecycled()) {
                mTouchHandler.touch(event);
                mImageMatrix.set(mTouchHandler.getMatrix());
                mScaleMatrix.set(mTouchHandler.getScaleMatrix());
                invalidate();
                return true;
            } else {
                return true;//super.onTouchEvent(event);
            }
        }
    }

    public void setEnableTouch(boolean enableTouch) {
        mEnableTouch = enableTouch;
    }

    public interface OnImageClickListener {
        void onLongClickImage(ItemsImageView view);

        void onDoubleClickImage(ItemsImageView view);
    }
}
