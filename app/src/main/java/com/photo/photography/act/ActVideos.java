package com.photo.photography.act;

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.callbacks.CallbackMediaClick;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.Media;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.frag.EditModeListener;
import com.photo.photography.frag.FragRvAllMedia;
import com.photo.photography.frag.FragRvAllMediaVideos;
import com.photo.photography.frag.NothingToShowCallback;
import com.photo.photography.util.DeviceUtil;
import com.photo.photography.util.Measures;
import com.photo.photography.util.utilsEdit.SupportClass;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ActVideos extends AppCompatActivity implements CallbackMediaClick, NothingToShowCallback, EditModeListener {

    public static final String ARGS_PICK_MODE = "pick_mode";
    private static final int REQUEST_CODE_VIEW_MEDIA = 140;
    private final boolean pickMode = false;
    @BindView(R.id.framContainer)
    FrameLayout framContainer;
    //    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_videos);
        toolbar = findViewById(R.id.toolbarVideo);
        initUi();
//        pickMode = getIntent().getBooleanExtra(ARGS_PICK_MODE, false);
//        new FragRvAllMediaVideos().make(Album.getAllMediaAlbum());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framContainer, new FragRvAllMediaVideos().make(Album.getAllMediaAlbum()), FragRvAllMedia.TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        FragRvAllMediaVideos fragmentVideo = (FragRvAllMediaVideos) getSupportFragmentManager().findFragmentByTag(FragRvAllMedia.TAG);
        fragmentVideo.deletePendingMedia(requestCode, resultCode);
    }

    @Override
    public void changedEditMode(boolean editMode, int selected, int total, String title) {
        if (editMode) {
            updateToolbar(getString(R.string.toolbar_selection_count, selected, total));
        } else {
            updateToolbar(title.isEmpty() ? getString(R.string.app_name_new) : title);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 591) {
            if (SupportClass.isExternalStoragePermissionGranted(this)) {
                FragRvAllMedia myFragment = (FragRvAllMedia) getSupportFragmentManager().findFragmentByTag(FragRvAllMedia.TAG);
                if (myFragment != null && myFragment.isVisible()) {
                    myFragment.writePermissionAction();
                }
            } else {
                Toast.makeText(this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemsSelected(int count, int total) {
        toolbar.setTitle(getString(R.string.toolbar_selection_count, count, total));
    }

    private void initUi() {


        setSupportActionBar(toolbar);
//        toolbar.bringToFront();
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
//        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));

        toolbar.setTitle(getString(R.string.videos));
//        toolbar.setTitleTextAppearance(this, R.style.BoldTextAppearance);

//        setSupportActionBar(toolbar);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (DeviceUtil.isLandscape(getResources()))
            params.setMargins(0, 0, Measures.getNavigationBarSize(ActVideos.this).x, 0);
        else
            params.setMargins(0, 0, 0, 0);

        toolbar.setLayoutParams(params);
    }

    public void updateToolbar(String title) {
        Fragment topFragment = getTopFragment();
        if (!title.isEmpty() && title.contains(" of ")) {
            toolbar.setTitle(title);
            return;
        }
        toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
            case R.id.settings_album:
                ActSettings.startActivity(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Fragment getTopFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        Fragment top = null;
        for (int i = fragmentList.size() - 1; i >= 0; i--) {
            top = fragmentList.get(i);
            if (top != null) {
                return top;
            }
        }
        return top;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onMediaClick(Album album, ArrayList<Media> media, int position, ImageView imageView) {
        if (!pickMode) {
            viewImage(album, media, position, imageView);
        } else {
            try {
                Media m = media.get(position);
                Uri uris = ContentUris.withAppendedId(m.isImage() ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Video.Media.EXTERNAL_CONTENT_URI, m.getId());
                Intent res = new Intent();
                res.setData(uris);
                res.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setResult(RESULT_OK, res);
                finish();
            } catch (Exception e) {
                Log.e("TAG", "Error : " + e.getMessage());
                Toast.makeText(ActVideos.this, R.string.something_went_wrong_please_try_again, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMediaViewClick(Album album, ArrayList<Media> media, int position, ImageView imageView) {
        viewImage(album, media, position, imageView);
    }

    private void viewImage(Album album, ArrayList<Media> media, int position, ImageView imageView) {
        Intent intent = new Intent(getApplicationContext(), ActSingleMedia.class);
        intent.putExtra(ActSingleMedia.EXTRA_ARGS_ALBUM, album);
        ArrayList<Media> newMedia = null;
        if (media.size() > position && position != -1) {
            Media clickedMedia = media.get(position);
            if (media.size() > Constants.MAXIMUM_VIEW) {
                newMedia = Constants.getFilteredMedia(media, position);
            }
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, /*String.valueOf(media.get(position).getId())*/"transitionImageMain");
            try {
                intent.setAction(ActSingleMedia.ACTION_OPEN_ALBUM);
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_MEDIA, newMedia != null ? newMedia : media);
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_POSITION, newMedia != null ? newMedia.indexOf(clickedMedia) : position);
                if (MyApp.getInstance().needToShowAd()) {
                    Log.e("TRACE_DELETE", "Open SingleMediaActivity without OnActivityResult handling ----------");
                    MyApp.getInstance().showInterstitial(ActVideos.this, intent, false, REQUEST_CODE_VIEW_MEDIA, activityOptionsCompat.toBundle());
                } else {
                    Log.e("TRACE_DELETE", "Open SingleMediaActivity with OnActivityResult handling ++++++++++");
                    startActivityForResult(intent, REQUEST_CODE_VIEW_MEDIA, activityOptionsCompat.toBundle());
                }
//                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

            } catch (Exception e) { // Putting too much data into the Bundle
                Log.e("TAG", "Error : " + e.getMessage());
                // TODO: Find a better way to pass data between the activities - possibly a key to
                // access a HashMap or a unique value of a singleton Data Repository of some sort.
                intent.setAction(ActSingleMedia.ACTION_OPEN_ALBUM_LAZY);
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_MEDIA, media.get(position));
                if (MyApp.getInstance().needToShowAd()) {
                    Log.e("TRACE_DELETE", "Open SingleMediaActivity without OnActivityResult handling ----------");
                    MyApp.getInstance().showInterstitial(ActVideos.this, intent, false, REQUEST_CODE_VIEW_MEDIA, activityOptionsCompat.toBundle());
                } else {
                    Log.e("TRACE_DELETE", "Open SingleMediaActivity with OnActivityResult handling ++++++++++");
                    startActivityForResult(intent, REQUEST_CODE_VIEW_MEDIA, activityOptionsCompat.toBundle());
                }
            }
        }
    }

    @Override
    public void changedNothingToShow(boolean nothingToShow) {
        enableNothingToSHowPlaceHolder(nothingToShow);
    }

    public void enableNothingToSHowPlaceHolder(boolean status) {
        findViewById(R.id.nothing_to_show_placeholder).setVisibility(status ? View.VISIBLE : View.GONE);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

}