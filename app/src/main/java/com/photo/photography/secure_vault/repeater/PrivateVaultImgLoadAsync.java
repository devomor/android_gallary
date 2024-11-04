package com.photo.photography.secure_vault.repeater;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.photo.photography.secure_vault.utils.CryptoExceptionUtils;
import com.photo.photography.secure_vault.utils.CryptoUtil;
import com.photo.photography.secure_vault.utils.MediaAsyncUtils;

import java.io.File;

public class PrivateVaultImgLoadAsync extends MediaAsyncUtils<String, String, Bitmap> {

    private ImageView mImageView;
    private Activity mContext;
    private int mWidth;
    boolean showOriginal;

    public PrivateVaultImgLoadAsync(Activity context, ImageView imageView, int width, boolean showOriginal) {
        mImageView = imageView;
        mContext = context;
        mWidth = width;
        this.showOriginal = showOriginal;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bmp = null;
        Bitmap oldBmp = null;
        try {
            oldBmp = CryptoUtil.decryptBitmap(new File(url));
            //  Log.e("TAG", "Bitmap width : " + mWidth );
            if (!showOriginal)
                bmp = Bitmap.createScaledBitmap(oldBmp, 200, 200, false);
            else
                bmp = Bitmap.createScaledBitmap(oldBmp, mWidth, (oldBmp.getHeight() * mWidth) / oldBmp.getWidth(), false);
        } catch (CryptoExceptionUtils e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        mContext.runOnUiThread(() -> {
            mImageView.setImageBitmap(result);
        });
    }
}
