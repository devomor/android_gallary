package com.photo.photography.duplicatephotos.act;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.data_helper.Media;
import com.photo.photography.duplicatephotos.AppDataBaseHandler;
import com.photo.photography.duplicatephotos.repeater.RepeaterIndividualGroup;
import com.photo.photography.duplicatephotos.backgroundasynk.DeleteSelectedFile;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.frag.FragExactDuplicates;
import com.photo.photography.duplicatephotos.frag.FragSimilarDuplicates;
import com.photo.photography.duplicatephotos.callback.CallbackChangeTab;
import com.photo.photography.duplicatephotos.callback.CallbackMarked;
import com.photo.photography.duplicatephotos.util.PopUpDialogs;
import com.photo.photography.util.MediaUtils;
import com.photo.photography.util.MimeTypeUtil;
import com.photo.photography.util.utils.CallbackOnDeleteProcess;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.google.android.material.tabs.TabLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class ActPhotoList extends ActBase implements CallbackChangeTab, CallbackMarked {
    private final ImageLoader imageLoader = GlobalVarsAndFunction.getImageLoadingInstance();
    ImageView blank;
    CheckBox checkBox;
    ImageView btnDeleteDuplicatePhotos;
    View dialogbutton;
    Context displayDuplicateContext;
    boolean exactOrSimilar = false;
    ImageView lockSelectedImages;
    Dialog mdialog;
    RecyclerView recyclerView;
    TextView tDuplicateFound;
    TextView tMarked;
    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    boolean isTaskRunning;
    int numberOfFilesPresentInOtherScan = 0;
    long deletedFileSize = 0;
    ArrayList<ImagesItem> deleteImageList = new ArrayList<>();
    ArrayList<Media> deleteImageMediaList = new ArrayList<>();
    ArrayList<List<ImagesItem>> deleteIndividualImageList = new ArrayList<List<ImagesItem>>();
    private DisplayImageOptions options;

    public void checkForLastScan(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActPhotoList.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_dup_rescan_confirm, null);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);

        TextView tvLastScanningDateTime = dialogView.findViewById(R.id.tvLastScanningDateTime);
        if (TextUtils.isEmpty(preferences.getString(SupportClass.LAST_SCAN_DATE_TIME, ""))) {
            tvLastScanningDateTime.setVisibility(View.GONE);
        } else {
            tvLastScanningDateTime.setText("Last scanned : " + preferences.getString(SupportClass.LAST_SCAN_DATE_TIME, ""));
            tvLastScanningDateTime.setVisibility(View.VISIBLE);
        }

        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            alert.dismiss();
            performNextOpt();
        });

        TextView btnContinue = dialogView.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            alert.dismiss();
            finish();
            startActivity(new Intent(ActPhotoList.this, ActDuplicateHomeMain.class));
        });

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    public static String getExceptionsText(int i, int i2, Context context) {
        Log.e("getExceptionsText", String.valueOf(i2));
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", 0);
        String string = sharedPreferences.getString("My_Language", "");
        String string2 = sharedPreferences.getString("MY_country", "");
        Log.e("language", string + "------" + string2);
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            Log.e("getExceptionsText", "inside similar tab");
            if (i > 1) {
                return "The selected similar photos will be excluded from all future scan. Press OK to exclude.";
            } else if (i == 1) {
                return "The selected similar photo will be excluded from all future scan. Press OK to exclude.";
            }
        }
        return null;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.act_duplicate_photo_list);
        CommonUsed.logmsg("DisplayDuplicateActivity!!!!!!!!!");

        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));

        displayDuplicateContext = getApplicationContext();
        Context context = displayDuplicateContext;
        GlobalVarsAndFunction.configureImageLoader(this.imageLoader, ActPhotoList.this);
        options = GlobalVarsAndFunction.getOptions();
        GlobalVarsAndFunction.setWalkThroughScreenCount(context, GlobalVarsAndFunction.getWalkThroughScreenCount(context) + 1);
        mdialog = new Dialog(this);
        getIntent().getStringExtra("memoryPopUpAndRecoverPopUp").equalsIgnoreCase("showRecoverPopUp");
        if (getIntent().getBooleanExtra("lastFromScan", false)) {
            checkForLastScan();
        }else{
            performNextOpt();
        }

    }

    private void performNextOpt() {

        initUi();
        initializeTabLayoutAndViewPager();
        initializeDrawerLayoutAndNavigationView();
        if (getIntent().getStringExtra("tS").equalsIgnoreCase("exact")) {
            GlobalVarsAndFunction.setTabSelected(0);
            showDuplicatesFoundAndMarked(getString(R.string.Duplicates_Found) + " " + GlobalVarsAndFunction.getTotalDuplicatesExact(), getString(R.string.Markedvalue));
        } else {
            showDuplicatesFoundAndMarked(getString(R.string.Duplicates_Found) + " " + GlobalVarsAndFunction.getTotalDuplicatesSimilar(), getString(R.string.Markedvalue));
        }
        if (getIntent().getStringExtra("memoryPopUpAndRecoverPopUp").equalsIgnoreCase("showMemoryPopUp")) {
            showMemoryRegainPopUp(GlobalVarsAndFunction.getTotalDuplicatesExact(),
                    getString(R.string.Exact_Duplicates) + " " + GlobalVarsAndFunction.getTotalDuplicatesExact(),
                    getString(R.string.Memory_Occupied) + " " + GlobalVarsAndFunction.getMemoryRegainedExact(),
                    getString(R.string.No_exact_duplicates_found));
            return;
        }
    }

    public void memoryRegainedPopUpDialog(String str, String str2, CallbackChangeTab changeTab) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.dialog_memories_regain, null);
        builder.setView(inflate);
        builder.setCancelable(false);

        TextView textView = inflate.findViewById(R.id.duplicatesfoundafter);
        textView.setText(str);

        TextView textView2 = inflate.findViewById(R.id.sizeofdupes);
        textView2.setText(str2);

        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inflate.findViewById(R.id.dialogButtonAccept).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GlobalVarsAndFunction.ONE_TIME_POPUP = true;
                create.dismiss();
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    GlobalVarsAndFunction.SHOW_REGAIN_POP_UP_ONLY_ON_SCAN_SIMILAR = true;
                }
                GlobalVarsAndFunction.getTotalDuplicatesExact();
                boolean isfirsttime = getSharedPreferences("firsttime", 0).getBoolean("f", Boolean.parseBoolean(""));
                Log.e("onCreate", String.valueOf(isfirsttime));
                if (!isfirsttime) {
                    showcaseView(0);
                }
            }
        });
        if (!GlobalVarsAndFunction.POP_ONLY_ONCE) {
            create.show();
        }
    }

    @Override
    public void onBackPressed() {
        new PopUpDialogs(displayDuplicateContext, this).showAlertBackPressed();
    }

    private void getMemoryRegainedSizeFromListOfDuplicates() {
        try {
            GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().size();
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
        }
    }

    @Override
    public void changeTabPosition() {
        viewPager.setCurrentItem(1);
    }

    private void initUi() {
        dialogbutton = findViewById(R.id.dialogButtonok);
        blank = findViewById(R.id.blanktext);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id._tabs);
        viewPager = findViewById(R.id.view_pager);
        tDuplicateFound = findViewById(R.id.dupes_found);
        tMarked = findViewById(R.id.marked);
        btnDeleteDuplicatePhotos = findViewById(R.id.delete);
        lockSelectedImages = findViewById(R.id.lock);
        checkBox = findViewById(R.id.cb_grp_checkbox);
        btnDeleteDuplicatePhotos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btnDeleteDuplicatePhotos.setEnabled(false);
                deleteDuplicatePhotos();
            }
        });
        lockSelectedImages.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lockSelectedPhotos();
            }
        });
    }

    public void showcaseView(final int i) {
        try {
            SharedPreferences.Editor edit = getSharedPreferences("firsttime", 0).edit();
            edit.putBoolean("f", true);
            edit.apply();
            if (recyclerView == null) {
                recyclerView = findViewById(R.id.recycler_view_exact);
            }
            recyclerView.scrollToPosition(0);
            View[] viewArr = {findViewById(R.id.ivTempFocus), tabLayout, blank, lockSelectedImages, btnDeleteDuplicatePhotos};
            String[] strArr = {getString(R.string.a), getString(R.string.b), getString(R.string.c), getString(R.string.d), getString(R.string.e)};
            if (i < viewArr.length) {
                new GuideView.Builder(this)
                        .setTitle(String.valueOf(i + 1))
                        .setContentText(strArr[i])
                        .setGravity(Gravity.auto)
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(viewArr[i])
                        .setGuideListener(new GuideListener() {
                            @Override
                            public void onDismiss(View view) {
                                showcaseView(i + 1);
                            }
                        }).setContentTextSize(12).setTitleTextSize(14).build().show();
            }
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void lockSelectedPhotos() {
        if (GlobalVarsAndFunction.getTabSelected() == 0) {
            Log.e("language", "inside loadlanguage");
            SharedPreferences sharedPreferences = displayDuplicateContext.getSharedPreferences("Setting", 0);
            String string = sharedPreferences.getString("My_Language", "");
            String string2 = sharedPreferences.getString("MY_country", "");
            Log.e("language", string + "------" + string2);
            int size = GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size();
            if (GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() <= 0) {
                CommonUsed.showmsg(displayDuplicateContext, getString(R.string.Please_select_at_least_one_photo));
                return;
            }
            if (!GlobalVarsAndFunction.getLockFirstTimeExactFlag(displayDuplicateContext)) {
                setLockPhotos();
            } else {
                ArrayList arrayList = new ArrayList();
                int i = 0;
                while (i < GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size()) {
                    arrayList.add(GlobalVarsAndFunction.file_To_Be_Deleted_Exact.get(i));
                    i++;
                }
                GlobalVarsAndFunction.setLockExactPhotos(displayDuplicateContext, arrayList);
            }
            new PopUpDialogs(displayDuplicateContext, this).lockSelectedImages(GlobalVarsAndFunction.file_To_Be_Deleted_Exact,
                    "Adding exceptions, please wait...",
                    size > 1 ? "The selected exact photos will be excluded from all future scan. Press OK to exclude."
                            : "The selected exact photo will be excluded from all future scan. Press OK to exclude.",
                    GlobalVarsAndFunction.getGroupOfDuplicatesExact());

        } else if (GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() <= 0) {
            CommonUsed.showmsg(displayDuplicateContext, getString(R.string.Please_select_at_least_one_photo));

        } else {
            if (!GlobalVarsAndFunction.getLockFirstTimeSimilarFlag(displayDuplicateContext)) {
                setLockPhotos();
            } else {
                ArrayList arrayList2 = new ArrayList();
                HashMap hashMap = new HashMap();
                int i = 0;
                while (i < GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size()) {
                    ImagesItem imageItem = GlobalVarsAndFunction.file_To_Be_Deleted_Similar.get(i);
                    hashMap.put(imageItem.getImage(), imageItem);
                    arrayList2.add(GlobalVarsAndFunction.file_To_Be_Deleted_Similar.get(i));
                    i++;
                }
                GlobalVarsAndFunction.setLockSimilarPhotos(displayDuplicateContext, arrayList2);
            }
            new PopUpDialogs(displayDuplicateContext, this).lockSelectedImages(GlobalVarsAndFunction.file_To_Be_Deleted_Similar,
                    "Adding exceptions, please wait...",
                    getExceptionsText(GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size(), GlobalVarsAndFunction.getTabSelected(), displayDuplicateContext),
                    GlobalVarsAndFunction.getGroupOfDuplicatesSimilar());
        }
    }

    private void setLockPhotos() {
        int i = 0;
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            if (GlobalVarsAndFunction.getLockSimilarPhotos(displayDuplicateContext) != null) {
                ArrayList<ImagesItem> lockSimilarPhotos = GlobalVarsAndFunction.getLockSimilarPhotos(displayDuplicateContext);
                while (i < GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size()) {
                    lockSimilarPhotos.add(GlobalVarsAndFunction.file_To_Be_Deleted_Similar.get(i));
                    i++;
                }
                GlobalVarsAndFunction.setLockSimilarPhotos(displayDuplicateContext, lockSimilarPhotos);
            }
        } else if (GlobalVarsAndFunction.getLockExactPhotos(displayDuplicateContext) != null) {
            ArrayList<ImagesItem> lockExactPhotos = GlobalVarsAndFunction.getLockExactPhotos(displayDuplicateContext);
            while (i < GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size()) {
                lockExactPhotos.add(GlobalVarsAndFunction.file_To_Be_Deleted_Exact.get(i));
                i++;
            }
            GlobalVarsAndFunction.setLockExactPhotos(displayDuplicateContext, lockExactPhotos);
        }
    }

    public void initializeTabLayoutAndViewPager() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTab(tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    recyclerView = findViewById(R.id.recycler_view_similar);
                    GlobalVarsAndFunction.setTabSelected(1);
                    showDuplicatesFoundAndMarked(getString(R.string.Duplicates_Found) + " " + GlobalVarsAndFunction.getTotalDuplicatesSimilar(), markedStringSimilar());
                    CommonUsed.logmsg("E or S: " + getIntent().getExtras().getBoolean("showSimilarRegainedPopUpExact"));
                    CommonUsed.logmsg("E or S: " + getIntent().getExtras().getBoolean("showSimilarRegainedPopUpSimilar"));
                    if (!exactOrSimilar && !getIntent().getExtras().getBoolean("showSimilarRegainedPopUpExact") && !getIntent().getExtras().getBoolean("showSimilarRegainedPopUpSimilar")) {
                        getMemoryRegainedSizeFromListOfDuplicates();
                        showMemoryRegainPopUp(GlobalVarsAndFunction.getTotalDuplicatesSimilar(),
                                getString(R.string.Similar_Duplicates) + " " + GlobalVarsAndFunction.getTotalDuplicatesSimilar(),
                                getString(R.string.Memory_Occupied) + " " + GlobalVarsAndFunction.getMemoryRegainedSimilar(),
                                getString(R.string.No_similar_duplicates_found));
                    }
                } else if (tab.getPosition() == 0) {
                    recyclerView = findViewById(R.id.recycler_view_exact);
                    GlobalVarsAndFunction.setTabSelected(0);
                    showDuplicatesFoundAndMarked(getString(R.string.Duplicates_Found) + " " + GlobalVarsAndFunction.getTotalDuplicatesExact(), markedStringExact());
                    exactOrSimilar = true;
                }
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

        View tabOne = LayoutInflater.from(this).inflate(R.layout.apk_tabs_custom, null);
        TextView textViewOne = tabOne.findViewById(R.id.tab);
        textViewOne.setText(getString(R.string.Exact));
        ImageView ivTabStripOne = tabOne.findViewById(R.id.ivTabStrip);
        ivTabStripOne.setVisibility(View.VISIBLE);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View tabTwo = LayoutInflater.from(this).inflate(R.layout.apk_tabs_custom, null);
        TextView textViewTwo = tabTwo.findViewById(R.id.tab);
        textViewTwo.setText(getString(R.string.Similar));
        ImageView ivTabStripTwo = tabTwo.findViewById(R.id.ivTabStrip);
        ivTabStripTwo.setVisibility(View.GONE);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void setCurrentTab(int tabPos) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tabOne = tabLayout.getTabAt(i).getCustomView();
            ImageView ivTabStripOne = tabOne.findViewById(R.id.ivTabStrip);
            ivTabStripOne.setVisibility(i == tabPos ? View.VISIBLE : View.INVISIBLE);
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    private void initializeDrawerLayoutAndNavigationView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.duplicate_photos));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMemoryRegainPopUp(int i, String str, String str2, String str3) {
        if (i > 0) {
            CommonUsed.logmsg(str2 + str);
            memoryRegainedPopUpDialog(str, str2, this);
            return;
        }
        memoryRegainedPopUpDialog(str3, "", this);
    }

    private void showDuplicatesFoundAndMarked(String str, String str2) {
        tDuplicateFound.setText(str);
        tMarked.setText(str2);
    }

    public void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragExactDuplicates(), getString(R.string.Exact), "exact");
        viewPagerAdapter.addFragment(new FragSimilarDuplicates(), getString(R.string.Similar), "similar");
        viewPager2.setAdapter(viewPagerAdapter);
        CommonUsed.logmsg("which tab selected: " + getIntent().getStringExtra("tS"));
        if (getIntent().getStringExtra("tS").equalsIgnoreCase("exact")) {
            viewPager2.setCurrentItem(0);
        } else if (getIntent().getStringExtra("tS").equalsIgnoreCase("similar")) {
            viewPager2.setCurrentItem(1);
        }
    }

    private void deleteDuplicatePhotos() {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            if (GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() > 0) {
                showSimilarImagesDeleteAlertMessage();
                return;
            }
            btnDeleteDuplicatePhotos.setEnabled(true);
            CommonUsed.showmsg(displayDuplicateContext, getString(R.string.Please_select_at_least_one_photo));
        } else if (GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() > 0) {
            showExactImagesDeleteAlertMessage();
        } else {
            btnDeleteDuplicatePhotos.setEnabled(true);
            CommonUsed.showmsg(displayDuplicateContext, getString(R.string.Please_select_at_least_one_photo));
        }
    }

    private void showExactImagesDeleteAlertMessage() {
        if (SupportClass.isExternalStoragePermissionGranted(ActPhotoList.this)) {
            startExactDeletionAfterPermissionParentGranted();
        } else {
            SupportClass.showTakeWritePermissionDialog(ActPhotoList.this);
        }
    }

    private void startExactDeletionAfterPermissionParentGranted() {
        if (GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() == 1) {
            btnDeleteDuplicatePhotos.setEnabled(true);
            deleteAlertPopUp(getString(R.string.Are_you_sure_you_want_to_delete_selected_exact_photo));
            return;
        }
        btnDeleteDuplicatePhotos.setEnabled(true);
        deleteAlertPopUp(getString(R.string.Are_you_sure_you_want_to_delete_selected_exact_photos));
    }

    public void deleteAlertPopUp(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str).setCancelable(false).setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deleteSelectedFilesNew();
            }
        }).setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog create = builder.create();
        create.setTitle(this.getString(R.string.Confirm_Delete));
        create.show();
    }

    private void deleteSelectedFilesNew() {
        ArrayList<ImagesItem> arrayList = GlobalVarsAndFunction.getTabSelected() == 0 ? GlobalVarsAndFunction.file_To_Be_Deleted_Exact : GlobalVarsAndFunction.file_To_Be_Deleted_Similar;

        deleteImageList = new ArrayList<>();
        deleteImageMediaList = new ArrayList<>();
        deleteIndividualImageList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            ImagesItem imageItem = arrayList.get(i);
            List<ImagesItem> individualGrpOfDupes = getIndividualGroupFromItemImage(imageItem);

            int position = imageItem.getPosition();
            for (int i2 = 0; i2 < individualGrpOfDupes.size(); i2++) {
                ImagesItem imageItem2 = individualGrpOfDupes.get(i2);
                if (imageItem2.getPosition() == position) {
                    deleteIndividualImageList.add(individualGrpOfDupes);
                    deleteImageList.add(imageItem2);
                    Media media = new Media();
                    media.setId(imageItem2.getId());
                    media.setPath(imageItem2.getImage());
                    media.setMimeType(MimeTypeUtil.getMimeType(media.getPath()));
                    Uri contentUri = Uri.fromFile(new File(media.getPath()));
                    if (contentUri == null)
                        contentUri = ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                media.getId()
                        );
                    media.setUri(contentUri.toString());
                    deleteImageMediaList.add(media);
                }
            }
        }
        startDeleteFile();
    }

    private List<ImagesItem> getIndividualGroupFromItemImage(ImagesItem imageItem) {
//        List<IndividualGroup> groupOfDuplicates = GlobalVarsAndFunc.getTabSelected() == 0 ? GlobalVarsAndFunc.getGroupOfDuplicatesExact() : GlobalVarsAndFunc.getGroupOfDuplicatesSimilar();
//        for (int i = 0; i < groupOfDuplicates.size(); i++) {
//            if (groupOfDuplicates.get(i).getGroupTag() == imageItem.getImageItemGrpTag()) {
//                return groupOfDuplicates.get(i).getIndividualGrpOfDupes();
//            }
//        }
//        return new ArrayList<>();
        List<IndividualGroups> groupOfDuplicates = GlobalVarsAndFunction.getTabSelected() == 0 ? GlobalVarsAndFunction.getGroupOfDuplicatesExact() : GlobalVarsAndFunction.getGroupOfDuplicatesSimilar();
        int imageItemGrpTag = imageItem.getImageItemGrpTag();
        return groupOfDuplicates.get(imageItemGrpTag).getIndividualGrpOfDupes();
    }

    private void startDeleteFile() {
        if (deleteImageMediaList.size() > 0) {
            MediaUtils.deleteMediaNew(ActPhotoList.this, deleteImageMediaList, new CallbackOnDeleteProcess() {
                @Override
                public void onDeleteComplete() {
                    for (int i = 0; i < deleteImageList.size(); i++) {
                        removeFromList(i);

                        int imageItemGrpTag = deleteImageList.get(i).getImageItemGrpTag();
                        List<ImagesItem> individualGrpOfDupes = getIndividualGroupFromItemImage(deleteImageList.get(i));
                        if (GlobalVarsAndFunction.getTabSelected() == 0) { // Exact
                            GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(imageItemGrpTag).setIndividualGrpOfDupes(individualGrpOfDupes);
                            GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(imageItemGrpTag).setGroupSetCheckBox(false);
                        } else { // Similar
                            GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(imageItemGrpTag).setIndividualGrpOfDupes(individualGrpOfDupes);
                            GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(imageItemGrpTag).setGroupSetCheckBox(false);
                        }
                        AppDataBaseHandler dbHandler = new AppDataBaseHandler(ActPhotoList.this);
                        dbHandler.insertUpdateGroupList(GlobalVarsAndFunction.getGroupOfDuplicatesExact(), false);
                        dbHandler.insertUpdateGroupList(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), true);
                    }
                    deleteReviewDialog();
                }

                @Override
                public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media media) {
                    if (isSuccess) {
                        ImagesItem imageItem = getMediaToImageItem(media);
                        if (imageItem != null) {
                            List<ImagesItem> individualGrpOfDupes = getIndividualGroupFromItemImage(imageItem);
                            ArrayList<ImagesItem> arrayList = GlobalVarsAndFunction.getTabSelected() == 0 ? GlobalVarsAndFunction.file_To_Be_Deleted_Exact : GlobalVarsAndFunction.file_To_Be_Deleted_Similar;
                            initializePageAndDataAfterDelete(individualGrpOfDupes, getImageItemFromPath(media.getPath()), arrayList);
                            GlobalVarsAndFunction.updateDeletionCount(ActPhotoList.this, 1);
                        }
                    }
                }
            });
        }
    }

    private void deleteReviewDialog() {
        ArrayList<ImagesItem> fileToBeDeleted = GlobalVarsAndFunction.getTabSelected() == 0 ? GlobalVarsAndFunction.file_To_Be_Deleted_Exact : GlobalVarsAndFunction.file_To_Be_Deleted_Similar;
        FragExactDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(displayDuplicateContext, ActPhotoList.this,
                GlobalVarsAndFunction.getGroupOfDuplicatesExact(), this, imageLoader, options));
        FragSimilarDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(displayDuplicateContext, ActPhotoList.this,
                GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), this, imageLoader, options));

        if (GlobalVarsAndFunction.getTabSelected() == 0) {
            GlobalVarsAndFunction.setMemoryRegainedExact(GlobalVarsAndFunction.getStringSizeLengthFile(GlobalVarsAndFunction.getMemoryRegainedExactInLong() - deletedFileSize));
            GlobalVarsAndFunction.setTotalDuplicatesExact(GlobalVarsAndFunction.getTotalDuplicatesExact() - numberOfFilesPresentInOtherScan);

            new PopUpDialogs(displayDuplicateContext, ActPhotoList.this)
                    .memoryRecoveredPopUp(displayDuplicateContext.getString(R.string.Exact_Duplicates_Cleaned) + " " + fileToBeDeleted.size(),
                            displayDuplicateContext.getString(R.string.Space_Regained) + " " + GlobalVarsAndFunction.sizeInString(),
                            String.valueOf(fileToBeDeleted.size()),
                            GlobalVarsAndFunction.sizeInDouble(),
                            this);
        } else {
            GlobalVarsAndFunction.setMemoryRegainedSimilar(GlobalVarsAndFunction.getStringSizeLengthFile(GlobalVarsAndFunction.getMemoryRegainedSimilarInLong() - deletedFileSize));
            GlobalVarsAndFunction.setTotalDuplicatesSimilar(GlobalVarsAndFunction.getTotalDuplicatesSimilar() - numberOfFilesPresentInOtherScan);

            new PopUpDialogs(displayDuplicateContext, ActPhotoList.this)
                    .memoryRecoveredPopUp(displayDuplicateContext.getString(R.string.Similar_Duplicates_Cleaned) + " " + fileToBeDeleted.size(),
                            displayDuplicateContext.getString(R.string.Space_Regained) + " " + GlobalVarsAndFunction.sizeInString(),
                            String.valueOf(fileToBeDeleted.size()),
                            GlobalVarsAndFunction.sizeInDouble(),
                            this);
        }
    }

    private ImagesItem getMediaToImageItem(Media media) {
        for (int i = 0; i < deleteImageList.size(); i++) {
            if (deleteImageList.get(i).getImage().equalsIgnoreCase(media.getPath())) {
                return deleteImageList.get(i);
            }
        }
        return null;
    }

    public List<ImagesItem> getIndividualGroupListFromItemImage(int grpTag, ImagesItem imageItem, boolean isSimilar) {
        List<IndividualGroups> groupOfDuplicates = isSimilar ? GlobalVarsAndFunction.getGroupOfDuplicatesSimilar() : GlobalVarsAndFunction.getGroupOfDuplicatesExact();
        List<ImagesItem> individualGrpOfDupes = groupOfDuplicates.get(grpTag).getIndividualGrpOfDupes();
        List<ImagesItem> finalList = new ArrayList<>();
        for (int i = 0; i < individualGrpOfDupes.size(); i++) {
            if (!individualGrpOfDupes.get(i).getImage().equalsIgnoreCase(imageItem.getImage()) || individualGrpOfDupes.get(i).getId() != imageItem.getId()) {
                finalList.add(individualGrpOfDupes.get(i));
            }
        }
        return finalList;
    }

    public int getDeletedImageTag(ImagesItem imageItem, boolean isSimilar) {
        List<IndividualGroups> list = isSimilar ? GlobalVarsAndFunction.getGroupOfDuplicatesSimilar() : GlobalVarsAndFunction.getGroupOfDuplicatesExact();
        for (int i = 0; i < list.size(); i++) {
            for (int p = 0; p < list.get(i).getIndividualGrpOfDupes().size(); p++) {
                if (list.get(i).getIndividualGrpOfDupes().get(p).getImage().equalsIgnoreCase(imageItem.getImage())) {
                    return list.get(i).getIndividualGrpOfDupes().get(p).getImageItemGrpTag();
                }
            }
        }
        return -1;
    }

    public void removeFromList(int i) {
        int eGrpTag = getDeletedImageTag(deleteImageList.get(i), false);
        if (eGrpTag != -1) {
            List<ImagesItem> eIndividualGrpOfDupes = getIndividualGroupListFromItemImage(eGrpTag, deleteImageList.get(i), false);
            GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(eGrpTag).setIndividualGrpOfDupes(eIndividualGrpOfDupes);
            GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(eGrpTag).setGroupSetCheckBox(false);
        }

        int sGrpTag = getDeletedImageTag(deleteImageList.get(i), true);
        if (sGrpTag != -1) {
            List<ImagesItem> sIndividualGrpOfDupes = getIndividualGroupListFromItemImage(sGrpTag, deleteImageList.get(i), true);
            GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(sGrpTag).setIndividualGrpOfDupes(sIndividualGrpOfDupes);
            GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(sGrpTag).setGroupSetCheckBox(false);
        }
    }

    public void deletePendingMedia(int requestCode, int resultCode) {
        MediaUtils.deletePendingMedia(ActPhotoList.this, requestCode, resultCode, new CallbackOnDeleteProcess() {
            @Override
            public void onDeleteComplete() {
                for (int i = 0; i < deleteImageList.size(); i++) {
                    removeFromList(i);

                    int imageItemGrpTag = deleteImageList.get(i).getImageItemGrpTag();
                    List<ImagesItem> individualGrpOfDupes = getIndividualGroupFromItemImage(deleteImageList.get(i));
                    if (GlobalVarsAndFunction.getTabSelected() == 0) { // Exact
                        GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(imageItemGrpTag).setIndividualGrpOfDupes(individualGrpOfDupes);
                        GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(imageItemGrpTag).setGroupSetCheckBox(false);
                    } else { // Similar
                        GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(imageItemGrpTag).setIndividualGrpOfDupes(individualGrpOfDupes);
                        GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(imageItemGrpTag).setGroupSetCheckBox(false);
                    }
                    AppDataBaseHandler dbHandler = new AppDataBaseHandler(ActPhotoList.this);
                    dbHandler.insertUpdateGroupList(GlobalVarsAndFunction.getGroupOfDuplicatesExact(), false);
                    dbHandler.insertUpdateGroupList(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), true);
                }
                deleteReviewDialog();
            }

            @Override
            public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media media) {
                if (isSuccess) {
                    ImagesItem imageItem = getMediaToImageItem(media);
                    if (imageItem != null) {
                        List<ImagesItem> individualGrpOfDupes = getIndividualGroupFromItemImage(imageItem);
                        ArrayList<ImagesItem> arrayList = GlobalVarsAndFunction.getTabSelected() == 0 ? GlobalVarsAndFunction.file_To_Be_Deleted_Exact : GlobalVarsAndFunction.file_To_Be_Deleted_Similar;
                        initializePageAndDataAfterDelete(individualGrpOfDupes, getImageItemFromPath(media.getPath()), arrayList);
                        GlobalVarsAndFunction.updateDeletionCount(ActPhotoList.this, 1);
                    }
                }
            }
        });
    }

    private ImagesItem getImageItemFromPath(String path) {
        for (int i = 0; i < deleteImageList.size(); i++) {
            if (deleteImageList.get(i).getImage().equalsIgnoreCase(path)) {
                return deleteImageList.get(i);
            }
        }
        return null;
    }

    private void initializePageAndDataAfterDelete(List<ImagesItem> list, ImagesItem imageItem, ArrayList<ImagesItem> fileToBeDeleted) {
        this.isTaskRunning = true;
        list.remove(imageItem);
        for (int i = 0; i < fileToBeDeleted.size(); i++) {
            deleteImageIfPresent(fileToBeDeleted.get(i));
        }
        deselectAllImagesInOtherGroup();
    }

    public void deleteImageIfPresent(ImagesItem imageItem) {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            deleteImageInAnotherGroup(GlobalVarsAndFunction.getGroupOfDuplicatesExact(), imageItem);
        } else {
            deleteImageInAnotherGroup(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), imageItem);
        }
    }

    public void deleteImageInAnotherGroup(List<IndividualGroups> list, ImagesItem imageItem) {
        for (int i = 0; i < list.size(); i++) {
            IndividualGroups individualGroup = list.get(i);
            List<ImagesItem> individualGrpOfDupes = individualGroup.getIndividualGrpOfDupes();
            int size = individualGrpOfDupes.size();
            int i2 = 0;
            for (int i3 = 0; i3 < individualGrpOfDupes.size(); i3++) {
                String image = imageItem.getImage();
                ImagesItem imageItem2 = individualGrpOfDupes.get(i3);
                String image2 = imageItem2.getImage();
                boolean isImageCheckBox = imageItem2.isImageCheckBox();
                if (image2.equalsIgnoreCase(image)) {
                    this.deletedFileSize += imageItem.getSizeOfTheFile();
                    this.numberOfFilesPresentInOtherScan++;
                    i2++;
                    if (i2 == size) {
                        this.numberOfFilesPresentInOtherScan--;
                        this.deletedFileSize -= imageItem.getSizeOfTheFile();
                    }
                    if (isImageCheckBox) {
                        CommonUsed.logmsg("Ya it is checked!!!! ");
                        if (GlobalVarsAndFunction.getTabSelected() != 0) {
                            removeFromDeletingArrayList(GlobalVarsAndFunction.file_To_Be_Deleted_Exact, imageItem);
                        } else {
                            removeFromDeletingArrayList(GlobalVarsAndFunction.file_To_Be_Deleted_Similar, imageItem);
                        }
                    }
                    individualGrpOfDupes.remove(imageItem2);
                }
            }
            individualGroup.setIndividualGrpOfDupes(individualGrpOfDupes);
        }
    }

    public void removeFromDeletingArrayList(ArrayList<ImagesItem> arrayList, ImagesItem imageItem) {
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                String image = imageItem.getImage();
                ImagesItem imageItem2 = arrayList.get(i);
                if (image.equalsIgnoreCase(imageItem2.getImage())) {
                    arrayList.remove(i);
                    if (GlobalVarsAndFunction.getTabSelected() != 0) {
                        GlobalVarsAndFunction.subSizeExact(imageItem2.getSizeOfTheFile());
                    } else {
                        GlobalVarsAndFunction.subSizeSimilar(imageItem2.getSizeOfTheFile());
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void deselectAllImagesInOtherGroup() {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            for (int i = 0; i < GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size(); i++) {
                ImagesItem imageItem = GlobalVarsAndFunction.file_To_Be_Deleted_Exact.get(i);
                imageItem.setImageCheckBox(false);
                GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(imageItem.getImageItemGrpTag()).setGroupSetCheckBox(false);
            }
            GlobalVarsAndFunction.file_To_Be_Deleted_Exact.clear();
            GlobalVarsAndFunction.size_Of_File_Exact = 0;
            return;
        }
        for (int i2 = 0; i2 < GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size(); i2++) {
            try {
                ImagesItem imageItem2 = GlobalVarsAndFunction.file_To_Be_Deleted_Similar.get(i2);
                imageItem2.setImageCheckBox(false);
                GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(imageItem2.getImageItemGrpTag()).setGroupSetCheckBox(false);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            }
        }
        GlobalVarsAndFunction.file_To_Be_Deleted_Similar.clear();
        GlobalVarsAndFunction.size_Of_File_Similar = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if ((requestCode == SupportClass.ANDROID_Q_DELETE_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_DELETE_REQUEST_CODE)) {
            deletePendingMedia(requestCode, resultCode);

        } else if (requestCode == GlobalVarsAndFunction.REQUEST_CODE_OPEN_DOCUMENT_TREE && resultCode == -1 && intent.getData() != null) {
            DocumentFile fromTreeUri = DocumentFile.fromTreeUri(this, intent.getData());
            if (CommonUsed.isSelectedStorageAccessFrameWorkPathIsProper(getApplicationContext(), fromTreeUri.getName())) {
                GlobalVarsAndFunction.setStorageAccessFrameWorkURIPermission(getApplicationContext(), String.valueOf(intent.getData()));
                if (GlobalVarsAndFunction.getTabSelected() == 0) {
                    new DeleteSelectedFile(displayDuplicateContext, this, getString(R.string.Deleting_exact_duplicate_photos), GlobalVarsAndFunction.file_To_Be_Deleted_Exact, GlobalVarsAndFunction.getGroupOfDuplicatesExact()).execute();
                } else {
                    new DeleteSelectedFile(displayDuplicateContext, this, getString(R.string.Deleting_similar_duplicate_photos), GlobalVarsAndFunction.file_To_Be_Deleted_Similar, GlobalVarsAndFunction.getGroupOfDuplicatesSimilar()).execute();
                }
            }
        }
    }

    private void showSimilarImagesDeleteAlertMessage() {
        if (SupportClass.isExternalStoragePermissionGranted(ActPhotoList.this)) {
            startSimilarDeletionAfterPermissionParentGranted();
        } else {
            SupportClass.showTakeWritePermissionDialog(ActPhotoList.this);
        }
    }

    private void startSimilarDeletionAfterPermissionParentGranted() {
        if (GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() == 1) {
            btnDeleteDuplicatePhotos.setEnabled(true);
            deleteAlertPopUp(getString(R.string.Are_you_sure_you_want_to_delete_selected_similar_photo));
            return;
        }
        btnDeleteDuplicatePhotos.setEnabled(true);
        deleteAlertPopUp(getString(R.string.Are_you_sure_you_want_to_delete_selected_similar_photos));
    }

    private String markedStringSimilar() {
        return getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")";
    }

    private String markedStringExact() {
        return getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")";
    }

    private void cancelEveryDayNotification() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(requestCode, strArr, iArr);
        if (requestCode == 591) {
            if (SupportClass.isExternalStoragePermissionGranted(this)) {
                if (GlobalVarsAndFunction.getTabSelected() == 0) {
                    startExactDeletionAfterPermissionParentGranted();
                } else {
                    startSimilarDeletionAfterPermissionParentGranted();
                }
            } else {
                btnDeleteDuplicatePhotos.setEnabled(true);
                Toast.makeText(this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonUsed.logmsg("In onresume Display Duplicate images!!!");
        cancelEveryDayNotification();
    }

    @Override
    public void photosCleanedExact(int i) {
        int photoCleaned = GlobalVarsAndFunction.getPhotoCleaned(displayDuplicateContext);
        CommonUsed.logmsg("Number of digits in Photo cleaned: " + GlobalVarsAndFunction.checkNumberOfDigits(photoCleaned));
        GlobalVarsAndFunction.setPhotoCleaned(displayDuplicateContext, photoCleaned);
    }

    @Override
    public void photosCleanedSimilar(int i) {
        GlobalVarsAndFunction.setPhotoCleaned(displayDuplicateContext, GlobalVarsAndFunction.getPhotoCleaned(displayDuplicateContext));
    }

    @Override
    public void updateDuplicateFoundExact(int i) {
        CommonUsed.logmsg("Update Duplicate Found: " + i);
        tDuplicateFound.setText(getString(R.string.Duplicates_Found) + " " + i);
    }

    @Override
    public void updateDuplicateFoundSimilar(int i) {
        tDuplicateFound.setText(getString(R.string.Duplicates_Found) + " " + GlobalVarsAndFunction.getTotalDuplicatesSimilar());
    }

    @Override
    public void updateMarkedExact() {
        CommonUsed.logmsg("Marked: " + GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
        tMarked.setText(getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
    }

    @Override
    public void updateMarkedSimilar() {
        CommonUsed.logmsg("Marked: " + GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
        tMarked.setText(getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTag = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str, String str2) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(str);
            mFragmentTag.add(str2);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return mFragmentTitleList.get(i);
        }
    }
}
