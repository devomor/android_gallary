package com.photo.photography.collage.screen;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.collage.repeater.RepeaterSelectedPhoto;
import com.photo.photography.collage.db.table.TableItemPackage;
import com.photo.photography.collage.screen.frag.FragBase;
import com.photo.photography.collage.screen.frag.FragDownloadedPackage;
import com.photo.photography.collage.screen.frag.FragGalleryAlbumImage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sira on 3/7/2018.
 */
public class ActDownloadedPackage extends ActBase implements RepeaterSelectedPhoto.OnDeleteButtonClickListener,
        FragGalleryAlbumImage.OnSelectImageListener {
    public static final String EXTRA_IMAGE_COUNT = "imageCount";
    public static final String EXTRA_IS_MAX_IMAGE_COUNT = "isMaxImageCount";
    public static final String EXTRA_SELECTED_IMAGES = "selectedImages";
    private final ArrayList<String> mSelectedImages = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TextView mImageCountView;
    private RepeaterSelectedPhoto mSelectedPhotoAdapter;
    private int mNeededImageCount = 0;
    private boolean mIsMaxImageCount = false;
    //Showing messages
    private String mFormattedText;
    private String mFormattedWarningText;
    private String mPackageType = TableItemPackage.BACKGROUND_TYPE;

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_photo);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name_new);
        }
        mNeededImageCount = getIntent().getIntExtra(EXTRA_IMAGE_COUNT, 0);
        mIsMaxImageCount = getIntent().getBooleanExtra(EXTRA_IS_MAX_IMAGE_COUNT, false);
        mPackageType = getIntent().getStringExtra(FragDownloadedPackage.EXTRA_PACKAGE_TYPE);
        //show data
        if (TableItemPackage.BACKGROUND_TYPE.equals(mPackageType)) {
            mNeededImageCount = 1;
            mIsMaxImageCount = false;
        } else {
            mNeededImageCount = FragBase.MAX_NEEDED_PHOTOS;
            mIsMaxImageCount = true;
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.selectedImageRecyclerView);
        mImageCountView = (TextView) findViewById(R.id.imageCountView);
        //Warning messages
        if (!mIsMaxImageCount) {
            mFormattedText = String.format(getString(R.string.please_select_photo), mNeededImageCount);
        } else {
            mFormattedText = getString(R.string.please_select_photo_without_counting);
        }
        mImageCountView.setText(mFormattedText.concat("(0)"));
        mFormattedWarningText = String.format(getString(R.string.you_need_photo), mNeededImageCount);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSelectedPhotoAdapter = new RepeaterSelectedPhoto(mSelectedImages, this);
        mSelectedPhotoAdapter.setImageFitCenter(TableItemPackage.STICKER_TYPE.equals(mPackageType));

        mRecyclerView.setAdapter(mSelectedPhotoAdapter);

        FragDownloadedPackage fragment = new FragDownloadedPackage();
        Bundle bundle = new Bundle();
        bundle.putString(FragDownloadedPackage.EXTRA_PACKAGE_TYPE, mPackageType);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();

        findViewById(R.id.tvStartCollageEditing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedImages.size() < mNeededImageCount && !mIsMaxImageCount) {
                    Toast.makeText(ActDownloadedPackage.this, mFormattedText, Toast.LENGTH_SHORT).show();
                } else {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
                    setResult(RESULT_OK, data);
                    //log
                    if (mSelectedImages != null && mSelectedImages.size() > 0) {
                        for (String str : mSelectedImages) {
                            if (mPackageType != null && str != null && mPackageType.length() > 0 && str.length() > 0) {
                                String msg = mPackageType.concat("/");
                                File file = new File(str);
                                if (file.getParentFile() != null) {
                                    msg = msg.concat(file.getParentFile().getName());
                                }

                            }
                        }
                    }
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment != null && fragment instanceof FragDownloadedPackage) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDeleteButtonClick(int position) {
        mSelectedImages.remove(position);
        mSelectedPhotoAdapter.notifyDataSetChanged();
        mImageCountView.setText(mFormattedText.concat("(" + mSelectedImages.size() + ")"));
    }

    @Override
    public void onSelectImage(String image) {
        if (mSelectedImages.size() == mNeededImageCount) {
            Toast.makeText(this, mFormattedWarningText, Toast.LENGTH_SHORT).show();
        } else {
            mSelectedImages.remove(image);
            mSelectedImages.add(image);
            mSelectedPhotoAdapter.notifyDataSetChanged();
            mImageCountView.setText(mFormattedText.concat("(" + mSelectedImages.size() + ")"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                try {
                    onBackPressed();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    finish();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
