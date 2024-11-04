package com.photo.photography.collage.util;

import android.content.Context;

import com.photo.photography.MyApp;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Class containing some static utility methods.
 */
public class Util {
    public static final String BIG_D_FOLDER = new VaultFileUtil(MyApp.mContext).getFile("collageMain").getAbsolutePath();
    public static final String TEMP_FOLDER = BIG_D_FOLDER.concat("/Temp");
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
}
