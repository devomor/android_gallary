package com.photo.photography.secure_vault;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.photo.photography.R;
import com.photo.photography.secure_vault.frag.FragPrivateVaultImage;
import com.photo.photography.secure_vault.frag.FragPrivateVaultVideo;
import com.photo.photography.secure_vault.helper.Constants;

public class ActVault extends AppCompatActivity implements FragPrivateVaultImage.OnImageSelectedListener, FragPrivateVaultVideo.OnVideoSelectedListener {

    public static final String IS_IMAGE_PICKED = "is_image_picked";
    public static final String PICKED_MEDIA_LIST = "picked_media_list";
    public static final String OPEN_FOR_IMAGE = "open_for_image";
    Fragment fragment;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vault);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(getString(R.string.secure_vault));

        String CURRENT_TAG = "";
        if (getIntent().getBooleanExtra(OPEN_FOR_IMAGE, true)) {
            CURRENT_TAG = Constants.PHOTOVAULT;
            fragment = new FragPrivateVaultImage();
        } else {
            CURRENT_TAG = Constants.VIDEOVAULT;
            fragment = new FragPrivateVaultVideo();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.contentView, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageSelected(int count) {
        ((FragPrivateVaultImage) fragment).onImageSelected(count);
    }

    @Override
    public void onVideoSelected(int count) {
        ((FragPrivateVaultVideo) fragment).onVideoSelected(count);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
