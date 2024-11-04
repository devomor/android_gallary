package com.photo.photography.duplicatephotos.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.duplicatephotos.AppDataBaseHandler;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.pixelsrelated.RGBObject;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GlobalVarsAndFunction {
    public static final int CURRENT_OS_VERSION = Build.VERSION.SDK_INT;
    public static final String APP_THEME_PREF = "appThemePref";
    public static final String APP_THEME = "appTheme";
    public static final int TESTING_LOOP_STOP_COUNT = 0;
    private static final String VERSION_NAME_KEY = "VersionName";
    private static final String VERSION_NAME_STORED_BY_PREF = "VERSIONNAME_Preference";
    public static String CANCEL_FLAG = "cancelFlag";
    public static String DATE_DOWN = "dateDown";
    public static String DATE_UP = "dateUp";
    public static boolean DEVICE_UNLOCK = false;
    public static boolean EVERY_DAY_SCAN_EXACT = false;
    public static boolean EVERY_DAY_SCAN_SIMILAR = false;
    public static String INSTALLATION_DAY_PREF = "installationDayPref";
    public static boolean IS_DEVICE_ACTIVE = false;
    public static String LOCK_EXACT_PHOTOS = "lockExactPhotos";
    public static String LOCK_EXACT_PHOTOS_PREF = "lockExactPhotosPref";
    public static String LOCK_FIRST_TIME_FLAG_EXACT = "lockFirstTimeFlagExact";
    public static String LOCK_FIRST_TIME_FLAG_EXACT_PREF = "lockFirstTimeFlagExactPref";
    public static String LOCK_FIRST_TIME_FLAG_SIMILAR = "lockFirstTimeFlagSimilar";
    public static String LOCK_FIRST_TIME_FLAG_SIMILAR_PREF = "lockFirstTimeFlagSimilarPref";
    public static String LOCK_SIMILAR_PHOTOS = "lockSimilarPhotos";
    public static String LOCK_SIMILAR_PHOTOS_PREF = "lockSimilarPhotosPref";
    public static String MATCHING_LEVEL = "matchingLevel";
    public static String MATCHING_LEVEL_PREF = "matchingLevelPref";
    public static String NOTIFICATION_LIMIT = "notificationLimit";
    public static String NOTIFICATION_LIMIT_PREF = "notificationLimitPref";
    public static String NOTIFY_DUPES = "notifyDupes";
    public static String NOTIFY_DUPLICATE_PREF = "notifyDupesPref";
    public static boolean ONE_TIME_POPUP = false;
    public static String ONE_TIME_SETTING_INSTALLATION = "oneTimeSettingInstallation";
    public static String PHOTO_CLEANED = "photoCleaned";
    public static String PHOTO_CLEANED_PREF = "photoCleanedPref";
    public static boolean POP_ONLY_ONCE = false;
    public static int REQUEST_CODE_OPEN_DOCUMENT_TREE = 432;
    public static int RGB_CUTOFF_1TO8 = 10;
    public static int RGB_CUTOFF_9 = 45;
    public static boolean SCANNING_FLAG_EXACT = false;
    public static boolean SCANNING_FLAG_SIMILAR = false;
    public static boolean SHOW_REGAIN_POP_UP_ONLY_ON_SCAN_EXACT = false;
    public static boolean SHOW_REGAIN_POP_UP_ONLY_ON_SCAN_SIMILAR = false;
    public static String SIZE_DOWN = "sizeDown";
    public static String SIZE_UP = "sizeUp";
    public static String SORT_BY = "sortBy";
    public static String SORT_BY_PREF = "sortByPref";
    public static int THRESHOLD_COUNT_SEG_1TO8 = 18;
    public static int THRESHOLD_COUNT_SEG_9 = 23;
    public static String WALK_THROUGH_SCREEN = "walkThroughScreen";
    public static String WALK_THROUGH_SCREEN_PREF = "walkThroughScreenPref";
    public static ArrayList<ImagesItem> file_To_Be_Deleted_Exact = new ArrayList<>();
    public static ArrayList<ImagesItem> file_To_Be_Deleted_Preview_Exact = new ArrayList<>();
    public static ArrayList<ImagesItem> file_To_Be_Deleted_Preview_Similar = new ArrayList<>();
    public static ArrayList<ImagesItem> file_To_Be_Deleted_Similar = new ArrayList<>();
    public static int index = 0;
    public static long size_Of_File_Exact = 0;
    public static long size_Of_File_Similar = 0;
    private static String MemoryRegainedExact = null;
    private static String MemoryRegainedSimilar = null;
    private static long MemoryRegainedSimilarInLong = 0;
    private static long MemoryRegainedExactInLong = 0;
    private static int TotalDuplicatesExact = 0;
    private static int TotalDuplicatesSimilar = 0;
    private static int exactDuplicateInOneDay = 0;
    private static List<IndividualGroups> groupOfDuplicatesExact = null;
    private static List<IndividualGroups> groupOfDuplicatesSimilar = null;
    private static int similarDuplicatesInOneDay = 0;
    private static int tabSelected;

    public static boolean compareRgbColors(int i, int i2, int i3) {
        return i2 <= i + i3 && i - i3 < i2;
    }

    public static SharedPreferences getWalkThroughScreenPref(Context context) {
        return context.getSharedPreferences(WALK_THROUGH_SCREEN_PREF, 0);
    }

    public static int getWalkThroughScreenCount(Context context) {
        return getWalkThroughScreenPref(context).getInt(WALK_THROUGH_SCREEN, 0);
    }

    public static void setWalkThroughScreenCount(Context context, int i) {
        getWalkThroughScreenPref(context).edit().putInt(WALK_THROUGH_SCREEN, i).commit();
    }

    // Create App theme preference
    public static SharedPreferences getAppThemePref(Context context) {
        return context.getSharedPreferences(APP_THEME_PREF, 0);
    }

    // Get App theme preference
    public static int getAppTheme(Context context) {
        return getAppThemePref(context).getInt(APP_THEME, 0);
    }

    // Set App theme preference
    public static void setAppTheme(Context context, int i) {
        getAppThemePref(context).edit().putInt(APP_THEME, i).commit();
    }

    public static SharedPreferences currentMatchingLevel(Context context) {
        return context.getSharedPreferences(MATCHING_LEVEL_PREF, 0);
    }

    public static int getCurrentMatchingLevel(Context context) {
        return currentMatchingLevel(context).getInt(MATCHING_LEVEL, 5);
    }

    public static void setCurrentMatchingLevel(Context context, int i) {
        currentMatchingLevel(context).edit().putInt(MATCHING_LEVEL, i).commit();
    }

    public static SharedPreferences NotificationLimitPref(Context context) {
        return context.getSharedPreferences(NOTIFICATION_LIMIT_PREF, 0);
    }

    public static int getNotificationLimit(Context context) {
        return NotificationLimitPref(context).getInt(NOTIFICATION_LIMIT, 3);
    }

    public static void setNotificationLimit(Context context, int i) {
        NotificationLimitPref(context).edit().putInt(NOTIFICATION_LIMIT, i).commit();
    }

    public static SharedPreferences sortByPref(Context context) {
        return context.getSharedPreferences(SORT_BY_PREF, 0);
    }

    public static String getSortBy(Context context) {
        return sortByPref(context).getString(SORT_BY, SIZE_DOWN);
    }

    public static SharedPreferences getPhotoCleanedPref(Context context) {
        return context.getSharedPreferences(PHOTO_CLEANED_PREF, 0);
    }

    public static int getPhotoCleaned(Context context) {
        return getPhotoCleanedPref(context).getInt(PHOTO_CLEANED, 0);
    }

    public static void setPhotoCleaned(Context context, int i) {
        getPhotoCleanedPref(context).edit().putInt(PHOTO_CLEANED, i).commit();
    }

    public static SharedPreferences getCancelAsyncPref(Context context) {
        return context.getSharedPreferences("cancelAsyncProgress", 0);
    }

    public static boolean getCancelFlag(Context context) {
        return getCancelAsyncPref(context).getBoolean(CANCEL_FLAG, false);
    }

    public static void setCancelFlag(Context context, boolean z) {
        getCancelAsyncPref(context).edit().putBoolean(CANCEL_FLAG, z).commit();
    }

    public static SharedPreferences getInstallationDayPref(Context context) {
        return context.getSharedPreferences(INSTALLATION_DAY_PREF, 0);
    }

    public static void setInitiateTimeForAlarm(Context context, long j) {
        getInstallationDayPref(context).edit().putLong("ALARM_INITIATE_TIME_IN_MILLISECOND", j).commit();
    }

    public static long getInitiateTimeForAlarm(Context context) {
        return getInstallationDayPref(context).getLong("ALARM_INITIATE_TIME_IN_MILLISECOND", 0);
    }

    public static void setInitiateScanningPermission(Context context, boolean z) {
        getInstallationDayPref(context).edit().putBoolean("ScheduleScanning Initiated", z).commit();
    }

    public static boolean getInitiateScanningPermission(Context context) {
        return getInstallationDayPref(context).getBoolean("ScheduleScanning Initiated", false);
    }

    public static SharedPreferences getNotifyDupesPref(Context context) {
        return context.getSharedPreferences(NOTIFY_DUPLICATE_PREF, 0);
    }

    public static boolean getNotifyDupesFlag(Context context) {
        return getNotifyDupesPref(context).getBoolean(NOTIFY_DUPES, false);
    }

    public static void setNotifyDupesFlag(Context context, boolean z) {
        getNotifyDupesPref(context).edit().putBoolean(NOTIFY_DUPES, z).commit();
    }

    public static boolean getOneTimeSettingInstallationFlag(Context context) {
        return getNotifyDupesPref(context).getBoolean(ONE_TIME_SETTING_INSTALLATION, false);
    }

    public static void setOneTimeSettingInstallationFlag(Context context, boolean z) {
        getNotifyDupesPref(context).edit().putBoolean(ONE_TIME_SETTING_INSTALLATION, z).commit();
    }

    public static SharedPreferences getLockFirstTimeFlagExactPref(Context context) {
        return context.getSharedPreferences(LOCK_FIRST_TIME_FLAG_EXACT_PREF, 0);
    }

    public static boolean getLockFirstTimeExactFlag(Context context) {
        return getLockFirstTimeFlagExactPref(context).getBoolean(LOCK_FIRST_TIME_FLAG_EXACT, true);
    }

    public static void setLockFirstTimeExactFlag(Context context, boolean z) {
        getLockFirstTimeFlagExactPref(context).edit().putBoolean(LOCK_FIRST_TIME_FLAG_EXACT, z).commit();
    }

    public static SharedPreferences getLockFirstTimeFlagSimilarPref(Context context) {
        return context.getSharedPreferences(LOCK_FIRST_TIME_FLAG_SIMILAR_PREF, 0);
    }

    public static boolean getLockFirstTimeSimilarFlag(Context context) {
        return getLockFirstTimeFlagSimilarPref(context).getBoolean(LOCK_FIRST_TIME_FLAG_SIMILAR, true);
    }

    public static void setLockFirstTimeSimilarFlag(Context context, boolean z) {
        getLockFirstTimeFlagSimilarPref(context).edit().putBoolean(LOCK_FIRST_TIME_FLAG_SIMILAR, z).commit();
    }

    public static SharedPreferences getLockExactPhotosPref(Context context) {
        return context.getSharedPreferences(LOCK_EXACT_PHOTOS_PREF, 0);
    }

    public static ArrayList<ImagesItem> getLockExactPhotos(Context context) {
        try {
            return (ArrayList) new Gson().fromJson(getLockExactPhotosPref(context).getString(LOCK_EXACT_PHOTOS, "{\"empty\":\"Lock\"}"), new TypeToken<ArrayList<ImagesItem>>() {
            }.getType());
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            CommonUsed.logmsg("Exception for exact first time: " + e.getMessage());
            return null;
        }
    }

    public static void setLockExactPhotos(Context context, ArrayList<ImagesItem> arrayList) {
        getLockExactPhotosPref(context).edit().putString(LOCK_EXACT_PHOTOS, new Gson().toJson(arrayList)).commit();
    }

    public static SharedPreferences getLocKSimilarPhotosPref(Context context) {
        return context.getSharedPreferences(LOCK_SIMILAR_PHOTOS_PREF, 0);
    }

    public static ArrayList<ImagesItem> getLockSimilarPhotos(Context context) {
        try {
            return (ArrayList) new Gson().fromJson(getLocKSimilarPhotosPref(context).getString(LOCK_SIMILAR_PHOTOS, "{\"empty\":\"Lock\"}"), new TypeToken<ArrayList<ImagesItem>>() {
            }.getType());
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            CommonUsed.logmsg("Exception for similar first time: " + e.getMessage());
            return null;
        }
    }

    public static Map<String, ImagesItem> getLockSimilarMapPhotos(Context context) {
        try {
            return (Map) new Gson().fromJson(getLocKSimilarPhotosPref(context).getString(LOCK_SIMILAR_PHOTOS, "{\"empty\":\"Lock\"}"), new TypeToken<Map<String, ImagesItem>>() {
            }.getType());
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            CommonUsed.logmsg("Exception for similar first time: " + e.getMessage());
            return null;
        }
    }

    public static void setLockSimilarPhotos(Context context, ArrayList<ImagesItem> arrayList) {
        getLocKSimilarPhotosPref(context).edit().putString(LOCK_SIMILAR_PHOTOS, new Gson().toJson(arrayList)).commit();
    }

    public static void setLockSimilarMapPhotos(Context context, Map<String, ImagesItem> map) {
        getLocKSimilarPhotosPref(context).edit().putString(LOCK_SIMILAR_PHOTOS, new Gson().toJson(map)).commit();
    }

    public static List<IndividualGroups> getGroupOfDuplicatesSimilar() {
        return groupOfDuplicatesSimilar;
    }

    public static void setGroupOfDuplicatesSimilar(Context sContext, List<IndividualGroups> list, boolean isNew) {
        if (isNew) {
            try {
                AppDataBaseHandler dbHandler = new AppDataBaseHandler(sContext);
                dbHandler.insertUpdateGroupList(list, true);
                groupOfDuplicatesSimilar = dbHandler.getAllItems(true);
                setLastScanningDateTime();
            } catch (Exception exc) {
                Log.e("TAG", exc.toString());
            }
        } else {
            groupOfDuplicatesSimilar = list;
        }
    }

    private static void setLastScanningDateTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor editor = preferences.edit();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss aa");
        editor.putString(SupportClass.LAST_SCAN_DATE_TIME, simpleDateFormat.format(new Date()));
        editor.commit();
    }

    public static List<IndividualGroups> getGroupOfDuplicatesExact() {
        return groupOfDuplicatesExact;
    }

    public static void setGroupOfDuplicatesExact(Context sContext, List<IndividualGroups> list, boolean isNew) {
        if (isNew) {
            try {
                AppDataBaseHandler dbHandler = new AppDataBaseHandler(sContext);
                dbHandler.insertUpdateGroupList(list, false);
                groupOfDuplicatesExact = dbHandler.getAllItems(false);
                setLastScanningDateTime();
            } catch (Exception exc) {
                Log.e("TAG", exc.toString());
            }
        } else {
            groupOfDuplicatesExact = list;
        }
    }

    public static int getExactDuplicateInOneDay() {
        return exactDuplicateInOneDay;
    }

    public static void setExactDuplicateInOneDay(int i) {
        exactDuplicateInOneDay = i;
    }

    public static int getSimilarDuplicatesInOneDay() {
        return similarDuplicatesInOneDay;
    }

    public static void setSimilarDuplicatesInOneDay(int i) {
        similarDuplicatesInOneDay = i;
    }

    public static String getMemoryRegainedExact() {
        return MemoryRegainedExact;
    }

    public static void setMemoryRegainedExact(String str) {
        MemoryRegainedExact = str;
    }

    public static int getTotalDuplicatesExact() {
        return TotalDuplicatesExact;
    }

    public static void setTotalDuplicatesExact(int i) {
        TotalDuplicatesExact = i;
    }

    public static long getMemoryRegainedExactInLong() {
        return MemoryRegainedExactInLong;
    }

    public static void setMemoryRegainedExactInLong(long memoryRegainedExactInLong) {
        MemoryRegainedExactInLong = memoryRegainedExactInLong;
    }

    public static String getMemoryRegainedSimilar() {
        return MemoryRegainedSimilar;
    }

    public static void setMemoryRegainedSimilar(String str) {
        MemoryRegainedSimilar = str;
    }

    public static long getMemoryRegainedSimilarInLong() {
        return MemoryRegainedSimilarInLong;
    }

    public static void setMemoryRegainedSimilarInLong(long j) {
        MemoryRegainedSimilarInLong = j;
    }

    public static int getTotalDuplicatesSimilar() {
        return TotalDuplicatesSimilar;
    }

    public static void setTotalDuplicatesSimilar(int i) {
        TotalDuplicatesSimilar = i;
    }

    public static String getStringSizeLengthFile(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float f = (float) j;
        if (f < 1048576.0f) {
            return decimalFormat.format((double) (f / 1024.0f)) + " KB";
        } else if (f < 1.07374182E9f) {
            return decimalFormat.format((double) (f / 1048576.0f)) + " MB";
        } else if (f >= 1.09951163E12f) {
            return "";
        } else {
            return decimalFormat.format((double) (f / 1.07374182E9f)) + " GB";
        }
    }

    public static double getDoubleSizeLengthFile(long j) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            Log.e("getDoubleSizeLengthFile", "---asdfasdf------size---" + j);
            double d = (double) j;
            if (d < 1048576.0d) {
                StringBuilder sb = new StringBuilder();
                sb.append("--sdfsd--1----");
                Double.isNaN(d);
                double d2 = d / 1024.0d;
                sb.append(decimalFormat.format(d2));
                Log.e("getDoubleSizeLengthFile", sb.toString());
                return Double.valueOf(decimalFormat.format(d2)).doubleValue();
            } else if (d < 1.073741824E9d) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("--sdfsd---2----");
                Double.isNaN(d);
                double d3 = d / 1048576.0d;
                sb2.append(decimalFormat.format(d3));
                Log.e("getDoubleSizeLengthFile", sb2.toString());
                return Double.valueOf(decimalFormat.format(d3)).doubleValue();
            } else if (d >= 1.099511627776E12d) {
                return 0.0d;
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("--sdfsd---3----");
                Double.isNaN(d);
                double d4 = d / 1.073741824E9d;
                sb3.append(decimalFormat.format(d4));
                Log.e("getDoubleSizeLengthFile", sb3.toString());
                return Double.valueOf(decimalFormat.format(d4)).doubleValue();
            }
        } catch (Exception unused) {
            return 0.0d;
        }
    }

    public static int getRedColor(int i) {
        return Color.red(i);
    }

    public static int getGreenColor(int i) {
        return Color.green(i);
    }

    public static int getBlueColor(int i) {
        return Color.blue(i);
    }

    public static RGBObject getRgbStringValue(int i) {
        RGBObject rgbObj = new RGBObject();
        rgbObj.setReb(getRedColor(i));
        rgbObj.setGreen(getGreenColor(i));
        rgbObj.setBlue(getBlueColor(i));
        return rgbObj;
    }

    public static boolean compareRgb(List<RGBObject> list, List<RGBObject> list2) {
        List<RGBObject> subList = list.subList(0, 32);
        List<RGBObject> subList2 = list.subList(32, 113);
        List<RGBObject> subList3 = list2.subList(0, 32);
        List<RGBObject> subList4 = list2.subList(32, 113);
        Log.e("compareRgb", "-----sdfsd-------threshold---" + THRESHOLD_COUNT_SEG_1TO8);
        return compareSegments(subList, subList3, THRESHOLD_COUNT_SEG_1TO8, 31) && compareSegments(subList2, subList4, THRESHOLD_COUNT_SEG_9, 80);
    }

    public static boolean compareSegments(List<RGBObject> list, List<RGBObject> list2, int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 <= i2; i4++) {
            RGBObject rgbObj = list.get(i4);
            RGBObject rgbObj2 = list2.get(i4);
            boolean compareRgbColors = compareRgbColors(rgbObj.getReb(), rgbObj2.getReb(), RGB_CUTOFF_1TO8);
            boolean compareRgbColors2 = compareRgbColors(rgbObj.getGreen(), rgbObj2.getGreen(), RGB_CUTOFF_1TO8);
            boolean compareRgbColors3 = compareRgbColors(rgbObj.getBlue(), rgbObj2.getBlue(), RGB_CUTOFF_1TO8);
            if (compareRgbColors && compareRgbColors2 && compareRgbColors3) {
                i3++;
            }
        }
        return i3 >= i;
    }

    public static void setCorrespondingValueForMatchingLevels(int i) {
        switch (i) {
            case 1:
                CommonUsed.logmsg("Percentage : 10%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 15;
                THRESHOLD_COUNT_SEG_9 = 20;
                return;
            case 2:
                CommonUsed.logmsg("Percentage : 20%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 16;
                THRESHOLD_COUNT_SEG_9 = 21;
                return;
            case 3:
                CommonUsed.logmsg("Percentage : 30%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 17;
                THRESHOLD_COUNT_SEG_9 = 22;
                return;
            case 4:
                CommonUsed.logmsg("Percentage : 40%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 18;
                THRESHOLD_COUNT_SEG_9 = 23;
                return;
            case 5:
                CommonUsed.logmsg("Percentage : 50%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 19;
                THRESHOLD_COUNT_SEG_9 = 24;
                return;
            case 6:
                CommonUsed.logmsg("Percentage : 60%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 25;
                THRESHOLD_COUNT_SEG_9 = 65;
                return;
            case 7:
                CommonUsed.logmsg("Percentage : 70%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 26;
                THRESHOLD_COUNT_SEG_9 = 68;
                return;
            case 8:
                CommonUsed.logmsg("Percentage : 80%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 28;
                THRESHOLD_COUNT_SEG_9 = 70;
                return;
            case 9:
                CommonUsed.logmsg("Percentage : 90%");
                RGB_CUTOFF_1TO8 = 10;
                RGB_CUTOFF_9 = 45;
                THRESHOLD_COUNT_SEG_1TO8 = 30;
                THRESHOLD_COUNT_SEG_9 = 75;
                return;
            case 10:
                CommonUsed.logmsg("Percentage : 100%");
                RGB_CUTOFF_1TO8 = 15;
                RGB_CUTOFF_9 = 70;
                THRESHOLD_COUNT_SEG_1TO8 = 32;
                THRESHOLD_COUNT_SEG_9 = 81;
                return;
            default:
                return;
        }
    }

    public static void deletePhotoUsingSAFPermission(Context context, String str) {
        File file = new File(str);
        if (Build.VERSION.SDK_INT < 21) {
            normalFunctionForDeleteFile(context, file);
        } else if (CommonUsed.getSDCardPath(context) != null) {
            String[] split = file.getPath().split("\\/");
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(split));
            try {
                if (arrayList.contains(CommonUsed.getExternalStorageName(context))) {
                    DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, Uri.parse(getStorageAccessFrameWorkURIPermission(context)));
                    for (int i = 3; i < arrayList.size(); i++) {
                        if (fromTreeUri != null) {
                            fromTreeUri = fromTreeUri.findFile((String) arrayList.get(i));
                        }
                    }
                    if (fromTreeUri == null) {
                        normalFunctionForDeleteFile(context, file);
                    } else if (fromTreeUri.delete()) {
                        updateDeletionCount(context, 1);
                    } else {
                        normalFunctionForDeleteFile(context, file);
                    }
                } else {
                    normalFunctionForDeleteFile(context, file);
                }
            } catch (Exception unused) {
                normalFunctionForDeleteFile(context, file);
            }
        } else {
            normalFunctionForDeleteFile(context, file);
        }
    }

    public static void updateDeletionCount(Context context, int i) {
        setPhotoCleaned(context, getPhotoCleaned(context) + i);
        if (getTabSelected() != 0) {
            setTotalDuplicatesSimilar(getTotalDuplicatesSimilar() - i);
            setMemoryRegainedSimilar("");
            return;
        }
        setTotalDuplicatesExact(getTotalDuplicatesExact() - i);
        setMemoryRegainedExact("");
    }

    public static void normalFunctionForDeleteFile(Context context, File file) {
        File file2 = new File(file.getAbsolutePath());
        if (!file2.exists()) {
            return;
        }
        if (file2.delete()) {
            deleteFileFromMediaStore(context, context.getContentResolver(), file2);
            updateDeletionCount(context, 1);
            return;
        }
        deleteFileFromMediaStore(context, context.getContentResolver(), file2);
        updateDeletionCount(context, 0);
    }

    public static long getFileSize(String str) {
        return new File(str).length();
    }

    @SuppressLint("LongLogTag")
    public static void deleteFileFromMediaStore(Context context, ContentResolver contentResolver, File file) {
        if (file != null) {
            String str;
            try {
                str = file.getCanonicalPath();
            } catch (IOException unused) {
                str = file.getAbsolutePath();
            }
            try {
                Cursor query = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, "_data = ?", new String[]{file.getAbsolutePath()}, null);
                if (query.moveToFirst()) {
                    contentResolver.delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow(MediaStore.Images.Media._ID))), null, null);
                }
                query.close();
                callBroadCast(context);
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                if (contentResolver.delete(uri, "_data=?", new String[]{str}) == 0) {
                    String absolutePath = file.getAbsolutePath();
                    if (!absolutePath.equals(str)) {
                        contentResolver.delete(uri, "_data=?", new String[]{absolutePath});
                    }
                }
            } catch (Exception unused2) {
                Log.e("DeleteFileWithMediaStore", "Delete Error " + unused2.getMessage());
            }
        }
    }

    public static void callBroadCast(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                MediaScannerConnection.scanFile(context, new String[]{new VaultFileUtil(context).getDirPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                        Log.e("ExternalStorage", "Scanned " + str + ":");
                        StringBuilder sb = new StringBuilder();
                        sb.append("-> uri=");
                        sb.append(uri);
                        Log.e("ExternalStorage", sb.toString());
                    }
                });
                return;
            }
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + new VaultFileUtil(context).getDirPath())));
        } catch (Exception unused) {
        }
    }

    public static void addSizeExact(long j) {
        size_Of_File_Exact += j;
    }

    public static void subSizeExact(long j) {
        size_Of_File_Exact -= j;
    }

    public static void addSizeSimilar(long j) {
        size_Of_File_Similar += j;
    }

    public static void subSizeSimilar(long j) {
        size_Of_File_Similar -= j;
    }

    public static String sizeInString() {
        if (getTabSelected() != 0) {
            return getStringSizeLengthFile(size_Of_File_Similar);
        }
        return getStringSizeLengthFile(size_Of_File_Exact);
    }

    public static double sizeInDouble() {
        if (getTabSelected() != 0) {
            Log.e("sizeInDouble", "-22--asdfasdfs-------" + size_Of_File_Similar);
            return getDoubleSizeLengthFile(size_Of_File_Similar);
        }
        Log.e("sizeInDouble", "-11--asdfasdfs-------" + size_Of_File_Exact);
        return getDoubleSizeLengthFile(size_Of_File_Exact);
    }

    public static ImageLoader getImageLoadingInstance() {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_rsz_empty_photo)
                .showImageForEmptyUri(R.drawable.icon_rsz_empty_photo)
                .showImageOnFail(R.drawable.icon_rsz_empty_photo)
                .cacheInMemory(true)
                .cacheOnDisc(false)
                .considerExifParams(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .preProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {
                        return GlobalVarsAndFunction.getResizeBitmap(bitmap);
                    }
                }).build();
    }

    public static Bitmap getResizeBitmap(Bitmap bitmap) {
        float f = (float) 100;
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float max = Math.max(f / width, f / height);
        float f2 = width * max;
        float f3 = max * height;
        float f4 = (f - f2) / 2.0f;
        float f5 = (f - f3) / 2.0f;
        RectF rectF = new RectF(f4, f5, f2 + f4, f3 + f5);
        if (bitmap == null || bitmap.getConfig() == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(100, 100, bitmap.getConfig());
        if (createBitmap != null) {
            new Canvas(createBitmap).drawBitmap(bitmap, (Rect) null, rectF, (Paint) null);
        }
        return createBitmap;
    }

    public static void configureImageLoader(ImageLoader imageLoader, Activity activity) {
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
            imageLoader.clearDiscCache();
        }
    }

    public static int getTabSelected() {
        return tabSelected;
    }

    public static void setTabSelected(int i) {
        tabSelected = i;
    }

    public static int checkNumberOfDigits(int i) {
        return ((int) Math.log10((double) i)) + 1;
    }

    public static boolean checkStorage(Context context) {
        return context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME}, null, null, null) != null;
    }

    public static String getImageResolution(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int i = options.outHeight;
        int i2 = options.outWidth;
        return "" + i2 + " x " + i;
    }

    public static long getDateAndTime(String str) {
        return new File(str).lastModified();
    }

    public static List<ImagesItem> sortByDateAscending(List<ImagesItem> list) {
        Collections.sort(list, new Comparator<ImagesItem>() {
            public int compare(ImagesItem imageItem, ImagesItem imageItem2) {
                return new Date(imageItem2.getDateAndTime()).compareTo(new Date(imageItem.getDateAndTime()));
            }
        });
        Log.e("sortByDateAscending", String.valueOf(list));
        return list;
    }

    public static List<ImagesItem> sortBySizeAscending(List<ImagesItem> list) {
        Collections.sort(list, new Comparator<ImagesItem>() {
            public int compare(ImagesItem imageItem, ImagesItem imageItem2) {
                return Long.valueOf(imageItem2.getSizeOfTheFile()).compareTo(Long.valueOf(imageItem.getSizeOfTheFile()));
            }
        });
        Log.e("sortByDateAscending", String.valueOf(list));
        return list;
    }

    public static List<ImagesItem> sortByDateDescending(List<ImagesItem> list) {
        Collections.sort(list, new Comparator<ImagesItem>() {
            public int compare(ImagesItem imageItem, ImagesItem imageItem2) {
                return new Date(imageItem.getDateAndTime()).compareTo(new Date(imageItem2.getDateAndTime()));
            }
        });
        Log.e("sortByDateAscending", String.valueOf(list));
        return list;
    }

    public static List<ImagesItem> sortBySizeDescending(List<ImagesItem> list) {
        Collections.sort(list, new Comparator<ImagesItem>() {
            public int compare(ImagesItem imageItem, ImagesItem imageItem2) {
                return Long.valueOf(imageItem.getSizeOfTheFile()).compareTo(Long.valueOf(imageItem2.getSizeOfTheFile()));
            }
        });
        Log.e("sortByDateAscending", String.valueOf(list));
        return list;
    }

    public static int getNotificationIcon() {
        return Build.VERSION.SDK_INT >= 21 ? R.drawable.icon_app : R.drawable.icon_app;
    }

    public static boolean properScheduleStartScanningFunction(Context context, long j) {
        long time = new Date(j).getTime() - new Date(getInitiateTimeForAlarm(context)).getTime();
        long j2 = (time / 1000) % 60;
        long j3 = (time / 60000) % 60;
        long j4 = (time / 3600000) % 24;
        long j5 = time / 86400000;
        return j5 > 0 && j5 % 15 == 0;
    }

    public static SharedPreferences getSAFSharedPreference(Context context) {
        return context.getSharedPreferences("SAF_PREFERENCE", 0);
    }

    public static void setStorageAccessFrameWorkURIPermission(Context context, String str) {
        getSAFSharedPreference(context).edit().putString("STORAGE_ACCESS_FRAMEWORK_PERMISSION_PATH", str).commit();
    }

    public static String getStorageAccessFrameWorkURIPermission(Context context) {
        return getSAFSharedPreference(context).getString("STORAGE_ACCESS_FRAMEWORK_PERMISSION_PATH", null);
    }

    public static SharedPreferences getVersionStorePreference(Context context) {
        return context.getSharedPreferences(VERSION_NAME_STORED_BY_PREF, 0);
    }

    public static String getVersionName(Context context) {
        return getVersionStorePreference(context).getString(VERSION_NAME_KEY, "2.1.0.34");
    }

    public static void setVersionName(Context context, String str) {
        getVersionStorePreference(context).edit().putString(VERSION_NAME_KEY, str).commit();
    }

    public static int getTotalDuplicate(List<IndividualGroups> eDataList) {
        int p = 0;
        for (int i = 0; i < eDataList.size(); i++) {
            p = p + eDataList.get(i).getIndividualGrpOfDupes().size();
        }
        return p - eDataList.size();
    }

    public static long getTotalDuplicateMemoryRegain(List<IndividualGroups> eDataList) {
        long p = 0;
        for (int i = 0; i < eDataList.size(); i++) {
            for (int j = 0; j < eDataList.get(i).getIndividualGrpOfDupes().size(); j++) {
                if (j != 0) {
                    p = p + eDataList.get(i).getIndividualGrpOfDupes().get(j).getSizeOfTheFile();
                }
            }
        }
        return p - eDataList.size();
    }
}
