package com.photo.photography.collage.util.frames;

import android.graphics.PointF;

import com.photo.photography.collage.model.DataTemplateItem;
import com.photo.photography.collage.poster.PhotoItemCustom;

import java.util.ArrayList;

/**
 * Created by Sira on 5/8/2018.
 */
class FrameTwoImage {
    static DataTemplateItem collage_2_0() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_0.png");
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
        photoItem.bound.set(0.5f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_2_1() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_1.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.5f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_2_2() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_2.png");
        //first frame
        PhotoItemCustom photoItem1 = new PhotoItemCustom();
        photoItem1.index = 0;
        photoItem1.bound.set(0, 0, 1, 0.333f);
        photoItem1.pointList.add(new PointF(0, 0));
        photoItem1.pointList.add(new PointF(1, 0));
        photoItem1.pointList.add(new PointF(1, 1));
        photoItem1.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem1);
        //second frame
        PhotoItemCustom photoItem2 = new PhotoItemCustom();
        photoItem2.index = 1;
        photoItem2.bound.set(0, 0.333f, 1, 1);
        photoItem2.pointList.add(new PointF(0, 0));
        photoItem2.pointList.add(new PointF(1, 0));
        photoItem2.pointList.add(new PointF(1, 1));
        photoItem2.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem2);
        return item;
    }

    static DataTemplateItem collage_2_3() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_3.png");
        //first frame
        PhotoItemCustom photoItem1 = new PhotoItemCustom();
        photoItem1.index = 0;
        photoItem1.bound.set(0, 0, 1, 0.667f);
        photoItem1.pointList.add(new PointF(0, 0));
        photoItem1.pointList.add(new PointF(1, 0));
        photoItem1.pointList.add(new PointF(1, 0.5f));
        photoItem1.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem1);
        //second frame
        PhotoItemCustom photoItem2 = new PhotoItemCustom();
        photoItem2.index = 1;
        photoItem2.bound.set(0, 0.333f, 1, 1);
        photoItem2.pointList.add(new PointF(0, 0.5f));
        photoItem2.pointList.add(new PointF(1, 0));
        photoItem2.pointList.add(new PointF(1, 1));
        photoItem2.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem2);
        return item;
    }

    static DataTemplateItem collage_2_4() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_4.png");
        //first frame
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.5714f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0.8333f, 0.75f));
        photoItem.pointList.add(new PointF(0.6666f, 1));
        photoItem.pointList.add(new PointF(0.5f, 0.75f));
        photoItem.pointList.add(new PointF(0.3333f, 1));
        photoItem.pointList.add(new PointF(0.1666f, 0.75f));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.4286f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0.25f));
        photoItem.pointList.add(new PointF(0.1666f, 0));
        photoItem.pointList.add(new PointF(0.3333f, 0.25f));
        photoItem.pointList.add(new PointF(0.5f, 0));
        photoItem.pointList.add(new PointF(0.6666f, 0.25f));
        photoItem.pointList.add(new PointF(0.8333f, 0));
        photoItem.pointList.add(new PointF(1, 0.25f));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_2_5() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_5.png");
        //first frame
        PhotoItemCustom photoItem = new PhotoItemCustom();
        photoItem.index = 0;
        photoItem.bound.set(0, 0, 1, 0.6667f);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        //second frame
        photoItem = new PhotoItemCustom();
        photoItem.index = 1;
        photoItem.bound.set(0, 0.6667f, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_2_6() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_6.png");
        //first frame
        PhotoItemCustom photoItem1 = new PhotoItemCustom();
        photoItem1.index = 0;
        photoItem1.bound.set(0, 0, 1, 0.667f);
        photoItem1.pointList.add(new PointF(0, 0));
        photoItem1.pointList.add(new PointF(1, 0));
        photoItem1.pointList.add(new PointF(1, 1));
        photoItem1.pointList.add(new PointF(0, 0.5f));
        item.getPhotoItemList().add(photoItem1);
        //second frame
        PhotoItemCustom photoItem2 = new PhotoItemCustom();
        photoItem2.index = 1;
        photoItem2.bound.set(0, 0.333f, 1, 1);
        photoItem2.pointList.add(new PointF(0, 0));
        photoItem2.pointList.add(new PointF(1, 0.5f));
        photoItem2.pointList.add(new PointF(1, 1));
        photoItem2.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem2);
        return item;
    }

    static DataTemplateItem collage_2_7() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_7.png");
        //first frame
        PhotoItemCustom photoItem1 = new PhotoItemCustom();
        photoItem1.index = 0;
        photoItem1.bound.set(0, 0, 1, 1);
        photoItem1.pointList.add(new PointF(0, 0));
        photoItem1.pointList.add(new PointF(1, 0));
        photoItem1.pointList.add(new PointF(1, 1));
        photoItem1.pointList.add(new PointF(0, 1));
        //clear area
        photoItem1.clearAreaPoints = new ArrayList<>();
        photoItem1.clearAreaPoints.add(new PointF(0.6f, 0.6f));
        photoItem1.clearAreaPoints.add(new PointF(0.9f, 0.6f));
        photoItem1.clearAreaPoints.add(new PointF(0.9f, 0.9f));
        photoItem1.clearAreaPoints.add(new PointF(0.6f, 0.9f));
        item.getPhotoItemList().add(photoItem1);
        //second frame
        PhotoItemCustom photoItem2 = new PhotoItemCustom();
        photoItem2.index = 1;
        //photoItem2.hasBackground = true;
        photoItem2.bound.set(0.6f, 0.6f, 0.9f, 0.9f);
        photoItem2.pointList.add(new PointF(0, 0));
        photoItem2.pointList.add(new PointF(1, 0));
        photoItem2.pointList.add(new PointF(1, 1));
        photoItem2.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem2);
        return item;
    }

    static DataTemplateItem collage_2_8() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_8.png");
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
        photoItem.bound.set(0.3333f, 0, 1, 1);
        photoItem.pointList.add(new PointF(0, 0));
        photoItem.pointList.add(new PointF(1, 0));
        photoItem.pointList.add(new PointF(1, 1));
        photoItem.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem);
        return item;
    }

    static DataTemplateItem collage_2_9() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_9.png");
        //first frame
        PhotoItemCustom photoItem1 = new PhotoItemCustom();
        photoItem1.index = 0;
        photoItem1.bound.set(0, 0, 0.6667f, 1);
        photoItem1.pointList.add(new PointF(0, 0));
        photoItem1.pointList.add(new PointF(0.5f, 0));
        photoItem1.pointList.add(new PointF(1, 1));
        photoItem1.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem1);
        //second frame
        PhotoItemCustom photoItem2 = new PhotoItemCustom();
        photoItem2.index = 1;
        photoItem2.bound.set(0.3333f, 0, 1, 1);
        photoItem2.pointList.add(new PointF(0, 0));
        photoItem2.pointList.add(new PointF(1, 0));
        photoItem2.pointList.add(new PointF(1, 1));
        photoItem2.pointList.add(new PointF(0.5f, 1));
        item.getPhotoItemList().add(photoItem2);
        return item;
    }

    static DataTemplateItem collage_2_10() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_10.png");
        //first frame
        PhotoItemCustom photoItem1 = new PhotoItemCustom();
        photoItem1.index = 0;
        photoItem1.bound.set(0, 0, 0.667f, 1);
        photoItem1.pointList.add(new PointF(0, 0));
        photoItem1.pointList.add(new PointF(1, 0));
        photoItem1.pointList.add(new PointF(1, 1));
        photoItem1.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem1);
        //second frame
        PhotoItemCustom photoItem2 = new PhotoItemCustom();
        photoItem2.index = 1;
        photoItem2.bound.set(0.667f, 0, 1, 1);
        photoItem2.pointList.add(new PointF(0, 0));
        photoItem2.pointList.add(new PointF(1, 0));
        photoItem2.pointList.add(new PointF(1, 1));
        photoItem2.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem2);
        return item;
    }

    static DataTemplateItem collage_2_11() {
        DataTemplateItem item = FrameImageUtils.collage("collage_2_11.png");
        //first frame
        PhotoItemCustom photoItem1 = new PhotoItemCustom();
        photoItem1.index = 0;
        photoItem1.bound.set(0, 0, 0.667f, 1);
        photoItem1.pointList.add(new PointF(0, 0));
        photoItem1.pointList.add(new PointF(1, 0));
        photoItem1.pointList.add(new PointF(0.5f, 1));
        photoItem1.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem1);
        //second frame
        PhotoItemCustom photoItem2 = new PhotoItemCustom();
        photoItem1.index = 1;
        photoItem2.bound.set(0.333f, 0, 1, 1);
        photoItem2.pointList.add(new PointF(0.5f, 0));
        photoItem2.pointList.add(new PointF(1, 0));
        photoItem2.pointList.add(new PointF(1, 1));
        photoItem2.pointList.add(new PointF(0, 1));
        item.getPhotoItemList().add(photoItem2);
        return item;
    }
}
