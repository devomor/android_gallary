package com.photo.photography.secure_vault;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.edit_views.SquareImagesView;
import com.photo.photography.util.PermissionUtil;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.models.SnapTrackImg;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.dworks.libs.astickyheader.SimpleSectionedGridAdapter;

public class ActSecretSnap extends AppCompatActivity {

    private static final int CAMERA_PERMISSIONS = 406;
    public static int VIEW_IMG_REQUEST_CODE = 28;

    @BindView(R.id.layout_no_data_found)
    LinearLayout mNoDataFoundLayout;

    @BindView(R.id.grid)
    GridView grid;

    @BindView(R.id.switchEnableSecretSnap)
    SwitchCompat switchEnableSecretSnap;

    ArrayList<SnapTrackImg> imgArrayList = new ArrayList<>();
    private Toolbar toolbar;
    private ImageAdapter mAdapter;
    private ArrayList<SimpleSectionedGridAdapter.Section> sections = new ArrayList<SimpleSectionedGridAdapter.Section>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_snap_track);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(getString(R.string.txt_view_secret_snaps));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        boolean isOn = preferences.getBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, false);
        switchEnableSecretSnap.setChecked(isOn);
        switchEnableSecretSnap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    if (PermissionUtil.isCameraPermissionsGranted(ActSecretSnap.this)) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, true);
                        editor.commit();

                    } else {
                        PermissionUtil.requestPermissions(this, CAMERA_PERMISSIONS, Manifest.permission.CAMERA);
                    }
                } else {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, false);
                    editor.commit();
                }
            }
        });

        if (com.photo.photography.secure_vault.utils.PermissionUtil.checkFileReadPermission(ActSecretSnap.this))
            startNextTask();
    }

    public String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void getFromSdcard() {
        imgArrayList = new ArrayList<>();
        File file = new VaultFileUtil(ActSecretSnap.this).getFile(VaultFileUtil.FOLDER_TO_SNAP);

        if (file.isDirectory()) {
            File[] listFile = file.listFiles();

            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].getAbsolutePath().contains("snapTrack")) {
                    String strPath = listFile[i].getAbsolutePath();
                    String imgName = strPath.substring(strPath.lastIndexOf("/") + 1, strPath.lastIndexOf("."));
                    String miliSec;
                    if (imgName.contains("_")) {
                        miliSec = (imgName.substring(imgName.indexOf("_") + 1)).replace("_", ".");
                    } else {
                        miliSec = imgName.substring(9);
                    }
                    String captureDate = getDate(Long.parseLong(miliSec), "dd MMM yyyy");// 22 Sep 2016
                    Log.e("TAG", "Path : " + strPath + " || Miliseconds : " + miliSec + " || Date : " + captureDate);
                    SnapTrackImg model = new SnapTrackImg();
                    model.setImgPath(listFile[i].getAbsolutePath());
                    model.setImgName(imgName);
                    model.setCapMillis(miliSec);
                    imgArrayList.add(model);
                }
            }
            Log.e("TAG", "Size of all snapTrack image array : " + imgArrayList.size());
            Collections.sort(imgArrayList, new Comparator<SnapTrackImg>() {
                final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

                @Override
                public int compare(SnapTrackImg t1, SnapTrackImg t2) {
                    Date d1 = null;
                    Date d2 = null;
                    try {
                        d1 = sdf.parse(getDate(Long.parseLong(t1.getCapMillis()), "dd MMM yyyy"));
                        d2 = sdf.parse(getDate(Long.parseLong(t2.getCapMillis()), "dd MMM yyyy"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return (d1.getTime() > d2.getTime() ? -1 : 1);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS:
                boolean gotPermission = grantResults.length > 0;

                for (int result : grantResults) {
                    gotPermission &= result == PackageManager.PERMISSION_GRANTED;
                }

                if (gotPermission) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, true);
                    editor.commit();

                } else {
                    switchEnableSecretSnap.setChecked(false);
                    Toast.makeText(ActSecretSnap.this, getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
                }
                break;

            case com.photo.photography.secure_vault.utils.PermissionUtil.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startNextTask();
                } else {
                    Toast.makeText(ActSecretSnap.this, R.string.txt_file_access_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startNextTask() {
        getFromSdcard();
        initControls();
    }

    private void initControls() {
        sections = new ArrayList<>();
        mAdapter = new ImageAdapter(ActSecretSnap.this);
        List arrTemp = new ArrayList();
        for (int p = 0; p < imgArrayList.size(); p++) {
            String date = getDate(Long.parseLong(imgArrayList.get(p).getCapMillis()), "dd MMM yyyy");
            if (!arrTemp.contains(date)) {
                arrTemp.add(date);
                sections.add(new SimpleSectionedGridAdapter.Section(p, date));
                Log.e("TAG", "Section Array Size : " + sections.size() + " || Header : " + sections.get(sections.size() - 1).getTitle());
            }
        }

        if (imgArrayList.size() > 0)
            mNoDataFoundLayout.setVisibility(View.GONE);
        else
            mNoDataFoundLayout.setVisibility(View.VISIBLE);

        SimpleSectionedGridAdapter simpleSectionedGridAdapter = new SimpleSectionedGridAdapter(ActSecretSnap.this, mAdapter, R.layout.row_grid_item_headers, R.id.header_layout, R.id.header);
        simpleSectionedGridAdapter.setGridView(grid);
        simpleSectionedGridAdapter.setSections(sections.toArray(new SimpleSectionedGridAdapter.Section[0]));
        grid.setAdapter(simpleSectionedGridAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == VIEW_IMG_REQUEST_CODE) {
            refreshActivity();
//            Toast.makeText(SecretSnapTrackActivity.this, "ResultOk", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshActivity() {
        startNextTask();
    }

    private class ImageAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        public ImageAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imgArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final RowHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_snap_tracks, parent, false);
                holder = new RowHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RowHolder) convertView.getTag();
            }

            if (holder.mIcon.getTag() != null) {
                ((ImageGetter) holder.mIcon.getTag()).cancel(true);
            }
            ImageGetter task = new ImageGetter(holder.mIcon);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new File(imgArrayList.get(position).getImgPath()));
            holder.mIcon.setTag(task);
            holder.mIcon.setId(position);
            holder.mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(SecretSnapTrackActivity.this, "Item Clicked on : " + holder.mIcon.getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActSecretSnap.this, ActSecretSnapView.class);
                    intent.putExtra("imgPath", imgArrayList.get(holder.mIcon.getId()).getImgPath());
                    intent.putExtra("imgCapMilli", imgArrayList.get(holder.mIcon.getId()).getCapMillis());
                    //startActivityForResult(intent, VIEW_IMG_REQUEST_CODE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(ActSecretSnap.this, view, "snap");
                        startActivityForResult(intent, VIEW_IMG_REQUEST_CODE, options.toBundle());
                    } else
                        startActivityForResult(intent, VIEW_IMG_REQUEST_CODE);
                }
            });
            return convertView;
        }
    }

    public class ImageGetter extends AsyncTask<File, Void, Bitmap> {
        private final SquareImagesView iv;

        public ImageGetter(SquareImagesView v) {
            iv = v;
        }

        @Override
        protected Bitmap doInBackground(File... params) {
            return SupportClass.decodeSampledBitmapFromFile(params[0].getAbsolutePath(), 100, 100);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            iv.setRotation(270);
            iv.setImageBitmap(result);
        }
    }

    public class RowHolder {
        @BindView(R.id.square_image)
        SquareImagesView mIcon;

        public RowHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
