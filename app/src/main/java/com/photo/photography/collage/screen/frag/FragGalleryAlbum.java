package com.photo.photography.collage.screen.frag;

import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.collage.repeater.RepeaterGalleryAlbum;
import com.photo.photography.collage.helper.HelperALog;
import com.photo.photography.collage.model.DataGalleryAlbum;
import com.photo.photography.collage.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Sira on 3/26/2018.
 */
public class FragGalleryAlbum extends FragBase {
    private GridView mListView;
    private View mProgressBar;
    private ArrayList<DataGalleryAlbum> mAlbums;
    private RepeaterGalleryAlbum mAdapter;
    private Parcelable mListViewState;

    @Override
    public void onPause() {
        // Save ListView mListViewState @ onPause
        if (mListView != null)
            mListViewState = mListView.onSaveInstanceState();
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gallery_album, container, false);
        mListView = (GridView) view.findViewById(R.id.listView);
        mProgressBar = view.findViewById(R.id.progressBar);

        AsyncTask<Void, Void, ArrayList<DataGalleryAlbum>> task = new AsyncTask<Void, Void, ArrayList<DataGalleryAlbum>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected ArrayList<DataGalleryAlbum> doInBackground(Void... params) {
                ArrayList<DataGalleryAlbum> result = loadPhotoAlbums();
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<DataGalleryAlbum> galleryAlbums) {
                super.onPostExecute(galleryAlbums);
                if (already()) {
                    mProgressBar.setVisibility(View.GONE);
                    mAlbums = galleryAlbums;
                    mAdapter = new RepeaterGalleryAlbum(getActivity(), mAlbums, new RepeaterGalleryAlbum.OnGalleryAlbumClickListener() {
                        @Override
                        public void onGalleryAlbumClick(DataGalleryAlbum album) {
                            ActBase activity = (ActBase) getActivity();
                            Bundle data = new Bundle();
                            data.putStringArrayList(FragGalleryAlbumImage.ALBUM_IMAGE_EXTRA, (ArrayList<String>) album.getImageList());
                            data.putString(FragGalleryAlbumImage.ALBUM_NAME_EXTRA, album.getAlbumName());
                            FragGalleryAlbumImage fragment = new FragGalleryAlbumImage();
                            fragment.setArguments(data);
                            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                            ft.replace(R.id.frame_container, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    });
                    mListView.setAdapter(mAdapter);
                    // Restore previous mListViewState (including selected item index and scroll position)
                    if (mListViewState != null) {
                        mListView.onRestoreInstanceState(mListViewState);
                    }
                }
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        setTitle(R.string.gallery_albums);
        return view;
    }

    public ArrayList<DataGalleryAlbum> loadPhotoAlbums() {
        final HashMap<Long, DataGalleryAlbum> map = new HashMap<>();
        // which image properties are we querying
        final String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

// Get the base URI for the People table in the Contacts content provider.
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = null;
        try {
// Make the query.
            ContentResolver cr = getActivity().getContentResolver();
            cur = cr.query(images,
                    projection, // Which columns to return
                    "",         // Which rows to return (all rows)
                    null,       // Selection arguments (none)
                    ""          // Ordering
            );

            HelperALog.i("ListingImages", " query count=" + cur.getCount());

            if (cur != null && cur.moveToFirst()) {
                do {
                    // Get the field values
                    String bucketName = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    long date = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                    String imagePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                    long bucketId = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                    // Do something with the values.
                    DataGalleryAlbum album = map.get(bucketId);
                    if (album == null) {
                        album = new DataGalleryAlbum(bucketId, bucketName);
                        album.setTakenDate(DateTimeUtil.toUTCDateTimeString(new Date(date)));
                        album.getImageList().add(imagePath);
                        map.put(bucketId, album);
                    } else {
                        album.getImageList().add(imagePath);
                    }
                } while (cur.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cur != null)
                cur.close();
        }

        Collection<DataGalleryAlbum> albums = map.values();
        ArrayList<DataGalleryAlbum> result = new ArrayList<>();
        result.addAll(albums);
        return result;
    }

    public void loadAlbumNames() {
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA
        };

        String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
        Cursor imageCursor = getActivity().getContentResolver().query(images,
                projection, // Which columns to return
                BUCKET_GROUP_BY,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                BUCKET_ORDER_BY        // Ordering
        );

        ArrayList<String> imageUrls = new ArrayList<String>();
        ArrayList<String> imageBuckets = new ArrayList<String>();
        for (int i = 0; i < imageCursor.getCount(); i++) {
            imageCursor.moveToPosition(i);
            int bucketColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            String bucketDisplayName = imageCursor.getString(bucketColumnIndex);
            imageBuckets.add(bucketDisplayName);
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imageCursor.getString(dataColumnIndex));
        }
        imageCursor.close();
        for (int idx = 0; idx < imageBuckets.size(); idx++) {
            HelperALog.d("SelectPhotoActivity", "loadAlbums, name=" + imageBuckets.get(idx) + ", imageUrl=" + imageUrls.get(idx));
        }
    }

}
