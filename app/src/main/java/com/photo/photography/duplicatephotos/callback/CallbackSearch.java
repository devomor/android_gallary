package com.photo.photography.duplicatephotos.callback;

public interface CallbackSearch {
    void checkSearchFinish();

    void updateUi(String... strArr);

    void updateTotalFileCountUi(String strArr);
}
