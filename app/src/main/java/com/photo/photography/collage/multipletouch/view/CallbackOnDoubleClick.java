package com.photo.photography.collage.multipletouch.view;

import com.photo.photography.collage.multipletouch.control.MultiTouchEntitys;

public interface CallbackOnDoubleClick {
    void onPhotoViewDoubleClick(PhotoViewCustom view, MultiTouchEntitys entity);

    void onBackgroundDoubleClick();
}
