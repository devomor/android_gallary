package com.photo.photography.act;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.photo.photography.BuildConfig;
import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.callbacks.CallbackMediaClick;
import com.photo.photography.collage.screen.ActSelectPhoto;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.Media;
import com.photo.photography.db.DatabasesManager;
import com.photo.photography.duplicatephotos.AppDataBaseHandler;
import com.photo.photography.duplicatephotos.act.ActDuplicateHomeMain;
import com.photo.photography.duplicatephotos.act.ActPhotoList;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.frag.EditModeListener;
import com.photo.photography.frag.FragRvAllMedia;
import com.photo.photography.frag.FragRvLocationMedia;
import com.photo.photography.frag.FragRvMediaNew;
import com.photo.photography.frag.NothingToShowCallback;
import com.photo.photography.secure_vault.ActSetupPinLock;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.preferences.Prefs;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActMain extends ActSharedMedia implements CallbackMediaClick, NothingToShowCallback, EditModeListener {

    public static final String EXTRA_IS_FRAME_IMAGE = "frameImage";
    public static final String ARGS_PICK_MODE = "pick_mode";
    public static final int REQUEST_CODE_WA_STATUS = 407;
    public static final int REQUEST_CODE_SECURE_VAULT = 406;
    private static final String TAG2 = "AppUpdate";
    private static final int RC_APP_UPDATE = 11;
    private static final int REQUEST_CODE_VIEW_MEDIA = 140;
    public static boolean IS_VAULT_CHANGED = false;
    public static boolean IS_NEW_STATUS_DOWNLOAD = false;
    @BindView(R.id.drawer_layout)
    DrawerLayout navigationDrawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.switchTheme)
    SwitchCompat switchTheme;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    boolean doubleBackToExitPressedOnce = false;
    //    FirebaseAnalytics mFirebaseAnalytics;
    private boolean pickMode = false;
    private Unbinder unbinder;
    private AppUpdateManager mAppUpdateManager;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("AppStartFrom", "MainActivity OnCreate");
        if (AppCompatDelegate.MODE_NIGHT_NO == Prefs.getTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (AppCompatDelegate.MODE_NIGHT_YES == Prefs.getTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setContentView(R.layout.act_main);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        UserHelpers.setUserInSplashIntro(false);
        // rateDialogInitialize();

        // Init and Load Ads
        /*loadBannerAds(findViewById(R.id.adContainerView));*/

        unbinder = ButterKnife.bind(this);
        if (!DatabasesManager.getInstance(this).isDbFileExisted()) {
            DatabasesManager.getInstance(this).createDb();
        } else {
            boolean isOpen = DatabasesManager.getInstance(this).openDb();
            //ALog.d("MainActivity", "onCreate, database isOpen=" + isOpen);
        }
        if (!DatabasesManager.getInstance(this).isDbFileExisted()) {
            DatabasesManager.getInstance(this).createDb();
        } else {
            boolean isOpen = DatabasesManager.getInstance(this).openDb();
            //ALog.d("MainActivity", "onCreate, database isOpen=" + isOpen);
        }
        initUi();
        inAppUpdate();
        pickMode = getIntent().getBooleanExtra(ARGS_PICK_MODE, false);

        switchTheme.setChecked(AppCompatDelegate.MODE_NIGHT_YES == Prefs.getTheme());
        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (AppCompatDelegate.MODE_NIGHT_YES == Prefs.getTheme()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(com.photo.photography.secure_vault.helper.Constants.theme, com.photo.photography.secure_vault.helper.Constants.white);

                    Prefs.setTheme(AppCompatDelegate.MODE_NIGHT_NO);
                    GlobalVarsAndFunction.setAppTheme(ActMain.this, AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                } else if (AppCompatDelegate.MODE_NIGHT_NO == Prefs.getTheme()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(com.photo.photography.secure_vault.helper.Constants.theme, com.photo.photography.secure_vault.helper.Constants.black);

                    Prefs.setTheme(AppCompatDelegate.MODE_NIGHT_YES);
                    GlobalVarsAndFunction.setAppTheme(ActMain.this, AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                restartScreenApp();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                navigationDrawer.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.navigation_item_all_media:
                        displayMedia();
                        return true;

                    case R.id.navigation_item_secure_vault:
                        Bundle bundle = new Bundle();
                        bundle.putString(com.photo.photography.secure_vault.helper.Constants.vaultOpen, com.photo.photography.secure_vault.helper.Constants.vaultOpen);

                        startActivityForResult(new Intent(ActMain.this, ActSetupPinLock.class), REQUEST_CODE_SECURE_VAULT);
                        return true;

                    case R.id.navigation_item_duplicate_photo:
                        if (SupportClass.isExternalStoragePermissionGranted(ActMain.this)) {
                            Bundle bundle2 = new Bundle();
                            bundle2.putString(com.photo.photography.secure_vault.helper.Constants.DuplicatePhotosOpen, com.photo.photography.secure_vault.helper.Constants.DuplicatePhotosOpen);

                            AppDataBaseHandler dbHandler = new AppDataBaseHandler(ActMain.this);
                            List<IndividualGroups> sDataList = dbHandler.getAllItems(true);
                            List<IndividualGroups> eDataList = dbHandler.getAllItems(false);
                            if (eDataList.size() > 0 || sDataList.size() > 0) {

                                GlobalVarsAndFunction.setCancelFlag(ActMain.this, false);

                                long jjj = GlobalVarsAndFunction.getTotalDuplicateMemoryRegain(eDataList);
                                GlobalVarsAndFunction.setMemoryRegainedExact(GlobalVarsAndFunction.getStringSizeLengthFile(jjj));
                                GlobalVarsAndFunction.setMemoryRegainedExactInLong(jjj);
                                GlobalVarsAndFunction.setTotalDuplicatesExact(GlobalVarsAndFunction.getTotalDuplicate(eDataList));

                                long kkk = GlobalVarsAndFunction.getTotalDuplicateMemoryRegain(sDataList);
                                GlobalVarsAndFunction.setMemoryRegainedSimilar(GlobalVarsAndFunction.getStringSizeLengthFile(kkk));
                                GlobalVarsAndFunction.setMemoryRegainedSimilarInLong(kkk);
                                GlobalVarsAndFunction.setTotalDuplicatesSimilar(GlobalVarsAndFunction.getTotalDuplicate(sDataList));

                                GlobalVarsAndFunction.setGroupOfDuplicatesSimilar(ActMain.this, sDataList, false);
                                GlobalVarsAndFunction.setGroupOfDuplicatesExact(ActMain.this, eDataList, false);

                                Intent intent = new Intent(ActMain.this, ActPhotoList.class);
                                intent.putExtra("memoryPopUpAndRecoverPopUp", "showMemoryPopUp");
                                intent.putExtra("tS", "exact");
                                intent.putExtra("showSimilarRegainedPopUpExact", false);
                                intent.putExtra("lastFromScan", true);
                                intent.putExtra("showSimilarRegainedPopUpSimilar", false);

                                if (MyApp.getInstance().needToShowAd()) {
                                    MyApp.getInstance().showInterstitial(ActMain.this, intent, false, -1, null);
                                } else {
                                    startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(ActMain.this, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left).toBundle());
                                }
                            } else {
                                startActivityForResult(new Intent(ActMain.this, ActDuplicateHomeMain.class), REQUEST_CODE_SECURE_VAULT);
                            }
                        } else {
                            SupportClass.showTakeWritePermissionDialog(ActMain.this);
                        }
                        return true;

                    case R.id.navigation_item_collage:
                        Bundle bundle3 = new Bundle();
                        bundle3.putString(com.photo.photography.secure_vault.helper.Constants.CollageOpen, com.photo.photography.secure_vault.helper.Constants.CollageOpen);

                        Intent intent = new Intent(ActMain.this, ActSelectPhoto.class);
                        intent.putExtra(EXTRA_IS_FRAME_IMAGE, true);
                        if (MyApp.getInstance().needToShowAd()) {
                            MyApp.getInstance().showInterstitial(ActMain.this, intent, false, -1, null);
                        } else {
                            startActivity(intent);
                        }
                        return true;

                    case R.id.navigation_item_settings:
                        Intent intent1 = new Intent(ActMain.this, ActSettings.class);
                        if (MyApp.getInstance().needToShowAd()) {
                            MyApp.getInstance().showInterstitial(ActMain.this, intent1, false, -1, null);
                        } else {
                            startActivity(intent1);
                        }
                        return true;

                    case R.id.nav_item_share:
                        shareWithOther(ActMain.this);
                        return true;

                    case R.id.nav_item_rate_us:
                        String appPackageName = BuildConfig.APPLICATION_ID;
//                        String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        return true;

                    case R.id.nav_item_policy:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://doc-hosting.flycricket.io/privacy"));
                        startActivity(browserIntent);
                        return true;

                    case R.id.nav_item_about_us:
                        Intent intent2 = new Intent(ActMain.this, ActAboutUs.class);
                        if (MyApp.getInstance().needToShowAd()) {
                            MyApp.getInstance().showInterstitial(ActMain.this, intent2, false, -1, null);
                        } else {
                            startActivity(intent2);
                        }
                        return true;

                    case R.id.nav_item_report:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.mainEmail), null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.txt_customer_support));
                        emailIntent.putExtra(Intent.EXTRA_TEXT, getEmailMsg(ActMain.this) + "\n\n");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        return true;

                    default:
                        return true;
                }
            }
        });

