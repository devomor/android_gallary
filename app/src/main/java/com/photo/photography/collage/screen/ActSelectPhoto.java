package com.photo.photography.collage.screen;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.act.ActCollageCreate;
import com.photo.photography.collage.repeater.RepeaterSelectedPhoto;
import com.photo.photography.collage.model.DataTemplateItem;
import com.photo.photography.collage.screen.frag.FragGalleryAlbum;
import com.photo.photography.collage.screen.frag.FragGalleryAlbumImage;
import com.photo.photography.collage.util.TemplateImageUtil;
import com.photo.photography.collage.util.frames.FrameImageUtils;
import com.photo.photography.models.TemplateItemModel;
import com.photo.photography.templates.PhotosItem;

import java.util.ArrayList;

public class ActSelectPhoto extends ActBase implements RepeaterSelectedPhoto.OnDeleteButtonClickListener, FragGalleryAlbumImage.OnSelectImageListener {
    public static final String EXTRA_IMAGE_COUNT = "imageCount";
    public static final String EXTRA_IS_MAX_IMAGE_COUNT = "isMaxImageCount";
    public static final String EXTRA_SELECTED_IMAGES = "selectedImages";
    public static final String EXTRA_IS_FRAME_IMAGE = "frameImage";
    private final ArrayList<String> mSelectedImages = new ArrayList<>();
    private final int mNeededImageCount = 10;
    private final ArrayList<DataTemplateItem> mTemplateItemList = new ArrayList<DataTemplateItem>();
    private final ArrayList<DataTemplateItem> mAllTemplateItemList = new ArrayList<DataTemplateItem>();
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private TextView mImageCountView;
    private RepeaterSelectedPhoto mSelectedPhotoAdapter;
    private String mFormattedWarningText;
    private boolean mFrameImages = false;
    private int mImageInTemplateCount = 0;

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_photo);

        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));

        mFrameImages = getIntent().getBooleanExtra(EXTRA_IS_FRAME_IMAGE, false);
        loadFrameImages(!mFrameImages);

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
        getSupportActionBar().setTitle(getString(R.string.pick_photo));

        mRecyclerView = (RecyclerView) findViewById(R.id.selectedImageRecyclerView);
        mImageCountView = (TextView) findViewById(R.id.imageCountView);
        mImageCountView.setText("Please select photos between 2 to 10");
        mFormattedWarningText = String.format(getString(R.string.you_need_photo), mNeededImageCount);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSelectedPhotoAdapter = new RepeaterSelectedPhoto(mSelectedImages, this);
        mRecyclerView.setAdapter(mSelectedPhotoAdapter);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, new FragGalleryAlbum()).commit();

        findViewById(R.id.tvStartCollageEditing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDoneSelection();
            }
        });
    }

    private void loadFrameImages(boolean template) {
        mAllTemplateItemList.clear();
        if (template) {
            mAllTemplateItemList.addAll(TemplateImageUtil.loadTemplates());
        } else {
            mAllTemplateItemList.addAll(FrameImageUtils.loadFrameImages(this));
        }
        mTemplateItemList.clear();
        if (mImageInTemplateCount > 0) {
            for (DataTemplateItem item : mAllTemplateItemList)
                if (item.getPhotoItemList().size() == mImageInTemplateCount) {
                    mTemplateItemList.add(item);
                }
        } else {
            mTemplateItemList.addAll(mAllTemplateItemList);
        }
    }

    public void actionDoneSelection() {
        if (mSelectedImages.size() > 10) {
            Toast.makeText(ActSelectPhoto.this, "Collage option available for less than 10 Image Files", Toast.LENGTH_SHORT).show();

        } else if (mSelectedImages.size() < 2) {
            Toast.makeText(ActSelectPhoto.this, "Collage option available for more than 1 Image Files", Toast.LENGTH_SHORT).show();

        } else if (mSelectedImages.size() > 1 && mSelectedImages.size() < 11) {
            ArrayList<TemplateItemModel> mTemplateItemList = new ArrayList<>();
            mTemplateItemList.addAll(com.photo.photography.util.utils.frame.FrameImageUtils.loadFrameImages(ActSelectPhoto.this));

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

            Intent intent = new Intent(ActSelectPhoto.this, ActCollageCreate.class);
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
                MyApp.getInstance().showInterstitial(ActSelectPhoto.this, intent, true, -1, null);
            } else {
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment != null && fragment instanceof FragGalleryAlbum) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDeleteButtonClick(int position) {
        mSelectedImages.remove(position);
        mSelectedPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectImage(String image) {
        if (mSelectedImages.size() == mNeededImageCount) {
            Toast.makeText(this, mFormattedWarningText, Toast.LENGTH_SHORT).show();
        } else {
            mSelectedImages.add(image);
            mSelectedPhotoAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mSelectedImages.size() - 1);
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
