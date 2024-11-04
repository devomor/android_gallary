package com.photo.photography.edit_views.StickerViewEdit;

/**
 * Created by  on 29-12-2017.
 */

public class FlipVerticallyEvents extends AbstractFlipEvents {

    @Override
    @StickerView.Flip
    protected int getFlipDirection() {
        return StickerView.FLIP_VERTICALLY;
    }
}
