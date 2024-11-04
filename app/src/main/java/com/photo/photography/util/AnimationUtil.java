package com.photo.photography.util;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.photo.photography.util.preferences.Prefs;


public class AnimationUtil {

    public static RecyclerView.ItemAnimator getItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        if (Prefs.animationsEnabled()) {
            return itemAnimator;
        }
        return null;
    }

    public static ViewPager.PageTransformer getPageTransformer(ViewPager.PageTransformer pageTransformer) {
        if (Prefs.animationsEnabled()) {
            return pageTransformer;
        }
        return null;
    }
}
