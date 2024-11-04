package com.photo.photography.duplicatephotos.backgroundasynk;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.extras.MD5CheckWithImagePath;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.files.MD5ChecksumFile;
import com.photo.photography.duplicatephotos.callback.CallbackSearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SearchExactDuplicat extends AsyncTask<String, String, List<IndividualGroups>> {

    Context eContext;
    long memoryRegainingSpace = 0;
    Activity scanningForDuplicates;
    CallbackSearch searchListener;
    int totalNumberOfDuplicates = 0;

    public SearchExactDuplicat(Activity activity, Context context, CallbackSearch searchListener2) {
        this.scanningForDuplicates = activity;
        this.eContext = context;
        this.searchListener = searchListener2;
    }

    public void stopExactAsync() {
        new SearchExactDuplicat(this.scanningForDuplicates, this.eContext, this.searchListener);
        CommonUsed.logmsg("Scanning similar is stopped!!!!!!!!");
        GlobalVarsAndFunction.setCancelFlag(this.scanningForDuplicates, true);
    }

    public List<IndividualGroups> doInBackground(String... strArr) {
        GlobalVarsAndFunction.SCANNING_FLAG_EXACT = true;
        Cursor query = this.eContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME}, null, null,
                MediaStore.Images.Media._ID + " DESC");

        CommonUsed.logmsg("Number of Image Files in Gallery: " + query.getCount());
        Log.e("doInBackground", MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "\n" + query.getCount());
        List<MD5CheckWithImagePath> arrayList2 = new ArrayList<>();
        ArrayList<String> arrayList3 = new ArrayList<>();
        if (GlobalVarsAndFunction.getLockExactPhotos(this.eContext) != null) {
            CommonUsed.logmsg("Number of Photos to be Saved Exact: " + GlobalVarsAndFunction.getLockExactPhotos(this.eContext).size());
        }
        while (query.moveToNext() && !GlobalVarsAndFunction.getCancelFlag(this.eContext)) {
            try {
                String string = query.getString(query.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                long imgId = query.getLong(query.getColumnIndexOrThrow(MediaStore.Images.Media._ID));

                String fileToMD5 = new MD5ChecksumFile().fileToMD5(string);
                arrayList3.add(fileToMD5);

                MD5CheckWithImagePath mD5ChecksumWithImagePath = new MD5CheckWithImagePath();
                mD5ChecksumWithImagePath.setMd5Checksum(fileToMD5);
                mD5ChecksumWithImagePath.setFilePath(string);
                mD5ChecksumWithImagePath.setId(imgId);
                mD5ChecksumWithImagePath.setImageResolution(GlobalVarsAndFunction.getImageResolution(string));
                mD5ChecksumWithImagePath.setDateAndTime(GlobalVarsAndFunction.getDateAndTime(string));
                arrayList2.add(mD5ChecksumWithImagePath);

            } catch (Exception e) {
                CommonUsed.logmsg("Exception in async task---searchingforexactdupes:" + e.getMessage());
            }


            if (GlobalVarsAndFunction.TESTING_LOOP_STOP_COUNT > 0 && arrayList2.size() == GlobalVarsAndFunction.TESTING_LOOP_STOP_COUNT) {
                break;
            }
        }
        query.close();
        if (GlobalVarsAndFunction.getLockExactPhotos(this.eContext) != null) {
            arrayList2 = unScanLockedPhotos(arrayList2, GlobalVarsAndFunction.getLockExactPhotos(this.eContext));
        }
        int i = 0;
        ArrayList<IndividualGroups> arrayList = new ArrayList<>();
        for (Object str : new HashSet<>(arrayList3)) {
            if (Collections.frequency(arrayList3, str) > 1 && !GlobalVarsAndFunction.getCancelFlag(this.eContext)) {
                int i2 = i + 1;
                List<ImagesItem> linearSearch = linearSearch(arrayList2, (String) str, i);
                if (linearSearch.size() > 1) {
                    IndividualGroups individualGroup = new IndividualGroups();
                    individualGroup.setGroupSetCheckBox(false);
                    individualGroup.setGroupTag(i2);
                    individualGroup.setIndividualGrpOfDupes(linearSearch);
                    arrayList.add(individualGroup);
                    Log.e("SearchExactDuplicates", "grouped duplicates: " + arrayList);
                    i = i2;
                } else {
                    i = i2 - 1;
                }
            }
        }
        return arrayList;
    }

    private List<MD5CheckWithImagePath> unScanLockedPhotos(List<MD5CheckWithImagePath> list, ArrayList<ImagesItem> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String image = arrayList.get(i).getImage();
            for (int i2 = 0; i2 < list.size(); i2++) {
                MD5CheckWithImagePath mD5ChecksumWithImagePath = list.get(i2);
                if (mD5ChecksumWithImagePath.getFilePath().equalsIgnoreCase(image)) {
                    list.remove(mD5ChecksumWithImagePath);
                }
            }
        }
        return list;
    }

    public void onPostExecute(List<IndividualGroups> list) {
        super.onPostExecute(list);
        if (!GlobalVarsAndFunction.getCancelFlag(this.eContext)) {
            GlobalVarsAndFunction.SCANNING_FLAG_EXACT = false;
            GlobalVarsAndFunction.setGroupOfDuplicatesExact(eContext, list, true);
            this.searchListener.checkSearchFinish();
        }
    }

    private List<ImagesItem> linearSearch(List<MD5CheckWithImagePath> list, String str, int i) {
        ArrayList<ImagesItem> arrayList = new ArrayList<>();
        int i3 = this.totalNumberOfDuplicates;
        int i4 = 0;
        long j2 = this.memoryRegainingSpace;
        for (int i5 = 0; i5 < list.size(); i5++) {
            if (list.get(i5).getMd5Checksum() != null && list.get(i5).getMd5Checksum().equalsIgnoreCase(str)) {
                if (i4 != 0) {
                    j2 = memoryRegainingSpace(GlobalVarsAndFunction.getFileSize(list.get(i5).getFilePath()));
                    i3 = totalNumberOfDuplicates();
                }
                i4++;
                ImagesItem imageItem = new ImagesItem();
                imageItem.setImage(list.get(i5).getFilePath());
                imageItem.setId(list.get(i5).getId());
                imageItem.setImageCheckBox(false);
                imageItem.setPosition(i5);
                imageItem.setImageItemGrpTag(i);
                imageItem.setSizeOfTheFile(GlobalVarsAndFunction.getFileSize(list.get(i5).getFilePath()));
                imageItem.setImageResolution(list.get(i5).getImageResolution());
                imageItem.setDateAndTime(list.get(i5).getDateAndTime());
                arrayList.add(imageItem);
                Log.e("SearchExactDuplicates", "list: " + arrayList.size());
            }
        }
        GlobalVarsAndFunction.setMemoryRegainedExact(GlobalVarsAndFunction.getStringSizeLengthFile(j2));
        GlobalVarsAndFunction.setMemoryRegainedExactInLong(j2);
        GlobalVarsAndFunction.setTotalDuplicatesExact(i3);

        CommonUsed.logmsg("Total group Exact: " + i + " Total Size: " + GlobalVarsAndFunction.getMemoryRegainedExact() + " Total ExactDuplicates: " + GlobalVarsAndFunction.getTotalDuplicatesExact());
        return arrayList;
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
