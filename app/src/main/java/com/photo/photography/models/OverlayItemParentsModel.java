package com.photo.photography.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.List;

public class OverlayItemParentsModel implements ItemParent<OverlaysModel>, Serializable {

    Drawable overlayImg;
    private List<OverlaysModel> mChildItemList;
    private String mParentText;

    @Override
    public List<OverlaysModel> getChildList() {
        return mChildItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public void setChildItemList(List<OverlaysModel> childItemList) {
        mChildItemList = childItemList;
    }

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public Drawable getOverlayImg() {
        return overlayImg;
    }

    public void setOverlayImg(Drawable overlayImg) {
        this.overlayImg = overlayImg;
    }

}
