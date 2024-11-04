package com.photo.photography.frag;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActMain;
import com.photo.photography.ads_notifier.AdsEventNotifier;
import com.photo.photography.ads_notifier.AdsEventState;
import com.photo.photography.ads_notifier.AdsEventTypes;
import com.photo.photography.ads_notifier.AdsIEventListener;
import com.photo.photography.ads_notifier.AdsListenerPriority;
import com.photo.photography.ads_notifier.AdsNotifierFactory;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.AlbumsHelper;
import com.photo.photography.data_helper.HandlingAlbum;
import com.photo.photography.data_helper.MediaHelper;
import com.photo.photography.data_helper.provider.CPHelpers;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;
import com.photo.photography.progress.BottomSheetProgress;
import com.photo.photography.repeater.RepeaterAlbums;
import com.photo.photography.util.DeviceUtil;
import com.photo.photography.util.Measures;
import com.photo.photography.util.preferences.Prefs;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.view.GridSpacingItemsDecoration;
import com.google.android.gms.ads.nativead.NativeAd;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragAlbums extends FragBaseMediaGrid implements AdsIEventListener {

    public static final String FOLDER_WHATSAPP_STATUS = ".Statuses";
    public static final String TAG = "AlbumsFragment";

    @BindView(R.id.albums)
    RecyclerView rv;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.nothing_to_show_placeholder)
    LinearLayout nothing_to_show_placeholder;

    ArrayList<String> excuded = new ArrayList<>();
    private RepeaterAlbums adapter;
    private GridSpacingItemsDecoration spacingDecoration;
    private boolean hidden = false;

    private static boolean isExcluded(String path, ArrayList<String> excludedAlbums) {
        for (String s : excludedAlbums) if (path.startsWith(s)) return true;
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        excuded = db().getExcludedFolders(mActivity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    public void displayAlbums(boolean hidden) {
        this.hidden = hidden;
        displayAlbums();
    }

    public NativeAd getGoogleNativeAd() {
        if (MyApp.getInstance().checkForNativeAdMain()) {
            NativeAd nativeAd = MyApp.getInstance().getGNativeHome().get(0);
            return nativeAd;
        }
        return null;
    }

    public void addWhatsappStatusFolder(SQLiteDatabase readableDatabase) {

        Album local = new Album("", "", 0, 0);
        local.setNativeAd(true);
        local.setNativeAdG(getGoogleNativeAd());
        adapter.add(local);


        File statusFolder = new File(Environment.getExternalStorageDirectory(), "WhatsApp/Media/" + FOLDER_WHATSAPP_STATUS);
        File statusFolder11 = new File(Environment.getExternalStorageDirectory(), "Android/media/com.whatsapp/Whatsapp/Media/" + FOLDER_WHATSAPP_STATUS);
        File statusFolder13 = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/.Statuses");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Album album = new Album(statusFolder13.getAbsolutePath(), FOLDER_WHATSAPP_STATUS, 0, statusFolder13.lastModified());
            album.withSettings(HandlingAlbum.getSettings(readableDatabase, album.getPath()));
            adapter.add(album);
        }if (statusFolder13.exists()) {
            File[] list = statusFolder13.listFiles();
            if (list != null && list.length != 0) {
                int count = 0;
                for (File f : list) {
                    String name = f.getName();
                    if (name.endsWith(".jpg") || name.endsWith(".mp4"))
                        count++;
                }
                Album album = new Album(statusFolder13.getAbsolutePath(), FOLDER_WHATSAPP_STATUS, count, statusFolder13.lastModified());
                album.withSettings(HandlingAlbum.getSettings(readableDatabase, album.getPath()));
                adapter.add(album);
            }
        }else {
            if (statusFolder11.exists()) {
                File[] list = statusFolder11.listFiles();
                if (list != null && list.length != 0) {
                    int count = 0;
                    for (File f : list) {
                        String name = f.getName();
                        if (name.endsWith(".jpg") || name.endsWith(".mp4"))
                            count++;
                    }
                    Album album = new Album(statusFolder11.getAbsolutePath(), FOLDER_WHATSAPP_STATUS, count, statusFolder11.lastModified());
                    album.withSettings(HandlingAlbum.getSettings(readableDatabase, album.getPath()));
                    adapter.add(album);
                }

            } else if (statusFolder.exists()) {
                File[] list = statusFolder.listFiles();
                if (list != null && list.length != 0) {
                    int count = 0;
                    for (File f : list) {
                        String name = f.getName();
                        if (name.endsWith(".jpg") || name.endsWith(".mp4"))
                            count++;
                    }
                    Album album = new Album(statusFolder.getAbsolutePath(), FOLDER_WHATSAPP_STATUS, count, statusFolder.lastModified());
                    album.withSettings(HandlingAlbum.getSettings(readableDatabase, album.getPath()));
                    adapter.add(album);
                }
            } else {
                Log.e("dfs", "fad");
            }
        }
    }

    public void notifyAds() {
        if (adapter != null && isAdded() && mActivity != null && !mActivity.isFinishing()) {
            if (adapter.getItemCount() > 1) {
                adapter.notifyItemChanged(0);
            } else {
                refresh.setRefreshing(false);
                changedNothingToShow(true);
            }
        } else {
            refresh.setRefreshing(false);
            changedNothingToShow(true);
        }
    }

    @SuppressLint("CheckResult")
    public void displayAlbums() {
        if (adapter != null) {
            refresh.setRefreshing(true);
            adapter.clear();
            SQLiteDatabase readableDatabase = HandlingAlbum.getInstance(mActivity).getReadableDatabase();
            if (!hidden) {
                addWhatsappStatusFolder(readableDatabase);
            }

            CPHelpers.getAlbums(mActivity, hidden, excuded)
                    .subscribeOn(Schedulers.io())
                    .map(album -> album.withSettings(HandlingAlbum.getSettings(readableDatabase, album.getPath())))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            album -> adapter.add(album),
                            throwable -> {
                                refresh.setRefreshing(false);
                                changedNothingToShow(getCount() == 0 || getCount() == 1);
                                throwable.printStackTrace();
                            },
                            () -> {
                                readableDatabase.close();
                                changedNothingToShow(getCount() == 0 || getCount() == 1);
                                refresh.setRefreshing(false);


                                Hawk.put(hidden ? "h" : "albums", adapter.getAlbumsPaths());
                            });
        }

    }

    public void changedNothingToShow(boolean showEmptyView) {
        if (showEmptyView) {
            rv.setVisibility(View.GONE);
            nothing_to_show_placeholder.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            nothing_to_show_placeholder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayAlbums();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpColumns();
    }

    public void setUpColumns() {
        int columnsCount = columnsCount();

        if (columnsCount != ((GridLayoutManager) rv.getLayoutManager()).getSpanCount()) {
            rv.removeItemDecoration(spacingDecoration);
            spacingDecoration = new GridSpacingItemsDecoration(columnsCount, Measures.pxToDp(3, mActivity), true);
            rv.addItemDecoration(spacingDecoration);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, columnsCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (adapter.getItemViewType(position) == 0) {
                        return gridLayoutManager.getSpanCount();
                    } else if (adapter.getItemViewType(position) == 2) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
            rv.setLayoutManager(gridLayoutManager);
        }
    }

    public int columnsCount() {
        return DeviceUtil.isPortrait(getResources())
                ? Prefs.getFolderColumnsPortrait()
                : Prefs.getFolderColumnsLandscape();
    }

    @Override
    public int getTotalCount() {
        return adapter.getItemCount();
    }

    @Override
    public View.OnClickListener getToolbarButtonListener(boolean editMode) {
        if (editMode) return null;
        else return v -> adapter.clearSelected();
    }

    @Override
    public String getToolbarTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_albums, container, false);
        ButterKnife.bind(this, v);

        registerAdsListener();
        int spanCount = columnsCount();
        spacingDecoration = new GridSpacingItemsDecoration(spanCount, Measures.pxToDp(3, mActivity), true);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(spacingDecoration);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == 0) {
                    return gridLayoutManager.getSpanCount();
                } else if (adapter.getItemViewType(position) == 2) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        rv.setLayoutManager(gridLayoutManager);

        adapter = new RepeaterAlbums(mActivity, this);

        refresh.setOnRefreshListener(this::displayAlbums);
        rv.setAdapter(adapter);


        return v;
    }

    public SortingModes sortingMode() {
        return adapter.sortingMode();
    }

    public SortingOrders sortingOrder() {
        return adapter.sortingOrder();
    }

    private HandlingAlbum db() {
        return HandlingAlbum.getInstance(mActivity.getApplicationContext());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_albums, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.grid_items).setVisible(true);
        menu.findItem(R.id.settings_album).setVisible(true);
        menu.findItem(R.id.sort_action_album).setVisible(true);
        if (Prefs.getFolderColumnsPortrait() == 3) {
            menu.findItem(R.id.three_column_album).setChecked(true);
        } else {
            menu.findItem(R.id.two_column_album).setChecked(true);
        }
        menu.findItem(R.id.ascending_sort_order_album).setChecked(sortingOrder() == SortingOrders.ASCENDING);
        switch (sortingMode()) {
            case DATE_DAY:
                menu.findItem(R.id.sort_by_date).setChecked(true);
                break;
            case NAME:
                menu.findItem(R.id.name_sort_mode).setChecked(true);
                break;
        }
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.two_column_album:
                item.setChecked(true);
                Prefs.setFolderColumnsPortrait(2);
                refreshAlbum();
                return true;

            case R.id.three_column_album:
                item.setChecked(true);
                Prefs.setFolderColumnsPortrait(3);
                refreshAlbum();
                return true;

            case R.id.name_sort_mode:
                adapter.changeSortingMode(SortingModes.NAME);
