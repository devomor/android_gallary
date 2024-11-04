@file: JvmName("MediaUtils")

package com.photo.photography.util

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.photo.photography.BuildConfig
import com.photo.photography.R
import com.photo.photography.collage.util.CollageFilesUtils.TEMP_FOLDER
import com.photo.photography.data_helper.Media
import com.photo.photography.data_helper.MediaHelper
import com.photo.photography.data_helper.StorageHelpers
import com.photo.photography.callbacks.CallbackFileOperation
import com.photo.photography.util.utils.CallbackOnDeleteProcess
import com.photo.photography.util.utilsEdit.SupportClass
import com.photo.photography.secure_vault.ActVault
import java.io.*
import java.lang.Exception

fun shareMedia(context: Context, mediaList: List<Media>) {
    val intent = Intent(Intent.ACTION_SEND_MULTIPLE)

    val types = HashMap<String, Int>()
    val files = ArrayList<Uri>()

    for (f in mediaList) {
        val mimeType = MimeTypeUtil.getTypeMime(f.mimeType)
        var count = 0
        if (types.containsKey(mimeType)) {
            count = types[mimeType]!!
        }
        types[mimeType] = count

        var uri: Uri? = null
        uri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", f.file)
        } else {
            Uri.fromFile(f.file)
        }

        files.add(uri)
    }

    val fileTypes = types.keys
    if (fileTypes.size > 1) {
        Toast.makeText(context, R.string.waring_share_multiple_file_types, Toast.LENGTH_SHORT).show()
    }

    val max = -1
    var type: String? = null
    for (fileType in fileTypes) {
        val count = types[fileType]!!
        if (count > max) {
            type = fileType
        }
    }

    intent.type = type!! + "/*"

    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_to)))
}

fun moveInVaultMediaNew(mActivity: Activity, mediaList: ArrayList<Media>, onDeleteProcessListener: CallbackOnDeleteProcess) {
    pendingMediaList = ArrayList()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val urisToModify = ArrayList<Uri>(mediaList.size)
        for (media in mediaList) {
            var uri: Uri? = null
            if (media.id > 0) {
                uri = ContentUris.withAppendedId(if (media.isImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI, media.id)
            } else {
                val uriLocal: Uri? = if (Build.VERSION.SDK_INT >= 24) {
                    FileProvider.getUriForFile(mActivity, mActivity.packageName + ".provider", media.file)
                } else {
                    Uri.fromFile(media.file)
                }
                if (uriLocal != null) {
                    uri = uriLocal
                }
            }
            if (uri != null) {
                urisToModify.add(uri)
            }
        }

        pendingMediaList = mediaList
        val editPendingIntent = MediaStore.createWriteRequest(mActivity.contentResolver, urisToModify)
        /* In Launch a system prompt requesting user permission for the operation. */
        mActivity.startIntentSenderForResult(editPendingIntent.intentSender, SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE, null, 0, 0, 0)

    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
        if (mediaList.size > 0) {
            pendingMediaList = mediaList
            doAndroidQProcess(mActivity, pendingMediaList[0], onDeleteProcessListener, 1)
        }

    } else {
        for (media in mediaList) {

            var success = false
            try {
                success = File(media.path).delete()
            } catch (e: java.lang.Exception) {
                Log.e("TAG", "Error when deleting file " + File(media.path).absolutePath, e)
            }

            // Try the Kitkat workaround.
            if (!success && Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                val resolver: ContentResolver = mActivity.contentResolver
                success = try {
                    val uri = StorageHelpers.getUriForFile(mActivity, File(media.path))
                    if (uri != null) {
                        resolver.delete(uri, null, null)
                    }
                    !File(media.path).exists()
                } catch (e: java.lang.Exception) {
                    Log.e("TAG", "Error when deleting file " + File(media.path).absolutePath, e)
                    false
                }
            }

            if (success) {
                onDeleteProcessListener.onMediaDeleteSuccess(true, media)
                MediaHelper.scanFile(mActivity, arrayOf(File(media.path).path))
            }
        }
        onDeleteProcessListener.onDeleteComplete()
    }
}

