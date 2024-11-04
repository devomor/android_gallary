package com.photo.photography.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.photo.photography.data_helper.Media;
import com.photo.photography.callbacks.CallbackFileOperation;
import com.photo.photography.secure_vault.ActVault;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.helper.Constants;
import com.photo.photography.secure_vault.helper.DbHandler;
import com.photo.photography.secure_vault.utils.CryptoExceptionUtils;
import com.photo.photography.secure_vault.utils.CryptoUtil;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

class EncryptFiles extends AsyncTask<Intent, String, String> {

    Activity mActivity;
    // Progress Dialog
    private ProgressDialog pDialog;
    private final DbHandler db;
    private final CallbackFileOperation fileOperationCallback;

    public EncryptFiles(Activity mActivity, CallbackFileOperation callback) {
        db = new DbHandler(mActivity);
        this.mActivity = mActivity;
        fileOperationCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Processing file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private String SaveThumbnail(String fileMain, String fileName, String fileType) {

        int THUMBSIZE = mActivity.getResources().getDisplayMetrics().widthPixels;
        Bitmap finalBitmap;
        if (fileType.equals(Constants.FILE_TYPE_IMAGE)) {
            finalBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileMain), THUMBSIZE, THUMBSIZE);
        } else {
            finalBitmap = ThumbnailUtils.createVideoThumbnail(fileMain, MediaStore.Video.Thumbnails.MINI_KIND);
        }
        File myDirSnaplock = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATEVAULT);
        File myDirSupport = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_SUPPORT);
        if (!myDirSnaplock.exists()) {
            myDirSnaplock.mkdir();
        }
        if (!myDirSupport.exists()) {
            myDirSupport.mkdir();
        }
        File file = new File(myDirSupport, fileName + ".jpg");
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);

            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            out.flush();
            out.close();
            Log.e("TAG", "Thumbnail successfully Saved" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    @Override
    protected String doInBackground(Intent... data) {
        if (!(new File(mActivity.getExternalFilesDir(null), "support")).exists()) {
            new File(mActivity.getExternalFilesDir(null), "support").mkdirs();
        }
        String privatevaultFolder = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).getAbsolutePath();
        if (!(new File(privatevaultFolder)).exists()) {
            new File(privatevaultFolder).mkdirs();
        }

        ArrayList<Media> fileList = data[0].getParcelableArrayListExtra(ActVault.PICKED_MEDIA_LIST);
        if (fileList.size() > 0) {
            for (int i = 0; i < fileList.size(); i++) {
                Media file = fileList.get(i);
                if (file.isImage()) {
                    String oldFilePath = file.getPath();
                    String oldFileName = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
                    String currentTimeInMillis = String.valueOf(System.currentTimeMillis() + i);
                    String newFilePath = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).getAbsolutePath() + currentTimeInMillis;
                    String newFileName = currentTimeInMillis;
                    String oldThumbnailPath = SaveThumbnail(oldFilePath, currentTimeInMillis, Constants.FILE_TYPE_IMAGE);
                    Log.e("TAG", "Old Thumbnail Path : " + oldThumbnailPath);
                    String newThumbnailPath = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).getAbsolutePath() + currentTimeInMillis;
                    try {
                        if (!new File(mActivity.getExternalFilesDir(null), "support").exists()) {
                            new File(mActivity.getExternalFilesDir(null), "support").mkdir();
                        }
                        if (!new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).exists()) {
                            new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).mkdir();
                        }
                        if (!new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).exists()) {
                            new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).mkdir();
                        }

                        Log.e("TAG", "Encrypt file : " + oldFilePath + " to\n" + newFilePath);
                        CryptoUtil.encrypt(mActivity, new File(oldFilePath), new File(newFilePath));
                        CryptoUtil.encrypt(mActivity, new File(oldThumbnailPath), new File(newThumbnailPath));
                        int p = db.addRecord(new VaultFile(oldFilePath, oldFileName, newFilePath, newFileName, Constants.FILE_TYPE_IMAGE, newThumbnailPath, false));
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            new File(oldThumbnailPath).delete();
                            new File(oldFilePath).delete();
//                            encryptImagesCallBroadCast(oldFilePath);
                        } else {
                            MediaUtils.deleteFileInAndroidQ(mActivity, file);
                        }
                        Log.e("TAG", "if complete without error");
                    } catch (CryptoExceptionUtils e) {
                        Log.e("TAG", "Error in Encryption of file : " + e.toString());
                    }

                } else {
                    String oldFilePath = file.getPath();
                    String oldFileName = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
                    String currentTimeInMillis = String.valueOf(System.currentTimeMillis() + 1);
                    String newFilePath = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).getAbsolutePath() + currentTimeInMillis;
                    String newFileName = currentTimeInMillis;
                    String oldThumbnailPath = SaveThumbnail(oldFilePath, currentTimeInMillis, Constants.FILE_TYPE_VIDEO);
                    Log.e("TAG", "Old Thumbnail Path : " + oldThumbnailPath);
                    String newThumbnailPath = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).getAbsolutePath() + currentTimeInMillis;
                    try {
                        if (!new File(mActivity.getExternalFilesDir(null), "support").exists()) {
                            new File(mActivity.getExternalFilesDir(null), "support").mkdir();
                        }
                        if (!new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).exists()) {
                            new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).mkdir();
                        }
                        if (!new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).exists()) {
                            new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).mkdir();
                        }

                        Log.e("TAG", "Encrypt file : " + oldFilePath + " to\n" + newFilePath);
                        CryptoUtil.encrypt(mActivity, new File(oldFilePath), new File(newFilePath));
                        CryptoUtil.encrypt(mActivity, new File(oldThumbnailPath), new File(newThumbnailPath));
                        int p = db.addRecord(new VaultFile(oldFilePath, oldFileName, newFilePath, newFileName, Constants.FILE_TYPE_VIDEO, newThumbnailPath, false));
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            new File(oldThumbnailPath).delete();
                            new File(oldFilePath).delete();
//                            encryptVideoCallBroadCast(oldFilePath);
                        } else {
                            MediaUtils.deleteFileInAndroidQ(mActivity, file);
                        }
                        Log.e("TAG", "if complete without error");
                    } catch (CryptoExceptionUtils e) {
                        Log.e("TAG", "Error in Encryption of file : " + e.toString());
                    }
                }

                ArrayList<VaultFile> vaultData = db.getAllRecords();
                if (vaultData.size() > 0) {
                    for (int p = 0; p < vaultData.size(); p++) {
                        int pro_id = vaultData.get(p).getID();
                        String oldFilePath = vaultData.get(p).getOldPath();
                        String oldFileName = vaultData.get(p).getOldFileName();
                        String newFilePath = vaultData.get(p).getNewPath();
                        String newFileName = vaultData.get(p).getNewFileName();
                        String type = vaultData.get(p).getType();
                        String thumbnail = vaultData.get(p).getThumbnail();
                        Log.e("TAG", "Data Selected Record =>  Id-" + pro_id + ",\noldFilePath-" + oldFilePath + ",\noldFileName-" + oldFileName + ",\nnewFilePath-" + newFilePath + ",\nnewFileName-" + newFileName + ",\nType-" + type + ",\nThumbnail-" + thumbnail);
                    }
                } else {
                    Log.e("TAG", "Data not Available in table");
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        pDialog.dismiss();

        fileOperationCallback.onReturnResult(file_url);
    }
}