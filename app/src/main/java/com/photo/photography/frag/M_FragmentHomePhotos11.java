package com.photo.photography.frag;

import android.content.UriPermission;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.photo.photography.R;
import com.photo.photography.models.StatusModel;
import com.photo.photography.repeater.RepeaterImage;
import com.photo.photography.util.Common;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;


public class M_FragmentHomePhotos11 extends Fragment {

    private final List<StatusModel> imagesList = new ArrayList<>();
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerViewImage)
    RecyclerView recyclerViewImage;
    @BindView(R.id.ivNoFilesFound)
    ImageView ivNoFilesFound;
    @BindView(R.id.prgressBarImage)
    ProgressBar prgressBarImage;
    @BindView(R.id.image_container)
    RelativeLayout image_container;
    private RepeaterImage imageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_images, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_dark)
                , ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark),
                ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
                ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_dark));

        swipeRefreshLayout.setOnRefreshListener(this::getStatus);

        recyclerViewImage.setHasFixedSize(true);
        recyclerViewImage.setLayoutManager(new GridLayoutManager(getActivity(), Common.GRID_COUNT));

        getStatus();

    }

    private void getStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            executeNew();

        } else if (Common.STATUS_DIRECTORY.exists()) {

            executeOld();

        } else {
            ivNoFilesFound.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Cannot find WhatsApp Dir", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private void executeOld() {

        new Thread(() -> {

            Handler mainHandler = new Handler(Looper.getMainLooper());

            File[] statusFiles;
            statusFiles = Common.STATUS_DIRECTORY.listFiles();
            imagesList.clear();

            if (statusFiles != null && statusFiles.length > 0) {

                Arrays.sort(statusFiles);
                for (File file : statusFiles) {
                    StatusModel status = new StatusModel(file, file.getName(), file.getAbsolutePath());

                    if (!status.isVideo() && status.getTitle().endsWith(".jpg")) {
                        imagesList.add(status);
                    }

                }

                mainHandler.post(() -> {

                    if (imagesList.size() <= 0) {
                        ivNoFilesFound.setVisibility(View.VISIBLE);
                    } else {
                        ivNoFilesFound.setVisibility(View.GONE);
                    }

                    imageAdapter = new RepeaterImage(imagesList, image_container);
                    recyclerViewImage.setAdapter(imageAdapter);
                    imageAdapter.notifyDataSetChanged();
//                    imageAdapter.notifyItemRangeChanged(0, imagesList.size());
                    prgressBarImage.setVisibility(View.GONE);
                });

            } else {

                mainHandler.post(() -> {
                    prgressBarImage.setVisibility(View.GONE);
                    ivNoFilesFound.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No Files Found", Toast.LENGTH_SHORT).show();
                });

            }
            swipeRefreshLayout.setRefreshing(false);

        }).start();
    }

    private void executeNew() {

        Executors.newSingleThreadExecutor().execute(() -> {
            Handler mainHandler = new Handler(Looper.getMainLooper());

            List<UriPermission> list = requireActivity().getContentResolver().getPersistedUriPermissions();

            DocumentFile file = DocumentFile.fromTreeUri(requireActivity(), list.get(0).getUri());

            imagesList.clear();

            if (file == null) {
                mainHandler.post(() -> {
                    prgressBarImage.setVisibility(View.GONE);
                    ivNoFilesFound.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No Files Found", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
                return;
            }

            DocumentFile[] statusFiles = file.listFiles();

            if (statusFiles.length <= 0) {
                mainHandler.post(() -> {
                    prgressBarImage.setVisibility(View.GONE);
                    ivNoFilesFound.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No Files Found", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
                return;
            }

            for (DocumentFile documentFile : statusFiles) {
                StatusModel status = new StatusModel(documentFile);

                if (!status.isVideo()) {
                    imagesList.add(status);
                }
            }

            Log.e("HEY: ", String.valueOf(imagesList.size()));

            mainHandler.post(() -> {

                if (imagesList.size() <= 0) {
                    ivNoFilesFound.setVisibility(View.VISIBLE);
                } else {
                    ivNoFilesFound.setVisibility(View.GONE);
                }

                imageAdapter = new RepeaterImage(imagesList, image_container);
                recyclerViewImage.setAdapter(imageAdapter);
                imageAdapter.notifyItemRangeChanged(0, imagesList.size());
                prgressBarImage.setVisibility(View.GONE);
            });

        });
    }

}
