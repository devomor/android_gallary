package com.photo.photography.collage.callback;

import android.view.MotionEvent;

public interface CallbackOnFrameTouch {
    void onFrameTouch(MotionEvent event);

    void onFrameDoubleClick(MotionEvent event);
}
