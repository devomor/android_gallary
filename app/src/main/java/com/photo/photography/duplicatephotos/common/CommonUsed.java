package com.photo.photography.duplicatephotos.common;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.os.EnvironmentCompat;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class CommonUsed {
    private static final String LOG_TAG = "CommonlyUsed";

    public static void showmsg(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void logmsg(String str) {
        Log.e("Duplicate remover ", str);
    }

    public static String[] getExternalStorageDirectories(Context context) {
        boolean z;
        ArrayList arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 19) {
            File[] externalFilesDirs = context.getExternalFilesDirs(null);
            try {
                for (File file : externalFilesDirs) {
                    String str = file.getPath().split("/Android")[0];
                    if (Build.VERSION.SDK_INT >= 21) {
                        z = Environment.isExternalStorageRemovable(file);
                    } else {
                        z = "mounted".equals(EnvironmentCompat.getStorageState(file));
                    }
                    if (z) {
                        arrayList.add(str);
                    }
                }
                Log.e("geirectories", "--asdfasdf----" + arrayList);
            } catch (Exception unused) {
            }
        }
        if (arrayList.isEmpty()) {
            String str2 = "";
            try {
                Process start = new ProcessBuilder().command("mount | grep /dev/block/vold").redirectErrorStream(true).start();
                start.waitFor();
                InputStream inputStream = start.getInputStream();
                byte[] bArr = new byte[1024];
                while (inputStream.read(bArr) != -1) {
                    str2 = str2 + new String(bArr);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!str2.trim().isEmpty()) {
                for (String str3 : str2.split("\n")) {
                    arrayList.add(str3.split(" ")[2]);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int i = 0;
            while (i < arrayList.size()) {
                if (!((String) arrayList.get(i)).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d(LOG_TAG, ((String) arrayList.get(i)) + " might not be extSDcard");
                    arrayList.remove(i);
                    i += -1;
                }
                i++;
            }
        } else {
            int i2 = 0;
            while (i2 < arrayList.size()) {
                if (!((String) arrayList.get(i2)).toLowerCase().contains("ext") && !((String) arrayList.get(i2)).toLowerCase().contains("sdcard")) {
                    Log.d(LOG_TAG, ((String) arrayList.get(i2)) + " might not be extSDcard");
                    arrayList.remove(i2);
                    i2 += -1;
                }
                i2++;
            }
        }
        String[] strArr = new String[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            strArr[i3] = (String) arrayList.get(i3);
        }
        return strArr;
    }

    public static String getSDCardPath(Context context) {
        if (getExternalStorageDirectories(context).length == 0) {
            return null;
        }
        return getExternalStorageDirectories(context)[0] + "/";
    }

    public static boolean isSelectedStorageAccessFrameWorkPathIsProper(Context context, String str) {
        String sDCardPath = getSDCardPath(context);
        if (sDCardPath == null) {
            return false;
        }
        try {
            String[] split = sDCardPath.split("/");
            return str.equals(split[split.length - 1]);
        } catch (Exception unused) {
            return false;
        }
    }

    public static String getExternalStorageName(Context context) {
        String sDCardPath = getSDCardPath(context);
        if (sDCardPath == null) {
            return null;
        }
        try {
            String[] split = sDCardPath.split("/");
            return split[split.length - 1];
        } catch (Exception unused) {
            return null;
        }
    }
}
