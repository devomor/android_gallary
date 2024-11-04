package com.photo.photography.edit_views.StickerViewEdit;

import android.view.MotionEvent;

public abstract class AbstractFlipEvents implements StickerIconEventListener {

    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        stickerView.flipCurrentSticker(getFlipDirection());
    }

    @StickerView.Flip
    protected abstract int getFlipDirection();
}
