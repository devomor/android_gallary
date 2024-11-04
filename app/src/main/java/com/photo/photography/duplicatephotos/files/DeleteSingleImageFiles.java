package com.photo.photography.duplicatephotos.files;

import android.app.Activity;
import android.content.Context;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.repeater.RepeaterIndividualGroup;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.frag.FragExactDuplicates;
import com.photo.photography.duplicatephotos.frag.FragSimilarDuplicates;
import com.photo.photography.duplicatephotos.callback.CallbackIDeletionPermission;
import com.photo.photography.duplicatephotos.callback.CallbackMarked;
import com.photo.photography.duplicatephotos.util.PopUpDialogs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteSingleImageFiles implements CallbackMarked, CallbackIDeletionPermission {
    Activity dSIFActivity;
    Context dSIFContext;
    List<ImagesItem> fileToBeDeleted;
    List<IndividualGroups> groupOfDuplicatesGlobal = new ArrayList();
    int numberOfFilesPresentInOtherScan = 0;
    private CallbackIDeletionPermission iDeletionPermissionListener;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public DeleteSingleImageFiles(Context context, Activity activity) {
        this.dSIFContext = context;
        this.dSIFActivity = activity;
        initUi();
    }

    @Override
    public void deletionResponseLister(Object obj, boolean z, String str, Object obj2) {
    }

    @Override
    public void photosCleanedExact(int i) {
    }

    @Override
    public void photosCleanedSimilar(int i) {
    }

    @Override
    public void updateDuplicateFoundExact(int i) {
    }

    @Override
    public void updateDuplicateFoundSimilar(int i) {
    }

    @Override
    public void updateMarkedExact() {
    }

    @Override
    public void updateMarkedSimilar() {
    }

    private void initUi() {
        this.imageLoader = GlobalVarsAndFunction.getImageLoadingInstance();
        this.options = GlobalVarsAndFunction.getOptions();
        GlobalVarsAndFunction.configureImageLoader(this.imageLoader, this.dSIFActivity);
    }

    public void deleteImage(List<ImagesItem> list, List<IndividualGroups> list2, CallbackIDeletionPermission iDeletionPermissionListener2) {
        this.groupOfDuplicatesGlobal = list2;
        this.fileToBeDeleted = list;
        this.iDeletionPermissionListener = iDeletionPermissionListener2;
        try {
            deleteImagesByPosition(this.dSIFActivity, list.get(0), this.groupOfDuplicatesGlobal);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
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
            for (int i2 = 0; i2 < individualGrpOfDupes.size(); i2++) {
                String image = imageItem.getImage();
                ImagesItem imageItem2 = individualGrpOfDupes.get(i2);
                String image2 = imageItem2.getImage();
                boolean isImageCheckBox = imageItem2.isImageCheckBox();
                if (image2.equalsIgnoreCase(image)) {
                    this.numberOfFilesPresentInOtherScan++;
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

    public void deselectAllImagesInOtherGroup() {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            for (int i = 0; i < GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size(); i++) {
                GlobalVarsAndFunction.file_To_Be_Deleted_Exact.get(i).setImageCheckBox(false);
            }
            GlobalVarsAndFunction.file_To_Be_Deleted_Exact.clear();
            GlobalVarsAndFunction.size_Of_File_Exact = 0;
            return;
        }
        for (int i2 = 0; i2 < GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size(); i2++) {
            GlobalVarsAndFunction.file_To_Be_Deleted_Similar.get(i2).setImageCheckBox(false);
        }
        GlobalVarsAndFunction.file_To_Be_Deleted_Similar.clear();
        GlobalVarsAndFunction.size_Of_File_Similar = 0;
    }

    private void updateDuplicateAndMarked(long j) {
        if (GlobalVarsAndFunction.getTabSelected() != 0) {
            RepeaterIndividualGroup individualGroupAdapter = new RepeaterIndividualGroup(this.dSIFContext, this.dSIFActivity, this.groupOfDuplicatesGlobal, this, this.imageLoader, this.options);
            FragSimilarDuplicates.recyclerViewForIndividualGrp.setAdapter(individualGroupAdapter);
            individualGroupAdapter.notifyDataSetChanged();
            GlobalVarsAndFunction.setMemoryRegainedExact(GlobalVarsAndFunction.getMemoryRegainedExact());
            GlobalVarsAndFunction.setTotalDuplicatesExact(GlobalVarsAndFunction.getTotalDuplicatesExact() - this.numberOfFilesPresentInOtherScan);
            FragExactDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(this.dSIFContext, this.dSIFActivity, GlobalVarsAndFunction.getGroupOfDuplicatesExact(), this, this.imageLoader, this.options));
            individualGroupAdapter.notifyDataSetChanged();
            new PopUpDialogs(this.dSIFContext, this.dSIFActivity).memoryRecoveredForSingleImagePopUp(this.dSIFContext.getString(R.string.Similar_Duplicates_Cleaned) + " " + 1,
                    this.dSIFContext.getString(R.string.Space_Regained) + " " + GlobalVarsAndFunction.getStringSizeLengthFile(j));
        } else {
            RepeaterIndividualGroup individualGroupAdapter2 = new RepeaterIndividualGroup(this.dSIFContext, this.dSIFActivity, this.groupOfDuplicatesGlobal, this, this.imageLoader, this.options);
            FragExactDuplicates.recyclerViewForIndividualGrp.setAdapter(individualGroupAdapter2);
            individualGroupAdapter2.notifyDataSetChanged();
            GlobalVarsAndFunction.setMemoryRegainedSimilar(GlobalVarsAndFunction.getMemoryRegainedSimilar());
            GlobalVarsAndFunction.setTotalDuplicatesSimilar(GlobalVarsAndFunction.getTotalDuplicatesSimilar() - this.numberOfFilesPresentInOtherScan);
            FragSimilarDuplicates.recyclerViewForIndividualGrp.setAdapter(new RepeaterIndividualGroup(this.dSIFContext, this.dSIFActivity, GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), this, this.imageLoader, this.options));
            individualGroupAdapter2.notifyDataSetChanged();
            new PopUpDialogs(this.dSIFContext, this.dSIFActivity).memoryRecoveredForSingleImagePopUp(this.dSIFContext.getString(R.string.Exact_Duplicates_Cleaned) + " " + 1,
                    this.dSIFContext.getString(R.string.Space_Regained) + " " + GlobalVarsAndFunction.getStringSizeLengthFile(j));
        }
        this.numberOfFilesPresentInOtherScan = 0;
    }

    private void deleteImagesByPosition(Activity activity, ImagesItem imageItem, List<IndividualGroups> list) {
        int imageItemGrpTag = imageItem.getImageItemGrpTag();
        int position = imageItem.getPosition();
        try {
            IndividualGroups individualGroup = list.get(imageItemGrpTag);
            List<ImagesItem> individualGrpOfDupes = individualGroup.getIndividualGrpOfDupes();
            for (int i = 0; i < individualGrpOfDupes.size(); i++) {
                ImagesItem imageItem2 = individualGrpOfDupes.get(i);
                if (imageItem2.getPosition() == position) {
                    deleteFile(activity, this.dSIFContext, imageItem2.getImage(), individualGrpOfDupes, imageItem2);
                }
            }
            individualGroup.setIndividualGrpOfDupes(individualGrpOfDupes);
            individualGroup.setGroupSetCheckBox(false);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NullPointerException e2) {
            e2.printStackTrace();
        }
    }

    public void deleteFile(Activity activity, Context context, String str, List<ImagesItem> list, ImagesItem imageItem) {
        File file = new File(str);
        if (!file.exists()) {
            return;
        }
        if (GlobalVarsAndFunction.getStorageAccessFrameWorkURIPermission(activity.getApplicationContext()) != null) {
            GlobalVarsAndFunction.deletePhotoUsingSAFPermission(context, str);
            initializePageAndDataAfterDeleteSingleImage(list, imageItem);
        } else if (file.delete()) {
            initializePageAndDataAfterDeleteSingleImage(list, imageItem);
            GlobalVarsAndFunction.updateDeletionCount(context, 1);
            GlobalVarsAndFunction.deleteFileFromMediaStore(context, context.getContentResolver(), file);
        } else {
            CallbackIDeletionPermission iDeletionPermissionListener2 = this.iDeletionPermissionListener;
            if (iDeletionPermissionListener2 != null) {
                iDeletionPermissionListener2.deletionResponseLister(null, false, null, null);
            }
        }
    }

    private void initializePageAndDataAfterDeleteSingleImage(List<ImagesItem> list, ImagesItem imageItem) {
        list.remove(imageItem);
        deleteImageIfPresent(this.fileToBeDeleted.get(0));
        deselectAllImagesInOtherGroup();
        updateDuplicateAndMarked(this.fileToBeDeleted.get(0).getSizeOfTheFile());
    }
}
