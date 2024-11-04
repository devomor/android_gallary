package com.photo.photography.secure_vault.utils;

/**
 * Created by Admin on 09-09-2016.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.photo.photography.collage.util.CollageFilesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final String Key = "2b7e151628aed2a6abf71589";

    public static void encrypt(Activity mActivity, File inputFile, File outputFile) throws CryptoExceptionUtils {
        doCrypto(mActivity, Cipher.ENCRYPT_MODE, Key, inputFile, outputFile, false);
    }

    public static void decrypt(Activity mActivity, File inputFile, File outputFile) throws CryptoExceptionUtils {
        doCrypto(mActivity, Cipher.DECRYPT_MODE, Key, inputFile, outputFile, false);
    }

    public static void decryptForView(Activity mActivity, File inputFile, File outputFile) throws CryptoExceptionUtils {
        doCrypto(mActivity, Cipher.DECRYPT_MODE, Key, inputFile, outputFile, true);
    }

    private static void doCrypto(Activity mActivity, int cipherMode, String key, File inputFile, File outputFile, boolean forView) throws CryptoExceptionUtils {
        try {
            if (cipherMode == Cipher.ENCRYPT_MODE) {
                Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(cipherMode, secretKey);
                if (inputFile.exists()) {
                    FileInputStream inputStream = new FileInputStream(inputFile);
                    byte[] inputBytes = new byte[(int) inputFile.length()];
                    inputStream.read(inputBytes);

                    byte[] outputBytes = cipher.doFinal(inputBytes);

                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    outputStream.write(outputBytes);

                    inputStream.close();
                    outputStream.close();
                }

            } else if (forView || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(cipherMode, secretKey);
                if (inputFile.exists()) {
                    FileInputStream inputStream = new FileInputStream(inputFile);
                    byte[] inputBytes = new byte[(int) inputFile.length()];
                    inputStream.read(inputBytes);

                    byte[] outputBytes = cipher.doFinal(inputBytes);

                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    outputStream.write(outputBytes);

                    inputStream.close();
                    outputStream.close();
                }

            } else {
                Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(cipherMode, secretKey);
                ContentResolver contentResolver = mActivity.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, outputFile.getName());
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, CollageFilesUtils.getMimeType(outputFile));
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "GallerySafe");

                FileInputStream inputStream = new FileInputStream(inputFile);
                byte[] inputBytes = new byte[(int) inputFile.length()];
                inputStream.read(inputBytes);

                byte[] outputBytes = cipher.doFinal(inputBytes);

                Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

                FileOutputStream outputStream = (FileOutputStream) contentResolver.openOutputStream(Objects.requireNonNull(uri));
                outputStream.write(outputBytes);

                inputStream.close();
                outputStream.close();
            }
        } catch (Exception ex) {
            throw new CryptoExceptionUtils("Error encrypting/decrypting file", ex);
        }
    }

    public static Bitmap decryptBitmap(File inputFile) throws CryptoExceptionUtils {
        Bitmap bmp = doCryptoBitmap(Cipher.DECRYPT_MODE, Key, inputFile);
        return bmp;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] data, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //options.inPurgeable = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    private static Bitmap doCryptoBitmap(int cipherMode, String key, File inputFile) throws CryptoExceptionUtils {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            Bitmap bmp = decodeSampledBitmapFromResource(outputBytes, 450, 450);
            //  Bitmap bmp = BitmapFactory.decodeByteArray(outputBytes, 0, outputBytes.length); //Convert bytearray to bitmap
            inputStream.close();
            Log.e("TAG", "Description Completed.");

            return bmp;

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoExceptionUtils("Error encrypting/decrypting file", ex);
        }
    }
}