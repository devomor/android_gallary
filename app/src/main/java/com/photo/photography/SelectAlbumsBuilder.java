package com.photo.photography;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.data_helper.StorageHelpers;
import com.photo.photography.data_helper.filters_mode.FoldersFileFilters;
import com.photo.photography.callbacks.CallbackRenameClick;
import com.photo.photography.liz_theme.ui_theme.ThemedIcons;
import com.photo.photography.util.AlertDialogHelper;
import com.photo.photography.util.Measures;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.photo.photography.view.GridSpacingItemsDecoration;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectAlbumsBuilder extends BottomSheetDialogFragment {

    final int INTERNAL_STORAGE = 0;
    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
    FragmentManager fragmentManager;
    private String title;
    private ArrayList<File> folders;
    private BottomSheetAlbumsAdapter adapter;
    private boolean exploreMode = false, canGoBack = false, forzed = false;
    private LinearLayout exploreModePanel;
    private TextView currentFolderPath;
    private OnFolderSelected onFolderSelected;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String path = view.findViewById(R.id.name_folder).getTag().toString();
            if (exploreMode) displayContentFolder(new File(path));
            else {
                dismiss();
                onFolderSelected.folderSelected(path);
            }
        }
    };
    private FloatingActionButton fabDone;
    private String sdCardPath = null;

    public static SelectAlbumsBuilder with(FragmentManager manager) {
        SelectAlbumsBuilder fragment = new SelectAlbumsBuilder();
        fragment.fragmentManager = manager;
        return fragment;
    }

    public SelectAlbumsBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SelectAlbumsBuilder exploreMode(boolean enabled) {
        exploreMode = enabled;
        return this;
    }

    public SelectAlbumsBuilder force(boolean force) {
        forzed = force;
        return this;
    }

    public SelectAlbumsBuilder onFolderSelected(OnFolderSelected callback) {
        onFolderSelected = callback;
        return this;
    }

    public void show() {
        show(fragmentManager, getTag());
    }

    private boolean canGoBack() {
        return canGoBack;
    }
    AlertDialog insertTextDialog ;
    @Override
    public void setupDialog(Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.select_folder_bottomsheet, null);
        final RecyclerView mRecyclerView = contentView.findViewById(R.id.folders);
        final Spinner spinner = contentView.findViewById(R.id.storage_spinner);
        currentFolderPath = contentView.findViewById(R.id.bottom_sheet_sub_title);
        exploreModePanel = contentView.findViewById(R.id.ll_explore_mode_panel);

        sdCardPath = StorageHelpers.getSdcardPath(getContext());

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(new GridSpacingItemsDecoration(2, Measures.pxToDp(3, getContext()), true));
        adapter = new BottomSheetAlbumsAdapter();
        mRecyclerView.setAdapter(adapter);

        spinner.setAdapter(new VolumeSpinnerAdapter(contentView.getContext()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                switch (pos) {
                    case INTERNAL_STORAGE:
                        displayContentFolder(new VaultFileUtil(MyApp.mContext).getDir());
                        break;
                    default:
                        // TODO: 12/11/16 check this plis
//                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                            DocumentFile documentFile = StorageHelper.getDocumentFile(getContext(), new File(StorageHelper.getExtSdCardPaths(getContext())[pos - 1]), true, false);
//                            if(documentFile != null){
//                                displayContentFolder(new File(StorageHelper.getExtSdCardPaths(getContext())[pos - 1]));
//                            } else {
//                                Toast.makeText(getContext(), getString(R.string.no_permission), Toast.LENGTH_LONG).choseProvider();
//                                spinner.setSelection(0);
//                            }
//                        } else {
//                            displayContentFolder(new File(StorageHelper.getExtSdCardPaths(getContext())[pos - 1]));
//                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        fabDone = contentView.findViewById(R.id.fab_bottomsheet_done);
        fabDone.setImageDrawable(new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_done).color(Color.WHITE));
        fabDone.setVisibility(exploreMode ? View.VISIBLE : View.GONE);
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onFolderSelected.folderSelected(currentFolderPath.getText().toString());
            }
        });

        ((TextView) contentView.findViewById(R.id.bottom_sheet_title)).setText(title);

        contentView.findViewById(R.id.rl_create_new_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getContext());
                 insertTextDialog = AlertDialogHelper.getInsertTextDialog(((AppCompatActivity) getActivity()), editText, R.string.new_folder, new CallbackRenameClick() {
                    @Override
                    public void onOkClick(View v) {
                        insertTextDialog.dismiss();
                        File folderPath = new File(currentFolderPath.getText().toString() + File.separator + editText.getText().toString());
                        if (folderPath.mkdir()) displayContentFolder(folderPath);
                    }

                    @Override
                    public void onCancelClick(View v) {
                        insertTextDialog.dismiss();
                    }
                });
                insertTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                insertTextDialog.show();
            }
        });
        contentView.findViewById(R.id.rl_bottom_sheet_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!forzed) {
                    toggleExplorerMode(!exploreMode);
                    fabDone.setVisibility(exploreMode ? View.VISIBLE : View.GONE);
                }
            }
        });

        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        adapter.notifyDataSetChanged();
        toggleExplorerMode(exploreMode);
    }

    private void displayContentFolder(File dir) {
        canGoBack = false;
        if (dir.canRead()) {
            folders = new ArrayList<>();
            File parent = dir.getParentFile();
            if (parent.canRead()) {
                canGoBack = true;
                folders.add(0, parent);
            }
            File[] files = dir.listFiles(new FoldersFileFilters());
            if (files != null && files.length > 0) {
                folders.addAll(new ArrayList<>(Arrays.asList(files)));
                currentFolderPath.setText(dir.getAbsolutePath());
            }
            currentFolderPath.setText(dir.getAbsolutePath());
            adapter.notifyDataSetChanged();
        }
    }

    private void toggleExplorerMode(boolean enabled) {
        folders = new ArrayList<>();
        exploreMode = enabled;

        if (exploreMode) {
            displayContentFolder(new VaultFileUtil(MyApp.mContext).getDir());
            exploreModePanel.setVisibility(View.VISIBLE);
        } else {
            currentFolderPath.setText(R.string.local_folder);

            for (String al : Hawk.get("albums", new ArrayList<String>()))
                folders.add(new File(al));
            /*for (Album album : ((App) getActivity().getApplicationContext()).getAlbums().albums) {
                folders.add(new File(album.getPath()));
            }*/
            exploreModePanel.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    public interface OnFolderSelected {
        void folderSelected(String path);
    }

    private class VolumeSpinnerAdapter extends ArrayAdapter<String> {

        VolumeSpinnerAdapter(Context context) {
            super(context, R.layout.spinner_item_with_pics, R.id.volume_name);
            insert(getString(R.string.internal_storage), INTERNAL_STORAGE);
            if (sdCardPath != null)
                add(getString(R.string.extrnal_storage));
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            GoogleMaterial.Icon icon;

            switch (position) {
                case INTERNAL_STORAGE:
                    icon = GoogleMaterial.Icon.gmd_storage;
                    break;
                default:
                    icon = GoogleMaterial.Icon.gmd_sd_card;
                    break;
            }

            ((ImageView) view.findViewById(R.id.volume_image)).setImageDrawable(new IconicsDrawable(getContext()).icon(icon).sizeDp(24).color(Color.WHITE));
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            GoogleMaterial.Icon icon;

            switch (position) {
                case INTERNAL_STORAGE:
                    icon = GoogleMaterial.Icon.gmd_storage;
                    break;
                default:
                    icon = GoogleMaterial.Icon.gmd_sd_card;
                    break;
            }
            ((ThemedIcons) view.findViewById(R.id.volume_image)).setIcon(icon);
            return view;
        }
    }

    private class BottomSheetAlbumsAdapter extends RecyclerView.Adapter<BottomSheetAlbumsAdapter.ViewHolder> {

        BottomSheetAlbumsAdapter() {
        }

        public BottomSheetAlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_folder_bottomsheet_item, parent, false);
            v.setOnClickListener(onClickListener);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BottomSheetAlbumsAdapter.ViewHolder holder, final int position) {

            File f = folders.get(position);

            holder.folderName.setText(f.getName());
            holder.folderName.setTag(f.getPath());

            if (canGoBack() && position == 0) { // go to parent folder
                holder.folderName.setText("..");
            }
        }

        public int getItemCount() {
            return folders.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView folderName;
            ThemedIcons imgFolder;
            LinearLayout llItemBackground;

            ViewHolder(View itemView) {
                super(itemView);
                folderName = itemView.findViewById(R.id.name_folder);
                imgFolder = itemView.findViewById(R.id.folder_icon_bottom_sheet_item);
                llItemBackground = itemView.findViewById(R.id.ll_album_bottom_sheet_item);
            }
        }
    }
}

