package com.photo.photography.models;


import com.photo.photography.templates.PhotosItem;
import com.photo.photography.templates.PhotoLayout;

import java.util.ArrayList;
import java.util.List;

public class TemplateItemModel extends ImageTemplateModel {
    private int mSectionManager;
    private int mSectionFirstPosition;
    private boolean mIsHeader = false;
    private String mHeader;

    private boolean mIsAds = false;
    private List<PhotosItem> mPhotoItemList = new ArrayList<>();

    public TemplateItemModel() {

    }

    public TemplateItemModel(ImageTemplateModel template) {
        setLanguages(template.getLanguages());
        setPackageId(template.getPackageId());
        setPreview(template.getPreview());
        setTemplate(template.getTemplate());
        setChild(template.getChild());
        setTitle(template.getTitle());
        setThumbnail(template.getThumbnail());
        setSelectedThumbnail(template.getSelectedThumbnail());
        setSelected(template.isSelected());
        // To be used to display
        setShowingType(template.getShowingType());
        // To be used in database
        setLastModified(template.getLastModified());
        setStatus(template.getStatus());
        setId(template.getId());
        mPhotoItemList = PhotoLayout.parseImageTemplate(template);
    }

    public String getHeader() {
        return mHeader;
    }

    public List<PhotosItem> getPhotoItemList() {
        return mPhotoItemList;
    }

    public int getSectionFirstPosition() {
        return mSectionFirstPosition;
    }

    public void setSectionFirstPosition(int sectionFirstPosition) {
        mSectionFirstPosition = sectionFirstPosition;
    }

    public int getSectionManager() {
        return mSectionManager;
    }

    public void setSectionManager(int sectionManager) {
        mSectionManager = sectionManager;
    }

    public boolean isHeader() {
        return mIsHeader;
    }

    public void setHeader(String header) {
        mHeader = header;
    }

    public void setIsHeader(boolean isHeader) {
        mIsHeader = isHeader;
    }

    public void setIsAds(boolean isAds) {
        mIsAds = isAds;
    }

    public boolean isAds() {
        return mIsAds;
    }
}
