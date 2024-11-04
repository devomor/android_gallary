package com.photo.photography.collage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.photo.photography.collage.helper.HelperALog;

public class HelperDatabase extends SQLiteOpenHelper {
    private static final String TAG = HelperDatabase.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;

    public HelperDatabase(Context context) {
        super(context, ManagerDatabase.DB_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        HelperALog.e(TAG, "onCreate()-db.getVersion=" + db.getVersion());
        try {
            db.beginTransaction();
            String[] assetSqlScripts = {"create_structure_and_default_data.sql"};
            for (int i = 0; i < assetSqlScripts.length; i++) {
                ManagerDatabase.runSqlScript(mContext, db, assetSqlScripts[i]);
            }
            db.setTransactionSuccessful();
            HelperALog.i(TAG, "IN onCreate() runAssetSqlScript() CALLED!");
        } catch (Exception ioe) {
            ioe.printStackTrace();
//            FirebaseCrash.report(ioe);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        HelperALog.i(TAG, "IN onUpgrade() oldVersion=" + oldVersion + ", newVersion=" + newVersion);
        try {
            db.beginTransaction();
            ManagerDatabase.runSqlScript(mContext, db, "upgrade_db.sql");
            db.setTransactionSuccessful();
            HelperALog.i(TAG, "IN onUpgrade() runAssetSqlScript() CALLED!");
        } catch (Exception ioe) {
            ioe.printStackTrace();
//            FirebaseCrash.report(ioe);
        } finally {
            db.endTransaction();
        }
    }
}
