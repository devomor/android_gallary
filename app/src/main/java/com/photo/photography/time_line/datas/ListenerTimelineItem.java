package com.photo.photography.time_line.datas;

import androidx.annotation.IntDef;

/**
 * Interface to define that this item is capable of being displayed on timeline
 */
public interface ListenerTimelineItem {

    int TYPE_HEADER = 101;
    int TYPE_MEDIA = 102;

    @TimelineItemType
    int getTimelineType();

    @IntDef({TYPE_HEADER, TYPE_MEDIA})
    @interface TimelineItemType {
    }
}
