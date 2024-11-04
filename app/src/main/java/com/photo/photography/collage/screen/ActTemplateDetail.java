package com.photo.photography.collage.screen;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.photo.photography.R;
import com.photo.photography.collage.view.TransitionImagesView;
import com.photo.photography.collage.model.DataTemplateItem;
import com.photo.photography.collage.poster.ItemImageViewCustom;
import com.photo.photography.collage.poster.PhotoItemCustom;
import com.photo.photography.collage.poster.PhotoLayoutCustom;
import com.photo.photography.collage.util.CollageFilesUtils;
import com.photo.photography.collage.util.PhotoUtil;

import java.io.File;

/**
 * Created by Sira on 3/11/2018.
 */
public class ActTemplateDetail extends ActBaseTemplateDetail implements PhotoLayoutCustom.OnQuickActionClickListener {
    private PhotoLayoutCustom mPhotoLayout;
    private ItemImageViewCustom mSelectedItemImageView;
    private TransitionImagesView mBackgroundImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (PhotoItemCustom item : mSelectedTemplateItem.getPhotoItemList())
            if (item.imagePath != null && item.imagePath.length() > 0) {
                mSelectedPhotoPaths.add(item.imagePath);
            }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_template_detail;
    }

    @Override
    public Bitmap createOutputImage() {
        Bitmap template = mPhotoLayout.createImage();
        Bitmap result = Bitmap.createBitmap(template.getWidth(), template.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(template, 0, 0, paint);
        template.recycle();
        template = null;
        Bitmap stickers = mPhotoView.getImage(mOutputScale);
        canvas.drawBitmap(stickers, 0, 0, paint);
        stickers.recycle();
        stickers = null;
        System.gc();
        return result;
    }

    @Override
    public void onEditActionClick(ItemImageViewCustom v) {
        mSelectedItemImageView = v;
        if (v.getImage() != null && v.getPhotoItem().imagePath != null && v.getPhotoItem().imagePath.length() > 0) {
            Uri uri = Uri.fromFile(new File(v.getPhotoItem().imagePath));
            requestEditingImage(uri);
        }
    }

    @Override
    public void onChangeActionClick(ItemImageViewCustom v) {
        mSelectedItemImageView = v;
        final Dialog dialog = getBackgroundImageDialog();
        if (dialog != null)
            dialog.findViewById(R.id.alterBackgroundView).setVisibility(View.GONE);
        requestPhoto();
    }

    @Override
    public void onChangeBackgroundActionClick(TransitionImagesView v) {
        mBackgroundImageView = v;
        final Dialog dialog = getBackgroundImageDialog();
        if (dialog != null)
            dialog.findViewById(R.id.alterBackgroundView).setVisibility(View.VISIBLE);
        requestPhoto();
    }

    @Override
    protected void resultEditImage(Uri uri) {
        if (mBackgroundImageView != null) {
            mBackgroundImageView.setImagePath(CollageFilesUtils.getPath(this, uri));
            mBackgroundImageView = null;
        } else if (mSelectedItemImageView != null) {
            mSelectedItemImageView.setImagePath(CollageFilesUtils.getPath(this, uri));
        }
    }

    @Override
    protected void resultFromPhotoEditor(Uri image) {
        if (mBackgroundImageView != null) {
            mBackgroundImageView.setImagePath(CollageFilesUtils.getPath(this, image));
            mBackgroundImageView = null;
        } else if (mSelectedItemImageView != null) {
            mSelectedItemImageView.setImagePath(CollageFilesUtils.getPath(this, image));
        }
    }

    @Override
    protected void resultBackground(Uri uri) {
        if (mBackgroundImageView != null) {
            mBackgroundImageView.setImagePath(CollageFilesUtils.getPath(this, uri));
            mBackgroundImageView = null;
        } else if (mSelectedItemImageView != null) {
            mSelectedItemImageView.setImagePath(CollageFilesUtils.getPath(this, uri));
        }
    }

    @Override
    protected void buildLayout(DataTemplateItem templateItem) {
        Bitmap backgroundImage = null;
        if (mPhotoLayout != null) {
            backgroundImage = mPhotoLayout.getBackgroundImage();
            mPhotoLayout.recycleImages(false);
        }

        final Bitmap frameImage = PhotoUtil.decodePNGImage(this, templateItem.getTemplate());
        int[] size = calculateThumbnailSize(frameImage.getWidth(), frameImage.getHeight());
        //Photo Item item_quick_action must be descended by index before creating photo layout.
        mPhotoLayout = new PhotoLayoutCustom(this, templateItem.getPhotoItemList(), frameImage);
        mPhotoLayout.setBackgroundImage(backgroundImage);
        mPhotoLayout.setQuickActionClickListener(this);
        mPhotoLayout.build(size[0], size[1], mOutputScale);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size[0], size[1]);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContainerLayout.removeAllViews();
        mContainerLayout.addView(mPhotoLayout, params);
        //add sticker view
        mContainerLayout.removeView(mPhotoView);
        mContainerLayout.addView(mPhotoView, params);
    }

    @Override
    public void finish() {
        if (mPhotoLayout != null) {
            mPhotoLayout.recycleImages(true);
        }
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                try {
                    onBackPressed();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    finish();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