//        navigationView.setCheckedItem(R.id.navigation_item_all_media);
        if (savedInstanceState == null) {
            displayMedia();
            return;
        }
    }

    public String getAppVersion(Activity activity) {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);
//            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }
                    } else {
                        Log.i(TAG2, "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    private void inAppUpdate() {

        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            Log.e(TAG2, "OnSuccess");
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE /*AppUpdateType.IMMEDIATE*/)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, IMMEDIATE /*AppUpdateType.IMMEDIATE*/, ActMain.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//CHECK THIS if AppUpdateType.IMMEDIATE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG2, "checkForAppUpdateAvailability: something else");
            }
        });
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.drawer_layout), "New app is ready!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.completeUpdate();
                }
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.show();
    }

    private String getMarketLink() {
        return "market://details?id=" + BuildConfig.APPLICATION_ID;
//        return "market://details?id=" + getPackageName();
    }

    public String getEmailMsg(Activity activity) {
        return "Device : " + getDeviceName() + "\n" +
                "App version : " + getAppVersion(activity) + "\n" +
                "Device OS : " + Build.VERSION.RELEASE + "\n" +
                "SDK version : " + Build.VERSION.SDK_INT;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public void shareWithOther(Activity mContext) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name_new));
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.txt_shareMessage));
        mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.txt_share_with)));
    }

    @Override
    protected void onStop() {
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo result) {
                    if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        try {
                            mAppUpdateManager.startUpdateFlowForResult(result, IMMEDIATE, ActMain.this, RC_APP_UPDATE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//CHECK THIS if AppUpdateType.IMMEDIATE, otherwise you can skip
                        popupSnackbarForCompleteUpdate();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        FragRvAllMedia myFragment = (FragRvAllMedia) getSupportFragmentManager().findFragmentByTag(FragRvAllMedia.TAG);
        FragRvMediaNew mediaFragmentNew = (FragRvMediaNew) getSupportFragmentManager().findFragmentByTag(FragRvMediaNew.TAG);
        FragRvLocationMedia locationMediaFragment = (FragRvLocationMedia) getSupportFragmentManager().findFragmentByTag(FragRvLocationMedia.TAG);
        if (requestCode == REQUEST_CODE_VIEW_MEDIA && resultCode == RESULT_OK) {
            Log.e("TRACE_DELETE", "onActivityResult");
            // There are no request codes
            if (mediaFragmentNew != null && mediaFragmentNew.isVisible()) {
                Log.e("TRACE_DELETE", "onActivityResult if call for RvMediaFragmentNew");
                mediaFragmentNew.refreshSpecificMediaFragment(resultData);
            }
            if (locationMediaFragment != null && locationMediaFragment.isVisible()) {
                Log.e("TRACE_DELETE", "onActivityResult if call for RvLocationMediaFragment");
                locationMediaFragment.refreshSpecificMediaFragment(resultData);
                locationMediaFragment.deletePendingMedia(requestCode, resultCode);
            }
            if (myFragment != null && myFragment.isVisible()) {
                Log.e("TRACE_DELETE", "onActivityResult if call for RvAllMediaFragment");
                myFragment.refreshSpecificMediaFragment(resultData);
            }

        } else if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e(TAG2, "onActivityResult: app download failed");
            }

        } else if (requestCode == 590) {
            if (mediaFragmentNew != null && mediaFragmentNew.isVisible() && getSupportFragmentManager().getBackStackEntryCount() > 1) {
                mediaFragmentNew.writePermissionAction();
            } else if (myFragment != null && myFragment.isVisible()) {
                myFragment.writePermissionAction();
            } else {
                Toast.makeText(ActMain.this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_CODE_SECURE_VAULT && IS_VAULT_CHANGED) {
            IS_VAULT_CHANGED = false;
            if (myFragment != null && myFragment.isVisible())
                myFragment.refreshViewPager();

        } else if (requestCode == REQUEST_CODE_WA_STATUS && IS_NEW_STATUS_DOWNLOAD) {
            IS_NEW_STATUS_DOWNLOAD = false;
            if (myFragment != null && myFragment.isVisible())
                myFragment.refreshViewPager();

        } else {
            boolean DeleteORMoveVault = (requestCode == SupportClass.ANDROID_Q_DELETE_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_DELETE_REQUEST_CODE ||
                    requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE);
            if (DeleteORMoveVault) {
                if (mediaFragmentNew != null && mediaFragmentNew.isVisible() && getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    mediaFragmentNew.onSuccessGive11Permission(requestCode, resultCode);
                } else if (locationMediaFragment != null && locationMediaFragment.isVisible()) {
                    Log.e("TRACE_DELETE", "onActivityResult if call for RvLocationMediaFragment");
//                    locationMediaFragment.refreshSpecificMediaFragment(resultData);
                    locationMediaFragment.deletePendingMedia(requestCode, resultCode);
                } else if (myFragment != null && myFragment.isVisible()) {
                    myFragment.onSuccessGive11Permission(requestCode, resultCode);
                }

            }
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

    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.add(R.id.content, fragment).commit();
        navigationDrawer.closeDrawer(GravityCompat.START);
    }

    public void displayMedia() {
        FragRvAllMedia rvAllMediaFragment = FragRvAllMedia.make();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, rvAllMediaFragment, FragRvAllMedia.TAG)
                .addToBackStack(null)
                .commit();
    }

    public void displayMediaNew(Album album) {
        if (UserHelpers.getAlbumClickEnabled() == 1) {
            MyApp.getInstance().needToShowAd();
        }
        openAlbum(album);
    }

    public void openAlbum(Album album) {
        FragRvMediaNew rvMediaFragmentNew = FragRvMediaNew.make(album);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, rvMediaFragmentNew, FragRvMediaNew.TAG)
                .addToBackStack(FragRvMediaNew.TAG)
                .commit();
        setToolbarIcon(true);
    }

    public void displayLocationMedia(Album album) {
        ArrayList<Media> mediaArrayList = album.getMediaArrayList();
        ArrayList<Media> mediaArrayList1 = new ArrayList<>();
//        album.getMediaArrayList().clear();
        for (int i = 0; i < mediaArrayList.size(); i++) {
            Media media = mediaArrayList.get(i);
            if (getPathFromURI(mediaArrayList.get(i).getPath())) {
                mediaArrayList1.add(media);
            }
        }
        album.setMediaArrayList(mediaArrayList1);
        FragRvLocationMedia rvLocationMediaFragment = FragRvLocationMedia.make(album);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, rvLocationMediaFragment, FragRvLocationMedia.TAG)
                .addToBackStack(FragRvLocationMedia.TAG)
                .commit();
        setToolbarIcon(true);
    }

    public boolean getPathFromURI(String path) {
        File yourFile = new File(path);
        return yourFile.exists();

    }

    public void setToolbarIcon(boolean isBack) {
        Drawable nav = ContextCompat.getDrawable(this, isBack ? R.drawable.e_back : R.drawable.e_toggle);
        if (nav != null) {
            toolbar.setNavigationIcon(nav);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
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
                Toast.makeText(ActMain.this, R.string.something_went_wrong_please_try_again, Toast.LENGTH_SHORT).show();
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
                    MyApp.getInstance().showInterstitial(ActMain.this, intent, false, REQUEST_CODE_VIEW_MEDIA, activityOptionsCompat.toBundle());
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
                    MyApp.getInstance().showInterstitial(ActMain.this, intent, false, REQUEST_CODE_VIEW_MEDIA, activityOptionsCompat.toBundle());
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

    @Override
    public void changedEditMode(boolean editMode, int selected, int total, @Nullable String title) {
        if (editMode) {
            updateToolbar(getString(R.string.toolbar_selection_count, selected, total));
        } else {
            updateToolbar(title.isEmpty() ? getString(R.string.app_name_new) : title);
        }
    }

    @Override
    public void onItemsSelected(int count, int total) {
        toolbar.setTitle(getString(R.string.toolbar_selection_count, count, total));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initUi() {
        setSupportActionBar(toolbar);
        setupNavigationDrawer();
    }

    private void setupNavigationDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawer, R.string.drawer_open, R.string.drawer_close);

        navigationDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_toggle);
        setToolbarIcon(false);
        toolbar.setTitleTextAppearance(this, R.style.BoldTextAppearance);
        toolbar.getOverflowIcon().setTint(getResources().getColor(R.color.black));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    onBackPressed();
                } else {
                    navigationDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    public void updateToolbar(String title) {
        Fragment topFragment = getTopFragment();
        if (!title.isEmpty() && title.contains(" of ")) {
            toolbar.setTitle(title);
            return;
        } else if (topFragment instanceof FragRvAllMedia) {
            toolbar.setTitle(((FragRvAllMedia) topFragment).titleArr[((FragRvAllMedia) topFragment).viewPager.getCurrentItem()]);
            return;
        }
        toolbar.setTitle(title);
    }

    public void enableNothingToSHowPlaceHolder(boolean status) {
        findViewById(R.id.nothing_to_show_placeholder).setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Deprecated
    public void checkNothing(boolean status) {
        if (status && Prefs.showMasterEgg()) {
            findViewById(R.id.ll_emoji_master).setVisibility(View.VISIBLE);
            findViewById(R.id.nothing_to_show_placeholder).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.ll_emoji_master).setVisibility(View.GONE);
            findViewById(R.id.nothing_to_show_placeholder).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
            case R.id.settings_album:
                ActSettings.startActivity(this);
                return true;

            default:
                if (actionBarDrawerToggle.onOptionsItemSelected(item))
                    return true;

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
//        Rate.showDialog();
        Fragment topFragment = getTopFragment();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if (topFragment instanceof FragRvMediaNew) {
                if (((FragRvMediaNew) topFragment).editMode()) {
                    ((FragRvMediaNew) topFragment).clearSelected();
                } else {
                    getSupportFragmentManager().popBackStack();
                    updateToolbar("Albums");
                    setToolbarIcon(false);
                }
            } else if (topFragment instanceof FragRvLocationMedia) {
                if (((FragRvLocationMedia) topFragment).editMode()) {
                    ((FragRvLocationMedia) topFragment).clearSelected();
                } else {
                    getSupportFragmentManager().popBackStack();
                    updateToolbar("Explore");
                    setToolbarIcon(false);
                }
            } else {
                getSupportFragmentManager().popBackStack();
                setToolbarIcon(false);
            }

        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                Fragment fragmentCurrent = getSupportFragmentManager().findFragmentById(R.id.content);
                if (fragmentCurrent instanceof FragRvAllMedia && ((FragRvAllMedia) fragmentCurrent).isMediaSelected()) {

                } else {
                    doubleBackToExit();
                }
            } else {
                doubleBackToExit();
            }
        }
//        navigationView.setCheckedItem(R.id.navigation_item_all_media);
    }

    private void doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
//            showRate();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }




}
