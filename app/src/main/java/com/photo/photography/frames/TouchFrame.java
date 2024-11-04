package com.photo.photography.frames;


import com.photo.photography.callbacks.CallbackOnFrameTouch;

public abstract class TouchFrame implements CallbackOnFrameTouch {
    private boolean mImageFrameMoving = false;

    public boolean isImageFrameMoving() {
        return mImageFrameMoving;
    }

    public void setImageFrameMoving(boolean imageFrameMoving) {
        mImageFrameMoving = imageFrameMoving;
    }

}