fun deleteMediaNew(mActivity: Activity, mediaList: ArrayList<Media>, onSuccessListener: CallbackOnDeleteProcess) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val urisToModify = ArrayList<Uri>()
        for (media in mediaList) {
            var uri: Uri? = null
            if (media.id > 0) {
                uri = ContentUris.withAppendedId(if (media.isImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI, media.id)
            } else {
                val uriLocal: Uri? = if (Build.VERSION.SDK_INT >= 24) {
                    FileProvider.getUriForFile(mActivity, mActivity.packageName + ".provider", media.file)
                } else {
                    Uri.fromFile(media.file)
                }
                if (uriLocal != null) {
                    uri = uriLocal
                }
            }
            if (uri != null) {
                urisToModify.add(uri)
            }
        }

        pendingMediaList = mediaList
        val editPendingIntent = MediaStore.createWriteRequest(mActivity.contentResolver, urisToModify)
        /* In Launch a system prompt requesting user permission for the operation. */
        mActivity.startIntentSenderForResult(editPendingIntent.intentSender, SupportClass.ANDROID_R_DELETE_REQUEST_CODE, null, 0, 0, 0)

    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
        pendingMediaList = ArrayList()
        if (mediaList.size > 0) {
            pendingMediaList = mediaList
            doAndroidQProcess(mActivity, pendingMediaList[0], onSuccessListener, 0)
        }
    } else {
        for (media in mediaList) {

            var success = false
            val deleteFile = File(media.path)
            if (!android.text.TextUtils.isEmpty(media.path) && deleteFile.exists()) {
                try {
                    success = deleteFile.delete()
                } catch (e: java.lang.Exception) {
                    Log.e("TAG", "Error when deleting file " + deleteFile.absolutePath, e)
                }

                // Try the Kitkat workaround.
                if (!success) {
                    val resolver: ContentResolver = mActivity.contentResolver
                    success = try {
                        val uri = StorageHelpers.getUriForFile(mActivity, deleteFile)
                        if (uri != null) {
                            resolver.delete(uri, null, null)
                        }
                        !deleteFile.exists()
                    } catch (e: java.lang.Exception) {
                        Log.e("TAG", "Error when deleting file " + deleteFile.absolutePath, e)
                        false
                    }
                }
            }

            if (success) {
                onSuccessListener.onMediaDeleteSuccess(true, media)
                MediaHelper.scanFile(mActivity, arrayOf(deleteFile.path))
            }
        }
        onSuccessListener.onDeleteComplete()
    }
}

@Throws(IOException::class)
private fun copyFile(src: File, dst: File): Boolean {
    try {
        if (src.absolutePath.toString() == dst.absolutePath.toString()) {
            return true
        } else {
            val `is`: InputStream = FileInputStream(src)
            val os: OutputStream = FileOutputStream(dst)
            val buff = ByteArray(1024)
            var len: Int
            while (`is`.read(buff).also { len = it } > 0) {
                os.write(buff, 0, len)
            }
            `is`.close()
            os.close()
        }
    } catch (e: Exception){

    }
    return true
}

fun doAndroidQProcess(mActivity: Activity, media: Media, onSuccessListener: CallbackOnDeleteProcess, operation: Int) {
    val contentResolver: ContentResolver = mActivity.contentResolver
    try {
        var copiedPath = ""
        if (operation == 1) {
            if (!File(TEMP_FOLDER).exists()) File(TEMP_FOLDER).mkdirs()

            copyFile(media.file, File(TEMP_FOLDER + File.separator + media.file.name))
            copiedPath = File(TEMP_FOLDER + File.separator + media.file.name).absolutePath
        }

        val uri = ContentUris.withAppendedId(if (media.isImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI, media.id)
        val deletedFiles = contentResolver.delete(uri, null, null)

        MediaScannerConnection.scanFile(mActivity, arrayOf(media.path), null, null)

        if (deletedFiles > 0) {
            if (operation == 1) {
                val arrayList = ArrayList<Media>()
                media.path = copiedPath
                arrayList.add(media)
                val intent = Intent()
                intent.putExtra(ActVault.PICKED_MEDIA_LIST, arrayList)
                EncryptFiles(mActivity,
                    CallbackFileOperation {
                        deleteSuccessOnAndroidQ(mActivity, onSuccessListener, operation, media)
                    }).execute(intent)
            } else {
                deleteSuccessOnAndroidQ(mActivity, onSuccessListener, operation, media)
            }
        }
    } catch (securityException: SecurityException) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (securityException is RecoverableSecurityException) {

                // Signal to the Activity that it needs to request permission and
                // try the delete again if it succeeds.
                try {
                    mActivity.startIntentSenderForResult(securityException.userAction.actionIntent.intentSender, if (operation == 0) SupportClass.ANDROID_Q_DELETE_REQUEST_CODE else SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE, null, 0, 0, 0, null)
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            }
        } else {
            Log.e("TAG", "Error when deleting file " + File(media.path).absolutePath, securityException)
            throw securityException
        }
    }
}

