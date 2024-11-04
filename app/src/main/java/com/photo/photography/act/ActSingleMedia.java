package com.photo.photography.act;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.repeater.RepeaterMediaPager;
import com.photo.photography.collage.util.DialogUtil;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.AlbumSetting;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.MediaHelper;
import com.photo.photography.data_helper.filters_mode.MediaFilters;
import com.photo.photography.data_helper.provider.CPHelpers;
import com.photo.photography.data_helper.sorting.MediaComparator;
import com.photo.photography.frag.FragBaseMedia;
import com.photo.photography.frag.FragImage;
import com.photo.photography.frag.FragVideo;
import com.photo.photography.callbacks.CallbackRenameClick;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.AlertDialogHelper;
import com.photo.photography.util.MediaUtils;
import com.photo.photography.util.StringUtil;
import com.photo.photography.util.utils.CallbackOnDeleteProcess;
import com.photo.photography.util.utils.Util;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.ActVault;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.helper.Constants;
import com.photo.photography.secure_vault.helper.DbHandler;
import com.photo.photography.secure_vault.utils.CryptoExceptionUtils;
import com.photo.photography.secure_vault.utils.CryptoUtil;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.photo.photography.view.HackeyViewPager;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("ResourceAsColor")
public class ActSingleMedia extends ActSharedMedia implements FragBaseMedia.MediaTapListener {

    public static final String ACTION_OPEN_ALBUM = "com.photogallery.photography.intent.VIEW_ALBUM";
    public static final String ACTION_OPEN_ALBUM_LAZY = "com.photogallery.photography.intent.VIEW_ALBUM_LAZY";
    public static final String EXTRA_ARGS_ALBUM = "args_album";
    public static final String EXTRA_ARGS_MEDIA = "args_media";
    public static final String EXTRA_ARGS_POSITION = "args_position";
    public static final String EXTRA_ARGS_IS_WA_STATUS = "args_isWAStatus";
    public static final String DELETED_POS_LIST = "deletedPosList";
    private static final int SLIDE_SHOW_INTERVAL = 5000;
    private static final String ISLOCKED_ARG = "isLocked";

