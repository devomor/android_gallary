package com.photo.photography.collage.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.photo.photography.collage.model.DataShadeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sira on 3/18/2018.
 */
public class TableShade extends TableBase {
    public static final String FRAME_TYPE = TableItemPackage.FRAME_TYPE;//"frame";
    public static final String SHADE_TYPE = TableItemPackage.SHADE_TYPE;//"shade";
    // Table structure
    public static final String TABLE_NAME = "Shade";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_SELECTED_THUMBNAIL = "selected_thumbnail";
    public static final String COLUMN_FOREGROUND = "foreground";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PACKAGE_ID = "package_id";

    // Database creation sql statement
    private static final String SQL_DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " text," + COLUMN_THUMBNAIL + " text,"
            + COLUMN_SELECTED_THUMBNAIL + " text,"
            + COLUMN_FOREGROUND + " text," + COLUMN_TYPE + " text," + COLUMN_PACKAGE_ID + " integer,"
            + COLUMN_LAST_MODIFIED + " text," + COLUMN_STATUS + " text" + ");";

    public TableShade(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(SQL_DATABASE_CREATE);
    }

    public static void upgradeTable(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public DataShadeInfo get(long packageId, String name, String type) {
        String[] selColumns = null;
        String selection = COLUMN_PACKAGE_ID + " = ? AND " + COLUMN_STATUS + " = ? AND " + COLUMN_TYPE + " = ? AND UPPER(" + COLUMN_NAME + ") = UPPER(?)";
        String[] selectionArgs = {String.valueOf(packageId), STATUS_ACTIVE, type, name};

        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor filesCursor = getDatabase().query(TABLE_NAME, selColumns, selection, selectionArgs, groupBy, having,
                orderBy);
        if (filesCursor == null)
            return null;
        if (filesCursor.moveToFirst()) {
            DataShadeInfo result = cursorToShadeInfo(filesCursor);
            filesCursor.close();
            return result;
        } else {
            return null;
        }
    }

    public long insert(DataShadeInfo info) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getTitle());
        values.put(COLUMN_THUMBNAIL, info.getThumbnail());
        values.put(COLUMN_SELECTED_THUMBNAIL, info.getSelectedThumbnail());
        values.put(COLUMN_FOREGROUND, info.getForeground());
        values.put(COLUMN_TYPE, info.getShadeType());
        values.put(COLUMN_PACKAGE_ID, info.getPackageId());
        if (info.getLastModified() == null || info.getLastModified().length() < 1) {
            info.setLastModified(getCurrentDateTime());
        }
        values.put(COLUMN_LAST_MODIFIED, info.getLastModified());
        if (info.getStatus() == null || info.getStatus().length() < 1) {
            info.setStatus(STATUS_ACTIVE);
        }

        values.put(COLUMN_STATUS, info.getStatus());

        long id = getDatabase().insert(TABLE_NAME, null, values);
        info.setId(id);
        return id;
    }

    public int update(DataShadeInfo info) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getTitle());
        values.put(COLUMN_THUMBNAIL, info.getThumbnail());
        values.put(COLUMN_SELECTED_THUMBNAIL, info.getSelectedThumbnail());
        values.put(COLUMN_FOREGROUND, info.getForeground());
        values.put(COLUMN_TYPE, info.getShadeType());
        values.put(COLUMN_PACKAGE_ID, info.getPackageId());
        values.put(COLUMN_LAST_MODIFIED, getCurrentDateTime());
        if (info.getStatus() == null || info.getStatus().length() < 1) {
            info.setStatus(STATUS_ACTIVE);
        }

        values.put(COLUMN_STATUS, info.getStatus());

        return getDatabase().update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(info.getId())});
    }

    public List<DataShadeInfo> getAllRows() {
        String[] selColumns = null;
        String selection = COLUMN_STATUS + " = ? ";
        String[] selectionArgs = {STATUS_ACTIVE};

        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor filesCursor = getDatabase().query(TABLE_NAME, selColumns, selection, selectionArgs, groupBy, having,
                orderBy);
        if (filesCursor == null)
            return null;

        List<DataShadeInfo> files = toList(filesCursor);
        filesCursor.close();
        return files;
    }

    /**
     * @param packageId
     * @param type      is frame or shade
     * @return list of ShadeInfos
     */
    public List<DataShadeInfo> getRows(long packageId, String type) {
        String[] selColumns = null;
        String selection = COLUMN_PACKAGE_ID + " = ? AND " + COLUMN_STATUS + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(packageId), STATUS_ACTIVE, type};

        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor filesCursor = getDatabase().query(TABLE_NAME, selColumns, selection, selectionArgs, groupBy, having,
                orderBy);
        if (filesCursor == null)
            return null;

        List<DataShadeInfo> files = toList(filesCursor);
        filesCursor.close();
        return files;
    }

    public int changeStatus(long id, String status) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_MODIFIED, getCurrentDateTime());
        values.put(COLUMN_STATUS, status);
        return getDatabase().update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int markDeleted(long id) {
        return changeStatus(id, STATUS_DELETED);
    }

    public int deleteAllItemInPackage(long packageId, String shadeType) {
        return getDatabase().delete(TABLE_NAME, COLUMN_PACKAGE_ID + "=? AND " + COLUMN_TYPE + "=?", new String[]{String.valueOf(packageId), shadeType});
    }

    private DataShadeInfo cursorToShadeInfo(Cursor itemsCursor) {
        DataShadeInfo item = new DataShadeInfo();
        item.setId(itemsCursor.getLong(itemsCursor.getColumnIndex(COLUMN_ID)));
        item.setLastModified(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_LAST_MODIFIED)));
        item.setStatus(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_STATUS)));
        item.setTitle(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_NAME)));
        item.setThumbnail(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_THUMBNAIL)));
        item.setSelectedThumbnail(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_SELECTED_THUMBNAIL)));
        item.setForeground(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_FOREGROUND)));
        item.setShadeType(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_TYPE)));
        item.setPackageId(itemsCursor.getInt(itemsCursor.getColumnIndex(COLUMN_PACKAGE_ID)));
        return item;
    }

    private List<DataShadeInfo> toList(Cursor itemsCursor) {
        List<DataShadeInfo> itemList = new ArrayList<DataShadeInfo>();
        if (itemsCursor.moveToFirst()) {
            do {
                DataShadeInfo item = cursorToShadeInfo(itemsCursor);
                itemList.add(item);
                itemsCursor.moveToNext();
            } while (!itemsCursor.isAfterLast());
        }
        return itemList;
    }
}
