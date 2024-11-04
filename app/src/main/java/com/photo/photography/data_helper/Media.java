package com.photo.photography.data_helper;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.signature.ObjectKey;
import com.drew.lang.GeoLocation;
import com.drew.lang.annotations.NotNull;
import com.photo.photography.collage.model.DataGoogleAdmobAd;
import com.photo.photography.time_line.datas.ListenerTimelineItem;
import com.photo.photography.util.MimeTypeUtil;
import com.photo.photography.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO Calvin: Separate out the logic here

/**
 * Ideally, we should have separate data classes for images, videos & gifs
 * Base class can be Media, and others should extend
 * Try to separate out Database logic and projections from this class
 */
public class Media extends DataGoogleAdmobAd implements ListenerTimelineItem, CursorHandlers, Parcelable {

    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
    private static final String[] sProjection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATE_ADDED
    };
    private static final int CURSOR_POS__ID = getIndex(sProjection, MediaStore.Images.Media._ID);
    private static final int CURSOR_POS_DATA = getIndex(sProjection, MediaStore.Images.Media.DATA);
    private static final int CURSOR_POS_DATE_TAKEN = getIndex(sProjection, MediaStore.Images.Media.DATE_TAKEN);
    private static final int CURSOR_POS_MIME_TYPE = getIndex(sProjection, MediaStore.Images.Media.MIME_TYPE);
    private static final int CURSOR_POS_SIZE = getIndex(sProjection, MediaStore.Images.Media.SIZE);
    private static final int CURSOR_POS_ORIENTATION = getIndex(sProjection, MediaStore.Images.Media.ORIENTATION);
    private static final int CURSOR_POS_DATE_MODIFIED = getIndex(sProjection, MediaStore.Images.Media.DATE_MODIFIED);
    private static final int CURSOR_POS_DATE_ADDED = getIndex(sProjection, MediaStore.Images.Media.DATE_ADDED);
    public boolean nativeAd = false;
    private String path = null;
    private long id = 0;
    private long dateModified = -1;
    private String mimeType = MimeTypeUtil.UNKNOWN_MIME_TYPE;
    private int orientation = 0;
    private String uriString = null;
    private long size = -1;
    private boolean selected = false;

    public Media() {
    }

    public Media(String path, long dateModified) {
        this.path = path;
        this.dateModified = dateModified;
        initDate(path);
        this.mimeType = MimeTypeUtil.getMimeType(path);
    }

    private void initDate(String path) {
        if (dateModified <= 0) {
            File file = new File(path);
            if (file.exists()) {
                BasicFileAttributes attr = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    try {
                        attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                        long createdAt = attr.creationTime().toMillis();
                        dateModified = createdAt;
                    } catch (IOException e) {
                        e.printStackTrace();
                        dateModified = file.lastModified();
                    }
                }
            } else {
                Log.e("Media", "File not exits");
            }
        }
    }

    public Media(String path, long dateModified, long id) {
        this.path = path;
        this.dateModified = dateModified;
        initDate(path);
        this.id = id;
        this.mimeType = MimeTypeUtil.getMimeType(path);
    }

    public Media(File file) {
        this(file.getPath(), file.lastModified());

        BasicFileAttributes attr = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                long createdAt = attr.creationTime().toMillis();
                dateModified = createdAt;
            } catch (IOException e) {
                e.printStackTrace();
                dateModified = file.lastModified();
            }
        }
        this.size = file.length();
        this.mimeType = MimeTypeUtil.getMimeType(path);
    }

    public Media(String path) {
        this(path, -1);
    }

    public Media(Uri mediaUri) {
        this.uriString = mediaUri.toString();
        this.path = null;
        this.mimeType = MimeTypeUtil.getMimeType(uriString);
    }

    public static String parseDate(long milliSeconds) {
        String dateFormat = "dd MMM, yyyy HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(new Date(milliSeconds));
    }

    public Media(@NotNull Cursor cur) {
        this.id = cur.getLong(CURSOR_POS__ID);
        this.path = cur.getString(CURSOR_POS_DATA);
//        this.dateModified = cur.getLong(CURSOR_POS_DATE_MODIFIED) == 0 ? cur.getLong(CURSOR_POS_DATE_TAKEN) : cur.getLong(CURSOR_POS_DATE_MODIFIED) * 1000;

        long dateModify = cur.getLong(CURSOR_POS_DATE_MODIFIED) * 1000;
        String strDateModify = parseDate(cur.getLong(CURSOR_POS_DATE_MODIFIED) * 1000);
        long dateCreated = cur.getLong(CURSOR_POS_DATE_TAKEN);
        String strDateCreated = parseDate(cur.getLong(CURSOR_POS_DATE_TAKEN));
        long dateAdded = cur.getLong(CURSOR_POS_DATE_ADDED) * 1000;
        String strDateAdded = parseDate(cur.getLong(CURSOR_POS_DATE_ADDED) * 1000);

        long finalDateMS = dateAdded > dateCreated ? dateAdded : dateCreated;
        String strFinalDateMS = parseDate(finalDateMS);
        long finalDateMS2 = finalDateMS > dateModify ? finalDateMS : dateModify;
        String strFinalDateMS2 = parseDate(finalDateMS2);
        this.dateModified = finalDateMS2;

        this.mimeType = cur.getString(CURSOR_POS_MIME_TYPE);
        this.size = cur.getLong(CURSOR_POS_SIZE);
        this.orientation = cur.getInt(CURSOR_POS_ORIENTATION);
        Uri contentUri = Uri.fromFile(new File(path));
        if (contentUri == null)
            contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    this.id
            );
        this.uriString = contentUri.toString();
    }

    protected Media(Parcel in) {
        this.id = in.readLong();
        this.path = in.readString();
        this.dateModified = in.readLong();
        this.mimeType = in.readString();
        this.orientation = in.readInt();
        this.uriString = in.readString();
        this.size = in.readLong();
        this.selected = in.readByte() != 0;
        Uri contentUri = Uri.fromFile(new File(path));
        if (contentUri == null)
            contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    this.id
            );
        this.uriString = contentUri.toString();
    }

    public static <T> int getIndex(@NonNull T[] array, @NonNull T element) {
        for (int pos = 0; pos < array.length; pos++) {
            if (array[pos].equals(element)) return pos;
        }
        return -1;
    }

    public static String[] getProjection() {
        return sProjection;
    }

    public boolean getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(boolean nativeAd) {
        this.nativeAd = nativeAd;
    }

    @Override
    public Media handle(Cursor cu) {
        return new Media(cu);
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }


    public boolean isSelected() {
        return selected;
    }

    public boolean setSelected(boolean selected) {
        if (this.selected == selected) return false;
        this.selected = selected;
        return true;
    }

    public boolean toggleSelected() {
        selected = !selected;
        return selected;
    }

    public boolean isGif() {
        if (TextUtils.isEmpty(mimeType))
            return false;
        return mimeType.endsWith("gif");
    }

    public boolean isImage() {
        if (TextUtils.isEmpty(mimeType))
            return false;
        return mimeType.startsWith("image");
    }

    public boolean isVideo() {
        if (TextUtils.isEmpty(mimeType))
            return false;
        return mimeType.startsWith("video");
    }

    public Uri getUri() {
        return uriString != null ? Uri.parse(uriString) : Uri.fromFile(new File(path));
    }

    public void setUri(String uriString) {
        this.uriString = uriString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayPath() {
        return path != null ? path : getUri().getEncodedPath();
    }

    public String getName() {
        return StringUtil.getPhotoNameByPath(path);
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public ObjectKey getSignature() {
        return new ObjectKey(getDateModified() + getPath() + getOrientation());
    }

    public int getOrientation() {
        return orientation;
    }


    @Deprecated
    public Bitmap getBitmap() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return bitmap;
    }

    @Deprecated
    public GeoLocation getGeoLocation() {
        return /*metadata != null ? metadata.getLocation() :*/ null;
    }

    @Deprecated
    public boolean setOrientation(final int orientation) {
        this.orientation = orientation;

        new Thread(new Runnable() {
            public void run() {
                int exifOrientation = -1;
                try {
                    ExifInterface exif = new ExifInterface(path);
                    switch (orientation) {
                        case 90:
                            exifOrientation = ExifInterface.ORIENTATION_ROTATE_90;
                            break;
                        case 180:
                            exifOrientation = ExifInterface.ORIENTATION_ROTATE_180;
                            break;
                        case 270:
                            exifOrientation = ExifInterface.ORIENTATION_ROTATE_270;
                            break;
                        case 0:
                            exifOrientation = ExifInterface.ORIENTATION_NORMAL;
                            break;
                    }
                    if (exifOrientation != -1) {
                        exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(exifOrientation));
                        exif.saveAttributes();
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Media)
            return getPath().equals(((Media) obj).getPath());

        return super.equals(obj);
    }

    //</editor-fold>

    @Deprecated
    private long getDateTaken() {
        return 1;
    }

    @Deprecated
    public boolean fixDate() {
        long newDate = getDateTaken();
        if (newDate != -1) {
            File f = new File(path);
            if (f.setLastModified(newDate)) {
                dateModified = newDate;
                return true;
            }
        }
        return false;
    }

    public File getFile() {
        if (path != null) {
            File file = new File(path);
            if (file.exists()) return file;
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.path);
        dest.writeLong(this.dateModified);
        dest.writeString(this.mimeType);
        dest.writeInt(this.orientation);
        dest.writeString(this.uriString);
        dest.writeLong(this.size);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    @Override
    public int getTimelineType() {
        return TYPE_MEDIA;
    }
}