package com.photo.photography.collage.view.horizontalList.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

public class ViewHelper16Util extends ViewHelper14Util {
    public ViewHelper16Util(View view) {
        super(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void postOnAnimation(Runnable action) {
        view.postOnAnimation(action);
    }
}