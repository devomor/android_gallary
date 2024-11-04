package com.photo.photography.duplicatephotos.pixelsrelated;

import android.graphics.Bitmap;

import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;

import java.util.ArrayList;
import java.util.List;

public class RGBValues {
    List<Integer> selectedPixelValue = new ArrayList();

    public List<RGBObject> getRgbValue(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        for (Integer num : getSelectedPixelValue(bitmap)) {
            arrayList.add(GlobalVarsAndFunction.getRgbStringValue(num.intValue()));
        }
        return arrayList;
    }

    public List<Integer> getSelectedPixelValue(Bitmap bitmap) {
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(30, 30)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(30, 31)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(31, 30)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(31, 31)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(30, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(30, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(31, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(31, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(30, 224)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(30, 225)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(31, 224)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(31, 225)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 30)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 31)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 30)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 31)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 224)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 225)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 224)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 225)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(224, 30)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(224, 31)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(225, 30)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(225, 31)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(224, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(224, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(225, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(225, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(224, 224)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(224, 225)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(225, 224)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(225, 225)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(124, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(125, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(126, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(127, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(128, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(129, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(130, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(131, 132)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 124)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 125)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 126)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 127)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 128)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 129)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 130)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 131)));
        this.selectedPixelValue.add(Integer.valueOf(bitmap.getPixel(132, 132)));
        return this.selectedPixelValue;
    }
}
