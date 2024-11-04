package com.photo.photography.collage.screen.frag;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.photo.photography.R;
import com.photo.photography.collage.repeater.RepeaterGalleryAlbumImage;
import com.photo.photography.collage.db.table.TableItemPackage;
import com.photo.photography.collage.db.table.TableShade;
import com.photo.photography.collage.model.DataImageItem;
import com.photo.photography.collage.model.DataShadeInfo;
import com.photo.photography.collage.util.PhotoUtil;
import com.photo.photography.collage.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sira on 4/6/2018.
 */
public class FragItemPackageDetail extends FragBase {
    public static final String ASSET_BACKGROUND_FOLDER = "background";
    public static final String ASSET_STICKER_FOLDER = "sticker";
    private static final String EXTRACTED_IMAGE_FOLDER_PATH = Util.BIG_D_FOLDER.concat("/assets");
    private final List<DataImageItem> mImageList = new ArrayList<DataImageItem>();
    private RepeaterGalleryAlbumImage mAdapter;
    private String mAssetImageFolder = ASSET_BACKGROUND_FOLDER;
    private GridView mGridView;
    private View mProgressView;

    private String mPackageName;
    private long mPackageId;
    private String mPackageType;
    private String mPackageFolder;
    private FragGalleryAlbumImage.OnSelectImageListener mListener;

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            final DataImageItem img = mImageList.get(position);
            String path = img.imagePath;
            File file;
            if (img.imagePath.startsWith(PhotoUtil.ASSET_PREFIX)) {
                path = path.substring(PhotoUtil.ASSET_PREFIX.length());
                file = Util.copyFileFromAsset(getActivity(), EXTRACTED_IMAGE_FOLDER_PATH, path, false);
            } else {
                file = new File(path);
            }

            if (file != null) {
                if (mListener != null) {
                    mListener.onSelectImage(file.getAbsolutePath());
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof FragGalleryAlbumImage.OnSelectImageListener) {
            mListener = (FragGalleryAlbumImage.OnSelectImageListener) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_item_package_detail, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mProgressView = view.findViewById(R.id.progressBar);

        mPackageId = getArguments().getLong(FragDownloadedPackage.EXTRA_PACKAGE_ID, 0);
        mPackageName = getArguments().getString(FragDownloadedPackage.EXTRA_PACKAGE_NAME);
        mPackageType = getArguments().getString(FragDownloadedPackage.EXTRA_PACKAGE_TYPE);
        mPackageFolder = getArguments().getString(FragDownloadedPackage.EXTRA_PACKAGE_FOLDER);

        if (mPackageName != null && mPackageName.length() > 0) {
            if (FragDownloadedPackage.DEFAULT_BACKGROUND_PACKAGE_INT_ID == mPackageId) {
                mAssetImageFolder = ASSET_BACKGROUND_FOLDER;
            } else {
                mAssetImageFolder = ASSET_STICKER_FOLDER;
            }
            setTitle(mPackageName);
        }
        loadImages();
        return view;
    }

    private void loadImages() {
        AsyncTask<Void, Void, List<DataImageItem>> task = new AsyncTask<Void, Void, List<DataImageItem>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressView.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<DataImageItem> doInBackground(Void... params) {
                if (FragDownloadedPackage.DEFAULT_BACKGROUND_PACKAGE_INT_ID == mPackageId
                        || FragDownloadedPackage.DEFAULT_STICKER_PACKAGE_INT_ID == mPackageId) {
                    return loadAssetPhotos(mAssetImageFolder);
                } else {
                    List<DataImageItem> imageItems = new ArrayList<>();
                    TableShade table = new TableShade(getActivity());
                    List<DataShadeInfo> frameInfos = table.getRows(mPackageId, mPackageType);
                    if (mPackageFolder != null && mPackageFolder.length() > 0) {
                        String baseFolder = Util.FRAME_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        if (TableItemPackage.BACKGROUND_TYPE.equals(mPackageType)) {
                            baseFolder = Util.BACKGROUND_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        } else if (TableItemPackage.STICKER_TYPE.equals(mPackageType)) {
                            baseFolder = Util.STICKER_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        } else if (TableItemPackage.CROP_TYPE.equals(mPackageType)) {
                            baseFolder = Util.CROP_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        } else if (TableItemPackage.FRAME_TYPE.equals(mPackageType)) {
                            baseFolder = Util.FRAME_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        }

                        for (DataShadeInfo info : frameInfos) {
                            info.setForeground(baseFolder.concat(info.getForeground()));
                            info.setThumbnail(baseFolder.concat(info.getThumbnail()));
                            DataImageItem item = new DataImageItem();
                            item.imagePath = info.getForeground();
                            item.thumbnailPath = info.getThumbnail();
                            item.isSelected = false;
                            item.isSticker = TableItemPackage.STICKER_TYPE.equals(mPackageType);

                            imageItems.add(item);
                        }
                    }

                    return imageItems;
                }
            }

            @Override
            protected void onPostExecute(List<DataImageItem> imageItems) {
                super.onPostExecute(imageItems);
                if (!already()) {
                    return;
                }

                mProgressView.setVisibility(View.GONE);
                mImageList.clear();
                mImageList.addAll(imageItems);
                final List<String> imagePaths = new ArrayList<>();
                for (DataImageItem item : imageItems)
                    imagePaths.add(item.imagePath);
//                    imagePaths.add(item.thumbnailPath);
                mAdapter = new RepeaterGalleryAlbumImage(getActivity(), imagePaths);
                mAdapter.setImageFitCenter(TableItemPackage.STICKER_TYPE.equals(mPackageType));
                mGridView.setOnItemClickListener(mItemClickListener);
                mGridView.setAdapter(mAdapter);
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private List<DataImageItem> loadAssetPhotos(String assetImageFolder) {
        List<DataImageItem> result = new ArrayList<>();
        if (assetImageFolder != null && assetImageFolder.length() > 0) {
            AssetManager am = mActivity.getAssets();
            try {
                String[] images = am.list(assetImageFolder);
                if (images != null) {
                    for (String str : images) {
                        DataImageItem item = new DataImageItem();
                        item.imagePath = PhotoUtil.ASSET_PREFIX.concat(assetImageFolder).concat("/").concat(str);
                        item.thumbnailPath = item.imagePath;
                        item.isSelected = false;
                        if (assetImageFolder.equals(ASSET_STICKER_FOLDER)) {
                            item.isSticker = true;
                        }
                        result.add(item);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
