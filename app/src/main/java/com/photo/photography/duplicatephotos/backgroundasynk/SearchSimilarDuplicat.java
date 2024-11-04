package com.photo.photography.duplicatephotos.backgroundasynk;

import static com.photo.photography.util.utilsEdit.SupportClass.returnPendingIntentFlag;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.act.ActScanning;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.extras.RGBValueNdPath;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.callback.CallbackSearch;
import com.photo.photography.duplicatephotos.pixelsrelated.RGBObject;
import com.photo.photography.duplicatephotos.pixelsrelated.RGBValues;

import java.util.ArrayList;
import java.util.List;

public class SearchSimilarDuplicat extends AsyncTask<String, String, List<IndividualGroups>> {
    private static final String CHANNEL_ID = "1";
    private final Activity scanningForDuplicates;
    Notification SBuilder;
    int groupTag = 0;
    long memoryRegainingSpace = 0;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder sBuilder;
    Context sContext;
    NotificationManager sNotifyManager;
    CallbackSearch searchListener;
    int totalNumberOfDuplicates = 0;
    String updateNotificationProgress;
    String strTotalCount = "";

    public SearchSimilarDuplicat(Activity activity, Context context, CallbackSearch searchListener2) {
        this.scanningForDuplicates = activity;
        this.sContext = context;
        this.searchListener = searchListener2;
    }

    public void stopSimilarAsync() {
        new SearchSimilarDuplicat(this.scanningForDuplicates, this.sContext, this.searchListener);
        CommonUsed.logmsg("Scanning similar is stopped!!!!!!!!");
        GlobalVarsAndFunction.setCancelFlag(this.scanningForDuplicates, true);
    }

    private void initAndShowScanningProgressNotification() {
        Intent intent = new Intent(this.scanningForDuplicates, ActScanning.class);
        this.sBuilder = new NotificationCompat.Builder(this.scanningForDuplicates, "1")
                .setSmallIcon(GlobalVarsAndFunction.getNotificationIcon())
                .setContentTitle(this.scanningForDuplicates.getString(R.string.scanning_photos_please_wait))
                .setPriority(0)
                .setContentIntent(PendingIntent.getActivity(this.scanningForDuplicates, 0, intent, returnPendingIntentFlag(0)))
                .setAutoCancel(true);
        this.notificationManager = NotificationManagerCompat.from(this.sContext);
    }

