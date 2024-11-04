package com.photo.photography.time_line.datas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Model for showing the Timeline headers.
 */
public class TimelineHeader implements ListenerTimelineItem {

    private final Calendar calendar;
    private String headerText;

    public TimelineHeader(@NonNull Date date) {
        this(date.getTime());
    }

    public TimelineHeader(long timeInMillis) {
        calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeInMillis);
    }

    public TimelineHeader(@NonNull Calendar calendar) {
        this.calendar = calendar;
    }

    @NonNull
    public Calendar getDate() {
        return calendar;
    }

    @Nullable
    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(@NonNull String headerText) {
        this.headerText = headerText;
    }

    @Override
    public int getTimelineType() {
        return TYPE_HEADER;
    }
}
