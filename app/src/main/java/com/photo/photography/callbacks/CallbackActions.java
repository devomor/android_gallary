package com.photo.photography.callbacks;


import android.widget.ImageView;

/**
 * Interface for listeners to be alerted when this entity wants to
 * perform some actions on items.
 */
public interface CallbackActions {

    /**
     * Used when the user clicks on an item.
     *
     * @param position The position that was clicked.
     */
    void onItemSelected(int position, ImageView imageView);

    void onItemViewSelected(int position, ImageView imageView);

    /**
     * Use to toggle Select Mode states
     *
     * @param selectMode Whether we want to be in select mode or not.
     */
    void onSelectMode(boolean selectMode);

    /**
     * Used to notify listeners about selection counts.
     *
     * @param selectionCount The number of selected items
     * @param totalCount     The number of total items
     */
    void onSelectionCountChanged(int selectionCount, int totalCount);
    void onLocationClick();
}
