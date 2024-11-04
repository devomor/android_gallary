package com.photo.photography.secure_vault.frag;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.photo.photography.MyApp;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.helper.DbHandler;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

public class FragBase extends Fragment {

    //    private Tracker mTracker;
    public Activity mActivity;

    public void analyticsTracker() {
        // Obtain the shared Tracker instance.
        MyApp application = (MyApp) mActivity.getApplication();
//        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public void saveDataInFile() {
        new android.os.AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                ArrayList<VaultFile> alldata = new DbHandler(mActivity).getAllRecords();
                if (alldata.size() > 0) {
                    String dbValue = SupportClass.convertArrayListToString(alldata);
                    File file = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_SAVE_DB_FILE);

                    try {
                        if (file.exists())
                            file.delete();
                        else
                            file.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(file);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.write(dbValue);
                        myOutWriter.close();
                        fOut.flush();
                        fOut.close();

                    } catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                }
                return null;
            }
        }.execute();
    }

//    public void analyticsTrackerSetScreenName(String screenName) {
//        if (mTracker == null)
//            analyticsTracker();
//        mTracker.setScreenName("Screen ~ " + screenName);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//    }


//    public void analyticsTrackerSetAction(String action) {
//        if (mTracker == null)
//            analyticsTracker();
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Action")
//                .setAction(action)
//                .build());
//    }
}
