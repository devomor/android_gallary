package com.photo.photography.frag;

//import javax.annotation.Nullable;


public interface EditModeListener {

    /**
     * Propagate Edit Mode switches to listeners.
     *
     * @param editMode Whether we are in Edit Mode or not.
     * @param selected The number of items selected.
     * @param total    The total number of items.
     * @param title    The Toolbar's title.
     */
    void changedEditMode(boolean editMode, int selected, int total, String title);

    /**
     * Propagate the selected item count to listeners.
     *
     * @param count The number of items selected.
     * @param total The total number of items.
     */
    void onItemsSelected(int count, int total);
}
