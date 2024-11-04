package com.photo.photography.duplicatephotos.backgroundasynk;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.extras.RGBValueNdPath;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.callback.CallbackEveryDayScan;
import com.photo.photography.duplicatephotos.pixelsrelated.RGBObject;
import com.photo.photography.duplicatephotos.pixelsrelated.RGBValues;

import java.util.ArrayList;
import java.util.List;

public class GroupSimilarDuplicat extends AsyncTask<Void, Void, Void> {

    private final Context gSDContext;
    CallbackEveryDayScan everyDayScanListener;
    int groupTag = 0;
    int totalNumberOfDuplicates = 0;

    public GroupSimilarDuplicat(Context context, CallbackEveryDayScan everyDayScanListener2) {
        this.gSDContext = context;
        this.everyDayScanListener = everyDayScanListener2;
    }

    public void getGroupOfSimilarDuplicates() {
        ArrayList arrayList = new ArrayList();
        try {
            Cursor query = this.gSDContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, null, null, null);
            int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
            CommonUsed.logmsg("Number of Image Files in Gallery: " + query.getCount());
            while (query.moveToNext()) {
                String string = query.getString(columnIndexOrThrow);
                String imageResolution = GlobalVarsAndFunction.getImageResolution(string);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap decodeFile = BitmapFactory.decodeFile(string, options);
                if (decodeFile != null && decodeFile.getWidth() >= 96 && decodeFile.getHeight() >= 96) {
                    Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeFile, 256, 256, true);
                    RGBValues rgbValue = new RGBValues();
                    RGBValueNdPath rgbValueAndPath = new RGBValueNdPath();
                    rgbValueAndPath.setListOfPixelsRgbValue(rgbValue.getRgbValue(createScaledBitmap));
                    rgbValueAndPath.setFilePath(string);
                    rgbValueAndPath.setImageResolution(imageResolution);
                    rgbValueAndPath.setDateAndTime(GlobalVarsAndFunction.getDateAndTime(string));
                    arrayList.add(rgbValueAndPath);
                }
            }
            GlobalVarsAndFunction.setSimilarDuplicatesInOneDay(linearSearch(arrayList));
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int linearSearch(List<RGBValueNdPath> list) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (list.size() > 0 && !GlobalVarsAndFunction.getCancelFlag(this.gSDContext)) {
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            List<RGBObject> listOfPixelsRgbValue = list.get(0).getListOfPixelsRgbValue();
            int i2 = i;
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                if (list.get(i4) != null && GlobalVarsAndFunction.compareRgb(listOfPixelsRgbValue, list.get(i4).getListOfPixelsRgbValue())) {
                    if (i3 != 0) {
                        i2 = totalNumberOfDuplicates();
                    }
                    i3++;
                    ImagesItem imageItem = new ImagesItem();
                    imageItem.setImage(list.get(i4).getFilePath());
                    imageItem.setImageCheckBox(false);
                    imageItem.setPosition(i4);
                    imageItem.setImageItemGrpTag(this.groupTag);
                    imageItem.setSizeOfTheFile(GlobalVarsAndFunction.getFileSize(list.get(i4).getFilePath()));
                    imageItem.setImageResolution(list.get(i4).getImageResolution());
                    imageItem.setDateAndTime(list.get(i4).getDateAndTime());
                    arrayList3.add(imageItem);
                    arrayList2.add(list.get(i4));
                }
            }
            list.removeAll(arrayList2);
            if (arrayList3.size() > 1) {
                this.groupTag++;
                IndividualGroups individualGroup = new IndividualGroups();
                individualGroup.setGroupSetCheckBox(false);
                individualGroup.setGroupTag(this.groupTag);
                individualGroup.setIndividualGrpOfDupes(arrayList3);
                arrayList.add(individualGroup);
            }
            i = i2;
        }
        return i;
    }

    private int totalNumberOfDuplicates() {
        this.totalNumberOfDuplicates++;
        return this.totalNumberOfDuplicates;
    }

    public Void doInBackground(Void... voidArr) {
        getGroupOfSimilarDuplicates();
        return null;
    }

    public void onPostExecute(Void r1) {
        super.onPostExecute(r1);
        GlobalVarsAndFunction.EVERY_DAY_SCAN_SIMILAR = true;
        this.everyDayScanListener.everyDayScan();
    }
}
