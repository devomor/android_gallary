package com.photo.photography.act;

import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.photo.photography.R;

public class ActPermission extends AppCompatActivity {

    private final int CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS = 100;
    private final int CAMERA_READ_EXTERNAL_STORAGE_PERMISSIONS = 101;
    Bundle myBundle = new Bundle();

    LinearLayout linTitle;
    TextView tvCameraMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_permission);


        linTitle = findViewById(R.id.linTitle);
        tvCameraMsg = findViewById(R.id.tvCameraMsg);

        startAnimation();

        myBundle = new Bundle();
        if (getIntent() != null) {
            myBundle = getIntent().getExtras();
        }

        findViewById(R.id.btn_allow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startAnimation();
                takePermission();
            }
        });
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void startAnimation() {

        Animation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(3000);

        Animation translateAnimation = new TranslateAnimation(800, 0, 0, 0);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        Animation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(1000);
        AnimationSet animationSet = new AnimationSet(false); //change to false
        animationSet.addAnimation(fadeInAnimation);
        animationSet.addAnimation(translateAnimation);

        linTitle.startAnimation(animationSet);

        Animation translateAnimationMsg = new TranslateAnimation(800, 0, 0, 0);
        translateAnimationMsg.setDuration(1000);
        translateAnimationMsg.setFillAfter(true);
        translateAnimationMsg.setStartOffset(500);
        Animation fadeInAnimationMsg = new AlphaAnimation(0f, 1f);
        fadeInAnimationMsg.setDuration(1000);
        fadeInAnimationMsg.setStartOffset(500);
        AnimationSet animationSetMsg = new AnimationSet(false); //change to false
        animationSetMsg.addAnimation(fadeInAnimationMsg);
        animationSetMsg.addAnimation(translateAnimationMsg);

//        linTitle.startAnimation(animationSetMsg);
        tvCameraMsg.startAnimation(animationSetMsg);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS || requestCode == CAMERA_READ_EXTERNAL_STORAGE_PERMISSIONS) {
            boolean gotPermission = grantResults.length > 0;

            for (int result : grantResults) {
                gotPermission &= result == PackageManager.PERMISSION_GRANTED;
            }

            if (gotPermission) {
                Log.e("AppStartFrom", "onRequestPermissionResult in PermissionActivity");
                moveMainScreen();

            } else {
                Toast.makeText(ActPermission.this, getString(R.string.storage_permission_denied), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void moveMainScreen() {
        if (myBundle != null) {
            startActivity(new Intent(ActPermission.this, ActMain.class).putExtras(myBundle));
        } else {
            startActivity(new Intent(ActPermission.this, ActMain.class));
        }
        finish();
    }

    private void takePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(ActPermission.this,
                    new String[]{CAMERA, ACCESS_MEDIA_LOCATION, READ_EXTERNAL_STORAGE}, CAMERA_READ_EXTERNAL_STORAGE_PERMISSIONS);

        } else {
            ActivityCompat.requestPermissions(ActPermission.this,
                    new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, CAMERA_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }
    }

}
