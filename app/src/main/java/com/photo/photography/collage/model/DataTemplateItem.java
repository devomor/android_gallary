package com.photo.photography.collage.model;

import com.photo.photography.collage.poster.PhotoItemCustom;
import com.photo.photography.collage.poster.PhotoLayoutCustom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sira on 3/25/2018.
 */
public class DataTemplateItem extends DataImageTemplate {
    private int mSectionManager;
    private int mSectionFirstPosition;
    private boolean mIsHeader = false;
    private String mHeader;

    private boolean mIsAds = false;
    private List<PhotoItemCustom> mPhotoItemList = new ArrayList<>();

    public DataTemplateItem() {

    }

    public DataTemplateItem(DataImageTemplate template) {
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
        mPhotoItemList = PhotoLayoutCustom.parseImageTemplate(template);
    }

    public String getHeader() {
        return mHeader;
    }

    public List<PhotoItemCustom> getPhotoItemList() {
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
