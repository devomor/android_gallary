package com.photo.photography.secure_vault;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.photo.photography.R;
import com.photo.photography.data_helper.Album;
import com.photo.photography.frag.EditModeListener;
import com.photo.photography.frag.FragRvAllMediaPhotos;
import com.photo.photography.frag.FragRvAllMediaVideos;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.helper.Constants;

public class ActPickImage extends AppCompatActivity implements EditModeListener {

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
            fragment = FragRvAllMediaPhotos.make(Album.getAllMediaAlbum(),Album.getAllMediaAlbum());
        } else {
            CURRENT_TAG = Constants.VIDEOVAULT;
            fragment = FragRvAllMediaVideos.make(Album.getAllMediaAlbum());
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.contentView, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
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
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changedEditMode(boolean editMode, int selected, int total, String title) {

    }

    @Override
    public void onItemsSelected(int count, int total) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        FragRvAllMediaPhotos myPhotosFragment = (FragRvAllMediaPhotos) getSupportFragmentManager().findFragmentByTag(Constants.PHOTOVAULT);
        FragRvAllMediaVideos myVideosFragment = (FragRvAllMediaVideos) getSupportFragmentManager().findFragmentByTag(Constants.VIDEOVAULT);
        if (myPhotosFragment != null && myPhotosFragment.isVisible()) {
            if (requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE) {
                myPhotosFragment.deletePendingMedia(requestCode, resultCode);
            }
        } else if (myVideosFragment != null && myVideosFragment.isVisible()) {
            if (requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE) {
                myVideosFragment.deletePendingMedia(requestCode, resultCode);
            }
        }
    }

}
