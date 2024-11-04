package com.photo.photography.collage.frames;

import com.photo.photography.collage.callback.CallbackOnFrameTouch;

public abstract class FrameTouch implements CallbackOnFrameTouch {
    private boolean mImageFrameMoving = false;

    public boolean isImageFrameMoving() {
        return mImageFrameMoving;
    }

    public void setImageFrameMoving(boolean imageFrameMoving) {
        mImageFrameMoving = imageFrameMoving;
    }

}
