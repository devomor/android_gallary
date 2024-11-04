package com.photo.photography.util.utils.frame;

import android.graphics.PointF;

import com.photo.photography.models.TemplateItemModel;
import com.photo.photography.templates.PhotosItem;

import java.util.HashMap;


public class FrameFourImage {
    static TemplateItemModel collage_4_25() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_25.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.6667f, 0.5f);
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
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0.5f, 0));
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
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0.5f, 1, 1);
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

    static TemplateItemModel collage_4_24() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_24.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 0.3f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.6667f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.2f, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.3333f));
        photoItem.pointList.add(new PointF(1, 0.6667f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.4f, 1, 0.8f);
        photoItem.pointList.add(new PointF(0, 0.25f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.75f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.7f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.3333f));
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

    static TemplateItemModel collage_4_23() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_23.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 0.6f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.6667f, 0));
        photoItem.pointList.add(new PointF(1, 0.8333f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.6f));
        photoItem.pointList.add(new PointF(0.25f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0.3f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.2857f));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.3333f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0.2f));
        photoItem.pointList.add(new PointF(0.75f, 0));
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

    static TemplateItemModel collage_4_22() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_22.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.8f, 0));
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
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.4f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.1666f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.2f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 0.6f, 1);
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
        return item;
    }

    static TemplateItemModel collage_4_21() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_21.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.5f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.25f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0.3333f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.25f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.75f));
        photoItem.pointList.add(new PointF(0.6667f, 1));
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

    static TemplateItemModel collage_4_20() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_20.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.75f, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.6667f, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0.3333f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 0.3333f);
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
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.25f, 0.3333f, 1, 1);
        photoItem.pointList.add(new PointF(0.6667f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.3333f, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.6667f, 0.5f, 1);
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

    static TemplateItemModel collage_4_19() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_19.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(1, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.5f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 0.5f);
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

    static TemplateItemModel collage_4_18() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_18.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
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
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
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
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
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
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0, 0));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_17() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_17.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.25f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.25f, 0.5f, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0.25f, 1, 0.75f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.75f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_16() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_16.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.3333f, 0.3333f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.3333f, 0, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_15() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_15.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.3333f, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6667f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0.6667f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_14() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_14.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.3333f, 0, 1, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.3333f, 0.6667f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.6667f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_13() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_13.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.6667f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.6667f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6667f, 0.3333f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.3333f, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_12() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_12.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 0.5f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.3333f, 0.5f, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.6666f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_11() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_11.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0.5f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_10() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_10.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0, 0.3333f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.3333f, 0, 0.6666f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0.6666f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_9() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_9.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.3333f, 0.5f, 0.6666f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0.6666f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_8() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_8.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.4f, 0.4f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.4f, 0, 1, 0.4f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6f, 0.4f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.4f, 0.6f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_7() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_7.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0.6667f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.3333f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_6() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_6.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.3333f, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0, 1, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.6667f, 0.6667f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.3333f, 0.6667f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(2, 2));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_5() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_5.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.6667f, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.5f));
        photoItem.pointList.add(new PointF(0.5f, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.6667f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0.3333f, 1, 1);
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        photoItem.pointList.add(new PointF(0, 0.5f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(4), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0, 0.6667f, 0.3333f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_4() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_4.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.6667f, 1);
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
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0, 1, 0.3333f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.1666f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.4444f, 0.3333f, 1, 0.6666f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.2f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5555f, 0.6666f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.25f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_3() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_3.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.51f, 0.675f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(0.653f, 0));
        photoItem.pointList.add(new PointF(1, 0.8f));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0, 1, 0.4646f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 0.3375f));
        photoItem.pointList.add(new PointF(0.23125f, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0.25f, 0));
        photoItem.pointList.add(new PointF(1, 0.3333f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.3333f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.25f));
        photoItem.pointList.add(new PointF(0.6667f, 1));
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

    static TemplateItemModel collage_4_2() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_2.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0, 0.6667f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(0.75f, 1));
        photoItem.pointList.add(new PointF(0, 0.6667f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 2));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.5f, 0, 1, 0.6667f);
        photoItem.pointList.add(new PointF(0.3333f, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 0.75f));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(1, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0.3333f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0.25f, 0));
        photoItem.pointList.add(new PointF(1, 0.3333f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        //shrink map
        photoItem.shrinkMap = new HashMap<>();
        photoItem.shrinkMap.put(photoItem.pointList.get(0), new PointF(1, 1));
        photoItem.shrinkMap.put(photoItem.pointList.get(1), new PointF(1, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(2), new PointF(2, 2));
        photoItem.shrinkMap.put(photoItem.pointList.get(3), new PointF(2, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.shrinkMethod = PhotosItem.SHRINK_METHOD_COMMON;
        photoItem.bound.set(0, 0.3333f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0.25f));
        photoItem.pointList.add(new PointF(0.6667f, 1));
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

    static TemplateItemModel collage_4_1() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_1.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.25f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.25f, 0, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0.5f, 0, 0.75f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0.75f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static TemplateItemModel collage_4_0() {
        TemplateItemModel item = FrameImageUtils.collage("collage_4_0.png");
        //first frame
        PhotosItem photoItem = new PhotosItem();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 0.5f, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotosItem();
        photoItem.index = 1;
        photoItem.bound.set(0.5f, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //third frame
        photoItem = new PhotosItem();
        photoItem.index = 2;
        photoItem.bound.set(0, 0.5f, 0.5f, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //four frame
        photoItem = new PhotosItem();
        photoItem.index = 3;
        photoItem.bound.set(0.5f, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }
}
