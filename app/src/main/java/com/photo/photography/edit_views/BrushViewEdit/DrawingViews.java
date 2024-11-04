package com.photo.photography.edit_views.BrushViewEdit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.photo.photography.edit_views.BrushViewEdit.floodfills.QueueLinearFlood;
import com.photo.photography.edit_views.BrushViewEdit.floodfills.ScalingUtils;
import com.photo.photography.edit_views.BrushViewEdit.manag.ThreadManag;

import java.util.ArrayList;
import java.util.List;

public class DrawingViews extends View {
    final private String TAG = DrawingViews.class.getSimpleName();
    final private int limit = 50;
    CallbackStartDrawing startDrawingListener;
    private GestureDetector gestureDetector;
    private CallbackOnGesture onGestureListener;
    private int CURRENT_STROKE_WIDTH = 10;
    private int CURRENT_STROKE_OPACITY = 100;
    private Point lastPosition;
    private Paint paint;
    private PorterDuffXfermode clearMode;
    private PathTrakers curPath;
    private List<PathTrakers> pathTrakerList;
    private List<PathTrakers> redoPathList;
    // buffer
    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;
    private Canvas newStartCanvas;
    private Bitmap newStartBitmap;
    private boolean actionUp = false;
    private boolean isDrawing = false;
    private boolean isEraser = false;
    private int originalWidth = 0;
    private int originalHeight = 0;
    private Canvas loadedCanvas;
    private Bitmap loadedBitmap;
    private int targetColor = -1;
    private boolean isBucket = false;
    private QueueLinearFlood queueLinearFloodFiller;

    public DrawingViews(Context context) {
        super(context);
        init();
    }

    public DrawingViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
        pathTrakerList = new ArrayList<>();
        redoPathList = new ArrayList<>();
        lastPosition = new Point(0, 0);
        onGestureListener = new CallbackOnGesture();
        gestureDetector = new GestureDetector(getContext(), onGestureListener);
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(CURRENT_STROKE_WIDTH);
        paint.setColor(Color.BLACK);
        paint.setAlpha((int) (CURRENT_STROKE_OPACITY * 2.55));
        clearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    public void setConfig(int width, int height) {
        onGestureListener.setCanvasBounds(width, height);
        bufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bufferCanvas = new Canvas(bufferBitmap);

        newStartBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        newStartCanvas = new Canvas(newStartBitmap);

        originalWidth = width;
        originalHeight = height;
        Log.v(TAG, "bufferBitmap w/h: " + bufferBitmap.getWidth() + "/" + bufferBitmap.getHeight());
    }