fun deleteSuccessOnAndroidQ(mActivity: Activity, onSuccessListener: CallbackOnDeleteProcess, operation: Int, media: Media) {
    pendingMediaList.removeAt(0)
    onSuccessListener.onMediaDeleteSuccess(true, media)
    when {
        pendingMediaList.size > 0 -> {
            doAndroidQProcess(mActivity, pendingMediaList[0], onSuccessListener, operation)
        }
        pendingMediaList.size == 0 -> {
            onSuccessListener.onDeleteComplete()
        }
    }
}

var pendingMediaList: ArrayList<Media> = ArrayList()
fun deletePendingMedia(mActivity: Activity, requestCode: Int, resultCode: Int, onSuccessListener: CallbackOnDeleteProcess) {
    if (resultCode == Activity.RESULT_OK && (requestCode == SupportClass.ANDROID_R_DELETE_REQUEST_CODE || requestCode == SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE)) {
        for (media in pendingMediaList) {
            try {
                val uri = ContentUris.withAppendedId(if (media.isImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI, media.id)
                val deletedFiles = mActivity.contentResolver.delete(uri, null, null)

                if (deletedFiles > 0) {
                    onSuccessListener.onMediaDeleteSuccess(true, media)
                }
            } catch (securityException: SecurityException) {
                Log.e("TAG", "Error when deleting file " + File(media.path).absolutePath, securityException)
            }
        }
        onSuccessListener.onDeleteComplete()

    } else if (requestCode == SupportClass.ANDROID_Q_DELETE_REQUEST_CODE || requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE) {
        if (pendingMediaList.size > 0) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val uri = ContentUris.withAppendedId(if (pendingMediaList[0].isImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI, pendingMediaList[0].id)
                    val deletedFiles = mActivity.contentResolver.delete(uri, null, null)
                    if (deletedFiles > 0) {
                        onSuccessListener.onMediaDeleteSuccess(true, pendingMediaList[0])
                    }
                } catch (securityException: SecurityException) {
                    Log.e("TAG", "Error when deleting file " + File(pendingMediaList[0].path).absolutePath, securityException)
                }
            }
            pendingMediaList.removeAt(0)
            if (pendingMediaList.size > 0) {
                doAndroidQProcess(mActivity, pendingMediaList[0], onSuccessListener, if (requestCode == SupportClass.ANDROID_Q_DELETE_REQUEST_CODE) 0 else 1)

            } else if (pendingMediaList.size == 0) {
                onSuccessListener.onDeleteComplete()
            }
        } else {
            onSuccessListener.onDeleteComplete()
        }
    }
}

fun deleteFileInAndroidQ(mActivity: Activity, media: Media) {
    try {
        val uri = ContentUris.withAppendedId(if (media.isImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI, media.id)
        val deletedFiles = mActivity.contentResolver.delete(uri, null, null)
        Log.e("TAG", "Delete Image with MediaUtils deleteFileInAndroidQ()" + File(media.path).absolutePath)
    } catch (securityException: SecurityException) {
        Log.e("TAG", "Error when deleting file " + File(media.path).absolutePath, securityException)
    }
}