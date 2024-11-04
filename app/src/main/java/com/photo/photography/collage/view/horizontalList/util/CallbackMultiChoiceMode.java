package com.photo.photography.collage.view.horizontalList.util;

import android.annotation.TargetApi;
import android.view.ActionMode;

@TargetApi(11)
public interface CallbackMultiChoiceMode extends ActionMode.Callback {
    void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked);
}