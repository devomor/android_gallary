package com.photo.photography.collage.util.frames;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.photo.photography.collage.model.DataTemplateItem;
import com.photo.photography.collage.poster.PhotoItemCustom;
import com.photo.photography.collage.util.GeometryUtil;

import java.util.HashMap;

/**
 * Created by Sira on 5/9/2018.
 */
public class FrameThreeImage {
    static DataTemplateItem collage_3_47() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_47.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.75f, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(0.75f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_46() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_46.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.4167f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.6f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);

        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.75f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.8333f, 1));
        photoItem.pointList.add(new PointF(0.3333f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0.6666f, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0.25f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_45() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_45.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.4f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.2f, 0, 0.8f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.6667f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.3333f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.6f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_44() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_44.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.4167f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.6f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.25f, 1, 0.75f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.3333f));
        photoItem.pointList.add(new PointF(1, 0.8333f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.25f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_43() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_43.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.4f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.2f, 1, 0.8f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.6667f));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0, 0.3333f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.6f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_42() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_42.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.6f, 0.8f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.6667f, 0));
        photoItem.pointList.add(new PointF(1, 0.75f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.4f, 0, 1, 0.7f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.3333f, 0.8571f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.6f, 1, 1);
        photoItem.pointList.add(new PointF(0.6f, 0));
        photoItem.pointList.add(new PointF(1, 0.25f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_41() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_41.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.6666f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0.3333f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.3333f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0.6667f));
        photoItem.pointList.add(new PointF(0.75f, 0.5f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_40() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_40.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(5), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_39() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_39.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_38() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_38.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_37() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_37.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_36() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_36.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_35() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_35.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_34() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_34.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_33() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_33.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0.5f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_32() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_32.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0.5f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_31() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_31.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_30() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_30.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0.5f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_29() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_29.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_28() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_28.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_27() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_27.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_26() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_26.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_25() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_25.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_24() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_24.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_23() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_23.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0.5f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_22() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_22.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_21() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_21.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.3333f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_20() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_20.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_19() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_19.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.25f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_18() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_18.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_17() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_17.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.3333f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0.3333f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_16() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_16.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_15() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_15.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.6667f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.6667f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_14() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_14.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.3333f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_13() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_13.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.fitBound = true;
        photoItem.clearPath = new Path();
        photoItem.clearPath.addRect(0, 0, 512, 512, Path.Direction.CCW);
        photoItem.clearPathRatioBound = new RectF(0.5f, 0.25f, 1.5f, 0.75f);
        photoItem.clearPathInCenterVertical = true;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_13;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.fitBound = true;
        photoItem.clearPath = new Path();
        photoItem.clearPath.addRect(0, 0, 512, 512, Path.Direction.CCW);
        photoItem.clearPathRatioBound = new RectF(-0.5f, 0.25f, 0.5f, 0.75f);
        photoItem.clearPathInCenterVertical = true;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_13;
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.25f, 0f, 0.75f, 1f);
        photoItem.path = new Path();
        photoItem.path.addRect(0, 0, 512, 512, Path.Direction.CCW);
        photoItem.pathRatioBound = new RectF(0, 0.25f, 1, 0.75f);
        photoItem.pathInCenterVertical = true;
        photoItem.pathInCenterHorizontal = true;
        photoItem.fitBound = true;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_13;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_12() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_12.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_11() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_11.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.6667f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.6667f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_10() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_10.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_USING_MAP;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.75f, 1));
        photoItem.pointList.add(new PointF(0.75f, 0.5f));
        photoItem.pointList.add(new PointF(0.25f, 0.5f));
        photoItem.pointList.add(new PointF(0.25f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(-2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(-2, -1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, -1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, -1));
        photoItem.shrinkMap.put(photoItem.pointList.get(5), new PointF(-1, -1));
        photoItem.shrinkMap.put(photoItem.pointList.get(6), new PointF(-1, -1));
        photoItem.shrinkMap.put(photoItem.pointList.get(7), new PointF(2, -1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_USING_MAP;
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.25f, 0));
        photoItem.pointList.add(new PointF(0.25f, 0.5f));
        photoItem.pointList.add(new PointF(0.75f, 0.5f));
        photoItem.pointList.add(new PointF(0.75f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(-1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(-1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(5), new PointF(-2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(6), new PointF(-2, -2));
        photoItem.shrinkMap.put(photoItem.pointList.get(7), new PointF(2, -2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.25f, 0.25f, 0.75f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_9() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_9.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.6667f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_8() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_8.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = new Path();
        photoItem.clearPath.addCircle(256, 256, 256, Path.Direction.CCW);
        photoItem.clearPathRatioBound = new RectF(0.5f, 0.25f, 1.5f, 0.75f);
        photoItem.clearPathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = new Path();
        photoItem.clearPath.addCircle(256, 256, 256, Path.Direction.CCW);
        photoItem.clearPathRatioBound = new RectF(-0.5f, 0.25f, 0.5f, 0.75f);
        photoItem.clearPathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_3_8;
        photoItem.bound.set(0.25f, 0f, 0.75f, 1f);
        photoItem.path = new Path();
        photoItem.path.addCircle(256, 256, 256, Path.Direction.CCW);
        photoItem.pathRatioBound = new RectF(0, 0.25f, 1, 0.75f);
        photoItem.pathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_7() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_7.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.3333f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_6() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_6.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_3_6;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_6;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = new Path();
        GeometryUtil.createRegularPolygonPath(photoItem.clearPath, 512, 6, 0);
        photoItem.clearPathRatioBound = new RectF(0.5f, 0.25f, 1.5f, 0.75f);
        photoItem.clearPathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_3_6;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_6;
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = new Path();
        GeometryUtil.createRegularPolygonPath(photoItem.clearPath, 512, 6, 0);
        photoItem.clearPathRatioBound = new RectF(-0.5f, 0.25f, 0.5f, 0.75f);
        photoItem.clearPathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_3_6;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_6;
        photoItem.bound.set(0.25f, 0f, 0.75f, 1f);
        photoItem.path = new Path();
        GeometryUtil.createRegularPolygonPath(photoItem.path, 512, 6, 0);
        photoItem.pathRatioBound = new RectF(0, 0.25f, 1, 0.75f);
        photoItem.pathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_5() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_5.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.5f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_4() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_4.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = FrameImageUtils.createHeartItem(0, 512);
        photoItem.clearPathRatioBound = new RectF(0.25f, 0.5f, 0.75f, 1.5f);
        photoItem.clearPathInCenterHorizontal = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = FrameImageUtils.createHeartItem(0, 512);
        photoItem.clearPathRatioBound = new RectF(0.25f, -0.5f, 0.75f, 0.5f);
        photoItem.clearPathInCenterHorizontal = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.25f, 1, 0.75f);
        photoItem.path = FrameImageUtils.createHeartItem(0, 512);
        photoItem.pathRatioBound = new RectF(0, 0, 1, 1);
        photoItem.pathInCenterHorizontal = true;
        photoItem.pathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_3() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_3.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_3_3;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.25f));
        photoItem.pointList.add(new PointF(0.5f, 0.5f));
        photoItem.pointList.add(new PointF(1, 0.75f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_3_3;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0, 0.75f));
        photoItem.pointList.add(new PointF(0.5f, 0.5f));
        photoItem.pointList.add(new PointF(0, 0.25f));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.25f, 0.25f, 0.75f, 0.75f);
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_2() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_2.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_6;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.path = new Path();
        GeometryUtil.createRegularPolygonPath(photoItem.path, 512, 6, 0);
        photoItem.pathRatioBound = new RectF(0, 0, 1, 1);
        photoItem.pathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_6;
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.path = new Path();
        GeometryUtil.createRegularPolygonPath(photoItem.path, 512, 6, 0);
        photoItem.pathRatioBound = new RectF(0, 0, 1, 1);
        photoItem.pathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.cornerMethod = PhotoItemCustom.CORNER_METHOD_3_6;
        photoItem.bound.set(0, 0.25f, 1, 0.75f);
        photoItem.path = new Path();
        GeometryUtil.createRegularPolygonPath(photoItem.path, 512, 6, 0);
        photoItem.pathRatioBound = new RectF(0, 0.25f, 1, 0.75f);
        photoItem.pathInCenterVertical = true;
        photoItem.pathAlignParentRight = true;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_1() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_1.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = new Path();
        photoItem.clearPath.addCircle(256, 256, 256, Path.Direction.CCW);
        photoItem.clearPathRatioBound = new RectF(0.25f, 0.5f, 0.75f, 1.5f);
        photoItem.clearPathInCenterHorizontal = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.clearPath = new Path();
        photoItem.clearPath.addCircle(256, 256, 256, Path.Direction.CCW);
        photoItem.clearPathRatioBound = new RectF(0.25f, -0.5f, 0.75f, 0.5f);
        photoItem.clearPathInCenterHorizontal = true;
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_3_8;
        photoItem.bound.set(0, 0.25f, 1, 0.75f);
        photoItem.path = new Path();
        photoItem.path.addCircle(256, 256, 256, Path.Direction.CCW);
        photoItem.pathRatioBound = new RectF(0.25f, 0, 0.75f, 1);
        photoItem.pathInCenterVertical = true;
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_3_0() {
        DataTemplateItem item = FrameImageUtils.collage("collage_3_0.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.6666f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }
}
