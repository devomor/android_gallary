package com.photo.photography.crop_image.anim;

@SuppressWarnings("unused")
public interface SimpleValueAnim {
    void startAnimation(long duration);

    void cancelAnimation();

    boolean isAnimationStarted();

    void addAnimatorListener(CallbackSimpleValueAnim animatorListener);
}
