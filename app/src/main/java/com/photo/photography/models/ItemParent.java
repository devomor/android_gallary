package com.photo.photography.models;

import java.util.List;

/**
 * Created by  on 29-12-2017.
 */

public interface ItemParent<C> {

    /**
     * Getter for the list of this parent's child items.
     * <p>
     * If list is empty, the parent has no children.
     *
     * @return A {@link List} of the children of this {@link ItemParent}
     */
    List<C> getChildList();

    /**
     * Getter used to determine if this {@link ItemParent}'s
     * {@link android.view.View} should show up initially as expanded.
     *
     * @return true if expanded, false if not
     */
    boolean isInitiallyExpanded();
}