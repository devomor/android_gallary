package com.photo.photography.util.utils;

import static com.photo.photography.util.MimeTypeUtil.getMimeType;
import static com.photo.photography.util.utils.ImageDecoder.getRealPathFromURI;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.util.configs.AppLog;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {
    public static final String OUTPUT_EDIT_FOLDER = new VaultFileUtil(MyApp.mContext).getFile("GEdit").getAbsolutePath();
    public static final String OUTPUT_EDIT_MEDIA = new VaultFileUtil(MyApp.mContext).getFile1("Gallery Edit").getAbsolutePath();
    private static final float MIN_OUTPUT_IMAGE_SIZE = 640.0f;

    public static void loadImageWithPicasso(final Context context, final ImageView imageView, final String uri) {
        if (uri != null && uri.length() > 1) {
            if (uri.startsWith("http://") || uri.startsWith("https://")) {
                Picasso.get().load(uri).into(imageView);
            } else if (uri.startsWith(PhotoUtil.DRAWABLE_PREFIX)) {
                try {
                    int id = Integer.parseInt(uri.substring(PhotoUtil.DRAWABLE_PREFIX.length()));
                    Picasso.get().load(id).into(imageView);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (uri.startsWith(PhotoUtil.ASSET_PREFIX)) {
                String file = uri.substring(PhotoUtil.ASSET_PREFIX.length());
                Picasso.get().load(Uri.parse("file:///android_asset/".concat(file))).into(imageView);
            } else {
                Picasso.get().load(new File(uri)).into(imageView);
            }
        }
    }

    public static String moveFile(File result) {
       /* try {
            File editForlder = new File(ImageUtils.OUTPUT_EDIT_MEDIA);
            if (!editForlder.exists()) {
                editForlder.mkdirs();
            }
            File outputFile = new File(editForlder, inputFile.getName());


            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = inputBytes;

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();
            return outputFile.getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = MyApp.getApplication().getContentResolver();
            ContentValues valuesvideos = new ContentValues();
            valuesvideos.put(MediaStore.MediaColumns.DISPLAY_NAME, result.getName());
            String fileMimeType = getMimeType(result.getAbsolutePath());
            valuesvideos.put(MediaStore.MediaColumns.MIME_TYPE, fileMimeType);
            valuesvideos.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS + File.separator + "Gallery Edit");
            final Uri uriSavedVideo = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, valuesvideos);

            try {
                OutputStream out = resolver.openOutputStream(uriSavedVideo);
                File fileCaseWatchBD = result;
                FileInputStream in = new FileInputStream(fileCaseWatchBD);
                byte[] buf = new byte[50192];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
                fileCaseWatchBD.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            if (uriSavedVideo != null) {
                String mSelectedImagePath = getRealPathFromURI(MyApp.getApplication(), uriSavedVideo);
                return mSelectedImagePath;
            } else {
                Toast.makeText(MyApp.getApplication(), MyApp.getApplication().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                return "";
            }

        } else {

            File sourceLocation = result;
            File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Gallery Edit");
            if (!myDir.exists() && !myDir.mkdirs()) {
                Log.i("INFO", "Directory not created");
            }
            File targetLocation = new File(myDir, result.getName());

            try {

                // moving the file to another directory

                // make sure the target file exists

                if (sourceLocation.exists()) {

                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[50192];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                    out.close();
                    return targetLocation.getAbsolutePath();

                } else {
                    Log.e("Error copy", "Copy file failed. Source file missing.");
                    return "";
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e("Error Null: ", e.getMessage());
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error Exception: ", e.getMessage());
                return "";
            }
        }
    }

    public static MemoryInfo getMemoryInfo(Context context) {
        final MemoryInfo info = new MemoryInfo();
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        info.availMem = memInfo.availMem;

        if (Build.VERSION.SDK_INT >= 16) {
            info.totalMem = memInfo.totalMem;
        } else {
            try {
                final RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
                final String load = reader.readLine();
                // Get the Number value from the string
                Pattern p = Pattern.compile("(\\d+)");
                Matcher m = p.matcher(load);
                String value = "";
                while (m.find()) {
                    value = m.group(1);
                }
                reader.close();
                info.totalMem = (long) Double.parseDouble(value) * 1024;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        AppLog.d("ImageUtils", "getMemoryInfo, availMem=" + info.availMem + ", totalMem=" + info.totalMem);
        return info;
    }

    public static float calculateOutputScaleFactor(int viewWidth, int viewHeight) {
        float ratio = Math.min(viewWidth, viewHeight) / MIN_OUTPUT_IMAGE_SIZE;
        if (ratio < 1 && ratio > 0) {
            ratio = 1.0f / ratio;
        } else {
            ratio = 1;
        }
        AppLog.d("ImageUtils", "calculateOutputScaleFactor, viewWidth=" + viewWidth + ", viewHeight=" + viewHeight + ", ratio=" + ratio);
        return ratio;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Matrix createMatrixToDrawImageInCenterView(final float viewWidth, final float viewHeight, final float imageWidth, final float imageHeight) {
        final float ratioWidth = ((float) viewWidth) / imageWidth;
        final float ratioHeight = ((float) viewHeight) / imageHeight;
        final float ratio = Math.max(ratioWidth, ratioHeight);
        final float dx = (viewWidth - imageWidth) / 2.0f;
        final float dy = (viewHeight - imageHeight) / 2.0f;
        Matrix result = new Matrix();
        result.postTranslate(dx, dy);
        result.postScale(ratio, ratio, viewWidth / 2, viewHeight / 2);
        return result;
    }

    public static long getUsedMemorySize() {

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedSize;

    }

    public static void recycleView(View iv) {
        if (iv == null) {
            return;
        }

        Drawable background = iv.getBackground();
        iv.setBackgroundColor(Color.TRANSPARENT);

        if (background != null && background instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) background).getBitmap();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }
    }

    public static void recycleImageView(ImageView iv) {
        if (iv == null) {
            return;
        }

        Drawable background = iv.getBackground();
        Drawable d = iv.getDrawable();
        iv.setBackgroundColor(Color.TRANSPARENT);
        iv.setImageBitmap(null);

        if (background != null && background instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) background).getBitmap();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }

        if (d != null && d instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) d).getBitmap();
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }
    }

    public static long getSizeInBytes(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * @param v
     * @return
     * @throws OutOfMemoryError
     * @deprecated
     */
    public static Bitmap loadBitmapFromView(View v) throws OutOfMemoryError {
        try {
            final int width = v.getMeasuredWidth();
            final int height = v.getMeasuredHeight();
            final Drawable bg = v.getBackground();
            v.setBackgroundColor(Color.TRANSPARENT);
            v.layout(0, 0, width, height);
            Bitmap returnedBitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(returnedBitmap);
            if (bg != null) {
                bg.draw(c);
            }

            v.draw(c);
            if (Build.VERSION.SDK_INT >= 16) {
                v.setBackground(bg);
            } else {
                v.setBackgroundDrawable(bg);
            }

            return returnedBitmap;
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
            throw err;
        }
    }

    /**
     * @param view
     * @param outputImagePath
     * @throws OutOfMemoryError
     * @deprecated
     */
    public static void takeScreen(View view, final String outputImagePath)
            throws OutOfMemoryError {
        try {
            Bitmap bitmap = loadBitmapFromView(view);
            File imageFile = new File(outputImagePath);
            imageFile.getParentFile().mkdirs();
            OutputStream fout = null;

            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
            throw err;
        }
    }

    public static void saveBitmap(Bitmap bitmap, final String path) {
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getImageOrientation(Context context, String imagePath) {
        int orientation = getOrientationFromExif(imagePath);
        if (orientation < 0) {
            Uri uri = Uri.fromFile(new File(imagePath));
            orientation = getOrientationFromMediaStore(context, uri);
        }

        return orientation;
    }

    // private static String getExifTag(ExifInterface exif,String tag){
    // String attribute = exif.getAttribute(tag);
    //
    // return (null != attribute ? attribute : "");
    // }
    private static int getOrientationFromExif(String imagePath) {
        int orientation = -1;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            // StringBuilder builder = new StringBuilder();
            //
            // builder.append("Date & Time: " +
            // getExifTag(exif,ExifInterface.TAG_DATETIME) + "\n\n");
            // builder.append("Flash: " +
            // getExifTag(exif,ExifInterface.TAG_FLASH) + "\n");
            // builder.append("Focal Length: " +
            // getExifTag(exif,ExifInterface.TAG_FOCAL_LENGTH) + "\n\n");
            // builder.append("GPS Datestamp: " +
            // getExifTag(exif,ExifInterface.TAG_FLASH) + "\n");
            // builder.append("GPS Latitude: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LATITUDE) + "\n");
            // builder.append("GPS Latitude Ref: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LATITUDE_REF) + "\n");
            // builder.append("GPS Longitude: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LONGITUDE) + "\n");
            // builder.append("GPS Longitude Ref: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_LONGITUDE_REF) + "\n");
            // builder.append("GPS Processing Method: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_PROCESSING_METHOD) + "\n");
            // builder.append("GPS Timestamp: " +
            // getExifTag(exif,ExifInterface.TAG_GPS_TIMESTAMP) + "\n\n");
            // builder.append("Image Length: " +
            // getExifTag(exif,ExifInterface.TAG_IMAGE_LENGTH) + "\n");
            // builder.append("Image Width: " +
            // getExifTag(exif,ExifInterface.TAG_IMAGE_WIDTH) + "\n\n");
            // builder.append("Camera Make: " +
            // getExifTag(exif,ExifInterface.TAG_MAKE) + "\n");
            // builder.append("Camera Model: " +
            // getExifTag(exif,ExifInterface.TAG_MODEL) + "\n");
            // builder.append("Camera Orientation: " +
            // getExifTag(exif,ExifInterface.TAG_ORIENTATION) + "\n");
            // builder.append("Camera White Balance: " +
            // getExifTag(exif,ExifInterface.TAG_WHITE_BALANCE) + "\n");
            // builder.append("Camera orientation=" + getExifTag(exif,
            // ExifInterface.TAG_ORIENTATION));
            // ALog.d("ImageUtils.getOrientationFromExif", "exif=" +
            // builder.toString());
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;

                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;

                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    private static int getOrientationFromMediaStore(Context context,
                                                    Uri imageUri)// (Context context, String imagePath)
    {
        // Uri imageUri = getImageContentUri(context, imagePath);
        if (imageUri == null) {
            return -1;
        }

        String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri,
                projection, null, null, null);

        int orientation = -1;
        if (cursor != null && cursor.moveToFirst()) {
            orientation = cursor.getInt(0);
            cursor.close();
        }

        return orientation;
    }

    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012
        //

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        if (bitmap.isRecycled())
            return null;
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int[] vmin = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int[] dv = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    //
    // private static Uri getImageContentUri(Context context, String imagePath)
    // {
    // String[] projection = new String[] {MediaStore.Images.Media._ID};
    // String selection = MediaStore.Images.Media.DATA + "=? ";
    // String[] selectionArgs = new String[] {imagePath};
    // Cursor cursor = context.getContentResolver().query(IMAGE_PROVIDER_URI,
    // projection,
    // selection, selectionArgs, null);
    //
    // if (cursor != null && cursor.moveToFirst()) {
    // int imageId = cursor.getInt(0);
    // cursor.close();
    //
    // return Uri.withAppendedPath(IMAGE_PROVIDER_URI,
    // Integer.toString(imageId));
    // }
    //
    // if (new File(imagePath).exists()) {
    // ContentValues values = new ContentValues();
    // values.put(MediaStore.Images.Media.DATA, imagePath);
    //
    // return context.getContentResolver().insert(
    // MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    // }
    //
    // return null;
    // }

    public static Bitmap getCircularArea(Bitmap bitmap, int circleX,
                                         int circleY, int radius) {
        Bitmap output = Bitmap.createBitmap(2 * radius, 2 * radius,
                Bitmap.Config.ARGB_8888);

        Rect rect = new Rect();
        rect.top = circleY - radius;
        rect.bottom = rect.top + 2 * radius;
        rect.left = circleX - radius;
        rect.right = rect.left + 2 * radius;

        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(output.getWidth() / 2, output.getHeight() / 2,
                radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, new Rect(0, 0, output.getWidth(),
                output.getHeight()), paint);

        return output;
    }

    public static Bitmap focus(Bitmap src, Bitmap filtratedBitmap, Rect rect,
                               boolean isCircle) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        Bitmap focusBitmap = null;
        Bitmap area = Bitmap.createBitmap(rect.right - rect.left, rect.bottom
                - rect.top, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(area);
        canvas.drawBitmap(src, rect,
                new Rect(0, 0, area.getWidth(), area.getHeight()), paint);

        if (isCircle) {
            focusBitmap = getCircularBitmap(area);
            area.recycle();
            area = null;
        } else {
            focusBitmap = area;
            area = null;
        }

        Bitmap result = Bitmap.createBitmap(filtratedBitmap.getWidth(),
                filtratedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(result);
        canvas.drawBitmap(focusBitmap, new Rect(0, 0, focusBitmap.getWidth(),
                focusBitmap.getHeight()), rect, paint);

        focusBitmap.recycle();
        focusBitmap = null;

        return result;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, r,
                paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static class MemoryInfo {
        public long availMem = 0;
        public long totalMem = 0;
    }

}
