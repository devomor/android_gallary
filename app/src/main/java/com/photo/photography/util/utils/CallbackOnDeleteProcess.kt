package com.photo.photography.util.utils

import com.photo.photography.data_helper.Media

interface CallbackOnDeleteProcess {

    fun onMediaDeleteSuccess(isSuccess: Boolean, media: Media)

    fun onDeleteComplete()

}