package com.photo.photography.collage.callback;

import com.photo.photography.collage.model.DataItemPackageInfo;


public interface CallbackOnDownloadedPackageClick {
    void onDeleteButtonClick(int position, DataItemPackageInfo info);

    void onItemClick(int position, DataItemPackageInfo info);
}
