package com.photo.photography.data_helper.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.AlbumsHelper;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.StorageHelpers;
import com.photo.photography.data_helper.filters_mode.FoldersFileFilters;
import com.photo.photography.data_helper.filters_mode.ImageFileFilters;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;
import com.photo.photography.util.preferences.Prefs;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class CPHelpers {

    public static Observable<Album> getAlbums(Context context, boolean hidden, ArrayList<String> excluded) {
        return hidden ? getHiddenAlbums(context, excluded) : getAlbums(context);
    }

    private static Observable<Album> getAlbums(Context context) {
        return Observable.create(subscriber -> {
            ArrayList<String> albumsNames = new ArrayList<>();

            // which image properties are we querying
            String[] projection = new String[]{
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Files.FileColumns.PARENT,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.BUCKET_ID
            };

            Uri images = MediaStore.Files.getContentUri("external");
            String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + (AlbumsHelper.getSortingOrder().isAscending() ? " " : " DESC");

            try {
                Cursor cur = context.getContentResolver().query(images, projection, // Which columns to return
                        MediaStore.Files.FileColumns.MEDIA_TYPE + " = ? or " + MediaStore.Files.FileColumns.MEDIA_TYPE + " = ? ",
                        new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)}, // Selection arguments (none)
                        BUCKET_ORDER_BY // Ordering
                );

                if (cur != null && cur.getCount() > 0) {
                    Log.i("DeviceImageManager", " query Cursor count=" + cur.getCount());

                    if (cur.moveToFirst()) {
                        do {
                            @SuppressLint("Range") String bucketName = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)); // AlbumName
                            @SuppressLint("Range") String data = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA)); // uri
                            @SuppressLint("Range") String imageId = cur.getString(cur.getColumnIndex(MediaStore.Images.Media._ID)); // imageId

                            if (!albumsNames.contains(bucketName)) {
                                Log.i("DeviceImageManager", "A new album was created => " + bucketName);
                                albumsNames.add(bucketName);
                                Album asd = new Album(cur);
                                if (TextUtils.isEmpty(asd.getName())) {
                                    asd.setName("InternalStorage");
                                }
                                asd.setLastMedia(new Media(data, -1, Long.parseLong(imageId)));
                                asd.setCount(getCount(context, asd));
                                subscriber.onNext(asd);
                            }

                        } while (cur.moveToNext());
                    }
                    subscriber.onComplete();
                    cur.close();
                }else {
                    Album asd = new Album();
                    subscriber.onNext(asd);
                    subscriber.onComplete();
                    cur.close();
                }
            } catch (Exception err) {
                subscriber.onError(err);
            }
        });
    }

    private static int getCount(@NonNull final Context context, Album asd) {
        Querys.Builder query = new Querys.Builder()
                .uri(MediaStore.Files.getContentUri("external"))
                .projection(Media.getProjection())
                .sort(SortingModes.DATE_DAY.getMediaColumn())
                .ascending(SortingOrders.ASCENDING.isAscending());

        if (Prefs.showVideos()) {
            query.selection(String.format("(%s=? or %s=?) and %s=?",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT));
            query.args(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                    asd.getId());
        } else {
            query.selection(String.format("%s=? and %s=?",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT));
            query.args(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, asd.getId());
        }
        try (final Cursor cursor = query.build().getCursor(context.getContentResolver())) {
            return ((cursor == null) || (!cursor.moveToFirst())) ? 0 : cursor.getCount();
        }
    }

    private static Observable<Album> getHiddenAlbums(Context context, ArrayList<String> excludedAlbums) {

        boolean includeVideo = Prefs.showVideos();
        return Observable.create(subscriber -> {
            try {

                ArrayList<String> lastHidden = Hawk.get("h", new ArrayList<>());
                for (String s : lastHidden)
                    checkAndAddFolder(new File(s), subscriber, includeVideo);

                lastHidden.addAll(excludedAlbums);

                for (File storage : StorageHelpers.getStorageRoots(context))
                    fetchRecursivelyHiddenFolder(storage, subscriber, lastHidden, includeVideo);
                subscriber.onComplete();
            } catch (Exception err) {
                subscriber.onError(err);
            }
        });
    }

    private static void fetchRecursivelyHiddenFolder(File dir, ObservableEmitter<Album> emitter, ArrayList<String> excludedAlbums, boolean includeVideo) {
        if (!isExcluded(dir.getPath(), excludedAlbums)) {
            File[] folders = dir.listFiles(new FoldersFileFilters());
            if (folders != null) {
                for (File temp : folders) {
                    File nomedia = new File(temp, ".nomedia");
                    if (!isExcluded(temp.getPath(), excludedAlbums) && (nomedia.exists() || temp.isHidden()))
                        checkAndAddFolder(temp, emitter, includeVideo);

                    fetchRecursivelyHiddenFolder(temp, emitter, excludedAlbums, includeVideo);
                }
            }
        }
    }

    private static void checkAndAddFolder(File dir, ObservableEmitter<Album> emitter, boolean includeVideo) {
        File[] files = dir.listFiles(new ImageFileFilters(includeVideo));
        if (files != null && files.length > 0) {
            //valid folder

            long lastMod = Long.MIN_VALUE;
            File choice = null;
            for (File file : files) {
                if (file.lastModified() > lastMod) {
                    choice = file;
                    lastMod = file.lastModified();
                }
            }
            if (choice != null) {
                Album asd = new Album(dir.getAbsolutePath(), dir.getName(), files.length, lastMod);
                asd.setLastMedia(new Media(choice.getAbsolutePath()));
                emitter.onNext(asd);
            }
        }
    }

    private static boolean isExcluded(String path, ArrayList<String> excludedAlbums) {
        for (String s : excludedAlbums) if (path.startsWith(s)) return true;
        return false;
    }

    //region Media
    public static Observable<Media> getMedia(Context context, Album album) {
        if (album.getId() == -1)
            return getMediaFromStorage(context, album);
        else if (album.getId() == Album.ALL_MEDIA_ALBUM_ID)
            return getAllMediaFromMediaStore(context, album.settings.getSortingMode(), album.settings.getSortingOrder());
        else
            return getMediaFromMediaStore(context, album, album.settings.getSortingMode(), album.settings.getSortingOrder());
    }

    private static Observable<Media> getAllMediaFromMediaStore(Context context, SortingModes sortingMode, SortingOrders sortingOrder) {
        Querys.Builder query = new Querys.Builder()
                .uri(MediaStore.Files.getContentUri("external"))
                .projection(Media.getProjection())
                .sort(sortingMode.getMediaColumn())
                .ascending(sortingOrder.isAscending());

        if (Prefs.showVideos()) {
            query.selection(String.format("(%s=? or %s=?)",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE));
            query.args(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            query.selection(String.format("%s=?", MediaStore.Files.FileColumns.MEDIA_TYPE));
            query.args(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        }

        return QueryUtil.query(query.build(), context.getContentResolver(), new Media());
    }

    private static Observable<Media> getMediaFromStorage(Context context, Album album) {

        return Observable.create(subscriber -> {
            File dir = new File(album.getPath());
            File[] files = dir.listFiles(new ImageFileFilters(Prefs.showVideos()));
            try {
                if (files != null && files.length > 0)
                    for (File file : files)
                        subscriber.onNext(new Media(file));
                subscriber.onComplete();

            } catch (Exception err) {
                subscriber.onError(err);
            }
        });

    }

    private static Observable<Media> getMediaFromMediaStore(Context context, Album album, SortingModes sortingMode, SortingOrders sortingOrder) {

        Querys.Builder query = new Querys.Builder()
                .uri(MediaStore.Files.getContentUri("external"))
                .projection(Media.getProjection())
                .sort(sortingMode.getMediaColumn())
                .ascending(sortingOrder.isAscending());

        if (Prefs.showVideos()) {
            query.selection(String.format("(%s=? or %s=?) and %s=?",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT));
            query.args(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                    album.getId());
        } else {
            query.selection(String.format("%s=? and %s=?",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.PARENT));
            query.args(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, album.getId());
        }

        return QueryUtil.query(query.build(), context.getContentResolver(), Media::new);
    }

    //endregion
}