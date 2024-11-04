package com.photo.photography.callbacks;


import com.photo.photography.models.ItemPackageInfoModel;


public interface CallbackOnDownloadedPackageClick {
    void onDeleteButtonClick(int position, ItemPackageInfoModel info);

    void onItemClick(int position, ItemPackageInfoModel info);
}