    @BindView(R.id.adContainerView)
    public FrameLayout adContainerView;
    @BindView(R.id.ivSoundOnOffActivity)
    public ImageButton ivSoundOnOffActivity;
    @BindView(R.id.ivDownloadWAStatus)
    ImageButton ivDownloadWAStatus;
    @BindView(R.id.linPlayVideo)
    LinearLayout linPlayVideo;
    @BindView(R.id.linBottomOptions)
    LinearLayout linBottomOptions;
    @BindView(R.id.photos_pager)
    HackeyViewPager mViewPager;
    @BindView(R.id.relToolbar)
    RelativeLayout relToolbar;
    @BindView(R.id.relNothingToShow)
    RelativeLayout relNothingToShow;
    @BindView(R.id.relParent)
    RelativeLayout relParent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.linDetails)
    LinearLayout linDetails;

    Handler handler = new Handler();
    Dialog textDialog;
    AlertDialog renameDialog;
    ArrayList<Media> removedPosList = new ArrayList<>();
    DbHandler db;
    private boolean fullScreenMode, customUri = false;
    private int position;
    private boolean isWAStatus;
    private Album album;
    Runnable slideShowRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % album.getCount());
            } catch (Exception e) {
                Log.e("TAG", "Error : " + e.getMessage());
                e.printStackTrace();
            } finally {
                handler.postDelayed(this, SLIDE_SHOW_INTERVAL);
            }
        }
    };
    private ArrayList<Media> media;
    private RepeaterMediaPager adapter;
    private boolean isSlideShowOn = false;

    public void displayViews(boolean displayMainView) {
        relParent.setVisibility(displayMainView ? View.VISIBLE : View.GONE);
        relNothingToShow.setVisibility(!displayMainView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_single_media);

        removedPosList = new ArrayList<>();
        ButterKnife.bind(this);
        showSystemBars();
        displayViews(true);

        Log.e("GAL_TAG", "statusBarHeight : " + Util.getStatusBarHeight(getResources()));
        relToolbar.setPaddingRelative(0, Util.getStatusBarHeight(getResources()), 0, 0);
        linDetails.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Remove Listener
                linDetails.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int navBarHeight = Util.getNavigationBarHeight(ActSingleMedia.this);
                Log.e("GAL_TAG", "adContainerViewHeight : " + adContainerView.getHeight());
                Log.e("GAL_TAG", "linDetailsHeight : " + linDetails.getHeight());
                Log.e("GAL_TAG", "navBarHeight : " + navBarHeight);
                adContainerView.setPaddingRelative(0, 0, 0, navBarHeight);
            }
        });

        // Init and Load Ads
        loadBannerAds(adContainerView);

        String action = getIntent().getAction();
        if (action != null) {
            switch (action) {
                case ACTION_OPEN_ALBUM:
                    loadAlbum(getIntent());
                    break;
                case ACTION_OPEN_ALBUM_LAZY:
                    loadAlbumsLazy(getIntent());
                    break;
                default:
                    loadUri(getIntent().getData());
                    break;
            }
        }

        if (savedInstanceState != null) {
            mViewPager.setLocked(savedInstanceState.getBoolean(ISLOCKED_ARG, false));
        }

        adapter = new RepeaterMediaPager(getSupportFragmentManager(), media);
        initUi();
        prepareSharedElementTransition();
        UserHelpers.updateReviewCount();
        if (UserHelpers.getReviewCount() == UserHelpers.getReviewPopupCount())
            showRate();
    }

    @OnClick(R.id.ivSoundOnOffActivity)
    public void soundClicked() {
        if (adapter != null && adapter.getItem(mViewPager.getCurrentItem()) instanceof FragVideo) {
            boolean isActivated = !ivSoundOnOffActivity.isActivated();
            Intent intent = new Intent();
            intent.setAction("fragment-" + mViewPager.getCurrentItem());
            intent.putExtra("volumeOn", isActivated);
            sendBroadcast(intent);
            Log.e("VOLUME_TRACE", "sendBroadcast for isActivated : " + isActivated + " on fragment-" + mViewPager.getCurrentItem());
        }
    }

    public void setVolumeControlView(boolean isActivated) {
        ivSoundOnOffActivity.setImageResource(isActivated ? R.drawable.icon_volume_on_24 : R.drawable.e_mute);
        ivSoundOnOffActivity.setActivated(isActivated);
    }

    @OnClick(R.id.linPlayVideo)
    public void playVideo() {
        try {
            if (getCurrentMedia() != null && getCurrentMedia().isVideo() && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
                Uri uriLocal;
                if (Build.VERSION.SDK_INT >= 24) {
//                    uriLocal = FileProvider.getUriForFile(SingleMediaActivity.this, getPackageName() + ".provider", getCurrentMedia().getFile());
                    uriLocal = FileProvider.getUriForFile(ActSingleMedia.this, BuildConfig.APPLICATION_ID + ".provider", getCurrentMedia().getFile());
                } else {

                    uriLocal = Uri.fromFile(getCurrentMedia().getFile());
                }
                if (uriLocal != null) {
                    Intent intent = new Intent(ActSingleMedia.this, ActPlayer.class);
                    intent.putExtra("FromVideo", uriLocal.toString());
                    intent.putExtra("openFromVault", false);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (MyApp.getInstance().needToShowAd()) {
                        MyApp.getInstance().showInterstitial(ActSingleMedia.this, intent, false, -1, null);
                    } else {
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
            }
        } catch (Exception exc) {
            Log.e("TAG", "PlayVideo Exception : " + exc.toString());
        }
    }

    @OnClick(R.id.ivDownloadWAStatus)
    public void downloadWAStatusClicked() {
        doNextTask();
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private void prepareSharedElementTransition() {
        TransitionSet transitionSet = new TransitionSet();
        Transition bound = new ChangeBounds();
        transitionSet.addTransition(bound);
        Transition changeImageTransform = new ChangeImageTransform();
        transitionSet.addTransition(changeImageTransform);
        transitionSet.setDuration(375);
        getWindow().setSharedElementEnterTransition(transitionSet);
        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                // Locate the image view at the primary fragment (the ImageFragment that is currently
                // visible). To locate the fragment, call instantiateItem with the selection position.
                // At this stage, the method will simply return the fragment at the position and will
                // not create a new one.
                Fragment currentFragment = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, position);
                View view = currentFragment.getView();
                if (view == null) {
                    return;
                }
                sharedElements.put(names.get(0), view.findViewById(R.id.imageView));
            }
        });
    }

    private void doNextTask() {
        if (isWAStatus && getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            String selectedFile = getCurrentMedia().getPath();
            SupportClass.copyFile(ActSingleMedia.this, selectedFile.substring(0, selectedFile.lastIndexOf("/")),
                    selectedFile.substring(selectedFile.lastIndexOf("/") + 1), new VaultFileUtil(ActSingleMedia.this).getFile1("").getAbsolutePath()
            );
            ActMain.IS_NEW_STATUS_DOWNLOAD = true;
            Toast.makeText(this, "Status downloaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAlbum(Intent intent) {
        album = intent.getParcelableExtra(EXTRA_ARGS_ALBUM);
        position = intent.getIntExtra(EXTRA_ARGS_POSITION, 0);
        isWAStatus = intent.getBooleanExtra(EXTRA_ARGS_IS_WA_STATUS, false);
        media = intent.getParcelableArrayListExtra(EXTRA_ARGS_MEDIA);
    }

    private void loadAlbumsLazy(Intent intent) {
        album = intent.getParcelableExtra(EXTRA_ARGS_ALBUM);
        //position = intent.getIntExtra(EXTRA_ARGS_POSITION, 0);
        isWAStatus = intent.getBooleanExtra(EXTRA_ARGS_IS_WA_STATUS, false);
        Media m = intent.getParcelableExtra(EXTRA_ARGS_MEDIA);
        media = new ArrayList<>();
        media.add(m);
        position = 0;

        ArrayList<Media> list = new ArrayList<>();

        Disposable disposable = CPHelpers.getMedia(getApplicationContext(), album)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(media -> MediaFilters.getFilter(album.filterMode()).accept(media) && !media.equals(m))
                .subscribe(ma -> {
                            int i = Collections.binarySearch(list, ma, MediaComparator.getComparator(album.settings));
                            if (i < 0) i = ~i;
                            list.add(i, ma);
                        },
                        throwable -> {
                            Log.wtf("asd", throwable);
                        },
                        () -> {
                            int i = Collections.binarySearch(
                                    list, m, MediaComparator.getComparator(album.settings));
                            if (i < 0) i = ~i;

                            list.add(i, m);
                            media.clear();
                            media.addAll(list);
                            adapter.notifyDataSetChanged();
                            position = i;
                            mViewPager.setCurrentItem(position);

                            updatePageTitle(position);
                        }
                );

        disposeLater(disposable);
    }

    private void loadUri(Uri uri) {
        try {
            album = new Album(uri.toString(), uri.getPath());
            album.settings = AlbumSetting.getDefaults();

            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) inputStream.close();
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            ((TextView) findViewById(R.id.nothing_to_show_text_emoji_master)).setText(getString(R.string.error_occured_open_media));
            displayViews(false);
        }

        media = new ArrayList<>(Collections.singletonList(new Media(uri)));
        position = 0;
        customUri = true;
    }

    @Override
    public void onBackPressed() {
        if (removedPosList.size() > 0) {
            Intent intent = new Intent();
            intent.putExtra(DELETED_POS_LIST, removedPosList);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void initUi() {
        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(ActSingleMedia.this, R.color.whiteOnly));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_fadein);
        mAnimation.setDuration(800);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        toolbar.setAnimation(mAnimation);
        linBottomOptions.setAnimation(mAnimation);

        updatePageTitle(position);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ActSingleMedia.this.position = position;
                updatePageTitle(position);

                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation() == Surface.ROTATION_90) {
            Configuration configuration = new Configuration();
            configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;
            onConfigurationChanged(configuration);
        }
    }

    @Override
    public void onViewTapped() {
        toggleSystemUI();
    }

    private void updatePageTitle(int position) {
        getSupportActionBar().setTitle(getString(R.string.of, position + 1, adapter.getCount()));
        if (isWAStatus) {
            ivDownloadWAStatus.setVisibility(View.VISIBLE);
            ivDownloadWAStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.e_download));
            linBottomOptions.setVisibility(View.GONE);
            if (position >= 0 && media.size() > position && media.get(position).isImage()) {
                linPlayVideo.setVisibility(View.GONE);
                showVolBtn(View.GONE);
            } else {
                linPlayVideo.setVisibility(fullScreenMode ? View.GONE : View.VISIBLE);
                showVolBtn(fullScreenMode ? View.GONE : View.VISIBLE);
            }
        } else {
            ivDownloadWAStatus.setVisibility(View.GONE);
            linBottomOptions.setVisibility(fullScreenMode ? View.GONE : View.VISIBLE);
            if (position >= 0 && media.size() > position && media.get(position).isImage()) {
                findViewById(R.id.linEdit).setVisibility(View.VISIBLE);
                findViewById(R.id.linVideoRotation).setVisibility(View.GONE);
                linPlayVideo.setVisibility(View.GONE);
                showVolBtn(View.GONE);
            } else {
                findViewById(R.id.linEdit).setVisibility(View.GONE);
                findViewById(R.id.linVideoRotation).setVisibility(View.VISIBLE);
                linPlayVideo.setVisibility(fullScreenMode ? View.GONE : View.VISIBLE);
                showVolBtn(fullScreenMode ? View.GONE : View.VISIBLE);
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getApplicationContext()).clearMemory();
        Glide.get(getApplicationContext()).trimMemory(TRIM_MEMORY_COMPLETE);
        System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isSlideShowOn) {
            getMenuInflater().inflate(R.menu.menu_view_page_slide_on, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_view_pager, menu);
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        relToolbar.setPaddingRelative(0, Util.getStatusBarHeight(getResources()), 0, 0);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        if (!isSlideShowOn && getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            Log.e("IS_IMAGE", getCurrentMedia().isImage() + " Option");
            boolean isImage;
            if (getCurrentMedia()!=null && getCurrentMedia().getMimeType()!=null) {
                isImage = getCurrentMedia().getMimeType().equalsIgnoreCase("image/jpeg")
                        || getCurrentMedia().getMimeType().equalsIgnoreCase("image/png");
                menu.setGroupVisible(R.id.only_photos_options, isImage);
            }
            if (customUri) {
                menu.setGroupVisible(R.id.on_internal_storage, false);
                menu.setGroupVisible(R.id.only_photos_options, false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayAlbums() {
        startActivity(new Intent(getApplicationContext(), ActMain.class));
        finish();
    }

    private void rotateImage(int rotationDegree) {
        Fragment mediaFragment = adapter.getRegisteredFragment(position);
        if (!(mediaFragment instanceof FragImage))
            throw new RuntimeException("Trying to rotate a wrong media type!");

        try {
            ((FragImage) mediaFragment).rotatePicture(rotationDegree);
        }catch (Exception e){

        }
    }

    @OnClick(R.id.linEdit)
    public void clickedOnEdit() {
        if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            Intent i = new Intent(ActSingleMedia.this, ActEditMedia.class);
            i.putExtra("selectedImagePath", getCurrentMedia().getPath());
            if (MyApp.getInstance().needToShowAd()) {
                MyApp.getInstance().showInterstitial(ActSingleMedia.this, i, false, -1, null);
            } else {
                startActivity(i);
            }
        }
    }

    @OnClick(R.id.linVideoRotation)
    public void clickedOnVideoRotation() {
        int rotation = (((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()).getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @OnClick(R.id.linShare)
    public void clickedOnShare() {
        doShareTask();
    }

    @OnClick(R.id.linDelete)
    public void clickedOnDelete() {
        doDeleteTask();
    }

    @OnClick(R.id.linMoveInVault)
    public void clickedOnMoveInVault() {
        if (!SupportClass.checkVaultSetup(ActSingleMedia.this)) {
            Log.e("TAG", "Vault not setupped.");
        } else if (!SupportClass.isExternalStoragePermissionGranted(ActSingleMedia.this)) {
            SupportClass.userAction = SupportClass.USER_ACTION.MOVE_TO_VAULT;
            SupportClass.showTakeWritePermissionDialog(ActSingleMedia.this);
        } else {
            moveInVault();
        }
    }

    @OnClick(R.id.linDetails)
    public void clickedOnDetails() {
        if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            final AlertDialog detailsDialog = AlertDialogHelper.getDetailsDialog(this, getCurrentMedia());
            detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            detailsDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment currentFragment;
        switch (item.getItemId()) {
            case R.id.e_rotate_180:
                rotateImage(180);
                break;

            case R.id.e_rotate_right_90:
                rotateImage(90);
                break;

            case R.id.e_rotate_left_90:
                rotateImage(-90);
                break;

            case R.id.action_use_as:
                try {
                    if (getCurrentMedia().getFile() != null) {
                        Uri uri = null;
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(ActSingleMedia.this, BuildConfig.APPLICATION_ID + ".provider", getCurrentMedia().getFile());
                        } else {
                            uri = Uri.fromFile(getCurrentMedia().getFile());
                        }
                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                        intent.setDataAndType(uri, getCurrentMedia().getMimeType());
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, getString(R.string.use_as)));
                    }
                } catch (Exception e) {
                    Log.e("TAG", "Error : " + e.getMessage());
                }
                return true;

            case R.id.action_rename:
                if (!SupportClass.isExternalStoragePermissionGranted(ActSingleMedia.this)) {
                    SupportClass.userAction = SupportClass.USER_ACTION.RENAME;
                    SupportClass.showTakeWritePermissionDialog(ActSingleMedia.this);
                } else {
                    renameFile();
                }
                break;

            case R.id.action_settings:
                ActSettings.startActivity(this);
                break;

            case R.id.action_palette:
                try {
                    if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
                        Uri uri = null;
                        if (getCurrentMedia().getUri() != null) {
                            uri = getCurrentMedia().getUri();

                        } else if (getCurrentMedia().getFile() != null) {
                            if (Build.VERSION.SDK_INT >= 24) {
                                uri = FileProvider.getUriForFile(ActSingleMedia.this, BuildConfig.APPLICATION_ID + ".provider", getCurrentMedia().getFile());
                            } else {
                                uri = Uri.fromFile(getCurrentMedia().getFile());
                            }
                        }

                        Intent paletteIntent = new Intent(ActSingleMedia.this, ActPalette.class);
                        paletteIntent.setData(uri);
                        if (MyApp.getInstance().needToShowAd()) {
                            MyApp.getInstance().showInterstitial(ActSingleMedia.this, paletteIntent, false, -1, null);
                        } else {
                            startActivity(paletteIntent);
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.something_went_wrong_please_try_again), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("TAG", "Error : " + e.getMessage());
                }
                break;

            case R.id.action_print:
                if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
                    PrintHelper photoPrinter = new PrintHelper(this);
                    photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                    try (InputStream in = getContentResolver().openInputStream(getCurrentMedia().getUri())) {
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        photoPrinter.printBitmap(String.format("print_%s", getCurrentMedia().getDisplayPath()), bitmap);
                    } catch (Exception e) {
                        Log.e("TAG", "Error : " + e.getMessage());
                        Log.e("print", String.format("unable to print %s", getCurrentMedia().getUri()), e);
                        Toast.makeText(getApplicationContext(), R.string.print_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.print_error, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.slide_show:
                isSlideShowOn = !isSlideShowOn;
                if (isSlideShowOn) {
                    handler.postDelayed(slideShowRunnable, SLIDE_SHOW_INTERVAL);
                    hideSystemUI();
                } else handler.removeCallbacks(slideShowRunnable);
                supportInvalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveInVault() {
        if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            ArrayList<Media> mediaArrayList = new ArrayList<>();
            mediaArrayList.add(getCurrentMedia());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaUtils.moveInVaultMediaNew(ActSingleMedia.this, mediaArrayList, new CallbackOnDeleteProcess() {
                    @Override
                    public void onDeleteComplete() {
                        adapter.notifyDataSetChanged();
                        updatePageTitle(mViewPager.getCurrentItem());
                    }

                    @Override
                    public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media mediaDeleted) {
                        doSingleDeleteListen(isSuccess, mediaDeleted);
                    }
                });
            } else {
                Intent intent = new Intent();
                intent.putExtra(ActVault.PICKED_MEDIA_LIST, mediaArrayList);
                new EncryptFile(-1, -1).execute(intent);
            }
        }
    }

    private void doDeleteTask() {
        textDialog = DialogUtil.getDialogObject(ActSingleMedia.this, R.layout.dialog_texts);

        if(textDialog == null){
            Toast.makeText(this, R.string.something_went_wrong_please_try_again, Toast.LENGTH_SHORT).show();
            return;
        }
        TextView dialogTitle = textDialog.findViewById(R.id.text_dialog_title);
        TextView dialogMessage = textDialog.findViewById(R.id.text_dialog_message);
        dialogTitle.setText(getString(R.string.delete));
        dialogMessage.setText(getString(R.string.delete_photo_message));

        TextView text_delete = textDialog.findViewById(R.id.text_delete);
        TextView text_cancel = textDialog.findViewById(R.id.text_cancel);
        text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
                if (!SupportClass.isExternalStoragePermissionGranted(ActSingleMedia.this)) {
                    SupportClass.userAction = SupportClass.USER_ACTION.DELETE;
                    SupportClass.showTakeWritePermissionDialog(ActSingleMedia.this);
                } else {
                    startDeleteFile();
                }
            }
        });

        text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
            }
        });
        textDialog.show();
    }

    private void startDeleteFile() {
        if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            ArrayList<Media> mediaArrayList = new ArrayList<>();
            mediaArrayList.add(getCurrentMedia());
            MediaUtils.deleteMediaNew(this, mediaArrayList, new CallbackOnDeleteProcess() {
                @Override
                public void onDeleteComplete() {
                    adapter.notifyDataSetChanged();
                    updatePageTitle(mViewPager.getCurrentItem());
                }

                @Override
                public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media mediaDeleted) {
                    doSingleDeleteListen(isSuccess, mediaDeleted);
                }
            });
        }
    }

    private void doShareTask() {
        if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            try {
                ArrayList<Media> arrList = new ArrayList<>();
                arrList.add(getCurrentMedia());
                MediaUtils.shareMedia(ActSingleMedia.this, arrList);
            } catch (Exception e) {
                Log.e("TAG", "Error : " + e.getMessage());
            }
        }
    }

    public Media getCurrentMedia() {
        return (media.size() > position && position != -1) ? media.get(position) : null;
    }

    private void renameFile() {
        if (renameDialog != null && renameDialog.isShowing()) {
            renameDialog.dismiss();
        }
        if (getCurrentMedia() != null && getCurrentMedia().getFile() != null && getCurrentMedia().getFile().exists()) {
            final EditText editTextNewName = new EditText(this);
            editTextNewName.setText(StringUtil.getPhotoNameByPath(getCurrentMedia().getPath()));
            renameDialog = AlertDialogHelper.getInsertTextDialog(this, editTextNewName, R.string.rename_photo_action, new CallbackRenameClick() {
                @Override
                public void onOkClick(View v) {
                    if (!SupportClass.isExternalStoragePermissionGranted(ActSingleMedia.this)) {
                        SupportClass.userAction = SupportClass.USER_ACTION.RENAME;
                        SupportClass.showTakeWritePermissionDialog(ActSingleMedia.this);
                    } else {
                        renameDialog.dismiss();
                        if (editTextNewName.length() != 0) {
                            Media currentMedia = getCurrentMedia();
                            boolean b = MediaHelper.renameMedia(getApplicationContext(), currentMedia, editTextNewName.getText().toString());
                            if (!b) {
                                StringUtil.showToast(getApplicationContext(), getString(R.string.rename_error));
                                //adapter.notifyDataSetChanged();
                            }
                        } else
                            StringUtil.showToast(getApplicationContext(), getString(R.string.nothing_changed));
                    }
                }

                @Override
                public void onCancelClick(View v) {
                    renameDialog.dismiss();
                }
            });
            renameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            renameDialog.show();
        } else {
            Toast.makeText(this, "Sorry, Files not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 590) {
            if (SupportClass.isExternalStoragePermissionGranted(ActSingleMedia.this)) {
                doUserAction();
            } else {
                Toast.makeText(ActSingleMedia.this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }

        } else {
            boolean DeleteORMoveVault = requestCode == SupportClass.ANDROID_Q_DELETE_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_DELETE_REQUEST_CODE ||
                    requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE;
            if (DeleteORMoveVault) {
                onSuccessGive11Permission(requestCode, resultCode);
            }
        }
    }

    public void onSuccessGive11Permission(int requestCode, int resultCode) {
        deletePendingMedia(requestCode, resultCode);
    }

    public void deletePendingMedia(int requestCode, int resultCode) {
        if (requestCode == SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE || requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE) {
            if (requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE) {
                ArrayList<Media> arrayList = new ArrayList<>();
                arrayList.add(MediaUtils.getPendingMediaList().get(0));
                Intent intent = new Intent();
                intent.putExtra(ActVault.PICKED_MEDIA_LIST, arrayList);
                new EncryptFile(requestCode, resultCode).execute(intent);

            } else {
                ArrayList<Media> mediaArrayList = new ArrayList<>();
                mediaArrayList.add(getCurrentMedia());
                Intent intent = new Intent();
                intent.putExtra(ActVault.PICKED_MEDIA_LIST, mediaArrayList);
                new EncryptFile(requestCode, resultCode).execute(intent);
            }

        } else {
            MediaUtils.deletePendingMedia(ActSingleMedia.this, requestCode, resultCode, new CallbackOnDeleteProcess() {
                @Override
                public void onDeleteComplete() {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        updatePageTitle(mViewPager.getCurrentItem());
                    }
                }

                @Override
                public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media mediaDeleted) {
                    doSingleDeleteListen(isSuccess, mediaDeleted);
                }
            });
        }
    }

    private void doUserAction() {
        if (SupportClass.userAction == SupportClass.USER_ACTION.DELETE) {
            startDeleteFile();
        } else if (SupportClass.userAction == SupportClass.USER_ACTION.MOVE_TO_VAULT) {
            moveInVault();
        }
        SupportClass.userAction = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 591) {// permission
            if (SupportClass.isExternalStoragePermissionGranted(ActSingleMedia.this)) {
                doUserAction();
            } else {
                Toast.makeText(ActSingleMedia.this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mViewPager != null) {
            outState.putBoolean(ISLOCKED_ARG, mViewPager.isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    public void toggleSystemUI() {
        if (fullScreenMode) showSystemUI();
        else hideSystemUI();
    }


    private void showSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        int orientation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        if (orientation == 1) {
            // Hide both the status bar and the navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
            return;
        }
        // Hide both the status bar and the navigation bar
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars());
    }

    public void hideSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                hideSystemBars();
                relToolbar.animate().translationY(-relToolbar.getHeight()).setInterpolator(new AccelerateInterpolator()).setDuration(200).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        relToolbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationStart(Animator animation, boolean isReverse) {
                        Animator.AnimatorListener.super.onAnimationStart(animation, isReverse);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
                if (getCurrentMedia() != null && getCurrentMedia().isVideo()) {
                    linPlayVideo.animate().translationY(linPlayVideo.getHeight() + Util.px2dp(getResources(), R.dimen.margin_70)).setInterpolator(new DecelerateInterpolator()).setDuration(200).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            linPlayVideo.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                    ivSoundOnOffActivity.animate().translationY(ivSoundOnOffActivity.getHeight() + Util.px2dp(getResources(), R.dimen.margin_70)).setInterpolator(new DecelerateInterpolator()).setDuration(200).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            showVolBtn(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                }
                float value = findViewById(R.id.adContainerView).getHeight() + linDetails.getHeight() + Util.getNavigationBarHeight(ActSingleMedia.this);
                Log.e("GAL_TAG", "" + value);
                adContainerView.animate().translationY(value).setInterpolator(new DecelerateInterpolator()).setDuration(400).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        adContainerView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                }).start();
                if (isWAStatus) {
                    ivDownloadWAStatus.animate().translationY(ivDownloadWAStatus.getHeight() + Util.px2dp(getResources(), R.dimen.margin_70)).setInterpolator(new DecelerateInterpolator()).setDuration(200).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            ivDownloadWAStatus.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                } else {
                    linBottomOptions.animate().translationY(value).setInterpolator(new DecelerateInterpolator()).setDuration(400).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            linBottomOptions.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                }
                fullScreenMode = true;
            }
        });
    }

    public void showSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                showSystemBars();
                relToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).setDuration(240).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        relToolbar.setVisibility(View.VISIBLE);
                        if (isWAStatus) {
                            ivDownloadWAStatus.setVisibility(View.VISIBLE);
                            ivDownloadWAStatus.setImageDrawable(ContextCompat.getDrawable(ActSingleMedia.this, R.drawable.e_download));
                            linBottomOptions.setVisibility(View.GONE);
                            if (position >= 0 && media.size() > position && media.get(position).isImage()) {
                                linPlayVideo.setVisibility(View.GONE);
                                showVolBtn(View.GONE);
                            } else {
                                linPlayVideo.setVisibility(fullScreenMode ? View.GONE : View.VISIBLE);
                                showVolBtn(fullScreenMode ? View.GONE : View.VISIBLE);
                            }
                        } else {
                            ivDownloadWAStatus.setVisibility(View.GONE);
                            linBottomOptions.setVisibility(View.VISIBLE);
                            if (position >= 0 && media.size() > position && getCurrentMedia() != null && getCurrentMedia().isImage()) {
                                findViewById(R.id.linEdit).setVisibility(View.VISIBLE);
                                findViewById(R.id.linVideoRotation).setVisibility(View.GONE);
                                linPlayVideo.setVisibility(View.GONE);
                                showVolBtn(View.GONE);
                            } else {
                                findViewById(R.id.linEdit).setVisibility(View.GONE);
                                findViewById(R.id.linVideoRotation).setVisibility(View.VISIBLE);
                                linPlayVideo.setVisibility(View.VISIBLE);
                                showVolBtn(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                }).start();
                if (getCurrentMedia() != null && getCurrentMedia().isVideo()) {
                    linPlayVideo.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).setDuration(240).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            linPlayVideo.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                    ivSoundOnOffActivity.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).setDuration(240).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            showVolBtn(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                }
                adContainerView.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).setDuration(200).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        adContainerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                }).start();
                if (isWAStatus) {
                    ivDownloadWAStatus.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).setDuration(240).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            ivDownloadWAStatus.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                } else {
                    linBottomOptions.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).setDuration(200).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            linBottomOptions.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    }).start();
                }
                fullScreenMode = false;
            }
        });
    }

    public boolean isBuildVersionValidForPlay() {
        Log.e("isBuildVersion", "ValidForPlay : " + (Build.VERSION.SDK_INT >= 26));
        return false/*Build.VERSION.SDK_INT >= 26*/;
    }

    public void showVolBtn(int visibility) {
        if (isBuildVersionValidForPlay()) {
            ivSoundOnOffActivity.setVisibility(View.VISIBLE);
            ivSoundOnOffActivity.setVisibility(visibility);
        } else {
            ivSoundOnOffActivity.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(slideShowRunnable);
        handler = null;
    }

    private void onCompleteEncryption() {
        adapter.notifyDataSetChanged();
        updatePageTitle(mViewPager.getCurrentItem());
        Toast.makeText(ActSingleMedia.this, "Successfully file Added in\nYour Private Vault.", Toast.LENGTH_SHORT).show();
    }

    private String SaveThumbnail(String fileMain, String fileName, String fileType) {
        int THUMBSIZE = getResources().getDisplayMetrics().widthPixels;
        Bitmap finalBitmap;
        if (fileType.equals(Constants.FILE_TYPE_IMAGE)) {
            finalBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileMain), THUMBSIZE, THUMBSIZE);
        } else {
            finalBitmap = ThumbnailUtils.createVideoThumbnail(fileMain, MediaStore.Video.Thumbnails.MINI_KIND);
        }
        File myDirSnaplock = new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATEVAULT);
        File myDirSupport = new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_SUPPORT);
        if (!myDirSnaplock.exists()) {
            myDirSnaplock.mkdir();
        }
        if (!myDirSupport.exists()) {
            myDirSupport.mkdir();
        }
        File file = new File(myDirSupport, fileName + ".jpg");
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);

            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            out.flush();
            out.close();
            Log.e("TAG", "Thumbnail successfully Saved" + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void encryptImagesCallBroadCast(String filename) {
//        Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Images.Media._ID};

//        Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{new File(filename).getAbsolutePath()};

//        Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
//        File not found in media store DB
        }
        c.close();
    }

    private void doSingleDeleteListen(boolean isSuccess, Media mediaDeleted) {
        if (isSuccess && adapter != null) {
            removedPosList.add(mediaDeleted);
            media.remove(mediaDeleted);
            if (media.size() == 0) {
                displayAlbums();
            }
        }
    }

    class EncryptFile extends AsyncTask<Intent, String, String> {
        int requestCode, resultCode;
        private ProgressDialog pDialog;

        public EncryptFile(int requestCode, int resultCode) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActSingleMedia.this);
            pDialog.setMessage("Processing file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Intent... data) {
            if (resultCode == Activity.RESULT_OK) {
                db = new DbHandler(ActSingleMedia.this);

                if (!(new File(getExternalFilesDir(null), "support")).exists()) {
                    new File(getExternalFilesDir(null), "support").mkdirs();
                }
                String privatevaultFolder = new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).getAbsolutePath();
                if (!(new File(privatevaultFolder)).exists()) {
                    new File(privatevaultFolder).mkdirs();
                }

                ArrayList<Media> imageList = data[0].getParcelableArrayListExtra(ActVault.PICKED_MEDIA_LIST);
                if (imageList.size() > 0) {
                    for (int i = 0; i < imageList.size(); i++) {
                        String oldFilePath = imageList.get(i).getPath();
                        if (!TextUtils.isEmpty(oldFilePath)) {
                            String oldFileName = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
                            String currentTimeInMillis = String.valueOf(System.currentTimeMillis() + i);
                            String newFilePath = new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).getAbsolutePath() + currentTimeInMillis;
                            String newFileName = currentTimeInMillis;
                            String oldThumbnailPath = SaveThumbnail(oldFilePath, currentTimeInMillis, imageList.get(i).isImage() ? Constants.FILE_TYPE_IMAGE : Constants.FILE_TYPE_VIDEO);
                            Log.e("TAG", "Old Thumbnail Path : " + oldThumbnailPath);
                            String newThumbnailPath = new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).getAbsolutePath() + currentTimeInMillis;
                            try {
                                if (!new File(getExternalFilesDir(null), "support").exists()) {
                                    new File(getExternalFilesDir(null), "support").mkdir();
                                }
                                if (!new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).exists()) {
                                    new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).mkdir();
                                }
                                if (!new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).exists()) {
                                    new VaultFileUtil(ActSingleMedia.this).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).mkdir();
                                }

                                Log.e("TAG", "Encrypt file : " + oldFilePath + " to\n" + newFilePath);
                                CryptoUtil.encrypt(ActSingleMedia.this, new File(oldFilePath), new File(newFilePath));
                                CryptoUtil.encrypt(ActSingleMedia.this, new File(oldThumbnailPath), new File(newThumbnailPath));
                                int p = db.addRecord(new VaultFile(oldFilePath, oldFileName, newFilePath, newFileName,
                                        imageList.get(i).isImage() ? Constants.FILE_TYPE_IMAGE : Constants.FILE_TYPE_VIDEO, newThumbnailPath, false));
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                    new File(oldThumbnailPath).delete();
                                    new File(oldFilePath).delete();
                                    encryptImagesCallBroadCast(oldFilePath);
                                }
                                Log.e("TAG", "if complete without error");
                            } catch (CryptoExceptionUtils e) {
                                Log.e("TAG", "Error in Encryption of file : " + e.toString());
                            }
                        }
                    }

                    ArrayList<VaultFile> vaultData = db.getAllRecords();
                    if (vaultData.size() > 0) {
                        for (int i = 0; i < vaultData.size(); i++) {
                            int pro_id = vaultData.get(i).getID();
                            String oldFilePath = vaultData.get(i).getOldPath();
                            String oldFileName = vaultData.get(i).getOldFileName();
                            String newFilePath = vaultData.get(i).getNewPath();
                            String newFileName = vaultData.get(i).getNewFileName();
                            String type = vaultData.get(i).getType();
                            String thumbnail = vaultData.get(i).getThumbnail();
                            Log.e("TAG", "Data Selected Record =>  Id-" + pro_id + ",\noldFilePath-" + oldFilePath + ",\noldFileName-" + oldFileName + ",\nnewFilePath-" + newFilePath + ",\nnewFileName-" + newFileName + ",\nType-" + type + ",\nThumbnail-" + thumbnail);
                        }
                    } else {
                        Log.e("TAG", "Data not Available in table");
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pDialog.dismiss();
            if (resultCode == Activity.RESULT_OK && requestCode == -1) {
                onCompleteEncryption();

            } else {
                MediaUtils.deletePendingMedia(ActSingleMedia.this, requestCode, resultCode, new CallbackOnDeleteProcess() {
                    @Override
                    public void onDeleteComplete() {
                        if (requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE) {
                            if (MediaUtils.getPendingMediaList().size() == 0) {
                                onCompleteEncryption();
                            }
                        } else {
                            onCompleteEncryption();
                        }
                    }

                    @Override
                    public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media mediaDeleted) {
                        doSingleDeleteListen(isSuccess, mediaDeleted);
                    }
                });
            }
        }
    }
}

