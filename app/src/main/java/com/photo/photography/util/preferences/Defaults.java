package com.photo.photography.util.preferences;

import com.photo.photography.CardViewStyles;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;

/**
 * Class for storing Preference default values.
 */
public final class Defaults {

    public static final int FOLDER_COLUMNS_PORTRAIT = 3;
    public static final int FOLDER_COLUMNS_LANDSCAPE = 4;
    public static final int MEDIA_COLUMNS_PORTRAIT = 4;
    public static final int MEDIA_COLUMNS_LANDSCAPE = 5;
    public static final int TIMELINE_ITEMS_PORTRAIT = 4;
    public static final int TIMELINE_ITEMS_LANDSCAPE = 5;
    public static final int ALBUM_SORTING_MODE = SortingModes.DATE_DAY.getValue();
    public static final int ALBUM_SORTING_ORDER = SortingOrders.DESCENDING.getValue();
    public static final int CARD_STYLE = CardViewStyles.MATERIAL.getValue();
    public static final boolean SHOW_VIDEOS = true;
    public static final boolean SHOW_MEDIA_COUNT = true;
    public static final boolean SHOW_ALBUM_PATH = false;
    public static final int LAST_VERSION_CODE = 0;
    public static final boolean SHOW_EASTER_EGG = false;
    public static final boolean ANIMATIONS_DISABLED = false;
    public static final boolean TIMELINE_ENABLED = false;


    // Prevent class instantiation
    private Defaults() {
    }
}
