package com.photo.photography.models;

import java.util.List;


public interface ItemParentMan<C> {

    /**
     * Getter for the list of this parent's child items.
     * <p>
     * If list is empty, the parent has no children.
     *
     * @return A {@link List} of the children of this {@link ItemParentMan}
     */
    List<C> getChildList();

    /**
     * Getter used to determine if this {@link ItemParentMan}'s
     * {@link android.view.View} should show up initially as expanded.
     *
     * @return true if expanded, false if not
     */
    boolean isInitiallyExpanded();
}