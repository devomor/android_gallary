package com.photo.photography.secure_vault.frag;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.act.ActMain;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.ActFullScreenImageViewer;
import com.photo.photography.secure_vault.ActPickImage;
import com.photo.photography.secure_vault.repeater.RepeaterPrivateVaultGrid;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.helper.Constants;
import com.photo.photography.secure_vault.helper.DbHandler;
import com.photo.photography.secure_vault.utils.CryptoExceptionUtils;
import com.photo.photography.secure_vault.utils.CryptoUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragPrivateVaultVideo extends FragBase {

    private final int CAMERA_READ_EXTERNAL_STORAGE_PERMISSIONS = 101;
    private final int CAMERA_READ_WRITE_EXTERNAL_STORAGE_PERMISSIONS = 100;
    public boolean enableSelectionMode = false;
    @BindView(R.id.gridViewFromMediaImage)
    GridView mGridView;
    @BindView(R.id.file_not_found_view)
    LinearLayout mFileNotFound;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    int SELECTION_REQUEST_CODE = 147;
    RepeaterPrivateVaultGrid mImageAdapter;
    DbHandler db;
    ArrayList<VaultFile> mGalleryModelList = new ArrayList<>();
    ArrayList<String> mSelectedGalleryModelList = new ArrayList<>();
    Activity activity;
    ArrayList<String> myList = new ArrayList<>();
    ImageView mCloseRateNowIcon;
    TextView mGoodRateNow, mBadRateNow;
    RelativeLayout mLayoutRateNow;
    View listHeader;
//    FirebaseAnalytics mFirebaseAnalytics;
    private OnVideoSelectedListener mCallback;
    private Boolean isFabOpen = false;
    private Animation rotate_forward, rotate_backward;

    @OnClick(R.id.fab)
    public void ClickOnFab() {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.vaultButtonAdd, Constants.video);
//        mFirebaseAnalytics.logEvent(Constants.vault, bundle);

        if (!SupportClass.isExternalStoragePermissionGranted(mActivity)) {
            SupportClass.userAction = SupportClass.USER_ACTION.MOVE_TO_VAULT;
            SupportClass.showTakeWritePermissionDialog(mActivity);
        } else {
            doAddRemoveFabAction();
        }
    }

    private void doAddRemoveFabAction() {
//        Toast.makeText(activity, "PickFile MMMMMMMMMM", Toast.LENGTH_SHORT).show();
        if (isFabOpen) {
            // Decrypt File
            new DecryptFile().execute("");
        } else {
            if (isPermissionGranted()) {
                pickFile();
            } else {
                takePermission();
            }
        }
    }

    private void takePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{CAMERA, READ_EXTERNAL_STORAGE}, CAMERA_READ_EXTERNAL_STORAGE_PERMISSIONS);

        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, CAMERA_READ_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }
    }

    public boolean isPermissionGranted() {
        int cameraePermission = ContextCompat.checkSelfPermission(mActivity, CAMERA);
        int readPermission = ContextCompat.checkSelfPermission(mActivity, READ_EXTERNAL_STORAGE);
        return cameraePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPause() {
        saveDataInFile();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void pickFile() {
        // Pick File
        Intent intent = new Intent(requireActivity(), ActPickImage.class);
        intent.putExtra(ActPickImage.OPEN_FOR_IMAGE, false);
        requireActivity().startActivityForResult(intent, SELECTION_REQUEST_CODE);
        Toast.makeText(requireActivity(), "Pick Video", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnVideoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnImageSelectedListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshGridview();
            Log.e("TAG", "Refresh private vault image fragment");
        } else if (requestCode == 590) {
            if (SupportClass.userAction == SupportClass.USER_ACTION.MOVE_TO_VAULT) {
                if (SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                    doAddRemoveFabAction();
                }
            }
            SupportClass.userAction = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 591) {
            if (SupportClass.userAction == SupportClass.USER_ACTION.MOVE_TO_VAULT) {
                if (SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                    doAddRemoveFabAction();
                }
            }
            SupportClass.userAction = null;

        } else if (requestCode == CAMERA_READ_EXTERNAL_STORAGE_PERMISSIONS) {
            doAddRemoveFabAction();

        } else if (requestCode == CAMERA_READ_WRITE_EXTERNAL_STORAGE_PERMISSIONS) {
            doAddRemoveFabAction();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_privatevault_video, container, false);
        ButterKnife.bind(this, view);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        mFileNotFound.setVisibility(View.GONE);
        rotate_forward = AnimationUtils.loadAnimation(requireActivity(), R.anim.anim_rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(requireActivity(), R.anim.anim_rotate_backward);
        setAdapter();
        return view;
    }

    public void refreshGridview() {
        setAdapter();
    }

    public void onVideoSelected(int count) {
        selectFabButton();
    }

    public void selectFabButton() {
        myList = new ArrayList<>();
        ArrayList<String> VideoList = getSelectedVideoList();
        myList.addAll(VideoList);
        Log.e("TAG", "Total " + myList.size() + " Media Selected Item : " + myList.toString());
        if (myList.size() == 0) {
            if (isFabOpen) {
                fab.startAnimation(rotate_backward);
                isFabOpen = false;
                Log.d("TAG", "FAB Click close");
            }
        } else if (myList.size() == 1) {
            if (!isFabOpen) {
                fab.startAnimation(rotate_forward);
                isFabOpen = true;
                Log.d("TAG", "FAB Click open");
            }
        }
    }

    public void decryptCallBroadCast(String filename) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(filename)));
        requireActivity().sendBroadcast(intent);
    }

    private void setAdapter() {
        if (isAdded()) {
            LayoutInflater layoutinflater = mActivity.getLayoutInflater();
            listHeader = (View) layoutinflater.inflate(R.layout.layout_headers_grid, mGridView, false);
            mCloseRateNowIcon = (ImageView) listHeader.findViewById(R.id.thumb_close);
            mGoodRateNow = (TextView) listHeader.findViewById(R.id.txt_good);
            mBadRateNow = (TextView) listHeader.findViewById(R.id.txt_bad);
            mLayoutRateNow = (RelativeLayout) listHeader.findViewById(R.id.mlayout_ratenow);
            if (SupportClass.getBoolean(SupportClass.APP_RATE_BY_USER))
                mLayoutRateNow.setVisibility(View.GONE);

            mGoodRateNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Uri uri = Uri.parse("market://details?id=" + mActivity.getPackageName());
                    Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    SupportClass.saveBoolean(true, SupportClass.APP_RATE_BY_USER);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("market://details?id=" + mActivity.getPackageName())));
                    }
                }
            });
            enableSelectionMode = false;
            mGalleryModelList = new ArrayList<>();
            db = new DbHandler(requireActivity());
            mGalleryModelList = db.getAllTypeWiseRecords(Constants.FILE_TYPE_VIDEO);
            Log.e("TAG", "Total Video on Database : " + mGalleryModelList.size());

            if (mGalleryModelList.size() > 0) {
                mImageAdapter = new RepeaterPrivateVaultGrid(requireActivity(), mGalleryModelList, true);
                mGridView.setAdapter(mImageAdapter);
                mGridView.setVisibility(View.VISIBLE);
                mFileNotFound.setVisibility(View.GONE);
            } else {
                mGridView.setVisibility(View.GONE);
                mFileNotFound.setVisibility(View.VISIBLE);
            }

            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    enableSelectionMode = true;
                    RepeaterPrivateVaultGrid adapter = (RepeaterPrivateVaultGrid) parent.getAdapter();
                    if (mGalleryModelList.get(position).getIsSelected()) {
                        VaultFile model = mGalleryModelList.get(position);
                        model.setIsSelected(false);
                        mGalleryModelList.set(position, model);
                        mSelectedGalleryModelList.remove(String.valueOf(mGalleryModelList.get(position).getID()).trim());
                    } else {
                        VaultFile model = mGalleryModelList.get(position);
                        model.setIsSelected(true);
                        mGalleryModelList.set(position, model);
                        mSelectedGalleryModelList.add(String.valueOf(mGalleryModelList.get(position).getID()));
                    }
                    adapter.notifyDataSetChanged();
                    if (mCallback != null) {
                        mCallback.onVideoSelected(mSelectedGalleryModelList.size());
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("list", mSelectedGalleryModelList);
                        requireActivity().setResult(Activity.RESULT_OK, intent);
                        Log.e("TAG", "Total selected item Video : " + mSelectedGalleryModelList.size());
                    }

                    enableSelectionMode = mSelectedGalleryModelList.size() > 0;
                    return true;
                }
            });

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // update the mStatus of each category in the adapter
                    if (enableSelectionMode) {
                        RepeaterPrivateVaultGrid adapter = (RepeaterPrivateVaultGrid) parent.getAdapter();

                        if (mGalleryModelList.get(position).getIsSelected()) {
                            VaultFile model = mGalleryModelList.get(position);
                            model.setIsSelected(false);
                            mGalleryModelList.set(position, model);
                            mSelectedGalleryModelList.remove(String.valueOf(mGalleryModelList.get(position).getID()).trim());
                        } else {
                            VaultFile model = mGalleryModelList.get(position);
                            model.setIsSelected(true);
                            mGalleryModelList.set(position, model);
                            mSelectedGalleryModelList.add(String.valueOf(mGalleryModelList.get(position).getID()));
                        }
                        adapter.notifyDataSetChanged();

                        if (mCallback != null) {
                            mCallback.onVideoSelected(mSelectedGalleryModelList.size());
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra("list", mSelectedGalleryModelList);
                            requireActivity().setResult(Activity.RESULT_OK, intent);
                            Log.e("TAG", "Total selected item Video : " + mSelectedGalleryModelList.size());
                        }

                        enableSelectionMode = mSelectedGalleryModelList.size() > 0;
                    } else {
//                    Toast.makeText(activity, "View Video", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mActivity, ActFullScreenImageViewer.class);
                        i.putExtra("data", mGalleryModelList);
                        i.putExtra("position", position);
                        i.putExtra("isVideo", true);
//                    mActivity.startActivityForResult(i, 10);
                        if (MyApp.getInstance().needToShowAd()) {
                            MyApp.getInstance().showInterstitial(mActivity, i, false, -1, null);
                        } else {
                            mActivity.startActivity(i);
                        }
                    }
                }
            });
        }
    }

    public ArrayList<String> getSelectedVideoList() {
        return mSelectedGalleryModelList;
    }

    // Container Activity must implement this interface
    public interface OnVideoSelectedListener {
        void onVideoSelected(int count);
    }

    class DecryptFile extends AsyncTask<String, String, String> {

        // Progress Dialog
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
                pDialog = new ProgressDialog(requireActivity());
            pDialog.setMessage("Processing file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            if (pDialog != null && pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            db = new DbHandler(requireActivity());
            for (int i = 0; i < myList.size(); i++) {
                try {
                    VaultFile dataModel = db.getRecord(myList.get(i));
                    if (new File(dataModel.getNewPath()).exists()) {
                        Log.e("DecryptFile", "Decrypt file : " + dataModel.getNewPath() + " to " + dataModel.getOldPath());
                        CryptoUtil.decrypt(mActivity, new File(dataModel.getNewPath()), new File(dataModel.getOldPath()));
                        db.deleteRecord(dataModel.getID());
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            new File(dataModel.getNewPath()).delete();
                            new File(dataModel.getThumbnail()).delete();
                            decryptCallBroadCast(dataModel.getOldPath());
                            Log.e("DecryptFile", "Delete file from if loop IF part");
                        }
                    } else {
                        db.deleteRecord(dataModel.getID());
                        Log.e("DecryptFile", "Delete file from if loop ELSE part");
                    }
                } catch (OutOfMemoryError e) {
                    Log.e("DecryptFile", "OutOfMemoryError Error in Decryption of file : " + e.getLocalizedMessage());
                } catch (CryptoExceptionUtils e) {
                    Log.e("DecryptFile", "CryptoException Error in Decryption of file : " + e.getLocalizedMessage());
                } catch (Exception e) {
                    Log.e("DecryptFile", "Exception Error in Decryption of file : " + e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            mSelectedGalleryModelList = new ArrayList<>();
            refreshGridview();
            if (isFabOpen) {
                fab.startAnimation(rotate_backward);
                isFabOpen = false;
                Log.d("DecryptFile", "FAB Click close");
            }
            Log.d("DecryptFile", "Successfully file removed from Your Private Vault.");
            ActMain.IS_VAULT_CHANGED = true;
            Toast.makeText(requireActivity(), "Successfully file removed from\nYour Private Vault.", Toast.LENGTH_SHORT).show();
        }
    }
}
