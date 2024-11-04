package com.photo.photography.duplicatephotos.backgroundasynk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteSelectedFile extends AsyncTask<Void, Void, Void> implements CallbackMarked {
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    Activity deleteActivity;
    Context deleteContext;
    long deletedFileSize = 0;
    ArrayList<ImagesItem> fileToBeDeleted;
    List<IndividualGroups> groupOfDuplicates;
    boolean isTaskRunning;
    String message;
    int numberOfFilesPresentInOtherScan = 0;
    private ProgressDialog deleteDialog;
    private TextView duplicatesFound;
    private TextView marked;

    public DeleteSelectedFile(Context context, Activity activity, String str, ArrayList<ImagesItem> arrayList, List<IndividualGroups> list) {
        this.deleteContext = context;
        this.deleteActivity = activity;
        this.message = str;
        this.fileToBeDeleted = arrayList;
        this.groupOfDuplicates = list;
        this.imageLoader = GlobalVarsAndFunction.getImageLoadingInstance();
        this.options = GlobalVarsAndFunction.getOptions();
    }

    public void onPreExecute() {
        super.onPreExecute();
        this.isTaskRunning = true;
        GlobalVarsAndFunction.configureImageLoader(this.imageLoader, this.deleteActivity);
        this.marked = (TextView) this.deleteActivity.findViewById(R.id.marked);
        this.duplicatesFound = (TextView) this.deleteActivity.findViewById(R.id.dupes_found);

        try {
            this.deleteDialog = new ProgressDialog(this.deleteActivity);
            this.deleteDialog.setMessage(this.message);
            this.deleteDialog.setCancelable(false);
            this.deleteDialog.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Void doInBackground(Void... voidArr) {
        ArrayList<ImagesItem> arrayList = this.fileToBeDeleted;
        for (int i = 0; i < arrayList.size(); i++) {
            Log.e("deleteImagesByPosition", "---is---" + this.isTaskRunning);
            if (this.isTaskRunning) {
                ImagesItem imageItem = arrayList.get(i);
                int imageItemGrpTag = imageItem.getImageItemGrpTag();
                int position = imageItem.getPosition();
                List<ImagesItem> individualGrpOfDupes = this.groupOfDuplicates.get(imageItemGrpTag).getIndividualGrpOfDupes();
                for (int i2 = 0; i2 < individualGrpOfDupes.size(); i2++) {
                    ImagesItem imageItem2 = individualGrpOfDupes.get(i2);
                    if (imageItem2.getPosition() == position) {
                        deleteIndividualFile(this.deleteContext, imageItem2.getImage(), individualGrpOfDupes, imageItem2);
                    }
                }
                this.groupOfDuplicates.get(imageItemGrpTag).setIndividualGrpOfDupes(individualGrpOfDupes);
                this.groupOfDuplicates.get(imageItemGrpTag).setGroupSetCheckBox(false);
            } else {
                break;
            }
        }
        return null;
    }

    public void onCancelled() {
        super.onCancelled();
        Log.e("onCancelled", "--cancellled--");
    }

    public void onCancelled(Void r1) {
        super.onCancelled(r1);
    }

    private void deleteIndividualFile(Context context, String str, List<ImagesItem> list, ImagesItem imageItem) {
        File file = new File(str);
        if (file.exists()) {
            Log.e("deleteIndividualFile", "----checking---" + GlobalVarsAndFunction.getStorageAccessFrameWorkURIPermission(this.deleteActivity.getApplicationContext()));
            if (GlobalVarsAndFunction.getStorageAccessFrameWorkURIPermission(this.deleteActivity.getApplicationContext()) != null) {
                GlobalVarsAndFunction.deletePhotoUsingSAFPermission(this.deleteContext, str);
                initializePageAndDataAfterDelete(list, imageItem);
            } else if (file.delete()) {
                Log.e("deleteIndividualFile", "--- delete ---");
                initializePageAndDataAfterDelete(list, imageItem);
                GlobalVarsAndFunction.deleteFileFromMediaStore(context, this.deleteContext.getContentResolver(), file);
                GlobalVarsAndFunction.updateDeletionCount(this.deleteContext, 1);
            } else {
                this.isTaskRunning = false;
                if (this.deleteDialog.isShowing()) {
                    this.deleteDialog.dismiss();
                }
                Log.e("deleteIndividualFile", "-------go to ----interface---");
            }
        }
    }

    private void initializePageAndDataAfterDelete(List<ImagesItem> list, ImagesItem imageItem) {
        this.isTaskRunning = true;
        list.remove(imageItem);
        for (int i = 0; i < this.fileToBeDeleted.size(); i++) {
            deleteImageIfPresent(this.fileToBeDeleted.get(i));
        }
        deselectAllImagesInOtherGroup();
    }

    public void deleteImageIfPresent(ImagesItem imageItem) {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            deleteImageInAnotherGroup(GlobalVarsAndFunction.getGroupOfDuplicatesExact(), imageItem);
        } else {
            deleteImageInAnotherGroup(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), imageItem);
        }
    }

    public void deleteImageInAnotherGroup(List<IndividualGroups> list, ImagesItem imageItem) {
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
                    this.deletedFileSize += imageItem.getSizeOfTheFile();
                    this.numberOfFilesPresentInOtherScan++;
                    i2++;
                    if (i2 == size) {
                        this.numberOfFilesPresentInOtherScan--;
                        this.deletedFileSize -= imageItem.getSizeOfTheFile();
                    }
                    if (isImageCheckBox) {
                        CommonUsed.logmsg("Ya it is checked!!!! ");
                        if (GlobalVarsAndFunction.getTabSelected() != 0) {
                            removeFromDeletingArrayList(GlobalVarsAndFunction.file_To_Be_Deleted_Exact, imageItem);
                        } else {
                            removeFromDeletingArrayList(GlobalVarsAndFunction.file_To_Be_Deleted_Similar, imageItem);
                        }
                    }
                    individualGrpOfDupes.remove(imageItem2);
                }
            }
            individualGroup.setIndividualGrpOfDupes(individualGrpOfDupes);
        }
    }

    public void removeFromDeletingArrayList(ArrayList<ImagesItem> arrayList, ImagesItem imageItem) {
        for (int i = 0; i < arrayList.size(); i++) {
            try {
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
            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void deselectAllImagesInOtherGroup() {
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
            try {
                ImagesItem imageItem2 = GlobalVarsAndFunction.file_To_Be_Deleted_Similar.get(i2);
                imageItem2.setImageCheckBox(false);
                GlobalVarsAndFunction.getGroupOfDuplicatesSimilar().get(imageItem2.getImageItemGrpTag()).setGroupSetCheckBox(false);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            }
        }
        GlobalVarsAndFunction.file_To_Be_Deleted_Similar.clear();
        GlobalVarsAndFunction.size_Of_File_Similar = 0;
    }

    public void onPostExecute(Void r11) {
        super.onPostExecute(r11);
        if (this.isTaskRunning) {
            ProgressDialog progressDialog = this.deleteDialog;
            if (progressDialog != null && progressDialog.isShowing()) {
                try {
                    this.deleteDialog.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                if (GlobalVarsAndFunction.getTabSelected() == 0) {
                    RepeaterIndividualGroup individualGroupAdapter = new RepeaterIndividualGroup(this.deleteContext, this.deleteActivity, this.groupOfDuplicates, this, this.imageLoader, this.options);
                    FragExactDuplicates.recyclerViewForIndividualGrp.setAdapter(individualGroupAdapter);

                    GlobalVarsAndFunction.setMemoryRegainedSimilar(GlobalVarsAndFunction.getStringSizeLengthFile(GlobalVarsAndFunction.getMemoryRegainedSimilarInLong() - this.deletedFileSize));
                    GlobalVarsAndFunction.setTotalDuplicatesSimilar(GlobalVarsAndFunction.getTotalDuplicatesSimilar() - this.numberOfFilesPresentInOtherScan);
                    FragSimilarDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(this.deleteContext, this.deleteActivity, GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), this, this.imageLoader, this.options));

                    new PopUpDialogs(this.deleteContext, this.deleteActivity)
                            .memoryRecoveredPopUp(this.deleteContext.getString(R.string.Exact_Duplicates_Cleaned) + " " + this.fileToBeDeleted.size(),
                                    this.deleteContext.getString(R.string.Space_Regained) + " " + GlobalVarsAndFunction.sizeInString(),
                                    String.valueOf(this.fileToBeDeleted.size()),
                                    GlobalVarsAndFunction.sizeInDouble(),
                                    this);
                } else {
                    RepeaterIndividualGroup individualGroupAdapter2 = new RepeaterIndividualGroup(this.deleteContext, this.deleteActivity, this.groupOfDuplicates, this, this.imageLoader, this.options);
                    FragSimilarDuplicates.recyclerViewForIndividualGrp.setAdapter(individualGroupAdapter2);

                    GlobalVarsAndFunction.setMemoryRegainedExact(GlobalVarsAndFunction.getMemoryRegainedExact());
                    GlobalVarsAndFunction.setTotalDuplicatesExact(GlobalVarsAndFunction.getTotalDuplicatesExact() - this.numberOfFilesPresentInOtherScan);
                    FragExactDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(this.deleteContext, this.deleteActivity, GlobalVarsAndFunction.getGroupOfDuplicatesExact(), this, this.imageLoader, this.options));

                    new PopUpDialogs(this.deleteContext, this.deleteActivity)
                            .memoryRecoveredPopUp(this.deleteContext.getString(R.string.Similar_Duplicates_Cleaned) + " " + this.fileToBeDeleted.size(),
                                    this.deleteContext.getString(R.string.Space_Regained) + " " + GlobalVarsAndFunction.sizeInString(),
                                    String.valueOf(this.fileToBeDeleted.size()),
                                    GlobalVarsAndFunction.sizeInDouble(),
                                    this);
                }
            }
            this.numberOfFilesPresentInOtherScan = 0;
        }
    }

    @Override
    public void updateMarkedExact() {
        CommonUsed.logmsg("Marked: " + GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
        this.marked.setText(this.deleteContext.getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
    }

    @Override
    public void updateDuplicateFoundExact(int i) {
        CommonUsed.logmsg("Update Duplicate Found: " + i);
        this.duplicatesFound.setText(this.deleteContext.getString(R.string.Duplicates_Found) + " " + i);
    }

    @Override
    public void photosCleanedExact(int i) {
        int photoCleaned = GlobalVarsAndFunction.getPhotoCleaned(this.deleteContext);
        CommonUsed.logmsg("Number of digits in Photo cleaned: " + GlobalVarsAndFunction.checkNumberOfDigits(photoCleaned));
        if (GlobalVarsAndFunction.checkNumberOfDigits(photoCleaned) > 4) {
//            this.tvPhotoCleaned.setTextSize(2, 35.0f);
        } else if (GlobalVarsAndFunction.checkNumberOfDigits(photoCleaned) > 7) {
//            this.tvPhotoCleaned.setTextSize(2, 20.0f);
        }
//        this.tvPhotoCleaned.setText("" + photoCleaned);
        GlobalVarsAndFunction.setPhotoCleaned(this.deleteContext, photoCleaned);
    }

    @Override
    public void updateMarkedSimilar() {
        CommonUsed.logmsg("Marked: " + GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
        this.marked.setText(this.deleteContext.getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
    }

    @Override
    public void updateDuplicateFoundSimilar(int i) {
        this.duplicatesFound.setText(this.deleteContext.getString(R.string.Duplicates_Found) + " " + GlobalVarsAndFunction.getTotalDuplicatesSimilar());
    }

    @Override
    public void photosCleanedSimilar(int i) {
        Context context = this.deleteContext;
        GlobalVarsAndFunction.setPhotoCleaned(context, GlobalVarsAndFunction.getPhotoCleaned(context));
    }

}
