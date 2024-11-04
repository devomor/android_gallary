package com.photo.photography.collage.screen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.collage.callback.CallbackOnChooseColor;
import com.photo.photography.collage.callback.CallbackOnShareImage;
import com.photo.photography.collage.screen.frag.FragBase;
import com.photo.photography.collage.screen.frag.FragPhotoCollage;
import com.photo.photography.collage.util.CommonUtil;


public class ActPhotoCollage extends ActBase implements CallbackOnShareImage, CallbackOnChooseColor {

    public ImageView iv1, iv2, iv3, iv4, iv5;
    private int mSelectedColor = Color.GREEN;
    private boolean mClickedShareButton = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_photocollage);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name_new);
        }

        findViewById(R.id.relatveAnimation).setVisibility(View.VISIBLE);
        CommonUtil.startScreenDisplayAnimation(this,
                (RelativeLayout) findViewById(R.id.relatveAnimation),
                (ImageView) findViewById(R.id.ivFeatures),
                ContextCompat.getDrawable(this, R.mipmap.ic_launcher));

        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new FragPhotoCollage())
                    .commit();
        } else {
            mClickedShareButton = savedInstanceState.getBoolean("mClickedShareButton", false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mClickedShareButton", mClickedShareButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mClickedShareButton) {
            mClickedShareButton = false;

        }
    }

    @Override
    public void onBackPressed() {
        FragBase fragment = (FragBase) getVisibleFragment();
        if (fragment instanceof FragPhotoCollage) {
            super.onBackPressed();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onShareImage(String imagePath) {
        mClickedShareButton = true;
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        return fragmentManager.findFragmentById(R.id.frame_container);
    }

    @Override
    public void onShareFrame(String imagePath) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Shared image frame: " + imagePath, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getSelectedColor() {
        return mSelectedColor;
    }

    @Override
    public void setSelectedColor(int color) {
        mSelectedColor = color;
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

