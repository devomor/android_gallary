package com.photo.photography.duplicatephotos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sira on 3/18/2018.
 */
public class DuplicatePhotosTables extends BaseTables {
    // Database structure
    public static final String TABLE_NAME = "DuplicatePhotos";
    public static final String TABLE_LOG_TAG = "DB_DATA";

    public static final String COLUMN_CHECK_BOX = "groupSetCheckBox";
    public static final String COLUMN_GROUP_TAG = "groupTag";
    public static final String COLUMN_IND_GRP_OF_DUPES = "individualGrpOfDupes";

    // Database creation sql statement
    private static final String SQL_DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CHECK_BOX + " text," + COLUMN_GROUP_TAG + " text," + COLUMN_IND_GRP_OF_DUPES + " text" + ");";

    public DuplicatePhotosTables(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(SQL_DATABASE_CREATE);
    }

    public static void upgradeTable(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void insertMultipleItem(List<IndividualGroups> list) {
        Log.e(TABLE_LOG_TAG, "==================== INSERT START ====================");
        for (int i = 0; i < list.size(); i++) {
            IndividualGroups info = list.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHECK_BOX, "" + info.isGroupSetCheckBox());
            values.put(COLUMN_GROUP_TAG, info.getGroupTag());
            values.put(COLUMN_IND_GRP_OF_DUPES, new Gson().toJson(info.getIndividualGrpOfDupes()));

            long id = getDatabase().insert(TABLE_NAME, null, values);
            info.setDbId(id);
            Log.e(TABLE_LOG_TAG, "INSERT Data id => " + id);
        }
        Log.e(TABLE_LOG_TAG, "==================== INSERT DONE ====================");
    }

    public List<IndividualGroups> getAllItems() {
        Log.e(TABLE_LOG_TAG, "==================== GET ALL START ====================");
        String selection = null;
        String[] selectionArgs = null;
        String[] selColumns = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor filesCursor = getDatabase().query(TABLE_NAME, selColumns, selection, selectionArgs, groupBy, having, orderBy);
        if (filesCursor == null) {
            Log.e(TABLE_LOG_TAG, "Cursor is null");
            return null;
        }

        Log.e(TABLE_LOG_TAG, "Total " + filesCursor.getCount() + " Records retrived");
        List<IndividualGroups> list = toList(filesCursor);
        filesCursor.close();
        Log.e(TABLE_LOG_TAG, "==================== GET ALL DONE ====================");
        return list;
    }

    private List<IndividualGroups> toList(Cursor itemsCursor) {
        List<IndividualGroups> itemList = new ArrayList<IndividualGroups>();
        if (itemsCursor.moveToFirst()) {
            do {
                IndividualGroups item = cursorToItemPackage(itemsCursor);
                itemList.add(item);
                itemsCursor.moveToNext();
            } while (!itemsCursor.isAfterLast());
        }
        return itemList;
    }

    @SuppressLint("Range")
    private IndividualGroups cursorToItemPackage(Cursor itemsCursor) {
        IndividualGroups item = new IndividualGroups();
        item.setDbId(itemsCursor.getLong(itemsCursor.getColumnIndex(COLUMN_ID)));
        item.setGroupSetCheckBox(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_CHECK_BOX)).equalsIgnoreCase("true"));
        item.setGroupTag(itemsCursor.getInt(itemsCursor.getColumnIndex(COLUMN_GROUP_TAG)));
        item.setIndividualGrpOfDupes(new Gson().fromJson(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_IND_GRP_OF_DUPES)), new TypeToken<ArrayList<IndividualGroups>>() {
        }.getType()));
        return item;
    }

