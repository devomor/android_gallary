package com.photo.photography.data_helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.photo.photography.collage.model.DataGoogleAdmobAd;
import com.photo.photography.data_helper.filters_mode.FilterModes;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;
import com.photo.photography.util.StringUtil;

import java.io.File;
import java.util.ArrayList;

public class Album extends DataGoogleAdmobAd implements CursorHandlers, Parcelable {

    public static final long ALL_MEDIA_ALBUM_ID = 8000;
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {

        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
    public AlbumSetting settings = null;
    private String name, path;
    private long id = -1, dateModified;
    private int count = -1;
    private boolean selected = false;
    private Media lastMedia = null;
    private ArrayList<Media> mediaArrayList = null;

    public Album() {}

    public Album(String path, String name) {
        this.name = name;
        this.path = path;
    }

    public Album(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public Album(String path, String name, long id, int count, long dateModified) {
        this(path, name);
        this.count = count;
        this.id = id;
        this.dateModified = dateModified;
    }

    public Album(String path, String name, int count, long dateModified) {
        this(path, name, -1, count, dateModified);
    }

    @SuppressLint("Range")
    public Album(Cursor cur) {
        this(cur.getColumnIndex(MediaStore.Images.Media.DATA) != -1 ? StringUtil.getBucketPathByImagePath(cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA))) : "",
                cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME) != -1 ? cur.getString(cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)) : "",
                cur.getColumnIndex(MediaStore.Files.FileColumns.PARENT) != -1 ? cur.getLong(cur.getColumnIndex(MediaStore.Files.FileColumns.PARENT)) : 0,
                0/*cur.getInt(2)*/,
                0/*cur.getLong(4)*/);
        setLastMedia(new Media(cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA))));
    }

    @Deprecated
    public Album(Context context, String path, long id, String name, int count) {
        this(path, name, id, count, 0);
        settings = AlbumSetting.getDefaults();
    }

    protected Album(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.id = in.readLong();
        this.dateModified = in.readLong();
        this.count = in.readInt();
        this.selected = in.readByte() != 0;
        this.settings = (AlbumSetting) in.readSerializable();
        this.lastMedia = in.readParcelable(Media.class.getClassLoader());
        this.mediaArrayList = in.readArrayList(Media.class.getClassLoader());
    }

    public static String[] getProjection() {
        return new String[]{
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                "count(*)",
                MediaStore.Images.Media.DATA,
                "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")"
        };
    }

    public static Album getEmptyAlbum() {
        Album album = new Album(null, null);
        album.settings = AlbumSetting.getDefaults();
        return album;
    }

    public static Album getAllMediaAlbum() {
        Album album = new Album("", ALL_MEDIA_ALBUM_ID);
        album.settings = AlbumSetting.getDefaults();
        return album;
    }

    public static Album getLocationMediaAlbum(String name, ArrayList<Media> mediaArrayList) {
        Album album = new Album("", ALL_MEDIA_ALBUM_ID);
        album.name = name;
        album.mediaArrayList = mediaArrayList;
        album.settings = AlbumSetting.getDefaults();
        return album;
    }

    static Album withPath(String path) {
        Album emptyAlbum = getEmptyAlbum();
        emptyAlbum.path = path;
        return emptyAlbum;
    }

    @Override
    public Album handle(Cursor cur) {
        return new Album(cur);
    }

    public Album withSettings(AlbumSetting settings) {
        this.settings = settings;
        return this;
    }

    //region Album Properties Getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public Media getCover() {
        if (hasCover())
            return new Media(settings.coverPath);
        if (lastMedia != null)
            return lastMedia;
        return new Media();
    }

    public void setCover(String path) {
        settings.coverPath = path;
    }

    public void setLastMedia(Media lastMedia) {
        this.lastMedia = lastMedia;
    }

    public long getId() {
        return this.id;
    }

    public boolean isHidden() {
        return new File(path, ".nomedia").exists();
    }

    public boolean isPinned() {
        if(settings == null)
            return false;
        return settings.pinned;
    }
    //endregion

    public boolean hasCover() {
        return settings.coverPath != null;
    }

    //region Album Properties Setters

    public FilterModes filterMode() {
        return settings != null ? settings.filterMode : FilterModes.ALL;
    }

    public void setFilterMode(FilterModes newMode) {
        settings.filterMode = newMode;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", id=" + id +
                ", count=" + count +
                '}';
    }

//    public ArrayList<String> getParentsFolders() {
//        ArrayList<String> result = new ArrayList<>();
//
//        File f = new File(getPath());
//        while (f != null && f.canRead()) {
//            result.add(f.getPath());
//            f = f.getParentFile();
//        }
//        return result;
//    }

    public boolean setSelected(boolean selected) {
        if (this.selected == selected)
            return false;
        this.selected = selected;
        return true;
    }

    public boolean toggleSelected() {
        selected = !selected;
        return selected;
    }

    public void removeCoverAlbum() {
        settings.coverPath = null;
    }


    public void setSortingMode(SortingModes column) {
        settings.sortingMode = column.getValue();
    }

    public void setSortingOrder(SortingOrders sortingOrder) {
        settings.sortingOrder = sortingOrder.getValue();
    }

//    public boolean togglePinAlbum() {
//        settings.pinned = !settings.pinned;
//        return settings.pinned;
//    }

//    @Deprecated
//    public int moveSelectedMedia(Context context, String targetDir) {
//		/*int n = 0;
//		try
//		{
//			for (int i = 0; i < selectedMedia.size(); i++) {
//
//				if (moveMedia(context, selectedMedia.get(i).getPath(), targetDir)) {
//					String from = selectedMedia.get(i).getPath();
//					scanFile(context, new String[]{ from, StringUtils.getPhotoPathMoved(selectedMedia.get(i).getPath(), targetDir) },
//							new MediaScannerConnection.OnScanCompletedListener() {
//								@Override
//								public void onScanCompleted(String s, Uri uri) {
//									Log.d("scanFile", "onScanCompleted: " + s);
//								}
//							});
//					media.remove(selectedMedia.get(i));
//					n++;
//				}
//			}
//		} catch (Exception e) { e.printStackTrace(); }
//		setCount(media.size());
//		return n;*/
//        return -1;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Album) {
            return path.equals(((Album) obj).getPath());
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.id);
        dest.writeLong(this.dateModified);
        dest.writeInt(this.count);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.settings);
        dest.writeParcelable(this.lastMedia, flags);
        dest.writeList(this.mediaArrayList);
    }

    public boolean nativeAd = false;
    public boolean isNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(boolean nativeAd) {
        this.nativeAd = nativeAd;
    }

    public ArrayList<Media> getMediaArrayList() {
        return mediaArrayList;
    }

    public void setMediaArrayList(ArrayList<Media> mediaArrayList) {
        this.mediaArrayList = mediaArrayList;
    }

}
