package com.photo.photography.util.multi_touch.view;


import com.photo.photography.util.multi_touch.helper.MultiTouchEntityHelper;

public interface CallbackOnDoubleClick {
    void onPhotoViewDoubleClick(PhotosView view, MultiTouchEntityHelper entity);

    void onBackgroundDoubleClick();
}
