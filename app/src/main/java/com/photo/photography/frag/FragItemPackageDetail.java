package com.photo.photography.frag;

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
import com.photo.photography.repeater.RepeaterGalleryAlbumImage;
import com.photo.photography.db.tables.ItemPackageTables;
import com.photo.photography.db.tables.ShadeTables;
import com.photo.photography.frag.collage.FragBaseCollage;
import com.photo.photography.models.ImageItemModel;
import com.photo.photography.models.ShadeInfo;
import com.photo.photography.util.utils.PhotoUtil;
import com.photo.photography.util.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragItemPackageDetail extends FragBaseCollage {
    public static final String ASSET_BACKGROUND_FOLDER = "background";
    public static final String ASSET_STICKER_FOLDER = "sticker";
    private static final String EXTRACTED_IMAGE_FOLDER_PATH = Util.BIG_D_FOLDER.concat("/assets");
    private final List<ImageItemModel> mImageList = new ArrayList<ImageItemModel>();
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
            final ImageItemModel img = mImageList.get(position);
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
            //  setTitle(mPackageName);
        }
        loadImages();
        return view;
    }

    private void loadImages() {
        AsyncTask<Void, Void, List<ImageItemModel>> task = new AsyncTask<Void, Void, List<ImageItemModel>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressView.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<ImageItemModel> doInBackground(Void... params) {
                if (FragDownloadedPackage.DEFAULT_BACKGROUND_PACKAGE_INT_ID == mPackageId
                        || FragDownloadedPackage.DEFAULT_STICKER_PACKAGE_INT_ID == mPackageId) {
                    return loadAssetPhotos(mAssetImageFolder);
                } else {
                    List<ImageItemModel> imageItems = new ArrayList<>();
                    ShadeTables table = new ShadeTables(getActivity());
                    List<ShadeInfo> frameInfos = table.getRows(mPackageId, mPackageType);
                    if (mPackageFolder != null && mPackageFolder.length() > 0) {
                        String baseFolder = Util.FRAME_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        if (ItemPackageTables.BACKGROUND_TYPE.equals(mPackageType)) {
                            baseFolder = Util.BACKGROUND_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        } else if (ItemPackageTables.STICKER_TYPE.equals(mPackageType)) {
                            baseFolder = Util.STICKER_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        } else if (ItemPackageTables.CROP_TYPE.equals(mPackageType)) {
                            baseFolder = Util.CROP_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        } else if (ItemPackageTables.FRAME_TYPE.equals(mPackageType)) {
                            baseFolder = Util.FRAME_FOLDER.concat("/").concat(mPackageFolder).concat("/");
                        }

                        for (ShadeInfo info : frameInfos) {
                            info.setForeground(baseFolder.concat(info.getForeground()));
                            info.setThumbnail(baseFolder.concat(info.getThumbnail()));
                            ImageItemModel item = new ImageItemModel();
                            item.imagePath = info.getForeground();
                            item.thumbnailPath = info.getThumbnail();
                            item.isSelected = false;
                            item.isSticker = ItemPackageTables.STICKER_TYPE.equals(mPackageType);

                            imageItems.add(item);
                        }
                    }

                    return imageItems;
                }
            }

            @Override
            protected void onPostExecute(List<ImageItemModel> imageItems) {
                super.onPostExecute(imageItems);
                /*if (!already()) {
                    return;
                }*/

                mProgressView.setVisibility(View.GONE);
                mImageList.clear();
                mImageList.addAll(imageItems);
                final List<String> imagePaths = new ArrayList<>();
                for (ImageItemModel item : imageItems)
                    imagePaths.add(item.imagePath);
//                    imagePaths.add(item.thumbnailPath);
                mAdapter = new RepeaterGalleryAlbumImage(getActivity(), imagePaths);
                mAdapter.setImageFitCenter(ItemPackageTables.STICKER_TYPE.equals(mPackageType));
                mGridView.setOnItemClickListener(mItemClickListener);
                mGridView.setAdapter(mAdapter);
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private List<ImageItemModel> loadAssetPhotos(String assetImageFolder) {
        List<ImageItemModel> result = new ArrayList<>();
        if (assetImageFolder != null && assetImageFolder.length() > 0) {
            AssetManager am = mActivity.getAssets();
            try {
                String[] images = am.list(assetImageFolder);
                if (images != null) {
                    for (String str : images) {
                        ImageItemModel item = new ImageItemModel();
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
