package com.photo.photography.frag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.act.ActCollageCreate;
import com.photo.photography.act.ActMain;
import com.photo.photography.act.ActPalette;
import com.photo.photography.act.ActSingleMedia;
import com.photo.photography.act.ActVideos;
import com.photo.photography.act.ActWAStatus;
import com.photo.photography.repeater.RepeaterMediaNew;
import com.photo.photography.collage.util.DialogUtil;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.HandlingAlbum;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.MediaHelper;
import com.photo.photography.data_helper.filters_mode.FilterModes;
import com.photo.photography.data_helper.filters_mode.MediaFilters;
import com.photo.photography.data_helper.provider.CPHelpers;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;
import com.photo.photography.callbacks.CallbackRenameClick;
import com.photo.photography.models.TemplateItemModel;
import com.photo.photography.ads_notifier.AdsEventNotifier;
import com.photo.photography.ads_notifier.AdsEventState;
import com.photo.photography.ads_notifier.AdsEventTypes;
import com.photo.photography.ads_notifier.AdsIEventListener;
import com.photo.photography.ads_notifier.AdsListenerPriority;
import com.photo.photography.ads_notifier.AdsNotifierFactory;
import com.photo.photography.templates.PhotosItem;
import com.photo.photography.util.AlertDialogHelper;
import com.photo.photography.util.DeviceUtil;
import com.photo.photography.util.Measures;
import com.photo.photography.util.MediaUtils;
import com.photo.photography.util.StringUtil;
import com.photo.photography.util.preferences.Prefs;
import com.photo.photography.util.utils.CallbackOnDeleteProcess;
import com.photo.photography.util.utils.frame.FrameImageUtils;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.ActPickImage;
import com.photo.photography.secure_vault.ActVault;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.helper.Constants;
import com.photo.photography.secure_vault.helper.DbHandler;
import com.photo.photography.secure_vault.utils.CryptoExceptionUtils;
import com.photo.photography.secure_vault.utils.CryptoUtil;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.photo.photography.view.GridSpacingItemsDecoration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragRvAllMediaVideos extends FragBaseMediaGrid implements AdsIEventListener {

    public static final String TAG = "RvAllMediaVideosFragment";
    private static final String BUNDLE_ALBUM = "album";

    @BindView(R.id.nothing_to_show_placeholder)
    LinearLayout nothing_to_show_placeholder;

    @BindView(R.id.media)
    RecyclerView rv;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;

    DbHandler db;
    AlertDialog renameDialog;
    private RepeaterMediaNew adapter;
    private GridSpacingItemsDecoration spacingDecoration;
    private Album album;

    public static FragRvAllMediaVideos make(Album album) {
        FragRvAllMediaVideos fragment = new FragRvAllMediaVideos();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_ALBUM, album);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    private void registerAdsListener() {
        AdsEventNotifier notifier = AdsNotifierFactory.getInstance().getNotifier(AdsNotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, AdsListenerPriority.PRIORITY_HIGH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            album = getArguments().getParcelable(BUNDLE_ALBUM);
            return;
        }
        album = savedInstanceState.getParcelable(BUNDLE_ALBUM);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    public void reload() {
        loadAlbum(album);
    }

    public void notifyAds(){
        if (adapter != null && isAdded() && mActivity != null && !mActivity.isFinishing()) {
            if (adapter.getItemCount()>0) {
                adapter.notifyItemChanged(0);
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void loadAlbum(Album album) {
        if (mActivity != null && !mActivity.isFinishing() && !mActivity.isDestroyed()) {
            this.album = album;
            adapter.setupFor(album);
            CPHelpers.getMedia(mActivity, album)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(media -> MediaFilters.getFilter(album.filterMode()).accept(media))
                    .subscribe(media ->
                        adapter.add(media),
                            throwable -> {
                                refresh.setRefreshing(false);
                                changedNothingToShow(getCount() == 0);
                                Log.wtf("asd", throwable);
                            },
                            () -> {
                                album.setCount(getCount());
                                changedNothingToShow(getCount() == 0);
                                refresh.setRefreshing(false);
                                adapter.buildMediaNew();
                            });
        } else {
            Log.e(TAG, "mActivity is null");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(BUNDLE_ALBUM, album);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_rv_media_photos, container, false);
        ButterKnife.bind(this, v);
        registerAdsListener();
        int spanCount = columnsCount();
        spacingDecoration = new GridSpacingItemsDecoration(spanCount, Measures.pxToDp(1, mActivity), true);
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

        adapter = new RepeaterMediaNew(mActivity, album.settings.getSortingMode(), album.settings.getSortingOrder(), this);
        refresh.setOnRefreshListener(this::reload);
        rv.setAdapter(adapter);
        return v;
    }

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        Log.e("Update: ", "eventNotify");
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
                                reload();
                                Log.e("Update: ", "adapter notifyDataSetChanged");
                            }
                        }, 1000);

                    }
                });
        }
        return eventState;
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

    public boolean isMediaSelected() {
        if (adapter != null && adapter.getSelectedCount() > 0) {
            adapter.clearSelected();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        album.setFilterMode(FilterModes.VIDEO);
        reload();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpColumns();
    }

    public void setUpColumns() {
        int columnsCount = columnsCount();

        if (columnsCount != ((GridLayoutManager) rv.getLayoutManager()).getSpanCount()) {
            ((GridLayoutManager) rv.getLayoutManager()).getSpanCount();
            rv.removeItemDecoration(spacingDecoration);
            spacingDecoration = new GridSpacingItemsDecoration(columnsCount, Measures.pxToDp(3, mActivity), true);
            rv.setLayoutManager(new GridLayoutManager(mActivity, columnsCount));
            rv.addItemDecoration(spacingDecoration);
        }
    }

    public int columnsCount() {
        return DeviceUtil.isPortrait(getResources())
                ? Prefs.getMediaColumnsPortrait()
                : Prefs.getMediaColumnsLandscape();
    }

    @Override
    public int getTotalCount() {
        return adapter.getOriginalItemCount();
    }

    @Override
    public View.OnClickListener getToolbarButtonListener(boolean editMode) {
        if (editMode) return null;
        else return v ->
                adapter.clearSelected();
    }

    @Override
    public String getToolbarTitle() {
        return editMode() ? null : getResources().getString(R.string.videos);
    }

    public SortingModes sortingMode() {
        return album.settings.getSortingMode();
    }

    public SortingOrders sortingOrder() {
        return album.settings.getSortingOrder();
    }

    private HandlingAlbum db() {
        return HandlingAlbum.getInstance(mActivity.getApplicationContext());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_media, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        boolean editMode = editMode();
        boolean oneSelected = getSelectedCount() == 1;

        menu.setGroupVisible(R.id.edit_mode_items, editMode);
        menu.setGroupVisible(R.id.one_selected_items, false);
        menu.findItem(R.id.collage).setVisible(false);
        menu.findItem(R.id.set_as_cover).setVisible(false);

        menu.findItem(R.id.select_all).setTitle(
                getSelectedCount() == getCount()
                        ? R.string.clear_selected
                        : R.string.select_all);
        if (editMode) {
            menu.findItem(R.id.sort_action).setVisible(false);
        } else {
            menu.findItem(R.id.sort_action).setVisible(true);

            menu.findItem(R.id.ascending_sort_order).setChecked(sortingOrder() == SortingOrders.ASCENDING);
            switch (sortingMode()) {
                case SIZE:
                    menu.findItem(R.id.size_sort_mode).setChecked(true);
                    break;
                case DATE_DAY:
                default:
                    MenuItem menuItemd = menu.findItem(R.id.date_taken_sort_mode);
                    if (menuItemd.hasSubMenu()) {
                        MenuItem subMenu = menuItemd.getSubMenu().findItem(R.id.date_taken_sort_by_day);
                        if (subMenu != null) {
                            subMenu.setChecked(true);
                        }
                    }
                    break;
                case DATE_MONTH:
                    MenuItem menuItemm = menu.findItem(R.id.date_taken_sort_mode);
                    if (menuItemm.hasSubMenu()) {
                        MenuItem subMenu = menuItemm.getSubMenu().findItem(R.id.date_taken_sort_by_month);
                        if (subMenu != null) {
                            subMenu.setChecked(true);
                        }
                    }
                    break;
                case DATE_YEAR:
                    MenuItem menuItemy = menu.findItem(R.id.date_taken_sort_mode);
                    if (menuItemy.hasSubMenu()) {
                        MenuItem subMenu = menuItemy.getSubMenu().findItem(R.id.date_taken_sort_by_year);
                        if (subMenu != null) {
                            subMenu.setChecked(true);
                        }
                    }
                    break;
                case NUMERIC:
                    menu.findItem(R.id.numeric_sort_mode).setChecked(true);
                    break;
            }
        }

        if (mActivity instanceof ActWAStatus) {
            menu.findItem(R.id.download_wa_status).setVisible(getSelectedCount() > 0);
            menu.findItem(R.id.selection_done).setVisible(false);

        } else if (mActivity instanceof ActPickImage) {
            menu.findItem(R.id.download_wa_status).setVisible(false);
            menu.setGroupVisible(R.id.sort_action, false);
//            menu.findItem(R.id.grid_items).setVisible(false);
            menu.setGroupVisible(R.id.edit_mode_items, false);
            menu.setGroupVisible(R.id.one_selected_items, false);
            menu.findItem(R.id.settings).setVisible(false);
            menu.findItem(R.id.select_all).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.selection_done).setVisible(true);

        } else {
            menu.findItem(R.id.download_wa_status).setVisible(false);
            menu.findItem(R.id.selection_done).setVisible(false);
        }

        super.onPrepareOptionsMenu(menu);
    }

    public void doUserAction() {
        if (SupportClass.isExternalStoragePermissionGranted(mActivity)) {
            if (SupportClass.userAction == SupportClass.USER_ACTION.DELETE) {
                startDeleteFile();
            } else if (SupportClass.userAction == SupportClass.USER_ACTION.RENAME) {
                renameFile();
            } else if (SupportClass.userAction == SupportClass.USER_ACTION.MOVE_TO_VAULT) {
                moveInVault();
            }
            SupportClass.userAction = null;
        } else {
            Toast.makeText(mActivity, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancelSelection:
                requireActivity().invalidateOptionsMenu();
                adapter.clearSelected();
                updateToolbar();
                return true;

            case R.id.sharePhotos:
                try {
                    ArrayList<Media> selectedMediaList = adapter.getSelected();
                    if (selectedMediaList != null && selectedMediaList.size() > 0)
                        MediaUtils.shareMedia(mActivity, selectedMediaList);
                } catch (Exception e) {
                    Log.e("TAG", "Error : " + e.getMessage());
                }
                return true;

            case R.id.set_as_cover:
                if (adapter.getFirstSelected() != null) {
                    String path = adapter.getFirstSelected().getPath();
                    album.setCover(path);
                    db().setCover(album.getPath(), path);
                    adapter.clearSelected();
                }
                return true;

            case R.id.action_palette:
                try {
                    Uri uri = null;
                    if (adapter.getFirstSelected().getUri() != null) {
                        uri = adapter.getFirstSelected().getUri();

                    } else if (adapter.getFirstSelected().getFile() != null) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", adapter.getFirstSelected().getFile());
                        } else {
                            uri = Uri.fromFile(adapter.getFirstSelected().getFile());
                        }
                    }

                    Intent paletteIntent = new Intent(getActivity(), ActPalette.class);
                    paletteIntent.setData(uri);
                    if (MyApp.getInstance().needToShowAd()) {
                        MyApp.getInstance().showInterstitial(mActivity, paletteIntent, false, -1, null);
                    } else {
                        startActivity(paletteIntent);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "Error : " + e.getMessage());
                }
                return true;

            case R.id.delete:
                Dialog textDialog = DialogUtil.getDialogObject((AppCompatActivity) getActivity(), R.layout.dialog_texts);
                if(textDialog == null){
                    Toast.makeText(getActivity(), R.string.something_went_wrong_please_try_again, Toast.LENGTH_SHORT).show();
                    return true;
                }
                textDialog.setCancelable(false);

                TextView dialogTitle = textDialog.findViewById(R.id.text_dialog_title);
                TextView dialogMessage = textDialog.findViewById(R.id.text_dialog_message);
                dialogTitle.setText(R.string.delete);
                dialogMessage.setText(R.string.delete_album_message);

                TextView text_delete = textDialog.findViewById(R.id.text_delete);
                TextView text_cancel = textDialog.findViewById(R.id.text_cancel);
                text_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textDialog.dismiss();
                        if (!SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                            SupportClass.userAction = SupportClass.USER_ACTION.DELETE;
                            SupportClass.showTakeWritePermissionDialog(mActivity);
                        } else {
                            startDeleteFile();
                        }
                    }
                });

                text_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textDialog.dismiss();
                    }
                });

                textDialog.show();
                return true;

            case R.id.rename:
                if (!SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                    SupportClass.userAction = SupportClass.USER_ACTION.RENAME;
                    SupportClass.showTakeWritePermissionDialog(mActivity);
                } else {
                    renameFile();
                }
                return true;

            case R.id.selection_done:
            case R.id.action_move_to_vault:
                if (!SupportClass.checkVaultSetup(mActivity)) {

                    Log.e("TAG", "Vault not setupped.");
                    isMediaSelected();
                } else if (adapter.getSelected().size() > 5) {
                    Toast.makeText(mActivity, "At a time maximum 5 videos Move to secure vault", Toast.LENGTH_SHORT).show();
                } else if (adapter.getSelected().size() > 0) {
                    if (!SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                        SupportClass.userAction = SupportClass.USER_ACTION.MOVE_TO_VAULT;
                        SupportClass.showTakeWritePermissionDialog(mActivity);
                    } else {
                        moveInVault();
                    }
                } else {
                    Toast.makeText(mActivity, "Please select at least one item to Move to secure vault", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.select_all:
                if (adapter.getSelectedCount() == adapter.getOriginalItemCount())
                    adapter.clearSelected();
                else adapter.selectAll();
                return true;

            case R.id.download_wa_status:
                if (adapter.getSelected().size() > 0) {
                    copyMultipleWAStatus();
                } else {
                    Toast.makeText(mActivity, "Please select at least one status to download", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.date_taken_sort_by_day:
                adapter.changeSortingMode(SortingModes.DATE_DAY);
                HandlingAlbum.getInstance(mActivity).setSortingMode(album.getPath(), SortingModes.DATE_DAY.getValue());
                album.setSortingMode(SortingModes.DATE_DAY);
                item.setChecked(true);
                return true;

            case R.id.date_taken_sort_by_month:
                adapter.changeSortingMode(SortingModes.DATE_MONTH);
                HandlingAlbum.getInstance(mActivity).setSortingMode(album.getPath(), SortingModes.DATE_MONTH.getValue());
                album.setSortingMode(SortingModes.DATE_MONTH);
                item.setChecked(true);
                return true;

            case R.id.date_taken_sort_by_year:
                adapter.changeSortingMode(SortingModes.DATE_YEAR);
                HandlingAlbum.getInstance(mActivity).setSortingMode(album.getPath(), SortingModes.DATE_YEAR.getValue());
                album.setSortingMode(SortingModes.DATE_YEAR);
                item.setChecked(true);
                return true;

            case R.id.size_sort_mode:
                adapter.changeSortingMode(SortingModes.SIZE);
                HandlingAlbum.getInstance(mActivity).setSortingMode(album.getPath(), SortingModes.SIZE.getValue());
                album.setSortingMode(SortingModes.SIZE);
                item.setChecked(true);
                return true;

            case R.id.numeric_sort_mode:
                adapter.changeSortingMode(SortingModes.NUMERIC);
                HandlingAlbum.getInstance(mActivity).setSortingMode(album.getPath(), SortingModes.NUMERIC.getValue());
                album.setSortingMode(SortingModes.NUMERIC);
                item.setChecked(true);
                return true;

            case R.id.ascending_sort_order:
                item.setChecked(!item.isChecked());
                SortingOrders sortingOrder = SortingOrders.fromValue(item.isChecked());
                adapter.changeSortingOrder(sortingOrder);
                HandlingAlbum.getInstance(mActivity).setSortingOrder(album.getPath(), sortingOrder.getValue());
                album.setSortingOrder(sortingOrder);
                return true;

            case R.id.collage:
                ArrayList<String> mSelectedImages = new ArrayList<>();
                for (int i = 0; i < adapter.getSelectedCount(); i++) {
                    if (!adapter.getSelected().get(i).isVideo())
                        if (isImageFile(adapter.getSelected().get(i).getPath())) {
                            mSelectedImages.add(adapter.getSelected().get(i).getPath());
                        } else {

                        }
                }

                if (mSelectedImages.size() > 10) {
                    Toast.makeText(mActivity, "Collage option available for less than 10 Image Files", Toast.LENGTH_SHORT).show();

                } else if (mSelectedImages.size() < 2) {
                    Toast.makeText(mActivity, "Collage option available for more than 1 Image Files", Toast.LENGTH_SHORT).show();

                } else {
                    ArrayList<TemplateItemModel> mTemplateItemList = new ArrayList<TemplateItemModel>();
                    mTemplateItemList.addAll(FrameImageUtils.loadFrameImages(mActivity));

                    int selectedFilesNo = mSelectedImages.size();

                    int mSelectedTemplateIndex = 0;
                    if (selectedFilesNo == 0) {
                        mSelectedTemplateIndex = 0;
                    } else if (selectedFilesNo == 1) {
                        mSelectedTemplateIndex = 0;
                    } else if (selectedFilesNo == 2) {
                        mSelectedTemplateIndex = 1;
                    } else if (selectedFilesNo == 3) {
                        mSelectedTemplateIndex = 13;
                    } else if (selectedFilesNo == 4) {
                        mSelectedTemplateIndex = 61;
                    } else if (selectedFilesNo == 5) {
                        mSelectedTemplateIndex = 86;
                    } else if (selectedFilesNo == 6) {
                        mSelectedTemplateIndex = 118;
                    } else if (selectedFilesNo == 7) {
                        mSelectedTemplateIndex = 133;
                    } else if (selectedFilesNo == 8) {
                        mSelectedTemplateIndex = 144;
                    } else if (selectedFilesNo == 9) {
                        mSelectedTemplateIndex = 161;
                    } else if (selectedFilesNo == 10) {
                        mSelectedTemplateIndex = 173;
                    }

                    final TemplateItemModel selectedTemplateItem = mTemplateItemList.get(mSelectedTemplateIndex);
                    int itemSize = selectedTemplateItem.getPhotoItemList().size();

                    int size = Math.min(itemSize, mSelectedImages.size());

                    for (int idx = 0; idx < size; idx++) {
                        selectedTemplateItem.getPhotoItemList().get(idx).imagePath = mSelectedImages.get(idx);
                    }

                    Intent intent = new Intent(mActivity, ActCollageCreate.class);
                    intent.putExtra("imageInTemplateCount", selectedTemplateItem.getPhotoItemList().size());
                    intent.putExtra("frameImage", true);
                    intent.putExtra("selectedTemplateIndex", mSelectedImages.size());
                    ArrayList<String> imagePaths = new ArrayList<>();
                    for (PhotosItem item2 : selectedTemplateItem.getPhotoItemList()) {
                        if (item2.imagePath == null) item2.imagePath = "";
                        imagePaths.add(item2.imagePath);
                    }
                    intent.putExtra("imagePaths", imagePaths);


                    if (MyApp.getInstance().needToShowAd()) {
                        MyApp.getInstance().showInterstitial(requireActivity(), intent, false, -1, null);
                    } else {
                        requireActivity().startActivity(intent);
                    }
                    if (adapter.getSelectedCount() > 0)
                        adapter.clearSelected();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveInVault() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaUtils.moveInVaultMediaNew(mActivity, adapter.getSelected(), new CallbackOnDeleteProcess() {
                @Override
                public void onDeleteComplete() {
                    adapter.invalidateSelectedCount();
                }

                @Override
                public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media media) {
                    if (isSuccess && adapter != null) {
                        adapter.removeSelectedMedia(media);
                    }
                }
            });
        } else {
            Intent intent = new Intent();
            intent.putExtra(ActVault.PICKED_MEDIA_LIST, adapter.getSelected());
            new EncryptFile(-1, -1).execute(intent);
        }
    }

    private void renameFile() {
        if (renameDialog != null && renameDialog.isShowing()) {
            renameDialog.dismiss();
        }
        final EditText editTextNewName = new EditText(getActivity());
        editTextNewName.setText(StringUtil.getPhotoNameByPath(adapter.getFirstSelected().getPath()));
        renameDialog = AlertDialogHelper.getInsertTextDialog(((AppCompatActivity) getActivity()), editTextNewName, R.string.rename_photo_action, new CallbackRenameClick() {
            @Override
            public void onOkClick(View v) {
                renameDialog.dismiss();
                if (editTextNewName.length() != 0) {
                    boolean b = MediaHelper.renameMedia(getActivity(), adapter.getFirstSelected(), editTextNewName.getText().toString());
                    if (!b) {
                        StringUtil.showToast(getActivity(), getString(R.string.rename_error));
                    } else
                        adapter.clearSelected(); // Deselect media if rename successful
                } else
                    StringUtil.showToast(getActivity(), getString(R.string.nothing_changed));
            }

            @Override
            public void onCancelClick(View v) {
                renameDialog.dismiss();
            }
        });
        renameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        renameDialog.show();
    }

    private void copyMultipleWAStatus() {
        File destination = new VaultFileUtil(mActivity).getFile(getString(R.string.gallery_vault));
        for (Media selectedMedia : adapter.getSelected()) {
            String selectedFile = selectedMedia.getPath();
            SupportClass.copyFile(mActivity, selectedFile.substring(0, selectedFile.lastIndexOf("/")),
                    selectedFile.substring(selectedFile.lastIndexOf("/") + 1), destination.getAbsolutePath());
            ActMain.IS_NEW_STATUS_DOWNLOAD = true;
        }
        adapter.clearSelected();
        Toast.makeText(mActivity, "Status downloaded successfully", Toast.LENGTH_SHORT).show();
    }

    private void startDeleteFile() {
        MediaUtils.deleteMediaNew(mActivity, adapter.getSelected(), new CallbackOnDeleteProcess() {
            @Override
            public void onDeleteComplete() {
                adapter.invalidateSelectedCount();
            }

            @Override
            public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media media) {
                if (isSuccess && adapter != null) {
                    adapter.removeSelectedMedia(media);
                }
            }
        });
    }

    public void deletePendingMedia(int requestCode, int resultCode) {
        if (isAdded() && adapter != null) {
            if (requestCode == SupportClass.ANDROID_R_MOVE_IN_VAULT_REQUEST_CODE || requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE) {
                if (requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE) {
                    ArrayList<Media> arrayList = new ArrayList<>();
                    arrayList.add(MediaUtils.getPendingMediaList().get(0));
                    Intent intent = new Intent();
                    intent.putExtra(ActVault.PICKED_MEDIA_LIST, arrayList);
                    new EncryptFile(requestCode, resultCode).execute(intent);

                } else {
                    Intent intent = new Intent();
                    intent.putExtra(ActVault.PICKED_MEDIA_LIST, adapter.getSelected());
                    new EncryptFile(requestCode, resultCode).execute(intent);
                }

            } else {
                MediaUtils.deletePendingMedia(requireActivity(), requestCode, resultCode, new CallbackOnDeleteProcess() {
                    @Override
                    public void onDeleteComplete() {
                        if (adapter != null)
                            adapter.invalidateSelectedCount();
                    }

                    @Override
                    public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media media) {
                        if (isSuccess && adapter != null) {
                            adapter.removeSelectedMedia(media);
                        }
                    }
                });
            }
        }
    }

    public int getCount() {
        return adapter.getOriginalItemCount();
    }

    public int getSelectedCount() {
        return adapter.getSelectedCount();
    }

    @Override
    public boolean editMode() {
        return adapter.selecting();
    }

    @Override
    public void onItemSelected(int position, ImageView imageView) {
        if (mActivity instanceof ActMain)
            ((ActMain) mActivity).onMediaClick(album, adapter.getMedia(), position, imageView);
        else if (mActivity instanceof ActWAStatus)
            ((ActWAStatus) mActivity).onMediaClick(album, adapter.getMedia(), position, imageView);
        else if (position < adapter.getItemCount() && mActivity instanceof ActPickImage)
            if (!SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                SupportClass.userAction = SupportClass.USER_ACTION.MOVE_TO_VAULT;
                SupportClass.showTakeWritePermissionDialog(mActivity);
            } else {
                adapter.setSelection(position);
                moveInVault();
            }
        else if (mActivity instanceof ActVideos)
            ((ActVideos) mActivity).onMediaClick(album, adapter.getMedia(), position, imageView);

    }

    @Override
    public void onItemViewSelected(int position, ImageView imageView) {
        if (mActivity instanceof ActMain)
            ((ActMain) mActivity).onMediaViewClick(album, adapter.getMedia(), position, imageView);
        else if (mActivity instanceof ActWAStatus)
            ((ActWAStatus) mActivity).onMediaViewClick(album, adapter.getMedia(), position, imageView);
        else if (mActivity instanceof ActVideos)
            ((ActVideos) mActivity).onMediaViewClick(album, adapter.getMedia(), position, imageView);
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

    @Override
    public void onLocationClick() {

    }

    @Override
    public boolean clearSelected() {
        if (adapter != null)
            return adapter.clearSelected();
        else
            return false;
    }

    private String SaveThumbnail(String fileMain, String fileName, String fileType) {

        int mWidth = getResources().getDisplayMetrics().widthPixels;
        int THUMBSIZE = mWidth;
        Bitmap finalBitmap;
        if (fileType.equals(Constants.FILE_TYPE_IMAGE)) {
            finalBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileMain), THUMBSIZE, THUMBSIZE);
        } else {
            finalBitmap = ThumbnailUtils.createVideoThumbnail(fileMain, MediaStore.Video.Thumbnails.MINI_KIND);
        }
        File myDirSnaplock = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATEVAULT);
        File myDirSupport = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_SUPPORT);
        if (!myDirSnaplock.exists()) {
            myDirSnaplock.mkdir();
        }
        if (!myDirSupport.exists()) {
            myDirSupport.mkdir();
        }
        File file = new File(myDirSupport, fileName + ".jpg");
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);

            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            out.flush();
            out.close();
            Log.e("TAG", "Thumbnail successfully Saved" + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void encryptImagesCallBroadCast(String filename) {
//        Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Images.Media._ID};

//        Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{new File(filename).getAbsolutePath()};

//        Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mActivity.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
//        File not found in media store DB
        }
        c.close();
    }

    public void encryptVideoCallBroadCast(String filename) {
//        Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Video.Media._ID};

//        Match on the file path
        String selection = MediaStore.Video.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{new File(filename).getAbsolutePath()};

//        Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mActivity.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
//        File not found in media store DB
        }
        c.close();
    }

    public void reInItMedia() {
        if (getArguments() != null)
            album = getArguments().getParcelable(BUNDLE_ALBUM);
        if (album == null)
            album = Album.getAllMediaAlbum();
        album.setFilterMode(FilterModes.VIDEO);
        reload();
    }

    public void reInItMedia(Intent result) {
        if (adapter != null) {
            ArrayList<Media> deletedPosList = result.getParcelableArrayListExtra(ActSingleMedia.DELETED_POS_LIST);
            for (int i = 0; i < deletedPosList.size(); i++) {
                if (deletedPosList.get(i).isVideo()) {
                    Log.e("TRACE_DELETE", "reInItMedia for loop if condition VideosFragment");
                    adapter.removeSelectedMedia(deletedPosList.get(i));
                } else {
                    Log.e("TRACE_DELETE", "reInItMedia for loop else condition VideosFragment");
                }
            }
        }
    }

    private void onCompleteEncryption() {
        adapter.invalidateSelectedCount();
        isMediaSelected();
        if (mActivity instanceof ActPickImage) {
            ActMain.IS_VAULT_CHANGED = true;
            mActivity.setResult(Activity.RESULT_OK);
            mActivity.finish();
        } else {
            reInItMedia();
        }
        Toast.makeText(mActivity, "Successfully file Added in\nYour Private Vault.", Toast.LENGTH_SHORT).show();
    }

    class EncryptFile extends AsyncTask<Intent, String, String> {
        int requestCode, resultCode;
        private ProgressDialog pDialog;

        public EncryptFile(int requestCode, int resultCode) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mActivity);
            pDialog.setMessage("Processing file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Intent... data) {
            if (resultCode == Activity.RESULT_OK) {
                db = new DbHandler(mActivity);

                if (!(new File(mActivity.getExternalFilesDir(null), "support")).exists()) {
                    new File(mActivity.getExternalFilesDir(null), "support").mkdirs();
                }
                String privatevaultFolder = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).getAbsolutePath();
                if (!(new File(privatevaultFolder)).exists()) {
                    new File(privatevaultFolder).mkdirs();
                }

                ArrayList<Media> videoList = data[0].getParcelableArrayListExtra(ActVault.PICKED_MEDIA_LIST);
                if (videoList.size() > 0) {
                    for (int i = 0; i < videoList.size(); i++) {
                        String oldFilePath = videoList.get(i).getPath();
                        String oldFileName = videoList.get(i).getPath().substring(videoList.get(i).getPath().lastIndexOf("/") + 1);
                        String currentTimeInMillis = String.valueOf(System.currentTimeMillis() + 1);
                        String newFilePath = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).getAbsolutePath() + currentTimeInMillis;
                        String newFileName = currentTimeInMillis;
                        String oldThumbnailPath = SaveThumbnail(oldFilePath, currentTimeInMillis, Constants.FILE_TYPE_VIDEO);
                        Log.e("TAG", "Old Thumbnail Path : " + oldThumbnailPath);
                        String newThumbnailPath = new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).getAbsolutePath() + currentTimeInMillis;
                        try {
                            if (!new File(mActivity.getExternalFilesDir(null), "support").exists()) {
                                new File(mActivity.getExternalFilesDir(null), "support").mkdir();
                            }
                            if (!new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).exists()) {
                                new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT).mkdir();
                            }
                            if (!new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).exists()) {
                                new VaultFileUtil(mActivity).getFile(VaultFileUtil.FOLDER_TO_PRIVATE_VAULT_THUMBNAIL).mkdir();
                            }

                            Log.e("TAG", "Encrypt file : " + oldFilePath + " to\n" + newFilePath);
                            CryptoUtil.encrypt(mActivity, new File(oldFilePath), new File(newFilePath));
                            CryptoUtil.encrypt(mActivity, new File(oldThumbnailPath), new File(newThumbnailPath));
                            int p = db.addRecord(new VaultFile(oldFilePath, oldFileName, newFilePath, newFileName, Constants.FILE_TYPE_VIDEO, newThumbnailPath, false));
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                new File(oldThumbnailPath).delete();
                                new File(oldFilePath).delete();
                                encryptVideoCallBroadCast(oldFilePath);
                            }
                            Log.e("TAG", "if complete without error");
                        } catch (CryptoExceptionUtils e) {
                            Log.e("TAG", "Error in Encryption of file : " + e.toString());
                        }
                    }

                    ArrayList<VaultFile> vaultData = db.getAllRecords();
                    if (vaultData.size() > 0) {
                        for (int i = 0; i < vaultData.size(); i++) {
                            int pro_id = vaultData.get(i).getID();
                            String oldFilePath = vaultData.get(i).getOldPath();
                            String oldFileName = vaultData.get(i).getOldFileName();
                            String newFilePath = vaultData.get(i).getNewPath();
                            String newFileName = vaultData.get(i).getNewFileName();
                            String type = vaultData.get(i).getType();
                            String thumbnail = vaultData.get(i).getThumbnail();
                            Log.e("TAG", "Data Selected Record =>  Id-" + pro_id + ",\noldFilePath-" + oldFilePath + ",\noldFileName-" + oldFileName + ",\nnewFilePath-" + newFilePath + ",\nnewFileName-" + newFileName + ",\nType-" + type + ",\nThumbnail-" + thumbnail);
                        }
                    } else {
                        Log.e("TAG", "Data not Available in table");
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pDialog.dismiss();
            if (resultCode == Activity.RESULT_OK && requestCode == -1) {
                onCompleteEncryption();

            } else {
                MediaUtils.deletePendingMedia(requireActivity(), requestCode, resultCode, new CallbackOnDeleteProcess() {
                    @Override
                    public void onDeleteComplete() {
                        if (requestCode == SupportClass.ANDROID_Q_MOVE_IN_VAULT_REQUEST_CODE) {
                            if (MediaUtils.getPendingMediaList().size() == 0) {
                                onCompleteEncryption();
                            }
                        } else {
                            onCompleteEncryption();
                        }
                    }

                    @Override
                    public void onMediaDeleteSuccess(boolean isSuccess, @NonNull Media media) {
                        if (isSuccess && adapter != null) {
                            adapter.removeSelectedMedia(media);
                        }
                    }
                });
            }
        }
    }
}