    private void setContentProgress(int i, int i2, int i3, String... strArr) {
        if (strArr[0].equalsIgnoreCase("scanning")) {
            this.sBuilder.setProgress(i, i2, false);
            String str = this.scanningForDuplicates.getString(R.string.scanning_photos) + " " + strArr[1];
            this.sBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(str));
            this.sBuilder.setContentText(str);
            this.notificationManager.notify(1, this.sBuilder.build());
        } else if (!strArr[0].equalsIgnoreCase("Sorting")) {
        } else {
            if (GlobalVarsAndFunction.SCANNING_FLAG_EXACT) {
                String string = this.scanningForDuplicates.getString(R.string.Sorting_duplicates);
                this.sBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(string));
                this.sBuilder.setContentText(string);
                this.sBuilder.setContentTitle(this.scanningForDuplicates.getString(R.string.sortingplswait));
                this.notificationManager.notify(1, this.sBuilder.build());
                return;
            }
            String string2 = this.scanningForDuplicates.getString(R.string.Sorting_duplicates);
            this.sBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(string2));
            this.sBuilder.setContentText(string2);
            this.sBuilder.setContentTitle(this.scanningForDuplicates.getString(R.string.sortingplswait));
            this.notificationManager.notify(1, this.sBuilder.build());
        }
    }

    public List<IndividualGroups> doInBackground(String... strArr) {
        GlobalVarsAndFunction.SCANNING_FLAG_SIMILAR = true;
        List<RGBValueNdPath> arrayList = new ArrayList<>();
        Cursor query = this.sContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME}, null, null,
                MediaStore.Images.Media._ID + " DESC");

        CommonUsed.logmsg("Number of Image Files in Gallery: " + query.getCount());
        Log.e("doInBackground", MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "\n" + query.getCount());
        strTotalCount = "/" + query.getCount();
        if (GlobalVarsAndFunction.getLockSimilarPhotos(this.sContext) != null) {
            CommonUsed.logmsg("Number of Photos to be Saved Similar: " + GlobalVarsAndFunction.getLockSimilarPhotos(this.sContext).size());
        }
        initAndShowScanningProgressNotification();
        int i = 0;
        while (query.moveToNext() && !GlobalVarsAndFunction.getCancelFlag(this.sContext)) {
            i++;
            try {
                String string = query.getString(query.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                long imgId = query.getLong(query.getColumnIndexOrThrow(MediaStore.Images.Media._ID));

                String[] strArr2 = {"scanning", "" + i};
                setContentProgress(query.getCount(), i, 0, strArr2);
                publishProgress(strArr2);
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
                    rgbValueAndPath.setId(imgId);
                    rgbValueAndPath.setImageResolution(imageResolution);
                    rgbValueAndPath.setDateAndTime(GlobalVarsAndFunction.getDateAndTime(string));
                    arrayList.add(rgbValueAndPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (GlobalVarsAndFunction.TESTING_LOOP_STOP_COUNT > 0 && arrayList.size() == GlobalVarsAndFunction.TESTING_LOOP_STOP_COUNT) {
                break;
            }
        }
        String[] strArr3 = {"sorting", this.sContext.getString(R.string.Sorting_duplicates)};
        publishProgress(strArr3);
        if (GlobalVarsAndFunction.getLockSimilarPhotos(this.sContext) != null) {
            arrayList = unScanLockedPhotos(arrayList, GlobalVarsAndFunction.getLockSimilarPhotos(this.sContext));
        }
        List<IndividualGroups> linearSearch = linearSearch(arrayList, query.getCount(), i, strArr3);
        query.close();
        return linearSearch;
    }

    private List<RGBValueNdPath> unScanLockedPhotos(List<RGBValueNdPath> list, ArrayList<ImagesItem> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String image = arrayList.get(i).getImage();
            for (int i2 = 0; i2 < list.size(); i2++) {
                RGBValueNdPath rgbValueAndPath = list.get(i2);
                if (rgbValueAndPath.getFilePath().equalsIgnoreCase(image)) {
                    list.remove(rgbValueAndPath);
                }
            }
        }
        return list;
    }

    private List<IndividualGroups> linearSearch(List<RGBValueNdPath> list, int i, int i2, String... strArr) {
        ArrayList arrayList = new ArrayList();
        long j = 0;
        int i3 = 0;
        while (list.size() > 0 && !GlobalVarsAndFunction.getCancelFlag(this.sContext)) {
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            List<RGBObject> listOfPixelsRgbValue = list.get(0).getListOfPixelsRgbValue();
            int i4 = i3;
            int i5 = 0;
            long j2 = j;
            for (int i6 = 0; i6 < list.size(); i6++) {
                if (list.get(i6) != null) {
                    if (GlobalVarsAndFunction.compareRgb(listOfPixelsRgbValue, list.get(i6).getListOfPixelsRgbValue())) {
                        if (i5 != 0) {
                            j2 = memoryRegainingSpace(GlobalVarsAndFunction.getFileSize(list.get(i6).getFilePath()));
                            i4 = totalNumberOfDuplicates();
                            setContentProgress(i, i2, i4, strArr);
                        }
                        i5++;
                        ImagesItem imageItem = new ImagesItem();
                        imageItem.setImage(list.get(i6).getFilePath());
                        imageItem.setId(list.get(i6).getId());
                        imageItem.setImageCheckBox(false);
                        imageItem.setPosition(i6);
                        imageItem.setImageItemGrpTag(this.groupTag);
                        imageItem.setSizeOfTheFile(GlobalVarsAndFunction.getFileSize(list.get(i6).getFilePath()));
                        imageItem.setImageResolution(list.get(i6).getImageResolution());
                        imageItem.setDateAndTime(list.get(i6).getDateAndTime());
                        arrayList3.add(imageItem);
                        arrayList2.add(list.get(i6));
                        Log.e("SearchSimilarDuplicates", "list: " + arrayList.size());
                    }
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
            j = j2;
            i3 = i4;
        }
        GlobalVarsAndFunction.setMemoryRegainedSimilar(GlobalVarsAndFunction.getStringSizeLengthFile(j));
        GlobalVarsAndFunction.setMemoryRegainedSimilarInLong(j);
        GlobalVarsAndFunction.setTotalDuplicatesSimilar(i3);
        CommonUsed.logmsg("Total group Similar: " + i + " Total Size: " + GlobalVarsAndFunction.getMemoryRegainedSimilar() + " Total ExactDuplicates: " + GlobalVarsAndFunction.getTotalDuplicatesSimilar());
        return arrayList;
    }

    public void onProgressUpdate(String... strArr) {
        super.onProgressUpdate(strArr);
        this.searchListener.updateUi(strArr);
        if (!TextUtils.isEmpty(strTotalCount))
            this.searchListener.updateTotalFileCountUi(strTotalCount);
    }

    public void onPostExecute(List<IndividualGroups> list) {
        super.onPostExecute(list);
        if (!GlobalVarsAndFunction.getCancelFlag(this.sContext)) {
            GlobalVarsAndFunction.SCANNING_FLAG_SIMILAR = false;
            GlobalVarsAndFunction.setGroupOfDuplicatesSimilar(sContext, list, true);
            this.searchListener.checkSearchFinish();
        }
        CommonUsed.logmsg("Notification progress is got cancelled!!!");
        NotificationManagerCompat.from(this.sContext).cancelAll();
    }

    private int totalNumberOfDuplicates() {
        this.totalNumberOfDuplicates++;
        return this.totalNumberOfDuplicates;
    }

    private long memoryRegainingSpace(long j) {
        this.memoryRegainingSpace += j;
        return this.memoryRegainingSpace;
    }
}
