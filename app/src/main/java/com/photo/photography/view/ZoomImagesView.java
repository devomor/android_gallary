package com.photo.photography.view;

import android.animation.AnimatorSet;
import android.view.GestureDetector;
import android.view.VelocityTracker;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;


public class ZoomImagesView {

    private final boolean draggingDown = false;
    private final float scale = 1;
    private final DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5f);
    private final float pinchStartScale = 1;
    private final boolean canZoom = true;
    private final boolean changingPage = false;
    private final boolean zooming = false;
    private final boolean moving = false;
    private final boolean doubleTap = false;
    private final boolean invalidCoords = false;
    private final boolean canDragDown = true;
    private final boolean zoomAnimation = false;
    private final boolean discardTap = false;
    private final int switchImageAfterAnimation = 0;
    private final VelocityTracker velocityTracker = null;
    private final Scroller scroller = null;
    private float dragY;
    private float translationX;
    private float translationY;
    private float animateToX;
    private float animateToY;
    private float animateToScale;
    private float animationValue;
    private int currentRotation;
    private long animationStartTime;
    private AnimatorSet imageMoveAnimation;
    private AnimatorSet changeModeAnimation;
    private GestureDetector gestureDetector;
    private float pinchStartDistance;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartX;
    private float pinchStartY;
    private float moveStartX;
    private float moveStartY;
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;


}
