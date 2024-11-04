package com.photo.photography.edit_views;

import android.util.Log;

import com.photo.photography.data_helper.Media;

import java.util.ArrayList;

public class Constants {
    public static final String KEY_JPG = ".jpg";
    public static final String KEY_PNG = ".png";
    public static final String IMAGE_URL_1 = "http://143.244.133.154/sticker/art/";
    public static final int IMAGE_SIZE_1 = 8;
    public static final String KEY_CATEGORY_1 = "Art";
    public static final String KEY_FILE_NAME_1 = "art_3d_";
    public static final String IMAGE_URL_2 = "http://143.244.133.154/sticker/birthday/";
    public static final int IMAGE_SIZE_2 = 7;
    public static final String KEY_CATEGORY_2 = "Birthday";
    public static final String KEY_FILE_NAME_2 = "birthday_";
    public static final String IMAGE_URL_3 = "http://143.244.133.154/sticker/candy/";
    public static final int IMAGE_SIZE_3 = 5;
    public static final String KEY_CATEGORY_3 = "Candy";
    public static final String KEY_FILE_NAME_3 = "candy_";
    public static final String IMAGE_URL_4 = "http://143.244.133.154/sticker/cool/";
    public static final int IMAGE_SIZE_4 = 29;
    public static final String KEY_CATEGORY_4 = "Cool";
    public static final String KEY_FILE_NAME_4 = "cool_";
    public static final String IMAGE_URL_5 = "http://143.244.133.154/sticker/cosmic/";
    public static final int IMAGE_SIZE_5 = 8;
    public static final String KEY_CATEGORY_5 = "Comic";
    public static final String KEY_FILE_NAME_5 = "comic_";
    public static final String IMAGE_URL_6 = "http://143.244.133.154/sticker/decorative/";
    public static final int IMAGE_SIZE_6 = 10;
    public static final String KEY_CATEGORY_6 = "Decorative";
    public static final String KEY_FILE_NAME_6 = "decorative_";
    public static final String IMAGE_URL_7 = "http://143.244.133.154/sticker/emoji/";
    public static final int IMAGE_SIZE_7 = 18;
    public static final String KEY_CATEGORY_7 = "Emoji";
    public static final String KEY_FILE_NAME_7 = "emoji_";
    public static final String IMAGE_URL_8 = "http://143.244.133.154/sticker/food/";
    public static final int IMAGE_SIZE_8 = 14;
    public static final String KEY_CATEGORY_8 = "Food";
    public static final String KEY_FILE_NAME_8 = "food_";
    public static final String IMAGE_URL_9 = "http://143.244.133.154/sticker/glasses/";
    public static final int IMAGE_SIZE_9 = 6;
    public static final String KEY_CATEGORY_9 = "Glasses";
    public static final String KEY_FILE_NAME_9 = "glasses_";
    public static final String IMAGE_URL_10 = "http://143.244.133.154/sticker/magic/";
    public static final int IMAGE_SIZE_10 = 30;
    public static final String KEY_CATEGORY_10 = "Magic";
    public static final String KEY_FILE_NAME_10 = "magic_";
    public static final String IMAGE_URL_11 = "http://143.244.133.154/sticker/panda/";
    public static final int IMAGE_SIZE_11 = 9;
    public static final String KEY_CATEGORY_11 = "Panda";
    public static final String KEY_FILE_NAME_11 = "panda_";
    public static final String IMAGE_URL_12 = "http://143.244.133.154/sticker/popart/";
    public static final int IMAGE_SIZE_12 = 14;
    public static final String KEY_CATEGORY_12 = "PopArt";
    public static final String KEY_FILE_NAME_12 = "popart_";
    public static final String IMAGE_URL_13 = "http://143.244.133.154/sticker/socialize/";
    public static final int IMAGE_SIZE_13 = 13;
    public static final String KEY_CATEGORY_13 = "Socialize";
    public static final String KEY_FILE_NAME_13 = "socialize_";
    public static final String IMAGE_URL_14 = "http://143.244.133.154/sticker/space/";
    public static final int IMAGE_SIZE_14 = 9;
    public static final String KEY_CATEGORY_14 = "Space";
    public static final String KEY_FILE_NAME_14 = "space_";
    public static final int MAXIMUM_VIEW = 200;
    public static boolean setFullScreenIsInView = false;
    public static String ADMOB = "admob";
    public static String ADX = "adx";

    public static ArrayList<Media> getFilteredMedia(ArrayList<Media> media, int position) {
        int listSize = media.size() - 1;
        int startPos = position - (MAXIMUM_VIEW / 2);
        int endPos = position + (MAXIMUM_VIEW / 2);
        if (startPos < 0) {
            startPos = 0;
        }
        if (startPos == 0 && listSize > MAXIMUM_VIEW && (startPos + endPos) < MAXIMUM_VIEW) {
            endPos = MAXIMUM_VIEW;
        }
        if (endPos > listSize) {
            startPos = startPos - 1;
            endPos = listSize + 1;
        }
        if (endPos == listSize && listSize > MAXIMUM_VIEW) {
            startPos = listSize - MAXIMUM_VIEW;
            if (startPos < 0) {
                startPos = 0;
            }
        }
        Log.e("FILTERED_ARRAY", "Start from " + startPos + " to " + endPos);
        return new ArrayList<Media>(media.subList(startPos, endPos));
    }
}
