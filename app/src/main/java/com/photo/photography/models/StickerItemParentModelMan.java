package com.photo.photography.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.List;

public class StickerItemParentModelMan implements ItemParentMan<StickerModelMan>, Serializable {

    Drawable parentIcon;
    private List<StickerModelMan> mChildItemList;
    private String mParentText;
    private Boolean isVisibleProgress = false;
    private int visibility = 0;

    public Boolean getVisibleProgress() {
        return isVisibleProgress;
    }

    public void setVisibleProgress(Boolean visibleProgress) {
        isVisibleProgress = visibleProgress;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public List<StickerModelMan> getChildList() {
        return mChildItemList;
    }

    public void setChildItemList(List<StickerModelMan> childItemList) {
        mChildItemList = childItemList;
    }

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public Drawable getParentIcon() {
        return parentIcon;
    }

    public void setParentIcon(Drawable parentIcon) {
        this.parentIcon = parentIcon;
    }

}