    public void clearDrawingBoard() {
        init();
        setConfig(originalWidth, originalHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(originalWidth, originalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        adjustCanvas(canvas);
        adjustCanvas(loadedCanvas);

        canvas.drawBitmap(bufferBitmap, 0, 0, null);
        if (curPath != null && isDrawing) {
            if (!actionUp) {
                if (isDrawing && !isEraser) {
                    canvas.drawPath(curPath, paint);
                }
            }
        }

        if (loadedBitmap != null) {
            canvas.drawBitmap(loadedBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        if (startDrawingListener != null)
            startDrawingListener.onDrawStart();
        float scaleFactor = onGestureListener.getScaleFactor();
        RectF viewRect = onGestureListener.getCurrentViewport();

        float touchX = (event.getX(0) + viewRect.left) / scaleFactor;
        float touchY = (event.getY(0) + viewRect.top) / scaleFactor;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                actionDown(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                actionUp();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                actionPointDown();
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onGestureListener.setViewBounds(w, h);
    }

    private void adjustCanvas(Canvas canvas) {
        if (canvas != null) {
            float scaleFactor = onGestureListener.getScaleFactor();
            RectF viewRect = onGestureListener.getCurrentViewport();
            canvas.translate(-viewRect.left, -viewRect.top);
            canvas.scale(scaleFactor, scaleFactor);
        }
    }

    private void actionDown(float x, float y) {
        if (isInCanvas(x, y)) {
            isDrawing = true;
            actionUp = false;

            lastPosition.set((int) x, (int) y);
            curPath = new PathTrakers(lastPosition);
            curPath.setEraser(isEraser);
            curPath.setPaint(paint);
            curPath.moveTo(x, y);
            curPath.setStartPoint(new Point((int) x, (int) y));
            curPath.setBucket(isBucket);
            redoPathList.clear();
            targetColor = bufferBitmap.getPixel((int) x, (int) y);
        }
    }

    private void actionMove(float x, float y) {
        if (isInCanvas(x, y)) {
            if (isDrawing && !curPath.isBucket()) {
                curPath.quadTo(lastPosition.x, lastPosition.y, (lastPosition.x + x) / 2, (lastPosition.y + y) / 2);
                lastPosition.set((int) x, (int) y);

                if (isEraser) {
                    bufferCanvas.drawPath(curPath, paint);
                }
            }
        }
    }

    private void actionUp() {
        if (isDrawing) {
            if (!isEraser) {
                if (!curPath.isBucket()) {
                    bufferCanvas.drawPath(curPath, paint);
                } else {
                    queueLinearFloodFiller = new QueueLinearFlood(bufferBitmap, targetColor, paint.getColor());
                    queueLinearFloodFiller.floodFill(curPath.getStartPoint().x, curPath.getStartPoint().y);
                }
            }

            pathTrakerList.add(curPath);
            if (pathTrakerList.size() > limit) {
                PathTrakers pathTraker = pathTrakerList.remove(0);
                newStartCanvas.drawPath(pathTraker, pathTraker.getPaint());
            }

            isDrawing = false;
        }

        actionUp = true;
    }

    private void actionPointDown() {
        isDrawing = false;
        curPath = null;
    }

    private boolean isInCanvas(float x, float y) {
        RectF canvasRect = onGestureListener.getCanvasRect();
        return canvasRect.contains(x, y);
    }

    public void change2Eraser() {
        isEraser = true;
        isBucket = false;
        paint.setXfermode(clearMode);
    }

    public void change2Brush() {
        isEraser = false;
        isBucket = false;
        paint.setXfermode(null);
    }

    public void change2Bucket() {
        isEraser = false;
        isBucket = true;
        paint.setXfermode(null);
    }

    public void undo() {
        if (pathTrakerList.size() > 0) {
            redoPathList.add(pathTrakerList.remove(pathTrakerList.size() - 1));
            bufferBitmap.eraseColor(Color.TRANSPARENT);
            bufferCanvas.drawBitmap(newStartBitmap, 0, 0, null);

            for (PathTrakers pathTraker : pathTrakerList) {
                bufferCanvas.drawPath(pathTraker, pathTraker.getPaint());
            }

            invalidate();
        }
    }

    public void redo() {
        if (redoPathList.size() > 0 && pathTrakerList.size() < limit) {
            PathTrakers pathTraker = redoPathList.remove(redoPathList.size() - 1);
            pathTrakerList.add(pathTraker);
            bufferCanvas.drawPath(pathTraker, pathTraker.getPaint());
        }

        invalidate();
    }

    public void setColor(int color) {
        int alpha = paint.getAlpha();
        paint.setColor(color);
        paint.setAlpha(alpha);
    }

    public int getStrokeWidth() {
        return CURRENT_STROKE_WIDTH;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.CURRENT_STROKE_WIDTH = strokeWidth;
        paint.setStrokeWidth(strokeWidth);
    }

    public int getOpacity() {
        return CURRENT_STROKE_OPACITY;
    }

    public void setOpacity(int alpha) {
        CURRENT_STROKE_OPACITY = alpha;
        paint.setAlpha((int) (CURRENT_STROKE_OPACITY * 2.55));
    }

    public void loadImg(final int resId) {
        ThreadManag.getInstance().postToBackgroungThread(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "loadImg");
                loadedBitmap = ScalingUtils.decodeResource(getResources(), resId,
                        originalWidth, originalHeight, ScalingUtils.ScalingLogic.FIT)
                        .copy(Bitmap.Config.ARGB_8888, true);
                loadedBitmap = ScalingUtils.createScaledBitmap(loadedBitmap, originalWidth, originalHeight,
                        ScalingUtils.ScalingLogic.FIT);
                if (loadedCanvas == null) {
                    loadedCanvas = new Canvas(loadedBitmap);
                } else {
                    loadedCanvas.setBitmap(loadedBitmap);
                }

                ThreadManag.getInstance().postToUIThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        });
    }

    public void loadImg(Bitmap bitmap) {
        bitmap = ScalingUtils.createScaledBitmap(bitmap, originalWidth, originalHeight,
                ScalingUtils.ScalingLogic.FIT);

        bufferCanvas.drawBitmap(bitmap, 0, 0, null);
        newStartBitmap = bitmap;
        ThreadManag.getInstance().postToUIThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    /**
     * just set bitmap which draw on last step
     *
     * @param bitmap
     */
    public void setLoadedBitmap(Bitmap bitmap) {
        loadedBitmap = ScalingUtils.createScaledBitmap(bitmap, originalWidth, originalHeight,
                ScalingUtils.ScalingLogic.FIT);
        if (loadedCanvas == null) {
            loadedCanvas = new Canvas(loadedBitmap);
        } else {
            loadedCanvas.setBitmap(loadedBitmap);
        }

        ThreadManag.getInstance().postToUIThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public Bitmap getViewBitmap() {
        return getViewBitmap(originalWidth, originalHeight);
    }

    public Bitmap getViewBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        if (bufferBitmap != null)
            canvas.drawBitmap(bufferBitmap, 0, 0, null);
        if (loadedBitmap != null)
            canvas.drawBitmap(loadedBitmap, 0, 0, null);

        return ScalingUtils.createScaledBitmap(bitmap, width, height, ScalingUtils.ScalingLogic.FIT);
    }

    public void setUserTouchListener(CallbackStartDrawing startDrawingListener) {
        this.startDrawingListener = startDrawingListener;
    }
}
