package com.photo.photography.collage.screen.frag;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.daimajia.swipe.util.Attributes;
import com.photo.photography.R;
import com.photo.photography.collage.repeater.RepeaterDownloadedPackage;
import com.photo.photography.collage.db.table.TableItemPackage;
import com.photo.photography.collage.callback.CallbackOnDownloadedPackageClick;
import com.photo.photography.collage.model.DataItemPackageInfo;
import com.photo.photography.collage.util.DateTimeUtil;
import com.photo.photography.collage.util.PhotoUtil;
import com.photo.photography.collage.util.editor_util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sira on 3/26/2018.
 */
public class FragDownloadedPackage extends FragBase implements CallbackOnDownloadedPackageClick {
    public static final String EXTRA_PACKAGE_TYPE = "packageType";
    public static final String EXTRA_PACKAGE_NAME = "packageName";
    public static final String EXTRA_PACKAGE_ID = "packageId";
    public static final String EXTRA_PACKAGE_FOLDER = "packageFolder";
    public static final String DEFAULT_BACKGROUND_PACKAGE_TEXT_ID = "default_background_package";
    public static final String DEFAULT_STICKER_PACKAGE_TEXT_ID = "default_sticker_package";
    public static final long DEFAULT_BACKGROUND_PACKAGE_INT_ID = -100;
    public static final long DEFAULT_STICKER_PACKAGE_INT_ID = -99;
    private static final String DEFAULT_BACKGROUND_THUMBNAIL = PhotoUtil.ASSET_PREFIX.concat("background/bg_1.jpg");
    private static final String DEFAULT_STICKER_THUMBNAIL = PhotoUtil.ASSET_PREFIX.concat("sticker/st_1.png");
    private static final String PREF_NAME = "downloadedPackagePref";
    private static final String OPEN_COUNT_KEY = "openCount";
    private final List<DataItemPackageInfo> mItemPackageInfos = new ArrayList<>();
    private ListView mListView;
    private View mProgressView;
    private String mPackageType = TableItemPackage.BACKGROUND_TYPE;
    private RepeaterDownloadedPackage mPackageAdapter;
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
        View view = inflater.inflate(R.layout.frag_downloaded_package, container, false);
        mPackageType = getArguments().getString(EXTRA_PACKAGE_TYPE);
        if (mPackageType == null) {
            mPackageType = TableItemPackage.BACKGROUND_TYPE;
        }
        if (TableItemPackage.BACKGROUND_TYPE.equals(mPackageType)) {
            setTitle(R.string.background);
        } else {
            setTitle(R.string.sticker);
        }

        mListView = (ListView) view.findViewById(R.id.listView);
        mProgressView = view.findViewById(R.id.progressBar);
        //show guide
        final View guideView = view.findViewById(R.id.guideView);
        SharedPreferences pref = mActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int count = pref.getInt(OPEN_COUNT_KEY, 0);
        if (count > 3) {
            guideView.setVisibility(View.GONE);
        } else {
            count++;
            pref.edit().putInt(OPEN_COUNT_KEY, count).commit();
        }

        loadData();

        return view;
    }

    private void loadData() {
        AsyncTask<Void, Void, List<DataItemPackageInfo>> task = new AsyncTask<Void, Void, List<DataItemPackageInfo>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressView.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<DataItemPackageInfo> doInBackground(Void... params) {
                TableItemPackage table = new TableItemPackage(mActivity);
                List<DataItemPackageInfo> result = table.getRows(mPackageType);
                for (DataItemPackageInfo info : result) {
//                    PackageAction.setAbsoluteBackgroundPath(info);
                }
                //add default package
                DataItemPackageInfo defaultPackage = new DataItemPackageInfo();
                if (TableItemPackage.BACKGROUND_TYPE.equalsIgnoreCase(mPackageType)) {
                    defaultPackage.setIdString(DEFAULT_BACKGROUND_PACKAGE_TEXT_ID);
                    defaultPackage.setTitle(getString(R.string.default_backgrounds));
                    defaultPackage.setThumbnail(DEFAULT_BACKGROUND_THUMBNAIL);
                    defaultPackage.setId(DEFAULT_BACKGROUND_PACKAGE_INT_ID);
                    defaultPackage.setType(mPackageType);
                } else {
                    defaultPackage.setIdString(DEFAULT_STICKER_PACKAGE_TEXT_ID);
                    defaultPackage.setTitle(getString(R.string.default_stickers));
                    defaultPackage.setThumbnail(DEFAULT_STICKER_THUMBNAIL);
                    defaultPackage.setId(DEFAULT_STICKER_PACKAGE_INT_ID);
                    defaultPackage.setType(mPackageType);
                }
                defaultPackage.setLastModified(DateTimeUtil.getCurrentDateTime());
                result.add(0, defaultPackage);
                return result;
            }

            @Override
            protected void onPostExecute(List<DataItemPackageInfo> result) {
                super.onPostExecute(result);
                if (!already()) {
                    return;
                }

                mProgressView.setVisibility(View.GONE);
                if (result != null) {
                    mItemPackageInfos.clear();
                    mItemPackageInfos.addAll(result);
                    mPackageAdapter = new RepeaterDownloadedPackage(getActivity(), mItemPackageInfos, FragDownloadedPackage.this);
                    mPackageAdapter.setMode(Attributes.Mode.Single);
                    mListView.setAdapter(mPackageAdapter);
                    if (mListViewState != null) {
                        mListView.onRestoreInstanceState(mListViewState);
                    }
                }
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onDeleteButtonClick(final int position, final DataItemPackageInfo info) {
        if (info.getIdString().equals(DEFAULT_STICKER_PACKAGE_TEXT_ID) || info.getIdString().equals(DEFAULT_BACKGROUND_PACKAGE_TEXT_ID)) {
            Toast.makeText(mActivity, getString(R.string.warning_uninstall_default_package), Toast.LENGTH_SHORT).show();
        } else {
            DialogUtil.showCoolConfirmDialog(getActivity(), R.string.app_name_new,
                    R.string.photo_editor_confirm_uninstall, new DialogUtil.ConfirmDialogOnClickListener() {

                        @Override
                        public void onOKButtonOnClick() {
//                            StoreUtils.uninstallItemPackage(getActivity(), info);
//                            mPackageAdapter.remove(info);
//                            //send statistics
//                            //log
//                            if (info.getTitle() != null && info.getType() != null) {
//                                Bundle bundle = new Bundle();
//                                String msg = "uninstall/".concat(info.getTitle()).concat("-").concat(info.getType());
//                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, msg);
//                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
//                            }
                        }

                        @Override
                        public void onCancelButtonOnClick() {

                        }
                    });
        }
    }

    @Override
    public void onItemClick(int position, DataItemPackageInfo info) {
        Bundle data = new Bundle();
        data.putLong(EXTRA_PACKAGE_ID, info.getId());
        data.putString(EXTRA_PACKAGE_NAME, info.getTitle());
        data.putString(EXTRA_PACKAGE_TYPE, info.getType());
        data.putString(EXTRA_PACKAGE_FOLDER, info.getFolder());
        FragItemPackageDetail fragment = new FragItemPackageDetail();
        fragment.setArguments(data);
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
