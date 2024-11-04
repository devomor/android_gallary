package com.photo.photography.data_helper;

import android.database.Cursor;


public interface CursorHandlers<T> {

    static String[] getProjection() {
        return new String[0];
    }

    T handle(Cursor cu);
}
