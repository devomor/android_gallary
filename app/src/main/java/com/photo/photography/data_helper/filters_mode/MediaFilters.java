package com.photo.photography.data_helper.filters_mode;

import com.photo.photography.data_helper.Media;

public class MediaFilters {
    public static IMediaFilters getFilter(FilterModes mode) {
        switch (mode) {
            case ALL:
            default:
                return media -> true;
            case GIF:
                return Media::isGif;
            case VIDEO:
                return Media::isVideo;
            case IMAGES:
                return Media::isImage;
        }
    }
}
