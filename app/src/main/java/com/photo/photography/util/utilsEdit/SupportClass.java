package com.photo.photography.util.utilsEdit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.util.AlertDialogHelper;
import com.photo.photography.secure_vault.ActSetupPinLock;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.helper.Constants;
import com.photo.photography.secure_vault.utils.PermissionUtil;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SupportClass {

    public static final String SAVE_APPLICATION_PREF = "savedList";
    public static final String SELECTED_RECOVERY_SECURITY_QUESTION = "recoveryQuestion";
    public static final String SELECTED_RECOVERY_SECURITY_ANSWER = "recoveryAnswer";
    public static final String IS_ENABLE_SECRET_SNAP = "isEnableSecretSnap";
    public static final String LAST_SCAN_DATE_TIME = "lastScanDateTime";
    public static final String PIN_LOCK = "pin_lock";
    public static final String IS_LOCKED = "is_locked";
    public static final String IS_GLOW_PATH = "true";
    public static final String BATTERY_CHARGE_ALERT = "low_bettery_alert";
    public static final String FONT_NAME = "fontName";
    public static final String APP_RATE_BY_USER = "appRateByUser";
    public static final int ANDROID_Q_DELETE_REQUEST_CODE = 123;
    public static final int ANDROID_R_DELETE_REQUEST_CODE = 124;
    public static final int ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE = 125;
    public static final int ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE = 126;
    public static int[] arrMainOptionIcon = {
            R.drawable.e_crop_white,
            R.drawable.e_text_t_white,
            R.drawable.e_stickers_white,
            R.drawable.e_brush_white,
            R.drawable.e_filter_white,
            R.drawable.e_adjust_white};
    public static int[] arrMainOptionIconSelected = {
            R.drawable.e_crop_white,
            R.drawable.e_text_t_white,
            R.drawable.e_stickers_white,
            R.drawable.e_brush_white,
            R.drawable.e_filter_white,
            R.drawable.e_adjust_white};
    public static int[] arrAdjustOptionIcon = {
            R.drawable.e_brightness_white,
            R.drawable.e_contrast_white,
            R.drawable.e_sharpness_white,
            R.drawable.e_satuation_white,
            R.drawable.e_blur_white,
            R.drawable.e_gamma_white,
            R.drawable.e_sepia_white,
            R.drawable.e_vignette_white};
    public static String[] xmlFileName = new String[]{"textbubble000.xml", "textbubble001.xml", "textbubble002.xml",
            "textbubble003.xml", "textbubble004.xml", "textbubble005.xml", "textbubble006.xml",
            "textbubble007.xml", "textbubble008.xml", "textbubble009.xml", "textbubble010.xml",
            "textbubble011.xml", "textbubble012.xml"};
    public static String filePath = ("textbubble" + File.separator);
    public static String overlayRootDir = ("overlay" + File.separator);
    public static String KEY_BUBBLE_IMAGE = "bubbleImage";
    public static String KEY_BUBBLE_OPACITY = "bubbleOpacity";
    public static String KEY_FONT = "font";
    public static String KEY_GUID = "guid";
    public static String KEY_SHADOW = "shadow";
    public static String KEY_STROKE_COLOR = "strokeColor";
    public static String KEY_TEXT_COLOR = "textColor";
    public static String KEY_TEXT_SIZE = "textSize";
    public static String KEY_TEXT_MATRIX = "textMatrix";
    public static String KEY_TEXT_OPACITY = "textOpacity";
    public static String KEY_TEXT_RECT = "textRect";
    public static String KEY_THUMB_IMAGE = "thumbImage";
    public static String KEY_TYPE = "type";
    public static String KEY_VERSION = "version";
    public static USER_ACTION userAction = null;
    public static String IS_PIN_LOCK = "isPinLockEnable";
    public static String IS_VIBRATE_ENABLE = "isVibrateEnable";
    public static String IS_SOUND_ENABLE = "isSoundEnable";
    public static String IS_NOTIFY_NEW_APP_LOCK = "isNotifyNewAppLock";
    public static String IS_LOW_POWER_NOTI_ENABLE = "isLowPowerNotiEnable";
    public static String IS_LOW_MEMORY_NOTI_ENABLE = "isLowMemoryNotiEnable";
    public static String IS_NOTI_ENABLE = "isNotiEnable";
    public static String IS_PHONE_LOCK_ENABLE = "isPhoneLockEnable";
    public static String IS_APP_LOCK_ENABLE = "isAppLockEnable";
    public static String IS_SECRET_EYE = "isSecretEyeEnable";
    public static String IS_DEFAULT_SET = "isDefaultset";
    public static String IS_EMERGENCY_SMS_SET = "isEmergencySMSSet";
    public static String COUNT_AD_SHOW = "adCount";
    public static Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream outStream = null;
            try {
                File file = new File(new VaultFileUtil(MyApp.mContext).getDir(), "snapTrack_" + Calendar.getInstance().getTimeInMillis() + ".jpg");

                outStream = new FileOutputStream(file);
                outStream.write(data);
                outStream.close();
                Log.d("picture taken", "onPictureTaken - wrote bytes: " + data.length);
            } catch (FileNotFoundException e) {
                Log.d("camera 7", "" + e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("camera 8", "" + e);
                e.printStackTrace();
            } finally {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            Log.d("picture taken", "onPictureTaken - jpeg");
        }
    };

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());

    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imageUri.toString());

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }
        return bitmap;
    }

    public static Typeface getTypefaceFromAsset(Context context, String fontName) {
        AssetManager assetManager = context.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, String.format(Locale.US, "fonts/%s", fontName + ".ttf"));
        return typeface;
    }

    public static boolean checkConnection(Context mContext) {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return netInfo != null;
    }

    public static void shareWithOther(Context mContext, String shareMessage, Uri imgUri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name_new));
        if (imgUri != null) {
            sharingIntent.setType("*/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        } else {
            sharingIntent.setType("text/plain");
        }
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.share_using)));
    }

    public static File copyFile(Activity mActivity, String inputPath, String inputFile, String outputPath) {
        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(outputPath, inputFile);

            InputStream in = new FileInputStream(inputPath + File.separator + inputFile);
            OutputStream out = new FileOutputStream(outputFile.getAbsolutePath());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file (You have now copied the file)
            out.flush();
            out.close();

            mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
            return outputFile;
        } catch (IOException exc) {
            Log.e("tag", exc.getMessage());
        }
        return null;
    }

    public static boolean isExternalStoragePermissionGranted(Activity mActivity) {
        /*if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            //For Android 11
            return Environment.isExternalStorageManager();
        } else {
            // For Below
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            return writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        } else {
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(mActivity, WRITE_EXTERNAL_STORAGE);
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(mActivity, READ_EXTERNAL_STORAGE);
            return writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED && readExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static void takeExternalStoragePermission(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(mActivity, new String[]{CAMERA, READ_EXTERNAL_STORAGE}, 591);
        } else {
            ActivityCompat.requestPermissions(mActivity, new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 591);
        }
    }

    public static void showTakeWritePermissionDialog(@NotNull Activity mActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(mActivity.getString(R.string.require_permission_for_this_operation))
                .setCancelable(false)
                .setPositiveButton(mActivity.getString(R.string.give_permission), (dialogInterface, i) -> {
                    takeExternalStoragePermission(mActivity);
                })
                .setNegativeButton(mActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog create = builder.create();
        create.setTitle(mActivity.getString(R.string.grant_permission));
        create.show();
    }

    public static Bitmap decodeSampledBitmapFromFile(String image, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(image, options);
    }

    public static void setFontText(TextView textView) {
        if (!getString(SupportClass.FONT_NAME).isEmpty()) {
            Typeface face1 = Typeface.createFromAsset(MyApp.mContext.getAssets(), "fonts/" + getString(SupportClass.FONT_NAME));
            textView.setTypeface(face1);
        }
    }

    public static String convertArrayListToString(ArrayList<VaultFile> data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        return json;
    }

    public static ArrayList<VaultFile> convertStringTOArraylist(String value) {
        Gson gson = new Gson();
        String json = value;
        if (json.isEmpty())
            return new ArrayList<>();
        return gson.fromJson(json, new TypeToken<List<VaultFile>>() {
        }.getType());
    }

    public static int getImageSize(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        Log.e("imageSize", "" + density);
        if (density <= 0.75) {
            return 250;
        } else if (density > 0.75 && density <= 1) {
            return 300;
        } else if (density > 1 && density <= 1.5) {
            return 370;
        }
        if (density > 1.5 && density <= 2) {
            return 450;
        }
        if (density > 2 && density <= 3) {
            return 500;
        } else {
            return 600;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void createSettingsPrefrences() {
        SharedPreferences settingSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor prefsEditor = settingSharedPrefs.edit();
        prefsEditor.putBoolean(IS_PIN_LOCK, true);
        prefsEditor.putBoolean(IS_VIBRATE_ENABLE, true);
        prefsEditor.putBoolean(IS_SOUND_ENABLE, false);
        prefsEditor.putBoolean(IS_NOTIFY_NEW_APP_LOCK, true);
        prefsEditor.putBoolean(IS_LOW_POWER_NOTI_ENABLE, true);
        prefsEditor.putBoolean(IS_LOW_MEMORY_NOTI_ENABLE, true);
        prefsEditor.putBoolean(IS_NOTI_ENABLE, true);
        prefsEditor.putBoolean(IS_PHONE_LOCK_ENABLE, false);
        prefsEditor.putBoolean(IS_APP_LOCK_ENABLE, true);
        prefsEditor.putBoolean(IS_SECRET_EYE, true);
        prefsEditor.putString(BATTERY_CHARGE_ALERT, "20");
        prefsEditor.putInt(COUNT_AD_SHOW, 0);
        prefsEditor.putBoolean(IS_EMERGENCY_SMS_SET, false);
        prefsEditor.putBoolean(Constants.IS_FIRST_TIME, true);
        prefsEditor.commit();
    }

    public static boolean getSettingsInfo(String strField) {
        SharedPreferences settingSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        boolean result = settingSharedPrefs.getBoolean(strField, false);
        return result;
    }

    public static void setSettingsInfo(String strField, boolean value) {
        SharedPreferences settingSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor prefsEditor = settingSharedPrefs.edit();
        prefsEditor.putBoolean(strField, value);
        prefsEditor.apply();
    }

    public static void saveArraylistSavedApps(ArrayList<String> data, String pref_name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString(pref_name, json);
        prefsEditor.commit();
    }

    public static ArrayList<String> getArraylistSavedApps(String pref_name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(pref_name, "");
        if (json.isEmpty())
            return new ArrayList<>();
        return gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }

    public static int getPrefInt(String strField) {
        SharedPreferences settingSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        int result = settingSharedPrefs.getInt(strField, 0);
        return result;
    }

    public static void setPrefInt(String strField, int value) {
        SharedPreferences settingSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor prefsEditor = settingSharedPrefs.edit();
        prefsEditor.putInt(strField, value);
        prefsEditor.apply();
    }

    public static void saveBoolean(boolean data, String pref_name) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(pref_name, data);
        prefsEditor.commit();
    }

    public static boolean getBoolean(String pref_name) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(MyApp.mContext);
        return appSharedPrefs.getBoolean(pref_name, false);

    }

    public static void saveString(String data, String pref_name) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(pref_name, data);
        prefsEditor.commit();
    }

    public static String getString(String pref_name) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(MyApp.mContext);
        return appSharedPrefs.getString(pref_name, "");

    }

    /**
     * Verify device's API before to load soundpool
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static SoundPool buildSoundPool() {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(25)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = createOldSoundPool();
        }
        return soundPool;
    }

    @SuppressWarnings("deprecation")
    public static SoundPool createOldSoundPool() {
        return new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    }

    public static int getFrontCameraId() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }

    public static void takeSnapShots(Activity mActivity, SurfaceView mCameraView, String appName) {

        if (PermissionUtil.checkCameraPermissionOnly(mActivity)) {
            if (SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                int cameraId = getFrontCameraId();
                if (cameraId != -1 && SupportClass.getSettingsInfo(SupportClass.IS_SECRET_EYE)) {
                    try {
                        Log.d("camera ", "exception ");
                        Camera camera = Camera.open(cameraId);
                        Log.d("camera 0", "exception ");
                        mCameraView.setVisibility(View.VISIBLE);
                        try {
                            camera.setPreviewDisplay(mCameraView.getHolder());
                            Log.d("camera 1", "exception ");
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            Log.d("camera 2", "exception " + e);
                            e.printStackTrace();
                        }
                        Log.d("camera 3", "exception ");
                        camera.startPreview();
                        Log.d("camera 4", "exception ");

                        camera.takePicture(null, null, jpegCallback);
                        mCameraView.setVisibility(View.GONE);
                        Log.d("camera 5", "exception ");
                    } catch (Exception exception) {
                        Log.d("camera 6", "exception " + exception);
                    }
                }
            } else {
                showTakeWritePermissionDialog(mActivity);
            }
        }
    }

    public static boolean checkVaultSetup(Activity mActivity) {
        if (TextUtils.isEmpty(SupportClass.getString(SupportClass.PIN_LOCK))) {
            AlertDialogHelper.getTextDialog(mActivity, mActivity.getString(R.string.txt_alert), mActivity.getString(R.string.txt_vault_alert), R.string.txt_set_passcode, R.string.cancel, v -> {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.vaultOpen, Constants.vaultOpen);
                mActivity.startActivity(new Intent(mActivity, ActSetupPinLock.class));
            }, v -> {

            });

            return false;
        } else {
            return true;
        }
    }

    public static int returnPendingIntentFlag(int oldFlag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return (PendingIntent.FLAG_MUTABLE);
        }else {
            return (PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        }
    }

    public enum USER_ACTION {
        DELETE,
        RENAME,
        MOVE_TO_VAULT,
        SAVE,
        COPY_TO,
        MOVE_TO;
    }
}
