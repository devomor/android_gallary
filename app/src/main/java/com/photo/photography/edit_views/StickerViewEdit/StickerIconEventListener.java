package com.photo.photography.edit_views.StickerViewEdit;

import android.view.MotionEvent;

/**
 * Created by  on 29-12-2017.
 */

public interface StickerIconEventListener {
    void onActionDown(StickerView stickerView, MotionEvent event);

    void onActionMove(StickerView stickerView, MotionEvent event);

    void onActionUp(StickerView stickerView, MotionEvent event);
}
