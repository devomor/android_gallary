package com.photo.photography.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.photo.photography.util.configs.AppLog;

public class DatabaseHelpers extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelpers.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;

    public DatabaseHelpers(Context context) {
        super(context, DatabasesManager.DB_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AppLog.e(TAG, "onCreate()-db.getVersion=" + db.getVersion());
        try {
            db.beginTransaction();
            String[] assetSqlScripts = {"create_structure_and_default_data.sql"};
            for (int i = 0; i < assetSqlScripts.length; i++) {
                DatabasesManager.runSqlScript(mContext, db, assetSqlScripts[i]);
            }
            db.setTransactionSuccessful();
            AppLog.i(TAG, "IN onCreate() runAssetSqlScript() CALLED!");
        } catch (Exception ioe) {
            ioe.printStackTrace();
//            FirebaseCrash.report(ioe);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppLog.i(TAG, "IN onUpgrade() oldVersion=" + oldVersion + ", newVersion=" + newVersion);
        try {
            db.beginTransaction();
            DatabasesManager.runSqlScript(mContext, db, "upgrade_db.sql");
            db.setTransactionSuccessful();
            AppLog.i(TAG, "IN onUpgrade() runAssetSqlScript() CALLED!");
        } catch (Exception ioe) {
            ioe.printStackTrace();
//            FirebaseCrash.report(ioe);
        } finally {
            db.endTransaction();
        }
    }
}
