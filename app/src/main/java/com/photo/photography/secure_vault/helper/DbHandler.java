package com.photo.photography.secure_vault.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.photo.photography.secure_vault.models.VaultFile;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Otp
    private static final String DATABASE_VAULT = "Snap_Vault";

    // Records table otp
    private static final String TABLE_DATA = "PrivateVaultData";
    private static final String TABLE_SNAP_IMG = "SnapImgData";

    // Records Table Columns otps
    private static final String KEY_ID = "id";
    private static final String KEY_OLD_PATH = "oldPath";
    private static final String KEY_OLD_FILE_NAME = "oldFileName";
    private static final String KEY_NEW_PATH = "newPath";
    private static final String KEY_NEW_FILE_NAME = "newFileName";
    private static final String KEY_TYPE = "type";
    private static final String KEY_THUMBNAIL = "thumbnail";

    public DbHandler(Context context) {
        super(context, DATABASE_VAULT, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_OLD_PATH + " TEXT,"
                + KEY_OLD_FILE_NAME + " TEXT,"
                + KEY_NEW_PATH + " TEXT,"
                + KEY_NEW_FILE_NAME + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_THUMBNAIL + " TEXT)";
        db.execSQL(CREATE_CART_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

        // Create tables again
        onCreate(db);
    }

    // Adding new records
    public int addRecord(VaultFile records) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//		values.put(KEY_ID, records.getID());
        values.put(KEY_OLD_PATH, records.getOldPath());
        values.put(KEY_OLD_FILE_NAME, records.getOldFileName());
        values.put(KEY_NEW_PATH, records.getNewPath());
        values.put(KEY_NEW_FILE_NAME, records.getNewFileName());
        values.put(KEY_TYPE, records.getType());
        values.put(KEY_THUMBNAIL, records.getThumbnail());

        // Inserting Row
        db.insert(TABLE_DATA, null, values);
        String countQuery = "SELECT  * FROM " + TABLE_DATA;
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToLast();
        int lastInsertedRow = Integer.parseInt(cursor.getString(0));
        cursor.close();
        db.close(); // Closing database connection
        return lastInsertedRow;
    }

    // Getting single records
    public VaultFile getRecord(String key_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DATA, new String[]{KEY_ID, KEY_OLD_PATH, KEY_OLD_FILE_NAME, KEY_NEW_PATH, KEY_NEW_FILE_NAME, KEY_TYPE, KEY_THUMBNAIL}, KEY_ID + "=?", new String[]{key_id}, null, null, null, null);

        if (cursor != null)
            cursor.moveToLast();

        VaultFile records = new VaultFile(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        cursor.close();
        db.close();
        // return records
        return records;
    }

    public boolean CheckIsDataAlreadyInDBorNot(String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_DATA + " where " + KEY_OLD_FILE_NAME + " = '" + value + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public void addAllOldRecords(ArrayList<VaultFile> allRecords) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < allRecords.size(); i++) {
            VaultFile records = allRecords.get(i);
            ContentValues values = new ContentValues();
//		values.put(KEY_ID, records.getID());
            values.put(KEY_OLD_PATH, records.getOldPath());
            values.put(KEY_OLD_FILE_NAME, records.getOldFileName());
            values.put(KEY_NEW_PATH, records.getNewPath());
            values.put(KEY_NEW_FILE_NAME, records.getNewFileName());
            values.put(KEY_TYPE, records.getType());
            values.put(KEY_THUMBNAIL, records.getThumbnail());

            // Inserting Row
            db.insert(TABLE_DATA, null, values);
        }

        db.close(); // Closing database connection
    }

    // Getting All Records
    public ArrayList<VaultFile> getAllRecords() {
        ArrayList<VaultFile> recordsList = new ArrayList<VaultFile>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VaultFile records = new VaultFile();
                records.setID(Integer.parseInt(cursor.getString(0)));
                records.setOldPath(cursor.getString(1));
                records.setOldFileName(cursor.getString(2));
                records.setNewPath(cursor.getString(3));
                records.setNewFileName(cursor.getString(4));
                records.setType(cursor.getString(5));
                records.setThumbnail(cursor.getString(6));
                // Adding records to list
                recordsList.add(records);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return records list
        return recordsList;
    }

    // Getting All Records
    public List<VaultFile> getLastRecords() {
        List<VaultFile> recordsList = new ArrayList<VaultFile>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_DATA + " ORDER BY " + KEY_ID + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            VaultFile records = new VaultFile();
            records.setID(Integer.parseInt(cursor.getString(0)));
            records.setOldPath(cursor.getString(1));
            records.setOldFileName(cursor.getString(2));
            records.setNewPath(cursor.getString(3));
            records.setNewFileName(cursor.getString(4));
            records.setType(cursor.getString(5));
            records.setThumbnail(cursor.getString(6));
            // Adding records to list
            recordsList.add(records);
        }
        cursor.close();
        db.close();
        // return records list
        return recordsList;
    }

    // Getting All Records
    public ArrayList<VaultFile> getAllTypeWiseRecords(String fileType) {
        ArrayList<VaultFile> recordsList = new ArrayList<VaultFile>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + KEY_TYPE + "='" + fileType + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VaultFile records = new VaultFile();
                records.setID(Integer.parseInt(cursor.getString(0)));
                records.setOldPath(cursor.getString(1));
                records.setOldFileName(cursor.getString(2));
                records.setNewPath(cursor.getString(3));
                records.setNewFileName(cursor.getString(4));
                records.setType(cursor.getString(5));
                records.setThumbnail(cursor.getString(6));
                // Adding records to list
                recordsList.add(records);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return records list
        return recordsList;
    }

    // Updating single records
    public int updateRecord(VaultFile records) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OLD_PATH, records.getOldPath());
        values.put(KEY_OLD_FILE_NAME, records.getOldFileName());
        values.put(KEY_NEW_PATH, records.getNewPath());
        values.put(KEY_NEW_FILE_NAME, records.getNewFileName());
        values.put(KEY_TYPE, records.getType());
        values.put(KEY_THUMBNAIL, records.getThumbnail());

        db.close();
        // updating row
        return db.update(TABLE_DATA, values, KEY_ID + " = ? AND " + KEY_OLD_FILE_NAME + " = ?",
                new String[]{String.valueOf(records.getID()), String.valueOf(records.getOldFileName())});
    }


    // Deleting single records
    public void deleteRecord(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_DATA, KEY_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
            Log.e("TAG", "Record Successfully Deleted...");
        } catch (Exception e) {
            Log.e("TAG", "Record Not Deleted...");
        }
    }


    // Delete table all data
    public void delete_all_records() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DATA, null, null);
        db.close();
    }


    // Getting recordss Count
    public int getRecordsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }

    public int getRecordsCountPerticular(String value) {
        String countQuery = "SELECT  * FROM " + TABLE_DATA + " WHERE " + KEY_OLD_FILE_NAME + "='" + value + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        // return count
        return cursor.getCount();
    }
}
