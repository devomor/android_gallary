package com.photo.photography.act;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.db.tables.ItemPackageTables;
import com.photo.photography.frag.FragDownloadedPackage;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.utils.DialogUtil;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ActBasePhoto extends ActBase implements
        DialogUtil.OnAddImageButtonClickListener {
    protected static final int REQUEST_ADD_TEXT_ITEM = 10000;
    protected static final int REQUEST_PHOTO_EDITOR_CODE = 9990;
    protected static final int PICK_IMAGE_REQUEST_CODE = 9980;
    protected static final int CAPTURE_IMAGE_REQUEST_CODE = 9970;
    protected static final int PICK_STICKER_REQUEST_CODE = 9960;
    protected static final int PICK_BACKGROUND_REQUEST_CODE = 9950;
    protected static final int REQUEST_EDIT_IMAGE = 9940;
    protected static final int PICK_MULTIPLE_IMAGE_REQUEST_CODE = 9930;
    protected static final int REQUEST_EDIT_TEXT_ITEM = 9920;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String selectedOutputPath;
    private Uri mCapturedImageUri;
    private Dialog mAddImageDialog;
    private View mAddImageView;
    private Animation mAnimation;

    public Dialog getBackgroundImageDialog() {
        return mAddImageDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCameraButtonClick() {

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        } else {
            getImageFromCamera();
            mAddImageDialog.dismiss();
        }

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageFromCamera();
                mAddImageDialog.dismiss();
            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }//end onRequestPermissionsResult
    /*@Override
    public void onCameraButtonClick() {
        getImageFromCamera();
        mAddImageDialog.dismiss();
    }*/

    @Override
    public void onGalleryButtonClick() {
        pickImageFromGallery();
        mAddImageDialog.dismiss();
    }

    @Override
    public void onBackgroundPhotoButtonClick() {

    }

    @Override
    public void onBackgroundColorButtonClick() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCapturedImageUri = savedInstanceState.getParcelable("mCapturedImageUri");
        }
        mAddImageDialog = DialogUtil.createAddImageDialog(this, this, false);
        mAddImageView = mAddImageDialog.findViewById(R.id.dialogAddImage);
        mAnimation = AnimationUtils.loadAnimation(this,
                R.anim.anim_slide_in_bottom);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("mCapturedImageUri", mCapturedImageUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PHOTO_EDITOR_CODE:
                    // output image path
                    Uri uri = data.getData();
                    resultFromPhotoEditor(uri);
                    break;
                case PICK_IMAGE_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
//                        startPhotoEditor(uri, false);
                        resultFromPhotoEditor(uri);
                    }
                    break;
                case CAPTURE_IMAGE_REQUEST_CODE:
                    if (mCapturedImageUri != null) {
//                        startPhotoEditor(mCapturedImageUri, true);
                        resultFromPhotoEditor(null);
                    }
                    break;
                case PICK_BACKGROUND_REQUEST_CODE:
                    ArrayList<String> allPaths = data.getStringArrayListExtra(ActSelectPhoto.EXTRA_SELECTED_IMAGES);
                    if (allPaths != null && allPaths.size() > 0) {
                        uri = Uri.fromFile(new File(allPaths.get(0)));
                        resultBackground(uri);
                    }
                    break;
                case REQUEST_EDIT_IMAGE:
                    uri = data.getData();
                    resultEditImage(uri);
                    break;
                case PICK_STICKER_REQUEST_CODE:
                    allPaths = data.getStringArrayListExtra(ActSelectPhoto.EXTRA_SELECTED_IMAGES);
                    if (allPaths != null && allPaths.size() > 0) {
                        final int len = allPaths.size();
                        Uri[] result = new Uri[len];
                        for (int idx = 0; idx < len; idx++) {
                            uri = Uri.fromFile(new File(allPaths.get(idx)));
                            result[idx] = uri;
                        }

                        resultStickers(result);
                    }
                    break;
                case PICK_MULTIPLE_IMAGE_REQUEST_CODE:
                    allPaths = data.getStringArrayListExtra(ActSelectPhoto.EXTRA_SELECTED_IMAGES);
                    if (allPaths != null && allPaths.size() > 0) {
                        final int len = allPaths.size();
                        Uri[] result = new Uri[len];
                        for (int idx = 0; idx < len; idx++) {
                            uri = Uri.fromFile(new File(allPaths.get(idx)));
                            result[idx] = uri;
                        }

                        resultPickMultipleImages(result);
                    }
                    break;
            }

        }
    }


    protected void requestPhoto() {
       /* if (mAddImageView != null) {
            mAddImageView.startAnimation(mAnimation);
        }*/
        mAddImageDialog.show();
    }

    public void pickImageFromGallery() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST_CODE);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getImageFromCamera() {
        try {
            mCapturedImageUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }


    public File createImageFile() throws IOException {
        File storageDir = new VaultFileUtil(this).getFile("CamPic");
        storageDir.mkdirs();
        File image = null;
        try {
            image = new File(storageDir, "Gallery");
            if (image.exists())
                image.delete();
            image.createNewFile();

            selectedOutputPath = image.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    public void pickSticker() {
        Intent intent = new Intent(this, ActDownloadedPackage.class);
        intent.putExtra(FragDownloadedPackage.EXTRA_PACKAGE_TYPE, ItemPackageTables.STICKER_TYPE);
        startActivityForResult(intent, PICK_STICKER_REQUEST_CODE);
    }

    public void pickBackground() {
        Intent intent = new Intent(this, ActDownloadedPackage.class);
        intent.putExtra(FragDownloadedPackage.EXTRA_PACKAGE_TYPE, ItemPackageTables.BACKGROUND_TYPE);
        startActivityForResult(intent, PICK_BACKGROUND_REQUEST_CODE);
    }

    public void requestEditingImage(Uri imageUri) {
//        Intent newIntent = new Intent(this, ImageProcessingActivity.class);
//        newIntent.putExtra(ImageProcessingActivity.IMAGE_URI_KEY, imageUri);
//        startActivityForResult(newIntent, REQUEST_EDIT_IMAGE);
    }

    protected void resultFromPhotoEditor(Uri image) {

    }

    protected void resultSticker(Uri uri) {

    }

    protected void resultBackground(Uri uri) {

    }

    protected void resultEditImage(Uri uri) {

    }

    protected void resultAddTextItem(String text, int color, String fontPath) {

    }

    protected void resultEditTextItem(String text, int color, String fontPath) {

    }

    public void resultPickMultipleImages(Uri[] uri) {

    }

    public void resultStickers(Uri[] uri) {

    }

    private AdSize getAdSize(Activity activity) {

        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    public void loadBannerAds(FrameLayout adContainerView) {
        adContainerView.setVisibility(View.GONE);
        if (!UserHelpers.isAdEnable()) {
            return;
        }
        if (!UserHelpers.isBanner()) {
            return;
        }

        if (SupportClass.checkConnection(this)) {
            AdView mAdView = new AdView(this);

            String adId = "";
            if (BuildConfig.DEBUG) {
                adId = getString(R.string.admob_banner_ads_id_test);
            } else {
                adId = MyApp.getSettings().getBannerid();
            }

            if (adId == null) {
                return;
            }
            if (adId.isEmpty()) {
                return;
            }

            mAdView.setAdUnitId(adId);

            adContainerView.removeAllViews();
            adContainerView.addView(mAdView);

            AdSize adSize = getAdSize(this);
            mAdView.setAdSize(adSize);

            if (SupportClass.checkConnection(this)) {
                if (UserHelpers.getAdType().equals(Constants.ADMOB)) {

                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    mAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.e("ADSTAG", "Banner onAdFailedToLoad()" + loadAdError.getCode());
                        }

                        @Override
                        public void onAdLoaded() {
                            Log.e("ADSTAG", "Banner onAdLoaded()");
                            adContainerView.setVisibility(View.VISIBLE);
                        }
                    });
                } else {

                    AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.e("ADSTAG", "Banner onAdFailedToLoad()" + loadAdError.getCode());
                        }

                        @Override
                        public void onAdLoaded() {
                            Log.e("ADSTAG", "Banner onAdLoaded()");
                            adContainerView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

        }
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
