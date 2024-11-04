package com.photo.photography.secure_vault.repeater;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActPlayer;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.helper.Constants;
import com.photo.photography.secure_vault.utils.CryptoExceptionUtils;
import com.photo.photography.secure_vault.utils.CryptoUtil;
import com.photo.photography.secure_vault.utils.CallbackMediaClick;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RepeaterFullScreenImage extends PagerAdapter {

    private final boolean mIsFromVideo;
    private final Activity _activity;
    private final File videoFile;
    private ArrayList<VaultFile> _imagePaths = new ArrayList<>();
    private final ProgressDialog mProgressDialog;
    private final CallbackMediaClick mediaTapListener;

    // constructor
    public RepeaterFullScreenImage(Activity activity, ArrayList<VaultFile> imagePaths, boolean mIsFromVideo, CallbackMediaClick mediaClickListener) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        this.mIsFromVideo = mIsFromVideo;
        this.mProgressDialog = new ProgressDialog(activity);
        this.mProgressDialog.setMessage("Processing file. Please wait...");
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.mProgressDialog.setCancelable(false);
        this.mediaTapListener = mediaClickListener;
        VaultFileUtil fileUtils = new VaultFileUtil(activity);
        videoFile = new File(fileUtils.getDirPath(), Constants.DEMO_DECRYPT_FILE_NAME);
    }

    @Override
    public int getCount() {
        if (_imagePaths == null)
            return 0;
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView imgDisplay;
        ImageView mPlayIcon;

        LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_images, container, false);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);
        mPlayIcon = viewLayout.findViewById(R.id.ic_play_icon);
        int mWidth = _activity.getResources().getDisplayMetrics().widthPixels;
        mPlayIcon.setVisibility(mIsFromVideo ? View.VISIBLE : View.GONE);

        imgDisplay.setImageBitmap(getBitmap(mWidth, mIsFromVideo ? _imagePaths.get(position).getThumbnail() : _imagePaths.get(position).getNewPath()));
        mPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        if (mProgressDialog != null && !mProgressDialog.isShowing())
                            mProgressDialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        if (videoFile.exists()) {
                            Intent intent = new Intent(_activity, ActPlayer.class);
                            intent.putExtra("FromVideo", Uri.fromFile(videoFile).toString());
                            intent.putExtra("openFromVault", true);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            if (MyApp.getInstance().needToShowAd()) {
                                MyApp.getInstance().showInterstitial(_activity, intent, false, -1, null);
                            } else {
                                _activity.startActivity(intent);
                            }
                        } else {
                            Toast.makeText(_activity, R.string.something_went_wrong_please_try_again, Toast.LENGTH_SHORT).show();
                        }
                        super.onPostExecute(aVoid);
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            if (new File(_imagePaths.get(position).getNewPath()).exists()) {
                                if (videoFile.exists()) {
                                    videoFile.delete();
                                }
                                videoFile.createNewFile();
                                Log.e("TAG", "Decrypt file : " + _imagePaths.get(position).getNewPath() + " to\n" + videoFile.getAbsolutePath());
                                CryptoUtil.decryptForView(_activity, new File(_imagePaths.get(position).getNewPath()), videoFile);
                            }
                        } catch (CryptoExceptionUtils | IOException e) {
                            Log.e("TAG", "Error in Decryption of file : " + e);
                        }
                        return null;
                    }
                }.execute();
            }
        });

        imgDisplay.setOnClickListener(view -> mediaTapListener.onMediaClick());
        container.addView(viewLayout);

        return viewLayout;
    }

    private Bitmap getBitmap(int mWidth, String path) {
        Bitmap bmp = null;
        Bitmap oldBmp = null;
        try {
            oldBmp = CryptoUtil.decryptBitmap(new File(path));
            if (oldBmp != null) {
                if (!true)
                    bmp = Bitmap.createScaledBitmap(oldBmp, 200, 200, false);
                else
                    bmp = Bitmap.createScaledBitmap(oldBmp, mWidth, (oldBmp.getHeight() * mWidth) / oldBmp.getWidth(), false);
            }
        } catch (CryptoExceptionUtils e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

}