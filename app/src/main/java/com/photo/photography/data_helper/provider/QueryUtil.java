package com.photo.photography.data_helper.provider;

import android.content.ContentResolver;
import android.database.Cursor;

import com.photo.photography.data_helper.CursorHandlers;

import io.reactivex.Observable;

public class QueryUtil {

    public static <T> Observable<T> query(Querys q, ContentResolver cr, CursorHandlers<T> ch) {
        return Observable.create(subscriber -> {
            Cursor cursor = null;
            try {
                cursor = q.getCursor(cr);
                if (cursor != null && cursor.getCount() > 0)
                    while (cursor.moveToNext()) subscriber.onNext(ch.handle(cursor));
                subscriber.onComplete();
            } catch (Exception err) {
                subscriber.onError(err);
            } finally {
                if (cursor != null) cursor.close();
            }
        });
    }

    /**
     * return only the first element if there is one
     *
     * @param q
     * @param cr
     * @param ch
     * @param <T>
     * @return
     */
    public static <T> Observable<T> querySingle(Querys q, ContentResolver cr, CursorHandlers<T> ch) {
        return Observable.create(subscriber -> {
            Cursor cursor = null;
            try {
                cursor = q.getCursor(cr);
                if (cursor != null && cursor.moveToFirst())
                    subscriber.onNext(ch.handle(cursor));
                subscriber.onComplete();
            } catch (Exception err) {
                subscriber.onError(err);
            } finally {
                if (cursor != null) cursor.close();
            }
        });
    }

}