//                HandlingAlbums.getInstance(mActivity).setSortingMode(album.getPath(), SortingMode.DATE_DAY.getValue());
//                album.setSortingMode(SortingMode.DATE_DAY);
                AlbumsHelper.setSortingMode(SortingModes.NAME);

                item.setChecked(true);
                return true;

            case R.id.sort_by_date:
                adapter.changeSortingMode(SortingModes.DATE_DAY);
                AlbumsHelper.setSortingMode(SortingModes.DATE_DAY);

//                HandlingAlbums.getInstance(mActivity).setSortingMode(album.getPath(), SortingMode.DATE_DAY.getValue());
//                album.setSortingMode(SortingMode.DATE_DAY);
                item.setChecked(true);
                return true;

            case R.id.ascending_sort_order_album:
                item.setChecked(!item.isChecked());
                SortingOrders sortingOrder = SortingOrders.fromValue(item.isChecked());
                adapter.changeSortingOrder(sortingOrder);
                AlbumsHelper.setSortingOrder(sortingOrder);
//                HandlingAlbums.getInstance(mActivity).setSortingOrder(album.getPath(), sortingOrder.getValue());
//                album.setSortingOrder(sortingOrder);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshAlbum() {
        setUpColumns();
        displayAlbums(false);
    }

    private void showDeleteBottomSheet() {
        if (!SupportClass.isExternalStoragePermissionGranted(mActivity)) {
            SupportClass.userAction = SupportClass.USER_ACTION.DELETE;
            SupportClass.showTakeWritePermissionDialog(mActivity);

        } else {
//        Toast.makeText(mActivity, "Delete MMMMMMMMMM", Toast.LENGTH_SHORT).show();
            List<Album> selected = adapter.getSelectedAlbums();
            ArrayList<io.reactivex.Observable<Album>> sources = new ArrayList<>(selected.size());
            for (Album media : selected)
                sources.add(MediaHelper.deleteAlbum(mActivity.getApplicationContext(), media));

            BottomSheetProgress<Album> bottomSheet = new BottomSheetProgress.Builder<Album>(R.string.delete_bottom_sheet_title)
                    .autoDismiss(false)
                    .sources(sources)
                    .listener(new BottomSheetProgress.Listener<Album>() {
                        @Override
                        public void onCompleted() {
                            adapter.invalidateSelectedCount();
                        }

                        @Override
                        public void onProgress(Album item) {
                            adapter.removeAlbum(item);
                        }
                    })
                    .build();

            bottomSheet.showNow(getChildFragmentManager(), null);
        }
    }

    public int getCount() {
        return adapter.getItemCount();
    }

    public int getSelectedCount() {
        return adapter.getSelectedCount();
    }

    @Override
    public boolean editMode() {
        return adapter != null && adapter.selecting();
    }

    @Override
    public boolean clearSelected() {
        if (adapter != null)
            return adapter.clearSelected();
        else
            return false;
    }

    @Override
    public void onItemSelected(int position, ImageView imageView) {
        if (position < adapter.getItemCount()) {
            ((ActMain) mActivity).displayMediaNew(adapter.get(position));
        }
    }

    @Override
    public void onItemViewSelected(int position, ImageView imageView) {
    }

    @Override
    public void onSelectMode(boolean selectMode) {
        refresh.setEnabled(!selectMode);
        updateToolbar();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onSelectionCountChanged(int selectionCount, int totalCount) {
        getEditModeListener().onItemsSelected(selectionCount, totalCount);
    }

//    AllbumCallBack callBack;

    @Override
    public void onLocationClick() {
//        callBack.onLocation();
    }

    public void reInItAlbums() {
        displayAlbums();
    }

    public void doUserAction() {
        if (SupportClass.isExternalStoragePermissionGranted(mActivity)) {
            if (SupportClass.userAction == SupportClass.USER_ACTION.DELETE) {
                showDeleteBottomSheet();
            }
            SupportClass.userAction = null;
        } else {
            Toast.makeText(mActivity, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
        }
    }

    private void registerAdsListener() {
        AdsEventNotifier
                notifier = AdsNotifierFactory.getInstance().getNotifier(AdsNotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, AdsListenerPriority.PRIORITY_HIGH);
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = AdsEventState.EVENT_IGNORED;
        switch (eventType) {
            case AdsEventTypes.EVENT_AD_LOADED_NATIVE:
                Log.e("Update: ", "adapter");
                eventState = AdsEventState.EVENT_PROCESSED;
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter != null && adapter.getItemCount() > 0) {
                                    adapter.notifyAds();
                                }
                                rv.smoothScrollToPosition(0);
                                Log.e("Update: ", "adapter notifyDataSetChanged");
                            }
                        }, 1000);
                    }
                });
        }
        return eventState;
    }
}
