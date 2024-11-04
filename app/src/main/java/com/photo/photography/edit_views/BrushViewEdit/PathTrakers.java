package com.photo.photography.edit_views.BrushViewEdit;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class PathTrakers extends Path {
    private boolean isEraser = false;
    private Paint paint;
    private Point startPoint;
    private List<Point> traker;
    private boolean isBucket = false;

    public PathTrakers(Point startPoint) {
        startPoint = startPoint;
        traker = new ArrayList<>();
        traker.add(startPoint);
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public List<Point> getTraker() {
        return traker;
    }

    public void setTraker(List<Point> traker) {
        this.traker = traker;
    }

    public boolean isEraser() {
        return isEraser;
    }

    public void setEraser(boolean eraser) {
        isEraser = eraser;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = new Paint(paint);
    }

    public boolean isBucket() {
        return isBucket;
    }

    public void setBucket(boolean bucket) {
        isBucket = bucket;
    }
}

