package com.photo.photography.secure_vault.ImageLoader;

/**
 * Created by Admin on 08-08-2016.
 */

import android.content.Context;
import android.os.Environment;

import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.File;

public class FileCaches {

    private static File cacheDir;

    public FileCaches(Context context) {
        // Find the dir to save cached images
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir = new VaultFileUtil(context).getFile("cache");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public static File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}