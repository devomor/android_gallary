package com.photo.photography.collage.view.horizontalList.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.photo.photography.collage.view.horizontalList.widget.AbsHListViews;

public class MultiChoiceModeWrap implements CallbackMultiChoiceMode {

    private final AbsHListViews mView;
    private CallbackMultiChoiceMode mWrapped;

    public MultiChoiceModeWrap(AbsHListViews view) {
        mView = view;
    }

    public void setWrapped(CallbackMultiChoiceMode wrapped) {
        mWrapped = wrapped;
    }

    public boolean hasWrappedCallback() {
        return mWrapped != null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (mWrapped.onCreateActionMode(mode, menu)) {
            mView.setLongClickable(false);
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return mWrapped.onPrepareActionMode(mode, menu);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return mWrapped.onActionItemClicked(mode, item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mWrapped.onDestroyActionMode(mode);
        mView.mChoiceActionMode = null;

        // Ending selection mode means deselecting everything.
        mView.clearChoices();
        mView.mDataChanged = true;
        mView.rememberSyncState();
        mView.requestLayout();
        mView.setLongClickable(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        mWrapped.onItemCheckedStateChanged(mode, position, id, checked);

        // If there are no items selected we no longer need the selection mode.
        if (mView.getCheckedItemCount() == 0) {
            mode.finish();
        }
    }
}