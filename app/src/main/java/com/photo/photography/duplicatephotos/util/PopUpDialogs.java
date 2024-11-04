package com.photo.photography.duplicatephotos.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.duplicatephotos.act.ActPhotoList;
import com.photo.photography.duplicatephotos.backgroundasynk.LockImag;
import com.photo.photography.duplicatephotos.backgroundasynk.SearchExactDuplicat;
import com.photo.photography.duplicatephotos.backgroundasynk.SearchSimilarDuplicat;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.files.DeleteSingleImageFiles;
import com.photo.photography.duplicatephotos.callback.CallbackIDeletionPermission;
import com.photo.photography.duplicatephotos.callback.CallbackMarked;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

//import com.facebook.CallbackManager;
//import com.facebook.FacebookSdk;
//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.widget.ShareDialog;

public class PopUpDialogs implements CallbackIDeletionPermission {
    Activity pUDActivity;
    Context pUDContext;
    int previousMatchingLevel;
    int previousNotificationLimit;
    private DiscreteSeekBar SeekbarWithIntervals = null;
    private DiscreteSeekBar SeekbarWithIntervalsNotification = null;

    public PopUpDialogs(Context context, Activity activity) {
        this.pUDContext = context;
        this.pUDActivity = activity;
        this.pUDContext.getSharedPreferences("myflag", 0);
    }

