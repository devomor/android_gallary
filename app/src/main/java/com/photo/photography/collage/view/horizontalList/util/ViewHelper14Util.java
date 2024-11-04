package com.photo.photography.collage.view.horizontalList.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import com.photo.photography.collage.view.horizontalList.util.ViewHelperFactories.ViewHelperDefault;


public class ViewHelper14Util extends ViewHelperDefault {

    public ViewHelper14Util(View view) {
        super(view);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setScrollX(int value) {
        view.setScrollX(value);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean isHardwareAccelerated() {
        return view.isHardwareAccelerated();
    }

}