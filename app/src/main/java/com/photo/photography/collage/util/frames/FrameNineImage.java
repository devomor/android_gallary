package com.photo.photography.collage.util.frames;

import android.graphics.PointF;

import com.photo.photography.collage.model.DataTemplateItem;
import com.photo.photography.collage.poster.PhotoItemCustom;

import java.util.HashMap;

/**
 * All points of polygon must be ordered by clockwise along<br>
 * Created by Sira on 7/3/2018.
 */
public class FrameNineImage {
    static DataTemplateItem collage_9_11() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_11.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.2666f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.7519f, 0));
        photoItem.pointList.add(new PointF(1, 1));
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
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.2f, 0, 0.8f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.8889f, 1));
        photoItem.pointList.add(new PointF(0.1111f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.7334f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0.2481f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.3333f, 0.3333f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.8f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.2666f, 0.3333f, 0.7334f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.8572f, 1));
        photoItem.pointList.add(new PointF(0.1428f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.6666f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0.2f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.6666f, 0.4f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.8333f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0.6666f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.8f, 1));
        photoItem.pointList.add(new PointF(0.2f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.6f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0.1666f, 0));
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

    static DataTemplateItem collage_9_10() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_10.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.39645f, 0.39645f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.73881f, 0));
        photoItem.pointList.add(new PointF(1, 0.6306f));
        photoItem.pointList.add(new PointF(0.6306f, 1));
        photoItem.pointList.add(new PointF(0, 0.73881f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.2929f, 0, 0.7071f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.75f, 1));
        photoItem.pointList.add(new PointF(0.25f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.60355f, 0, 1, 0.39645f);
        photoItem.pointList.add(new PointF(0.26119f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.73881f));
        photoItem.pointList.add(new PointF(0.3694f, 1));
        photoItem.pointList.add(new PointF(0, 0.6306f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.75f, 0.2929f, 1, 0.7071f);
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.75f));
        photoItem.pointList.add(new PointF(0, 0.25f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.60355f, 0.60355f, 1, 1);
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.26199f, 1));
        photoItem.pointList.add(new PointF(0, 0.3694f));
        photoItem.pointList.add(new PointF(0.3694f, 0));
        photoItem.pointList.add(new PointF(1, 0.26199f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.2929f, 0.75f, 0.7071f, 1);
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0.25f, 0));
        photoItem.pointList.add(new PointF(0.75f, 0));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.60355f, 0.39645f, 1);
        photoItem.pointList.add(new PointF(0.6306f, 0));
        photoItem.pointList.add(new PointF(1, 0.3694f));
        photoItem.pointList.add(new PointF(0.73881f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0, 0.26199f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.2929f, 0.25f, 0.7071f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.25f));
        photoItem.pointList.add(new PointF(1, 0.75f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.25f, 0.25f, 0.75f, 0.75f);
        photoItem.pointList.add(new PointF(0.2929f, 0));
        photoItem.pointList.add(new PointF(0.7071f, 0));
        photoItem.pointList.add(new PointF(1, 0.2929f));
        photoItem.pointList.add(new PointF(1, 0.7071f));
        photoItem.pointList.add(new PointF(0.7071f, 1));
        photoItem.pointList.add(new PointF(0.2929f, 1));
        photoItem.pointList.add(new PointF(0, 0.7071f));
        photoItem.pointList.add(new PointF(0, 0.2929f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(5), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(6), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(7), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);

        return item;
    }

    static DataTemplateItem collage_9_9() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_9.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.3f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.6f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 0.3f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.6f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 0.3f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.4f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.7f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0.6f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.7f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.4f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0.7f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.4f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.7f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0.6f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.shrinkMethod = PhotoItemCustom.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 0.3f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.4f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.3f, 0.3f, 0.7f, 0.7f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);

        return item;
    }

    static DataTemplateItem collage_9_8() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_8.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.5f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.5f, 0, 0.75f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.75f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.6666f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.5f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.3333f, 0.3333f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0.3333f, 0.3333f, 0.6666f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);

        return item;
    }

    static DataTemplateItem collage_9_7() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_7.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.5f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.5f, 0, 0.75f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.75f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.3333f, 0.5f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.5f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.6666f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0.3333f, 0.6666f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);

        return item;
    }

    static DataTemplateItem collage_9_6() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_6.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.2f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.2f, 0, 0.4f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.4f, 0, 0.6f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.6f, 0, 0.8f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0.8f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.2f, 0.5f, 0.4f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0.4f, 0.5f, 0.6f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0.6f, 0.5f, 0.8f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.8f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);

        return item;
    }

    static DataTemplateItem collage_9_5() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_5.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.6666f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.25f, 0.3333f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0.3333f, 0.25f, 0.6666f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.6666f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0, 0.5f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0.6666f, 0.5f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.6666f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);

        return item;
    }

    static DataTemplateItem collage_9_4() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_4.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.3333f, 0.25f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0.25f, 0.3333f, 0.5f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.5f, 0.3333f, 0.75f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0.75f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0.25f, 0.6666f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.5f, 0.6666f, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.75f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_9_3() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_3.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.2f, 0.4f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.4f, 0.2f, 0.8f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.8f, 0.4f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0.4f, 0.8f, 0.8f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.8f, 0.6f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0.8f, 0.2f, 1, 0.6f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0.2f, 0, 0.6f, 0.2f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.6f, 0, 1, 0.2f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.2f, 0.2f, 0.8f, 0.8f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_9_2() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_2.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.25f, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0.5f, 0.25f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0, 0.5f, 0.5f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0.5f, 0.5f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.75f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.3333f, 0.75f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.6666f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_9_1() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_1.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.6666f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.3333f, 0.3333f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.3333f, 0.3333f, 0.6666f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0.6666f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.6666f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.3333f, 0.6666f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.6666f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_9_0() {
        DataTemplateItem item = FrameImageUtils.collage("collage_9_0.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.75f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 2;
        photoItem.bound.set(0.75f, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fourth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.25f, 0.25f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //fifth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 4;
        photoItem.bound.set(0.25f, 0.25f, 0.75f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //sixth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 5;
        photoItem.bound.set(0.75f, 0.25f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //seventh frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 6;
        photoItem.bound.set(0, 0.75f, 0.25f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //eighth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 7;
        photoItem.bound.set(0.25f, 0.75f, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //ninth frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 8;
        photoItem.bound.set(0.75f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }
}
