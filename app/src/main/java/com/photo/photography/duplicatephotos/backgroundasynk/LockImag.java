package com.photo.photography.duplicatephotos.backgroundasynk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.repeater.RepeaterIndividualGroup;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.frag.FragExactDuplicates;
import com.photo.photography.duplicatephotos.frag.FragSimilarDuplicates;
import com.photo.photography.duplicatephotos.callback.CallbackMarked;
import com.photo.photography.duplicatephotos.util.PopUpDialogs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LockImag extends AsyncTask<Void, Void, Void> implements CallbackMarked {
    private final ImageLoader imageLoader;
    private final Activity lIActivity;
    private final Context lIContext;
    private final DisplayImageOptions options;
    long deletedFileSize = 0;
    ArrayList<ImagesItem> fileToBeLocked;
    List<IndividualGroups> groupOfDuplicates;
    String lockMessage;
    int numberOfFilesPresentInOtherScan = 0;
    private TextView duplicatesFound;
    private ProgressDialog lockDialog;
    private TextView marked;

    public LockImag(Context context, Activity activity, String str, ArrayList<ImagesItem> arrayList, List<IndividualGroups> list) {
        this.lIContext = context;
        this.lIActivity = activity;
        this.lockMessage = str;
        this.fileToBeLocked = arrayList;
        this.groupOfDuplicates = list;
        this.imageLoader = GlobalVarsAndFunction.getImageLoadingInstance();
        this.options = GlobalVarsAndFunction.getOptions();
    }

    @Override
    public void photosCleanedExact(int i) {
    }

    @Override
    public void photosCleanedSimilar(int i) {
    }

    public void onPreExecute() {
        super.onPreExecute();
        this.lIContext.getSharedPreferences("myflag", 0);
        GlobalVarsAndFunction.configureImageLoader(this.imageLoader, this.lIActivity);
        this.marked = (TextView) this.lIActivity.findViewById(R.id.marked);
        this.duplicatesFound = (TextView) this.lIActivity.findViewById(R.id.dupes_found);
        this.lockDialog = new ProgressDialog(this.lIActivity);
        this.lockDialog.setMessage(this.lockMessage);
        this.lockDialog.setCancelable(false);
        this.lockDialog.show();
    }

    public Void doInBackground(Void... voidArr) {
        lockImagesByPosition(this.fileToBeLocked, this.groupOfDuplicates);
        CommonUsed.logmsg("in lock what is the Size of image to be locked: " + this.fileToBeLocked.size());
        for (int i = 0; i < this.fileToBeLocked.size(); i++) {
            CommonUsed.logmsg("How many time: " + i);
            lockImageIfPresent(this.fileToBeLocked.get(i));
        }
        unSelectAllImagesInOtherGroup();
        return null;
    }

    private void unSelectAllImagesInOtherGroup() {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            for (int i = 0; i < GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size(); i++) {
                ImagesItem imageItem = GlobalVarsAndFunction.file_To_Be_Deleted_Exact.get(i);
                imageItem.setImageCheckBox(false);
                GlobalVarsAndFunction.getGroupOfDuplicatesExact().get(imageItem.getImageItemGrpTag()).setGroupSetCheckBox(false);
            }
            GlobalVarsAndFunction.file_To_Be_Deleted_Exact.clear();
            GlobalVarsAndFunction.size_Of_File_Exact = 0;
            return;
        }
        for (int i2 = 0; i2 < GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size(); i2++) {
            ImagesItem imageItem2 = GlobalVarsAndFunction.file_To_Be_Deleted_Similar.get(i2);
            imageItem2.setImageCheckBox(false);
            GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(imageItem2.getImageItemGrpTag()).setGroupSetCheckBox(false);
        }
        GlobalVarsAndFunction.file_To_Be_Deleted_Similar.clear();
        GlobalVarsAndFunction.size_Of_File_Similar = 0;
    }

    private void lockImageIfPresent(ImagesItem imageItem) {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            lockImageInAnotherGroup(GlobalVarsAndFunction.getGroupOfDuplicatesExact(), imageItem);
        } else {
            lockImageInAnotherGroup(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), imageItem);
        }
    }

    private void lockImageInAnotherGroup(List<IndividualGroups> list, ImagesItem imageItem) {
        for (int i = 0; i < list.size(); i++) {
            IndividualGroups individualGroup = list.get(i);
            List<ImagesItem> individualGrpOfDupes = individualGroup.getIndividualGrpOfDupes();
            int size = individualGrpOfDupes.size();
            int i2 = 0;
            for (int i3 = 0; i3 < individualGrpOfDupes.size(); i3++) {
                String image = imageItem.getImage();
                ImagesItem imageItem2 = individualGrpOfDupes.get(i3);
                String image2 = imageItem2.getImage();
                boolean isImageCheckBox = imageItem2.isImageCheckBox();
                if (image2.equalsIgnoreCase(image)) {
                    if (GlobalVarsAndFunction.getTabSelected() != 0) {
                        addToLockedExactList(imageItem);
                    } else {
                        addToLockedSimilarList(imageItem);
                    }
                    this.deletedFileSize += imageItem.getSizeOfTheFile();
                    this.numberOfFilesPresentInOtherScan++;
                    i2++;
                    if (i2 == size) {
                        this.deletedFileSize -= imageItem.getSizeOfTheFile();
                        this.numberOfFilesPresentInOtherScan--;
                    }
                    if (isImageCheckBox) {
                        CommonUsed.logmsg("Ya it is checked!!!! ");
                        if (GlobalVarsAndFunction.getTabSelected() != 0) {
                            removeFromLockedArrayList(GlobalVarsAndFunction.getLockExactPhotos(this.lIContext), imageItem);
                        } else {
                            removeFromLockedArrayList(GlobalVarsAndFunction.getLockSimilarPhotos(this.lIContext), imageItem);
                        }
                    }
                    individualGrpOfDupes.remove(imageItem2);
                }
            }
            individualGroup.setIndividualGrpOfDupes(individualGrpOfDupes);
        }
    }

    private void addToLockedExactList(ImagesItem imageItem) {
        if (GlobalVarsAndFunction.getLockExactPhotos(this.lIContext) != null) {
            ArrayList<ImagesItem> lockExactPhotos = GlobalVarsAndFunction.getLockExactPhotos(this.lIContext);
            lockExactPhotos.add(imageItem);
            GlobalVarsAndFunction.setLockExactPhotos(this.lIContext, lockExactPhotos);
            CommonUsed.logmsg("next time add s: similar " + GlobalVarsAndFunction.getLockSimilarPhotos(this.lIContext).size());
            CommonUsed.logmsg("next time add s: exact " + GlobalVarsAndFunction.getLockExactPhotos(this.lIContext).size());
            return;
        }
        GlobalVarsAndFunction.setLockFirstTimeExactFlag(this.lIContext, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(imageItem);
        GlobalVarsAndFunction.setLockExactPhotos(this.lIContext, arrayList);
        CommonUsed.logmsg("First time add s: similar " + GlobalVarsAndFunction.getLockSimilarPhotos(this.lIContext).size());
        CommonUsed.logmsg("First time add s: exact " + GlobalVarsAndFunction.getLockExactPhotos(this.lIContext).size());
    }

    private void addToLockedSimilarList(ImagesItem imageItem) {
        if (GlobalVarsAndFunction.getLockSimilarPhotos(this.lIContext) != null) {
            ArrayList<ImagesItem> lockSimilarPhotos = GlobalVarsAndFunction.getLockSimilarPhotos(this.lIContext);
            lockSimilarPhotos.add(imageItem);
            GlobalVarsAndFunction.setLockSimilarPhotos(this.lIContext, lockSimilarPhotos);
            CommonUsed.logmsg("next time addd e: Exact " + GlobalVarsAndFunction.getLockExactPhotos(this.lIContext).size());
            CommonUsed.logmsg("next time addd e: similar " + GlobalVarsAndFunction.getLockSimilarPhotos(this.lIContext).size());
            return;
        }
        CommonUsed.logmsg("Whats the size of the set: " + GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().size());
        GlobalVarsAndFunction.setLockFirstTimeSimilarFlag(this.lIContext, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(imageItem);
        GlobalVarsAndFunction.setLockSimilarPhotos(this.lIContext, arrayList);
        CommonUsed.logmsg("First time addd e: Exact " + GlobalVarsAndFunction.getLockExactPhotos(this.lIContext).size());
        CommonUsed.logmsg("First time addd e: similar " + GlobalVarsAndFunction.getLockSimilarPhotos(this.lIContext).size());
    }

    private void removeFromLockedArrayList(ArrayList<ImagesItem> arrayList, ImagesItem imageItem) {
        for (int i = 0; i < arrayList.size(); i++) {
            String image = imageItem.getImage();
            ImagesItem imageItem2 = arrayList.get(i);
            if (image.equalsIgnoreCase(imageItem2.getImage())) {
                arrayList.remove(i);
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    GlobalVarsAndFunction.subSizeExact(imageItem2.getSizeOfTheFile());
                } else {
                    GlobalVarsAndFunction.subSizeSimilar(imageItem2.getSizeOfTheFile());
                }
            }
        }
    }

    private void lockImagesByPosition(ArrayList<ImagesItem> arrayList, List<IndividualGroups> list) {
        for (int i = 0; i < arrayList.size(); i++) {
            ImagesItem imageItem = arrayList.get(i);
            int imageItemGrpTag = imageItem.getImageItemGrpTag();
            int position = imageItem.getPosition();
            IndividualGroups individualGroup = list.get(imageItemGrpTag);
            List<ImagesItem> individualGrpOfDupes = individualGroup.getIndividualGrpOfDupes();
            for (int i2 = 0; i2 < individualGrpOfDupes.size(); i2++) {
                ImagesItem imageItem2 = individualGrpOfDupes.get(i2);
                if (imageItem2.getPosition() == position) {
                    individualGrpOfDupes.remove(imageItem2);
                }
            }
            individualGroup.setIndividualGrpOfDupes(individualGrpOfDupes);
            individualGroup.setGroupSetCheckBox(false);
        }
    }

    public void onPostExecute(Void r11) {
        super.onPostExecute(r11);
        if (this.lockDialog.isShowing()) {
            this.lockDialog.dismiss();
            String str = null;
            int size = this.fileToBeLocked.size();
            if (GlobalVarsAndFunction.getTabSelected() != 0) {
                if (size > 1) {
                    str = this.lIActivity.getString(R.string.successfully_added) + " " + size + " " + this.lIContext.getString(R.string.similar_photos_to_exceptions);
                } else if (size == 1) {
                    str = this.lIActivity.getString(R.string.successfully_added) + " " + size + " " + this.lIContext.getString(R.string.similar_photo_to_exceptions);
                }
            } else if (size > 1) {
                str = this.lIActivity.getString(R.string.successfully_added) + " " + size + " " + this.lIContext.getString(R.string.exact_photos_to_exceptions);
            } else if (size == 1) {
                str = this.lIActivity.getString(R.string.successfully_added) + " " + size + " " + this.lIContext.getString(R.string.exact_photo_to_exceptions);
            }
            if (GlobalVarsAndFunction.getTabSelected() != 0) {
                GlobalVarsAndFunction.setLockFirstTimeSimilarFlag(this.lIContext, false);
                RepeaterIndividualGroup individualGroupAdapter = new RepeaterIndividualGroup(this.lIContext, this.lIActivity, this.groupOfDuplicates, this, this.imageLoader, this.options);
                FragSimilarDuplicates.recyclerViewForIndividualGrp.setAdapter(individualGroupAdapter);
                individualGroupAdapter.notifyDataSetChanged();
                GlobalVarsAndFunction.setMemoryRegainedExact(GlobalVarsAndFunction.getMemoryRegainedExact());
                GlobalVarsAndFunction.setTotalDuplicatesExact(GlobalVarsAndFunction.getTotalDuplicatesExact() - this.numberOfFilesPresentInOtherScan);
                FragExactDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(this.lIContext, this.lIActivity, GlobalVarsAndFunction.getGroupOfDuplicatesExact(), this, this.imageLoader, this.options));
                individualGroupAdapter.notifyDataSetChanged();
                new PopUpDialogs(this.lIContext, this.lIActivity).memoryExceptionPopUp(str, this);
            } else {
                GlobalVarsAndFunction.setLockFirstTimeExactFlag(this.lIContext, false);
                RepeaterIndividualGroup individualGroupAdapter2 = new RepeaterIndividualGroup(this.lIContext, this.lIActivity, this.groupOfDuplicates, this, this.imageLoader, this.options);
                FragExactDuplicates.recyclerViewForIndividualGrp.setAdapter(individualGroupAdapter2);
                individualGroupAdapter2.notifyDataSetChanged();
                GlobalVarsAndFunction.setMemoryRegainedSimilar(GlobalVarsAndFunction.getStringSizeLengthFile(GlobalVarsAndFunction.getMemoryRegainedSimilarInLong() - this.deletedFileSize));
                GlobalVarsAndFunction.setTotalDuplicatesSimilar(GlobalVarsAndFunction.getTotalDuplicatesSimilar() - this.numberOfFilesPresentInOtherScan);
                FragSimilarDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(this.lIContext, this.lIActivity, GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), this, this.imageLoader, this.options));
                individualGroupAdapter2.notifyDataSetChanged();
                new PopUpDialogs(this.lIContext, this.lIActivity).memoryExceptionPopUp(str, this);
            }
        }
        this.numberOfFilesPresentInOtherScan = 0;
    }

    @Override
    public void updateMarkedExact() {
        this.marked.setText(this.lIContext.getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
    }

    @Override
    public void updateDuplicateFoundExact(int i) {
        this.duplicatesFound.setText(this.lIContext.getString(R.string.Duplicates_Found) + " " + i);
    }

    @Override
    public void updateMarkedSimilar() {
        this.marked.setText(this.lIContext.getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
    }

    @Override
    public void updateDuplicateFoundSimilar(int i) {
        this.duplicatesFound.setText(this.lIContext.getString(R.string.Duplicates_Found) + " " + i);
    }

}
