package com.photo.photography.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.edit_views.colorpick.ColorPickDialog;
import com.photo.photography.frames.ImageViewFrame;
import com.photo.photography.frames.PhotoLayoutFrame;
import com.photo.photography.models.TemplateItemModel;
import com.photo.photography.util.utils.FileUtil;
import com.photo.photography.util.utils.ImageUtil;
import com.photo.photography.util.utils.editorUtil.ImageDecoders;

import java.io.File;
import java.util.ArrayList;

public class ActCollageCreate extends ActBaseTemplateDetail implements PhotoLayoutFrame.OnQuickActionClickListener, ColorPickDialog.OnColorChangedListener {
    private static final int REQUEST_SELECT_PHOTO = 99;
    private static final float MAX_SPACE = ImageUtil.pxFromDp(MyApp.getInstance(), 30);
    private static final float MAX_CORNER = ImageUtil.pxFromDp(MyApp.getInstance(), 60);
    private static final float DEFAULT_SPACE = ImageUtil.pxFromDp(MyApp.getInstance(), 2);
    private static final float MAX_SPACE_PROGRESS = 300.0f;
    private static final float MAX_CORNER_PROGRESS = 200.0f;

    private ImageViewFrame mSelectedFrameImageView;
    private PhotoLayoutFrame mFramePhotoLayout;
    private ViewGroup mSpaceLayout;
    private SeekBar mSpaceBar;
    private SeekBar mCornerBar;
    private float mSpace = DEFAULT_SPACE;
    private float mCorner = 0;
    //Background
    private int mBackgroundColor = Color.WHITE;
    private Bitmap mBackgroundImage;
    private Uri mBackgroundUri = null;
    private ColorPickDialog mColorPickerDialog;
    //Saved instance state
    private Bundle mSavedInstanceState;

    @Override
    protected boolean isShowingAllTemplates() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //restore old params
        if (savedInstanceState != null) {
            mSpace = savedInstanceState.getFloat("mSpace");
            mCorner = savedInstanceState.getFloat("mCorner");
            mBackgroundColor = savedInstanceState.getInt("mBackgroundColor");
            mBackgroundUri = savedInstanceState.getParcelable("mBackgroundUri");
            mSavedInstanceState = savedInstanceState;
            if (mBackgroundUri != null)
                mBackgroundImage = ImageDecoders.decodeUriToBitmap(this, mBackgroundUri);
        }
        //add ads view
        //addAdsView(R.id.adsLayout);
        //inflate widgets
//        mAddImageDialog.findViewById(R.id.dividerTextView).setVisibility(View.VISIBLE);
        mAddImageDialog.findViewById(R.id.alterBackgroundView).setVisibility(View.VISIBLE);
        mAddImageDialog.findViewById(R.id.dividerBackgroundPhotoView).setVisibility(View.VISIBLE);
        mAddImageDialog.findViewById(R.id.alterBackgroundColorView).setVisibility(View.VISIBLE);
        mSpaceLayout = (ViewGroup) findViewById(R.id.spaceLayout);
        mSpaceBar = (SeekBar) findViewById(R.id.spaceBar);
        mSpaceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpace = MAX_SPACE * seekBar.getProgress() / MAX_SPACE_PROGRESS;
                if (mFramePhotoLayout != null)
                    mFramePhotoLayout.setSpace(mSpace, mCorner);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCornerBar = (SeekBar) findViewById(R.id.cornerBar);
        mCornerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCorner = MAX_CORNER * seekBar.getProgress() / MAX_CORNER_PROGRESS;
                if (mFramePhotoLayout != null)
                    mFramePhotoLayout.setSpace(mSpace, mCorner);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("mSpace", mSpace);
        outState.putFloat("mCornerBar", mCorner);
        outState.putInt("mBackgroundColor", mBackgroundColor);
        outState.putParcelable("mBackgroundUri", mBackgroundUri);
        if (mFramePhotoLayout != null) {
            mFramePhotoLayout.saveInstanceState(outState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_ratio).setVisible(true);
        return result;
    }

