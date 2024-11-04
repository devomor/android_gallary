package com.photo.photography.callbacks;

import android.view.MotionEvent;

public interface CallbackOnFrameTouch {
    void onFrameTouch(MotionEvent event);

    void onFrameDoubleClick(MotionEvent event);
}
