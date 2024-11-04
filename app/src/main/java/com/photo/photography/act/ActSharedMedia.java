package com.photo.photography.act;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.photo.photography.R;
import com.photo.photography.data_helper.StorageHelpers;

public abstract class ActSharedMedia extends ActBase {

    private final int REQUEST_CODE_SD_CARD_PERMISSIONS = 42;

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SD_CARD_PERMISSIONS) {
                Uri treeUri = resultData.getData();
                // Persist URI in shared preference so that you can use it later.
                StorageHelpers.saveSdCardInfo(getApplicationContext(), treeUri);
                getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                Toast.makeText(this, R.string.got_permission_wr_sdcard, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