    @Override
    public void onBackgroundColorButtonClick() {
        if (mColorPickerDialog == null) {
            mColorPickerDialog = new ColorPickDialog(this, mBackgroundColor);
            mColorPickerDialog.setOnColorChangedListener(this);
        }

        mColorPickerDialog.setOldColor(mBackgroundColor);
        if (!mColorPickerDialog.isShowing()) {
            mColorPickerDialog.show();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_frame_detail;
    }

    @Override
    public Bitmap createOutputImage() throws OutOfMemoryError {
        try {
            Bitmap template = mFramePhotoLayout.createImage();
            Bitmap result = Bitmap.createBitmap(template.getWidth(), template.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if (mBackgroundImage != null && !mBackgroundImage.isRecycled()) {
                canvas.drawBitmap(mBackgroundImage, new Rect(0, 0, mBackgroundImage.getWidth(), mBackgroundImage.getHeight()),
                        new Rect(0, 0, result.getWidth(), result.getHeight()), paint);
            } else {
                canvas.drawColor(mBackgroundColor);
            }

            canvas.drawBitmap(template, 0, 0, paint);
            template.recycle();
            template = null;
            Bitmap stickers = mPhotoView.getImage(mOutputScale);
            canvas.drawBitmap(stickers, 0, 0, paint);
            stickers.recycle();
            stickers = null;
            System.gc();
            return result;
        } catch (OutOfMemoryError error) {
            throw error;
        }
    }

    @Override
    protected void buildLayout(TemplateItemModel item) {
        mFramePhotoLayout = new PhotoLayoutFrame(this, item.getPhotoItemList());
        mFramePhotoLayout.setQuickActionClickListener(this);
        if (mBackgroundImage != null && !mBackgroundImage.isRecycled()) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mBackgroundImage);
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            if (Build.VERSION.SDK_INT >= 16)
                mContainerLayout.setBackground(bitmapDrawable);
            else
                mContainerLayout.setBackgroundDrawable(bitmapDrawable);
        } else {
            mContainerLayout.setBackgroundColor(mBackgroundColor);
        }

        int viewWidth = mContainerLayout.getWidth();
        int viewHeight = mContainerLayout.getHeight();
        if (mLayoutRatio == RATIO_SQUARE) {
            if (viewWidth > viewHeight) {
                viewWidth = viewHeight;
            } else {
                viewHeight = viewWidth;
            }
        } else if (mLayoutRatio == RATIO_GOLDEN) {
            final double goldenRatio = 1.61803398875;
            if (viewWidth <= viewHeight) {
                if (viewWidth * goldenRatio >= viewHeight) {
                    viewWidth = (int) (viewHeight / goldenRatio);
                } else {
                    viewHeight = (int) (viewWidth * goldenRatio);
                }
            } else if (viewHeight <= viewWidth) {
                if (viewHeight * goldenRatio >= viewWidth) {
                    viewHeight = (int) (viewWidth / goldenRatio);
                } else {
                    viewWidth = (int) (viewHeight * goldenRatio);
                }
            }
        }
        mOutputScale = ImageUtil.calculateOutputScaleFactor(viewWidth, viewHeight);
        mFramePhotoLayout.build(viewWidth, viewHeight, mOutputScale, mSpace, mCorner);
        if (mSavedInstanceState != null) {
            mFramePhotoLayout.restoreInstanceState(mSavedInstanceState);
            mSavedInstanceState = null;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContainerLayout.removeAllViews();
        mContainerLayout.addView(mFramePhotoLayout, params);
        //add sticker view
        mContainerLayout.removeView(mPhotoView);
        mContainerLayout.addView(mPhotoView, params);
        //reset space and corner seek bars
        mSpaceBar.setProgress((int) (MAX_SPACE_PROGRESS * mSpace / MAX_SPACE));
        mCornerBar.setProgress((int) (MAX_CORNER_PROGRESS * mCorner / MAX_CORNER));
    }

    @Override
    public void onEditActionClick(ImageViewFrame v) {
        mSelectedFrameImageView = v;
        if (v.getImage() != null && v.getPhotoItem().imagePath != null && v.getPhotoItem().imagePath.length() > 0) {
            Uri uri = Uri.fromFile(new File(v.getPhotoItem().imagePath));
            requestEditingImage(uri);
        }
    }

    @Override
    public void onChangeActionClick(ImageViewFrame v) {
        mSelectedFrameImageView = v;
        requestPhoto();
    }

    @Override
    protected void resultEditImage(Uri uri) {
        if (mSelectedFrameImageView != null) {
            mSelectedFrameImageView.setImagePath(FileUtil.getPath(this, uri));
        }
    }

   /* @Override
    protected void resultFromPhotoEditor(Uri image) {
        if (mSelectedFrameImageView != null) {
            mSelectedFrameImageView.setImagePath(FileUtils.getPath(this, image));
        }
    }*/

    @Override
    protected void resultFromPhotoEditor(Uri image) {
        if (mSelectedFrameImageView != null) {
            if (image == null && !android.text.TextUtils.isEmpty(selectedOutputPath)) {
                mSelectedFrameImageView.setImagePath(selectedOutputPath);
                selectedOutputPath = "";
            } else {
                mSelectedFrameImageView.setImagePath(FileUtil.getPath(this, image));
            }
        }
    }


    private void recycleBackgroundImage() {
        if (mBackgroundImage != null && !mBackgroundImage.isRecycled()) {
            mBackgroundImage.recycle();
            mBackgroundImage = null;
            System.gc();
        }
    }

    @Override
    protected void resultBackground(Uri uri) {
        recycleBackgroundImage();
        mBackgroundUri = uri;
        mBackgroundImage = ImageDecoders.decodeUriToBitmap(this, uri);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mBackgroundImage);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        if (Build.VERSION.SDK_INT >= 16) {
            mContainerLayout.setBackground(bitmapDrawable);
        } else {
            mContainerLayout.setBackgroundDrawable(bitmapDrawable);
        }
    }

    @Override
    public void onColorChanged(int color) {
        recycleBackgroundImage();
        mBackgroundColor = color;
        mContainerLayout.setBackgroundColor(mBackgroundColor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK) {
            ArrayList<String> mSelectedImages = data.getStringArrayListExtra(ActSelectPhoto.EXTRA_SELECTED_IMAGES);
            if (mSelectedFrameImageView != null && mSelectedImages != null && !mSelectedImages.isEmpty()) {
                mSelectedFrameImageView.setImagePath(mSelectedImages.get(0));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void finish() {
        recycleBackgroundImage();
        if (mFramePhotoLayout != null) {
            mFramePhotoLayout.recycleImages();
        }
        super.finish();
    }
}
