package com.photo.photography.util.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.photo.photography.MyApp;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Class containing some static utility methods.
 */
public class Util {
    public static final String BIG_D_FOLDER = new VaultFileUtil(MyApp.mContext).getDirPath();
    public static final String FILE_FOLDER = BIG_D_FOLDER.concat("/files");
    public static final String ROOT_EDITED_IMAGE_FOLDER = FILE_FOLDER.concat("/edited");
    public static final String EDITED_IMAGE_FOLDER = ROOT_EDITED_IMAGE_FOLDER.concat("/images");
    public static final String EDITED_IMAGE_THUMBNAIL_FOLDER = ROOT_EDITED_IMAGE_FOLDER.concat("/thumbnails");
    public static final String CROP_FOLDER = FILE_FOLDER.concat("/crop");
    public static final String FRAME_FOLDER = FILE_FOLDER.concat("/frame");
    public static final String FILTER_FOLDER = FILE_FOLDER.concat("/filter");
    public static final String BACKGROUND_FOLDER = FILE_FOLDER.concat("/background");
    public static final String STICKER_FOLDER = FILE_FOLDER.concat("/sticker");
    private static final String TAG = Util.class.getSimpleName();

    private Util() {
    }

    public static int getStatusBarHeight(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
//        return (int) (24 * res.getDisplayMetrics().density);
    }

    // Return the NavigationBar height in pixels if it is present, otherwise return 0
    public static int getNavigationBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.heightPixels - (rectangle.top + rectangle.height());
    }

    public static float px2dp(Resources resource, float px) {
        return (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, resource.getDisplayMetrics());
    }

    public static int dp2px(Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }

    public static File copyFileFromAsset(Context context, final String outFolder, final String assetFilePath, boolean override) {
        try {
            File file = new File(assetFilePath);
            final String outFilePath = outFolder.concat("/").concat(file.getName());
            file = new File(outFilePath);
            if (!file.exists() || file.length() == 0 || override) {
                InputStream is = context.getAssets().open(assetFilePath);
                file.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buff = new byte[2048];
                int len = -1;
                while ((len = is.read(buff)) != -1) {
                    fos.write(buff, 0, len);
                }
                fos.flush();
                fos.close();
                is.close();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static boolean isPhoneLandscape(@NonNull Resources resources) {
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPhonePortrait(@NonNull Resources resources) {
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        return new Point();
    }

    private static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

}
