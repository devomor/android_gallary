package com.photo.photography.act;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.HandlingAlbum;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.filters_mode.FilterModes;
import com.photo.photography.frag.EditModeListener;
import com.photo.photography.frag.FragRvAllMediaPhotos;
import com.photo.photography.frag.FragRvAllMediaVideos;
import com.photo.photography.callbacks.CallbackMediaClick;
import com.photo.photography.frag.M_FragmentHomePhotos11;
import com.photo.photography.frag.M_FragmentHomeVideos11;
import com.photo.photography.util.Common;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;

public class ActWAStatus extends ActSharedMedia implements CallbackMediaClick, EditModeListener {
    public static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSIONS = 1234;
    public static final String BUNDLE_ALBUM = "album";
    public static final String FOLDER_WHATSAPP_STATUS = ".Statuses";
    private final boolean pickMode = false;
    ViewPagerAdapter pagerAdapter;
    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onResume() {
        super.onResume();
        if (arePermissionDenied()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissionQ();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if (arePermissionDenied()) {
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }
        }
    }

    private boolean arePermissionDenied() {
        Common.APP_DIR = Environment.getExternalStorageDirectory().getPath() +
                File.separator + "StatusDownloader";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getContentResolver().getPersistedUriPermissions().size() <= 0;
        }

        for (String permissions : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestPermissionQ() {
        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

        Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        String startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";

        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/document/");
        scheme += "%3A" + startDir;

        uri = Uri.parse(scheme);

        Log.d("URI", uri.toString());

        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);


        activityResultLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Toast.makeText(ActWAStatus.this, "Success", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wa_status);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(getString(R.string.wa_status));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        createTabIcons();
        setCurrentTab(0);
    }

