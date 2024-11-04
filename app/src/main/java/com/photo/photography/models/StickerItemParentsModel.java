package com.photo.photography.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.List;

public class StickerItemParentsModel implements ItemParent<StickersModel>, Serializable {

    Drawable parentIcon;
    private List<StickersModel> mChildItemList;
    private String mParentText;

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public List<StickersModel> getChildList() {
        return mChildItemList;
    }

    public void setChildItemList(List<StickersModel> childItemList) {
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