    public static List<IndividualGroups> sortByDateAscending(List<IndividualGroups> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                List<ImagesItem> individualGrpOfDupes = list.get(i).getIndividualGrpOfDupes();
                GlobalVarsAndFunction.sortByDateAscending(individualGrpOfDupes);
                list.get(i).setIndividualGrpOfDupes(individualGrpOfDupes);
            }
        }
        return list;
    }

    public static List<IndividualGroups> sortBySizeAscending(List<IndividualGroups> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                List<ImagesItem> individualGrpOfDupes = list.get(i).getIndividualGrpOfDupes();
                GlobalVarsAndFunction.sortBySizeAscending(individualGrpOfDupes);
                list.get(i).setIndividualGrpOfDupes(individualGrpOfDupes);
            }
        }
        return list;
    }

    public static List<IndividualGroups> sortByDateDescending(List<IndividualGroups> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                List<ImagesItem> individualGrpOfDupes = list.get(i).getIndividualGrpOfDupes();
                GlobalVarsAndFunction.sortByDateDescending(individualGrpOfDupes);
                list.get(i).setIndividualGrpOfDupes(individualGrpOfDupes);
            }
        }
        return list;
    }

    public static List<IndividualGroups> sortBySizeDescending(List<IndividualGroups> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                List<ImagesItem> individualGrpOfDupes = list.get(i).getIndividualGrpOfDupes();
                GlobalVarsAndFunction.sortBySizeDescending(individualGrpOfDupes);
                list.get(i).setIndividualGrpOfDupes(individualGrpOfDupes);
            }
        }
        return list;
    }

    @Override
    public void deletionResponseLister(Object obj, boolean z, String str, Object obj2) {
    }

    private DiscreteSeekBar getSeekBarWithIntervals(final View view) {
        if (this.SeekbarWithIntervals != null) {
            return null;
        }
        this.SeekbarWithIntervals = view.findViewById(R.id.seekbarWithIntervals);
        this.SeekbarWithIntervals.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                int i2 = i * 10;
                ((TextView) view.findViewById(R.id.matching_level_offsets)).setText(i2 + "%");
                return i2;
            }
        });
        return this.SeekbarWithIntervals;
    }

    private DiscreteSeekBar getSeekBarWithIntervalsNotification(final View view) {
        if (this.SeekbarWithIntervalsNotification != null) {
            return null;
        }
        this.SeekbarWithIntervalsNotification = view.findViewById(R.id.seekbarWithIntervalsNotification);
        this.SeekbarWithIntervalsNotification.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                int i2 = i * 10;
                ((TextView) view.findViewById(R.id.matching_level_offsets_Notification)).setText("" + i2);
                return i2;
            }
        });
        return this.SeekbarWithIntervalsNotification;
    }

    public void matchingLevelPopUpDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        View inflate = this.pUDActivity.getLayoutInflater().inflate(R.layout.dialog_matching_setting, null);
        builder.setView(inflate);
        builder.setCancelable(false);
        getSeekBarWithIntervals(inflate);
        getSeekBarWithIntervalsNotification(inflate);
        this.previousMatchingLevel = GlobalVarsAndFunction.getCurrentMatchingLevel(this.pUDContext);
        CommonUsed.logmsg("previousMatchingLevel : " + this.previousMatchingLevel);
        this.SeekbarWithIntervals.setProgress(this.previousMatchingLevel);
        this.previousNotificationLimit = GlobalVarsAndFunction.getNotificationLimit(this.pUDContext);
        CommonUsed.logmsg("previousNotificationLimit : " + this.previousNotificationLimit);
        this.SeekbarWithIntervalsNotification.setProgress(this.previousNotificationLimit);
        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inflate.findViewById(R.id.dialog_matching_level_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SeekbarWithIntervals = null;
                SeekbarWithIntervalsNotification = null;
                create.dismiss();
            }
        });
        inflate.findViewById(R.id.dialog_matching_level_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SeekbarWithIntervals = null;
                CommonUsed.logmsg("previousMatchingLevel in cancel: " + previousMatchingLevel);
                CommonUsed.logmsg("previousNotification in cancel: " + previousNotificationLimit);
                GlobalVarsAndFunction.setCurrentMatchingLevel(pUDContext, previousMatchingLevel);
                GlobalVarsAndFunction.setNotificationLimit(pUDContext, previousNotificationLimit);
                create.dismiss();
            }
        });
        this.SeekbarWithIntervals.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            @Override
            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int i, boolean z) {
                CommonUsed.logmsg("Progress: " + i);
                GlobalVarsAndFunction.setCurrentMatchingLevel(pUDContext, i);
                GlobalVarsAndFunction.setCorrespondingValueForMatchingLevels(i);
            }
        });
        this.SeekbarWithIntervalsNotification.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            @Override
            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int i, boolean z) {
                CommonUsed.logmsg("Notification: " + i);
                GlobalVarsAndFunction.setNotificationLimit(pUDContext, i);
            }
        });
        ((CheckBox) inflate.findViewById(R.id.default_check_box)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SeekbarWithIntervals.setProgress(5);
                }
            }
        });
        ((CheckBox) inflate.findViewById(R.id.default_check_box_Notification)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SeekbarWithIntervalsNotification.setProgress(3);
                }
            }
        });
        create.show();
    }

    public void memoryRecoveredPopUp(String str, String str2, final String str3, final double d, final CallbackMarked markedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        View inflate = this.pUDActivity.getLayoutInflater().inflate(R.layout.dialog_after_deleted, null);
        builder.setView(inflate);
        builder.setCancelable(false);

        TextView textView = inflate.findViewById(R.id.cleaned_photo);
        textView.setText(str);

        TextView textView2 = inflate.findViewById(R.id.cleaned_memory);
        textView2.setText(str2);

        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inflate.findViewById(R.id.dialogButtonok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    int size = GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size();
                    GlobalVarsAndFunction.file_To_Be_Deleted_Similar.clear();
                    GlobalVarsAndFunction.size_Of_File_Similar = 0;
                    GlobalVarsAndFunction.setMemoryRegainedSimilar("");
                    markedListener.updateMarkedSimilar();
                    markedListener.updateDuplicateFoundSimilar(GlobalVarsAndFunction.getTotalDuplicatesSimilar());
                    markedListener.photosCleanedSimilar(size);
                } else {
                    int size2 = GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size();
                    GlobalVarsAndFunction.file_To_Be_Deleted_Exact.clear();
                    GlobalVarsAndFunction.size_Of_File_Exact = 0;
                    GlobalVarsAndFunction.setMemoryRegainedExact("");
                    markedListener.updateMarkedExact();
                    markedListener.updateDuplicateFoundExact(GlobalVarsAndFunction.getTotalDuplicatesExact());
                    markedListener.photosCleanedExact(size2);
                }
                create.dismiss();
            }
        });
        try {
            create.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    private boolean appInstalledOrNot(String str) {
        try {
            this.pUDContext.getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public void deleteAlertForSingleImagePopUp(String str, final CallbackIDeletionPermission iDeletionPermissionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        builder.setMessage(str).setCancelable(false).setPositiveButton(this.pUDContext.getString(R.string.YES), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    new DeleteSingleImageFiles(pUDContext, pUDActivity).deleteImage(GlobalVarsAndFunction.file_To_Be_Deleted_Preview_Similar, GlobalVarsAndFunction.getGroupOfDuplicatesSimilar(), iDeletionPermissionListener);
                } else {
                    new DeleteSingleImageFiles(pUDContext, pUDActivity).deleteImage(GlobalVarsAndFunction.file_To_Be_Deleted_Preview_Exact, GlobalVarsAndFunction.getGroupOfDuplicatesExact(), iDeletionPermissionListener);
                }
            }
        }).setNegativeButton(this.pUDContext.getString(R.string.NO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Preview_Similar.clear();
                } else {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Preview_Exact.clear();
                }
            }
        });
        AlertDialog create = builder.create();
        create.setTitle(this.pUDActivity.getString(R.string.Confirm_Delete));
        create.show();
    }

    public void showAlertBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        View inflate = this.pUDActivity.getLayoutInflater().inflate(R.layout.dialog_alerts, null);
        builder.setView(inflate);
        builder.setCancelable(false);

        TextView textView = inflate.findViewById(R.id.tvTitle);
        textView.setText(R.string.Alert);

        TextView textView1 = inflate.findViewById(R.id.tvMsg);
        textView1.setText(R.string.Are_you_sure_want_to_go_back);

        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inflate.findViewById(R.id.tvNo).setOnClickListener(view -> create.dismiss());
        inflate.findViewById(R.id.tvYes).setOnClickListener(view -> {
            create.dismiss();
            pUDActivity.finish();
        });
        create.show();
    }

    public void showAlertStopScanning(final SearchSimilarDuplicat searchSimilarDuplicates, final SearchExactDuplicat searchExactDuplicates) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        builder.setMessage(R.string.Scanning_is_in_progress_Do_you_want_to_abort).setCancelable(false).setPositiveButton(this.pUDContext.getString(R.string.YES), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                searchSimilarDuplicates.stopSimilarAsync();
                searchExactDuplicates.stopExactAsync();
                pUDActivity.finish();
            }
        }).setNegativeButton(this.pUDContext.getString(R.string.NO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog create = builder.create();
        create.setTitle(this.pUDContext.getString(R.string.Stop_Scanning));
        create.show();
    }

    public void memoryRecoveredForSingleImagePopUp(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        View inflate = this.pUDActivity.getLayoutInflater().inflate(R.layout.dialog_memories_regain, null);
        builder.setView(inflate);
        builder.setCancelable(false);

        TextView textView = inflate.findViewById(R.id.duplicatesfoundafter);
        textView.setText(str);

        TextView textView2 = inflate.findViewById(R.id.sizeofdupes);
        textView2.setText(str2);

        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inflate.findViewById(R.id.dialogButtonAccept).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Preview_Similar.clear();
                    Intent intent = new Intent(pUDActivity, ActPhotoList.class);
                    intent.putExtra("memoryPopUpAndRecoverPopUp", "showRecoverPopUp");
                    intent.putExtra("tS", "similar");
                    intent.putExtra("showSimilarRegainedPopUpExact", GlobalVarsAndFunction.SHOW_REGAIN_POP_UP_ONLY_ON_SCAN_EXACT);
                    intent.putExtra("showSimilarRegainedPopUpSimilar", GlobalVarsAndFunction.SHOW_REGAIN_POP_UP_ONLY_ON_SCAN_SIMILAR);

                    if (MyApp.getInstance().needToShowAd()) {
                        MyApp.getInstance().showInterstitial(pUDActivity, intent, false, -1, null);
                    } else {
                        pUDActivity.startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(pUDContext, R.anim.anim_slide_in_left, R.anim.anim_slide_out_right).toBundle());
                    }

                } else {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Preview_Exact.clear();
                    Intent intent2 = new Intent(pUDActivity, ActPhotoList.class);
//                    intent2.addFlags(335577088);
                    intent2.putExtra("memoryPopUpAndRecoverPopUp", "showRecoverPopUp");
                    intent2.putExtra("tS", "exact");
                    intent2.putExtra("showSimilarRegainedPopUpExact", GlobalVarsAndFunction.SHOW_REGAIN_POP_UP_ONLY_ON_SCAN_EXACT);
                    intent2.putExtra("showSimilarRegainedPopUpSimilar", GlobalVarsAndFunction.SHOW_REGAIN_POP_UP_ONLY_ON_SCAN_SIMILAR);

                    if (MyApp.getInstance().needToShowAd()) {
                        MyApp.getInstance().showInterstitial(pUDActivity, intent2, false, -1, null);
                    } else {
                        pUDActivity.startActivity(intent2, ActivityOptionsCompat.makeCustomAnimation(pUDContext, R.anim.anim_slide_in_left, R.anim.anim_slide_out_right).toBundle());
                    }

                }
                create.dismiss();
            }
        });
        create.show();
    }

    public void lockSelectedImages(final ArrayList<ImagesItem> arrayList, final String str, String str2, final List<IndividualGroups> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        View inflate = this.pUDActivity.getLayoutInflater().inflate(R.layout.dialog_locks, null);
        builder.setView(inflate);
        builder.setCancelable(false);

        TextView textView = inflate.findViewById(R.id.lockText);
        textView.setText(str2);

        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inflate.findViewById(R.id.lockOk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new LockImag(pUDContext, pUDActivity, str, arrayList, list).execute();
                create.dismiss();
            }
        });
        inflate.findViewById(R.id.lockCancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    ArrayList<ImagesItem> lockSimilarPhotos = GlobalVarsAndFunction.getLockSimilarPhotos(pUDContext);
                    lockSimilarPhotos.clear();
                    GlobalVarsAndFunction.setLockSimilarPhotos(pUDContext, lockSimilarPhotos);
                    return;
                }
                ArrayList<ImagesItem> lockExactPhotos = GlobalVarsAndFunction.getLockExactPhotos(pUDContext);
                lockExactPhotos.clear();
                GlobalVarsAndFunction.setLockExactPhotos(pUDContext, lockExactPhotos);
            }
        });
        create.show();
    }

    public void memoryExceptionPopUp(String str, final CallbackMarked markedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.pUDActivity);
        View inflate = this.pUDActivity.getLayoutInflater().inflate(R.layout.dialog_after_locks, null);
        builder.setView(inflate);
        builder.setCancelable(false);

        TextView textView = inflate.findViewById(R.id.afterLockPopupText);
        textView.setText(str);

        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inflate.findViewById(R.id.afterLockPopupBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    int size = GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size();
                    GlobalVarsAndFunction.file_To_Be_Deleted_Similar.clear();
                    GlobalVarsAndFunction.size_Of_File_Similar = 0;
                    int totalDuplicatesSimilar = GlobalVarsAndFunction.getTotalDuplicatesSimilar() - size;
                    GlobalVarsAndFunction.setMemoryRegainedSimilar("");
                    GlobalVarsAndFunction.setTotalDuplicatesSimilar(totalDuplicatesSimilar);
                    markedListener.updateMarkedSimilar();
                    markedListener.updateDuplicateFoundSimilar(totalDuplicatesSimilar);
                    return;
                }
                int size2 = GlobalVarsAndFunction.file_To_Be_Deleted_Exact.size();
                GlobalVarsAndFunction.file_To_Be_Deleted_Exact.clear();
                GlobalVarsAndFunction.size_Of_File_Exact = 0;
                int totalDuplicatesExact = GlobalVarsAndFunction.getTotalDuplicatesExact() - size2;
                GlobalVarsAndFunction.setMemoryRegainedExact("");
                GlobalVarsAndFunction.setTotalDuplicatesExact(totalDuplicatesExact);
                markedListener.updateMarkedExact();
                markedListener.updateDuplicateFoundExact(totalDuplicatesExact);
            }
        });
        create.show();
    }

}
