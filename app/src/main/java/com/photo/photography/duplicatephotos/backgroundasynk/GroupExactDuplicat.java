package com.photo.photography.duplicatephotos.backgroundasynk;

import android.content.Context;
import android.database.Cursor;
import android.database.StaleDataException;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.MD5CheckWithImagePath;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.files.MD5ChecksumFile;
import com.photo.photography.duplicatephotos.callback.CallbackEveryDayScan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class GroupExactDuplicat extends AsyncTask<Void, Void, Void> {
    private final Context gEDContext;
    CallbackEveryDayScan everyDayScanListener;
    int totalNumberOfDuplicates = 0;

    public GroupExactDuplicat(Context context, CallbackEveryDayScan everyDayScanListener2) {
        this.gEDContext = context;
        this.everyDayScanListener = everyDayScanListener2;
    }

    public void getGroupOfExactDuplicates() {
        try {
            int i = 0;
            Cursor query = this.gEDContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, null, null, null);
            int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            CommonUsed.logmsg(this.gEDContext.getString(R.string.number_of_image_in_gallery) + query.getCount());
            while (query.moveToNext()) {
                String string = query.getString(columnIndexOrThrow);
                try {
                    String fileToMD5 = new MD5ChecksumFile().fileToMD5(string);
                    MD5CheckWithImagePath mD5ChecksumWithImagePath = new MD5CheckWithImagePath();
                    mD5ChecksumWithImagePath.setMd5Checksum(fileToMD5);
                    arrayList2.add(fileToMD5);
                    mD5ChecksumWithImagePath.setFilePath(string);
                    mD5ChecksumWithImagePath.setImageResolution(GlobalVarsAndFunction.getImageResolution(string));
                    mD5ChecksumWithImagePath.setDateAndTime(GlobalVarsAndFunction.getDateAndTime(string));
                    arrayList.add(mD5ChecksumWithImagePath);
                } catch (Exception e) {
                    CommonUsed.logmsg("Exception in async task---searchingforexactdupes:" + e.getMessage());
                }
            }
            int i2 = 0;
            for (Object str : new HashSet(arrayList2)) {
                if (Collections.frequency(arrayList2, str) > 1 && !GlobalVarsAndFunction.getCancelFlag(this.gEDContext)) {
                    i = linearSearch(arrayList, (String) str, i2);
                    i2++;
                }
            }
            GlobalVarsAndFunction.setExactDuplicateInOneDay(i);
            query.close();
        } catch (SecurityException e2) {
            e2.printStackTrace();
        } catch (NullPointerException e3) {
            e3.printStackTrace();
        } catch (StaleDataException e4) {
            e4.printStackTrace();
        }
    }

    private int linearSearch(List<MD5CheckWithImagePath> list, String str, int i) {
        int size = list.size();
        int i2 = this.totalNumberOfDuplicates;
        ArrayList arrayList = new ArrayList();
        int i3 = i2;
        int i4 = 0;
        for (int i5 = 0; i5 < size; i5++) {
            if (list.get(i5).getMd5Checksum() != null && list.get(i5).getMd5Checksum().equalsIgnoreCase(str)) {
                if (i4 != 0) {
                    i3 = totalNumberOfDuplicates();
                }
                i4++;
                ImagesItem imageItem = new ImagesItem();
                imageItem.setImage(list.get(i5).getFilePath());
                imageItem.setImageCheckBox(false);
                imageItem.setPosition(i5);
                imageItem.setImageItemGrpTag(i);
                imageItem.setSizeOfTheFile(GlobalVarsAndFunction.getFileSize(list.get(i5).getFilePath()));
                imageItem.setImageResolution(list.get(i5).getImageResolution());
                imageItem.setDateAndTime(list.get(i5).getDateAndTime());
                arrayList.add(imageItem);
            }
        }
        return i3;
    }

    private int totalNumberOfDuplicates() {
        this.totalNumberOfDuplicates++;
        return this.totalNumberOfDuplicates;
    }

    public Void doInBackground(Void... voidArr) {
        getGroupOfExactDuplicates();
        return null;
    }

    public void onPostExecute(Void r1) {
        super.onPostExecute(r1);
        GlobalVarsAndFunction.EVERY_DAY_SCAN_EXACT = true;
        this.everyDayScanListener.everyDayScan();
    }
}
