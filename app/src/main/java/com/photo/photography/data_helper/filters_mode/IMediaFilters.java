package com.photo.photography.data_helper.filters_mode;

import com.photo.photography.data_helper.Media;


public interface IMediaFilters {
    boolean accept(Media media);
}