    private void createTabIcons() {
        View tabOne = LayoutInflater.from(ActWAStatus.this).inflate(R.layout.apk_tabs_custom, null);
        TextView textViewOne = tabOne.findViewById(R.id.tab);
        textViewOne.setText("Photos");
        ImageView ivTabStripOne = tabOne.findViewById(R.id.ivTabStrip);
        ivTabStripOne.setVisibility(View.VISIBLE);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View tabTwo = LayoutInflater.from(ActWAStatus.this).inflate(R.layout.apk_tabs_custom, null);
        TextView textViewTwo = tabTwo.findViewById(R.id.tab);
        textViewTwo.setText("Videos");
        ImageView ivTabStripTwo = tabTwo.findViewById(R.id.ivTabStrip);
        ivTabStripTwo.setVisibility(View.GONE);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void setCurrentTab(int tabPos) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tabOne = tabLayout.getTabAt(i).getCustomView();
            TextView textViewOne = tabOne.findViewById(R.id.tab);
            textViewOne.setTextColor(ContextCompat.getColor(this, i == tabPos ? R.color.tab_selected_new : R.color.tab_unselected_new));
            ImageView ivTabStripOne = tabOne.findViewById(R.id.ivTabStrip);
            ivTabStripOne.setColorFilter(ContextCompat.getColor(this, R.color.tab_selected_new));
            ivTabStripOne.setVisibility(i == tabPos ? View.VISIBLE : View.INVISIBLE);
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    public Album getWhatsappStatusFolder(FilterModes filterMode) {
        File statusFolder = new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/" + FOLDER_WHATSAPP_STATUS);
        File statusFolder11 = new File(Environment.getExternalStorageDirectory(), "Android/media/com.whatsapp/Whatsapp/Media/" + FOLDER_WHATSAPP_STATUS);
        if (statusFolder11.exists()) {
            File[] list = statusFolder11.listFiles();
            int count = 0;
            for (File f : list) {
                String name = f.getName();
                if (name.endsWith(".jpg") || name.endsWith(".mp4"))
                    count++;
            }
            SQLiteDatabase db = HandlingAlbum.getInstance(getApplicationContext()).getReadableDatabase();
            Album album = new Album(statusFolder11.getAbsolutePath(), FOLDER_WHATSAPP_STATUS, count, statusFolder11.lastModified());
            album.withSettings(HandlingAlbum.getSettings(db, album.getPath()));
            album.setFilterMode(filterMode);
            return album;

        } else if (statusFolder.exists()) {
            File[] list = statusFolder.listFiles();
            int count = 0;
            for (File f : list) {
                String name = f.getName();
                if (name.endsWith(".jpg") || name.endsWith(".mp4"))
                    count++;
            }
            SQLiteDatabase db = HandlingAlbum.getInstance(getApplicationContext()).getReadableDatabase();
            Album album = new Album(statusFolder.getAbsolutePath(), FOLDER_WHATSAPP_STATUS, count, statusFolder.lastModified());
            album.withSettings(HandlingAlbum.getSettings(db, album.getPath()));
            album.setFilterMode(filterMode);
            return album;
        } else {
            return null;
        }
    }

    private void createViewPager(ViewPager viewPager) {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            pagerAdapter.addFrag(new M_FragmentHomePhotos11(), "Photos");
            pagerAdapter.addFrag(new M_FragmentHomeVideos11(), "Videos");
        }else {
            pagerAdapter.addFrag(new FragRvAllMediaPhotos().make(getWhatsappStatusFolder(FilterModes.IMAGES)), "Photos");
            pagerAdapter.addFrag(new FragRvAllMediaVideos().make(getWhatsappStatusFolder(FilterModes.VIDEO)), "Videos");
        }
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void changedEditMode(boolean editMode, int selected, int total, String title) {

    }

    @Override
    public void onItemsSelected(int count, int total) {

    }

    @Override
    public void onMediaClick(Album album, ArrayList<Media> media, int position, ImageView imageView) {

        if (!pickMode) {
            Intent intent = new Intent(getApplicationContext(), ActSingleMedia.class);
            intent.putExtra(ActSingleMedia.EXTRA_ARGS_ALBUM, album);
            ArrayList<Media> newMedia = null;
            if (media.size() > Constants.MAXIMUM_VIEW) {
                newMedia = Constants.getFilteredMedia(media, position);
            }
            try {
                intent.setAction(ActSingleMedia.ACTION_OPEN_ALBUM);
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_MEDIA, newMedia != null ? newMedia : media);
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_POSITION, position);
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_IS_WA_STATUS, true);
            //    startActivity(intent);

                if (MyApp.getInstance().needToShowAd()) {
                    MyApp.getInstance().showInterstitial(ActWAStatus.this, intent, false, -1, null);
                } else {
                    startActivity(intent);
                }

            } catch (Exception e) { // Putting too much data into the Bundle
                // TODO: Find a better way to pass data between the activities - possibly a key to
                // access a HashMap or a unique value of a singleton Data Repository of some sort.
                intent.setAction(ActSingleMedia.ACTION_OPEN_ALBUM_LAZY);
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_MEDIA, media.get(position));
                intent.putExtra(ActSingleMedia.EXTRA_ARGS_IS_WA_STATUS, true);
                if (MyApp.getInstance().needToShowAd()) {
                    MyApp.getInstance().showInterstitial(ActWAStatus.this, intent, false, -1, null);
                } else {
                    startActivity(intent);
                }
            }

        } else {
            try {
                Media m = media.get(position);
                Uri uri = null;
                if (m.getUri() != null) {
                    uri = m.getUri();

                } else if (m.getFile() != null) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(ActWAStatus.this, BuildConfig.APPLICATION_ID + ".provider", m.getFile());
                    } else {
                        uri = Uri.fromFile(m.getFile());
                    }
                }
                Intent res = new Intent();
                res.setData(uri);
                res.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setResult(RESULT_OK, res);
                finish();
            } catch (Exception e) {
                Log.e("TAG", "Error : " + e.getMessage());
            }
        }
    }

    @Override
    public void onMediaViewClick(Album album, ArrayList<Media> media, int position, ImageView imageView) {

        Intent intent = new Intent(getApplicationContext(), ActSingleMedia.class);
        intent.putExtra(ActSingleMedia.EXTRA_ARGS_ALBUM, album);
        ArrayList<Media> newMedia = null;
        if (media.size() > Constants.MAXIMUM_VIEW) {
            newMedia = Constants.getFilteredMedia(media, position);
        }
        try {
            intent.setAction(ActSingleMedia.ACTION_OPEN_ALBUM);
            intent.putExtra(ActSingleMedia.EXTRA_ARGS_MEDIA, newMedia != null ? newMedia : media);
            intent.putExtra(ActSingleMedia.EXTRA_ARGS_POSITION, position);
            intent.putExtra(ActSingleMedia.EXTRA_ARGS_IS_WA_STATUS, true);

            if (MyApp.getInstance().needToShowAd()) {
                MyApp.getInstance().showInterstitial(ActWAStatus.this, intent, false, -1, null);
            } else {
                startActivity(intent);
            }
        } catch (Exception e) { // Putting too much data into the Bundle
            // TODO: Find a better way to pass data between the activities - possibly a key to
            // access a HashMap or a unique value of a singleton Data Repository of some sort.
            intent.setAction(ActSingleMedia.ACTION_OPEN_ALBUM_LAZY);
            intent.putExtra(ActSingleMedia.EXTRA_ARGS_MEDIA, media.get(position));
            intent.putExtra(ActSingleMedia.EXTRA_ARGS_IS_WA_STATUS, true);
            startActivity(intent);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}