package com.photo.photography.act;

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
import com.photo.photography.repeater.RepeaterSelectedPhoto;
import com.photo.photography.frag.FragGalleryAlbumImage;

import java.util.ArrayList;


public class ActSelectPhoto extends ActBase implements RepeaterSelectedPhoto.OnDeleteButtonClickListener, FragGalleryAlbumImage.OnSelectImageListener {
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
        //add ads view
//        addNativeAdView(R.id.adsLayout);
        //inflate widgets
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
        mRecyclerView.setAdapter(mSelectedPhotoAdapter);
        // getFragmentManager().beginTransaction().replace(R.id.frame_container, new GalleryAlbumFragment()).commit();

        findViewById(R.id.tvStartCollageEditing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDoneSelection();
            }
        });
    }

    public void actionDoneSelection() {
        if (mSelectedImages.size() < mNeededImageCount && !mIsMaxImageCount) {
            Toast.makeText(this, mFormattedText, Toast.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
        /*if (fragment != null && fragment instanceof GalleryAlbumFragment) {
            super.onBackPressed();
        } else {*/
        getFragmentManager().popBackStack();
        //  }
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
            mSelectedImages.add(image);
            mSelectedPhotoAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mSelectedImages.size() - 1);
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