//    public static ArrayList<ImageItem> getLockExactPhotos(Context context) {
//        try {
//            return (ArrayList) new Gson().fromJson(getLockExactPhotosPref(context).getString(LOCK_EXACT_PHOTOS, "{\"empty\":\"Lock\"}"), new TypeToken<ArrayList<ImageItem>>() {
//            }.getType());
//        } catch (JsonSyntaxException | IllegalArgumentException e) {
//            CommonlyUsed.logmsg("Exception for exact first time: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public static void setLockExactPhotos(Context context, ArrayList<ImageItem> arrayList) {
//        getLockExactPhotosPref(context).edit().putString(LOCK_EXACT_PHOTOS, new Gson().toJson(arrayList)).commit();
//    }
/*    public long insert(IndividualGroup info) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getTitle());
        values.put(COLUMN_THUMBNAIL, info.getThumbnail());
        values.put(COLUMN_SELECTED_THUMBNAIL, info.getSelectedThumbnail());
        values.put(COLUMN_TYPE, info.getType());
        values.put(COLUMN_FOLDER, info.getFolder());
        values.put(COLUMN_TEXT_ID, info.getIdString());
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

    public int update(IndividualGroup info) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, info.getTitle());
        values.put(COLUMN_THUMBNAIL, info.getThumbnail());
        values.put(COLUMN_SELECTED_THUMBNAIL, info.getSelectedThumbnail());
        values.put(COLUMN_TYPE, info.getType());
        values.put(COLUMN_LAST_MODIFIED, getCurrentDateTime());
        values.put(COLUMN_FOLDER, info.getFolder());
        values.put(COLUMN_TEXT_ID, info.getIdString());
        if (info.getStatus() == null || info.getStatus().length() < 1) {
            info.setStatus(STATUS_ACTIVE);
        }

        values.put(COLUMN_STATUS, info.getStatus());

        return getDatabase().update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(info.getId())});
    }

    public boolean hasItem(String id, boolean active) {
        String selectString = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_TEXT_ID + " =?";
        if (active) {
            selectString = selectString.concat(" AND ").concat(COLUMN_STATUS).concat(" =?");
        }
        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = null;
        if (active) {
            cursor = getDatabase().rawQuery(selectString, new String[]{id, STATUS_ACTIVE});
        } else {
            cursor = getDatabase().rawQuery(selectString, new String[]{id});
        }

        boolean hasObject = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                hasObject = true;
            }

            cursor.close();
        }

        return hasObject;
    }

    public IndividualGroup getRowWithName(String name) {
        String[] selColumns = null;
        String selection = COLUMN_STATUS + " = ? AND UPPER(" + COLUMN_NAME + ") = UPPER(?)";
        String[] selectionArgs = {STATUS_ACTIVE, name};

        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor filesCursor = getDatabase().query(TABLE_NAME, selColumns, selection, selectionArgs, groupBy, having,
                orderBy);
        if (filesCursor == null)
            return null;

        List<IndividualGroup> files = toList(filesCursor);
        filesCursor.close();
        if (files.size() > 0) {
            return files.get(0);
        } else {
            return null;
        }
    }

    public IndividualGroup getRowWithStoreId(String idStr) {
        String[] selColumns = null;
        String selection = COLUMN_TEXT_ID + " = ?";
        String[] selectionArgs = {idStr};

        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor filesCursor = getDatabase().query(TABLE_NAME, selColumns, selection, selectionArgs, groupBy, having,
                orderBy);
        if (filesCursor == null)
            return null;

        List<IndividualGroup> files = toList(filesCursor);
        filesCursor.close();
        if (files.size() > 0) {
            return files.get(0);
        } else {
            return null;
        }
    }

    public List<IndividualGroup> getAllRows() {
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

        List<IndividualGroup> files = toList(filesCursor);
        filesCursor.close();
        return files;
    }

    public List<IndividualGroup> getRows(String type) {
        String[] selColumns = null;
        String selection = COLUMN_STATUS + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {STATUS_ACTIVE, type};

        String groupBy = null;
        String having = null;
        String orderBy = COLUMN_LAST_MODIFIED.concat(" DESC");

        Cursor filesCursor = getDatabase().query(TABLE_NAME, selColumns, selection, selectionArgs, groupBy, having,
                orderBy);
        if (filesCursor == null)
            return null;

        List<IndividualGroup> files = toList(filesCursor);
        filesCursor.close();
        return files;
    }

    public int changeStatus(String idStr, String status) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_MODIFIED, getCurrentDateTime());
        values.put(COLUMN_STATUS, status);
        return getDatabase().update(TABLE_NAME, values, COLUMN_TEXT_ID + " = ?", new String[]{idStr});
    }

    public int markDeleted(String idStr) {
        return changeStatus(idStr, STATUS_DELETED);
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

    public int delete(long id) {
        return getDatabase().delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int delete(String idStr) {
        return getDatabase().delete(TABLE_NAME, COLUMN_TEXT_ID + "=?", new String[]{idStr});
    }

    private IndividualGroup cursorToItemPackage(Cursor itemsCursor) {
        IndividualGroup item = new IndividualGroup();
        item.setId(itemsCursor.getLong(itemsCursor.getColumnIndex(COLUMN_ID)));
        item.setLastModified(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_LAST_MODIFIED)));
        item.setStatus(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_STATUS)));
        item.setTitle(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_NAME)));
        item.setThumbnail(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_THUMBNAIL)));
        item.setSelectedThumbnail(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_SELECTED_THUMBNAIL)));
        item.setType(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_TYPE)));
        item.setFolder(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_FOLDER)));
        item.setIdString(itemsCursor.getString(itemsCursor.getColumnIndex(COLUMN_TEXT_ID)));
        return item;
    }

    private List<IndividualGroup> toList(Cursor itemsCursor) {
        List<IndividualGroup> itemList = new ArrayList<IndividualGroup>();
        if (itemsCursor.moveToFirst()) {
            do {
                IndividualGroup item = cursorToItemPackage(itemsCursor);
                itemList.add(item);
                itemsCursor.moveToNext();
            } while (!itemsCursor.isAfterLast());
        }
        return itemList;
    }*/
}
