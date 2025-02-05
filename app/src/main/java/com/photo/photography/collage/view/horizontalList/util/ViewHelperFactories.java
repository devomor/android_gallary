package com.photo.photography.collage.view.horizontalList.util;

import android.util.Log;
import android.view.View;


public class ViewHelperFactories {

    private static final String LOG_TAG = "ViewHelper";

    public static final ViewHelper create(View view) {
        final int version = android.os.Build.VERSION.SDK_INT;

        if (version >= 16) {
            // jelly bean
            return new ViewHelper16Util(view);
        } else if (version >= 14) {
            // ice cream sandwich
            return new ViewHelper14Util(view);
        } else {
            // fallback
            return new ViewHelperDefault(view);
        }
    }

    public static abstract class ViewHelper {


        protected View view;

        protected ViewHelper(View view) {
            this.view = view;
        }

        public abstract void postOnAnimation(Runnable action);

        public abstract void setScrollX(int value);

        public abstract boolean isHardwareAccelerated();
    }

    public static class ViewHelperDefault extends ViewHelper {

        public ViewHelperDefault(View view) {
            super(view);
        }

        @Override
        public void postOnAnimation(Runnable action) {
            view.post(action);
        }

        @Override
        public void setScrollX(int value) {
            Log.d(LOG_TAG, "setScrollX: " + value);
            view.scrollTo(value, view.getScrollY());
        }

        @Override
        public boolean isHardwareAccelerated() {
            return false;
        }
    }

}
