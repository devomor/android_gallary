package com.photo.photography.act;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.db.tables.ItemPackageTables;
import com.photo.photography.frag.FragDownloadedPackage;
import com.photo.photography.models.TemplateItemModel;
import com.photo.photography.repeater.RepeaterHorizontalPreviewTemplate;
import com.photo.photography.templates.PhotosItem;
import com.photo.photography.util.actions.QuickActions;
import com.photo.photography.util.actions.QuickActionsItem;
import com.photo.photography.util.configs.AppLog;
import com.photo.photography.util.configs.Constants;
import com.photo.photography.util.multi_touch.helper.ImageEntityHelper;
import com.photo.photography.util.multi_touch.helper.MultiTouchEntityHelper;
import com.photo.photography.util.multi_touch.helper.TextEntityHelper;
import com.photo.photography.util.multi_touch.view.CallbackOnDoubleClick;
import com.photo.photography.util.multi_touch.view.PhotosView;
import com.photo.photography.util.utils.DateTimeUtil;
import com.photo.photography.util.utils.DialogUtil;
import com.photo.photography.util.utils.ImageUtil;
import com.photo.photography.util.utils.ResultContainers;
import com.photo.photography.util.utils.TemplateImageUtil;
import com.photo.photography.util.utils.frame.FrameImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public abstract class ActBaseTemplateDetail extends ActBasePhoto implements RepeaterHorizontalPreviewTemplate.OnPreviewTemplateClickListener, CallbackOnDoubleClick {
    public static final String EXTRA_IMAGE_IN_TEMPLATE_COUNT = "imageInTemplateCount";
    public static final String EXTRA_IS_FRAME_IMAGE = "frameImage";
    public static final String EXTRA_SELECTED_TEMPLATE_INDEX = "selectedTemplateIndex";
    public static final String EXTRA_IMAGE_PATHS = "imagePaths";

    protected static final int RATIO_SQUARE = 0;
    protected static final int RATIO_FIT = 1;
    protected static final int RATIO_GOLDEN = 2;
    private static final String TAG = ActBaseTemplateDetail.class.getSimpleName();
    private static final String PREF_NAME = "templateDetailPref";
    private static final String RATIO_KEY = "ratio";
    //action id
    private static final int ID_EDIT = 1;
    private static final int ID_DELETE = 2;
    private static final int ID_CANCEL = 3;

    protected RelativeLayout mContainerLayout;
    protected RecyclerView mTemplateView;
    protected PhotosView mPhotoView;
    protected float mOutputScale = 1;
    protected Dialog mAddImageDialog;
    protected View mAddImageView;
    protected Animation mAnimation;
    protected int mItemType = Constants.NORMAL_IMAGE_ITEM;
    protected TemplateItemModel mSelectedTemplateItem;
    protected ArrayList<TemplateItemModel> mTemplateItemList = new ArrayList<>();
    protected RepeaterHorizontalPreviewTemplate mTemplateAdapter;
    protected List<String> mSelectedPhotoPaths = new ArrayList<>();
    protected int mLayoutRatio = RATIO_SQUARE;
    protected SharedPreferences mPreferences;

    private int mImageInTemplateCount = 0;
    private Dialog mRatioDialog;
    private SharedPreferences mPref;
    private ImageEntityHelper mSelectedEntity = null;
    //    private QuickAction mTextQuickAction;
    private QuickActions mStickerQuickAction;
    private boolean mIsFrameImage = true;
    private boolean mClickedShareButton = false;
    private boolean mClickedSaveButton = false;

    //abstract methods
    protected abstract int getLayoutId();

    protected abstract void buildLayout(TemplateItemModel templateItem);

    protected abstract Bitmap createOutputImage();

    protected boolean isShowingAllTemplates() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.d(TAG, "onCreate, savedInstanceState=" + savedInstanceState);
        setContentView(getLayoutId());
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.collage);
        }

        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));

        mPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mLayoutRatio = mPref.getInt(RATIO_KEY, RATIO_SQUARE);
        mImageInTemplateCount = getIntent().getIntExtra(EXTRA_IMAGE_IN_TEMPLATE_COUNT, 0);
        mIsFrameImage = getIntent().getBooleanExtra(EXTRA_IS_FRAME_IMAGE, true);
        int selectedItemIndex = getIntent().getIntExtra(EXTRA_SELECTED_TEMPLATE_INDEX, 0);
        final ArrayList<String> extraImagePaths = getIntent().getStringArrayListExtra(EXTRA_IMAGE_PATHS);
        //pref
        mPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        mContainerLayout = (RelativeLayout) findViewById(R.id.containerLayout);
        mTemplateView = (RecyclerView) findViewById(R.id.templateView);
        mPhotoView = new PhotosView(this);
        mPhotoView.setOnDoubleClickListener(this);
        createQuickAction();
        //Option dialog
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_bottom);
        mAddImageDialog = DialogUtil.createAddImageDialog(this, this, false);
        mAddImageDialog.findViewById(R.id.cameraView).setVisibility(View.GONE);
        mAddImageDialog.findViewById(R.id.dividerCameraView).setVisibility(View.GONE);
        mAddImageDialog.findViewById(R.id.galleryView).setVisibility(View.GONE);
        mAddImageDialog.findViewById(R.id.dividerGalleryView).setVisibility(View.GONE);
        mAddImageView = mAddImageDialog.findViewById(R.id.dialogAddImage);
        //loading data
        if (savedInstanceState != null) {
            mClickedShareButton = savedInstanceState.getBoolean("mClickedShareButton", false);
            mClickedSaveButton = savedInstanceState.getBoolean("mClickedSaveButton", false);
            final int idx = savedInstanceState.getInt("mSelectedTemplateItemIndex", 0);
            mImageInTemplateCount = savedInstanceState.getInt("mImageInTemplateCount", 0);
            mIsFrameImage = savedInstanceState.getBoolean("mIsFrameImage", false);
            loadFrameImages(mIsFrameImage);
            AppLog.d(TAG, "onCreate, mTemplateItemList size=" + mTemplateItemList.size() + ", selected idx=" + idx + ", mImageInTemplateCount=" + mImageInTemplateCount);
            if (idx < mTemplateItemList.size() && idx >= 0)
                mSelectedTemplateItem = mTemplateItemList.get(idx);
            if (mSelectedTemplateItem != null) {
                ArrayList<String> imagePaths = savedInstanceState.getStringArrayList("photoItemImagePaths");
                if (imagePaths != null) {
                    int size = Math.min(imagePaths.size(), mSelectedTemplateItem.getPhotoItemList().size());
                    for (int i = 0; i < size; i++)
                        mSelectedTemplateItem.getPhotoItemList().get(i).imagePath = imagePaths.get(i);
                }
            }
            ArrayList<MultiTouchEntityHelper> entities = savedInstanceState.getParcelableArrayList("mPhotoViewImageEntities");
            if (entities != null) {
                mPhotoView.setImageEntities(entities);
            }
        } else {
            loadFrameImages(mIsFrameImage);
            if (selectedItemIndex >= mTemplateItemList.size()) {
                selectedItemIndex = mTemplateItemList.size() - 1;
            }
            mSelectedTemplateItem = mTemplateItemList.get(selectedItemIndex);
            mSelectedTemplateItem.setSelected(true);
            if (extraImagePaths != null) {
                int size = Math.min(extraImagePaths.size(), mSelectedTemplateItem.getPhotoItemList().size());
                for (int i = 0; i < size; i++)
                    mSelectedTemplateItem.getPhotoItemList().get(i).imagePath = extraImagePaths.get(i);
            }
        }

        mTemplateAdapter = new RepeaterHorizontalPreviewTemplate(mTemplateItemList, this);
        //Show templates
        mTemplateView.setHasFixedSize(true);
        mTemplateView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTemplateView.setAdapter(mTemplateAdapter);
        //Create after initializing
        mContainerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mOutputScale = ImageUtil.calculateOutputScaleFactor(mContainerLayout.getWidth(), mContainerLayout.getHeight());
                buildLayout(mSelectedTemplateItem);
                // remove listener
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContainerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mContainerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

//        showAddingImageOptions(false, false);
        //Scroll to selected item
        if (mTemplateItemList != null && selectedItemIndex >= 0 && selectedItemIndex < mTemplateItemList.size()) {
            mTemplateView.scrollToPosition(selectedItemIndex);
        }
    }

    private void loadFrameImages(boolean isFrameImage) {
        ArrayList<TemplateItemModel> mAllTemplateItemList = new ArrayList<>();
        if (!isFrameImage) {
            mAllTemplateItemList.addAll(TemplateImageUtil.loadTemplates());
        } else {
            mAllTemplateItemList.addAll(FrameImageUtils.loadFrameImages(this));
        }

        mTemplateItemList = new ArrayList<>();
        if (mImageInTemplateCount > 0) {
            for (TemplateItemModel item : mAllTemplateItemList)
                if (item.getPhotoItemList().size() == mImageInTemplateCount) {
                    mTemplateItemList.add(item);
                }
        } else {
            mTemplateItemList.addAll(mAllTemplateItemList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int idx = mTemplateItemList.indexOf(mSelectedTemplateItem);
        if (idx < 0) idx = 0;
        AppLog.d(TAG, "onSaveInstanceState, idx=" + idx);
        outState.putInt("mSelectedTemplateItemIndex", idx);
        //saved all image path of template item
        ArrayList<String> imagePaths = new ArrayList<>();
        for (PhotosItem item : mSelectedTemplateItem.getPhotoItemList()) {
            if (item.imagePath == null) item.imagePath = "";
            imagePaths.add(item.imagePath);
        }
        outState.putStringArrayList("photoItemImagePaths", imagePaths);
        outState.putParcelableArrayList("mPhotoViewImageEntities", mPhotoView.getImageEntities());
        outState.putInt("mImageInTemplateCount", mImageInTemplateCount);
        outState.putBoolean("mIsFrameImage", mIsFrameImage);
        outState.putBoolean("mClickedShareButton", mClickedShareButton);
        outState.putBoolean("mClickedSaveButton", mClickedSaveButton);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLog.d("PhotoCollageFragment.onPause",
                "onPause: width=" + mPhotoView.getWidth() + ", height = "
                        + mPhotoView.getHeight());
        mPhotoView.unloadImages();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.d("PhotoCollageFragment.onResume",
                "onResume: width=" + mPhotoView.getWidth() + ", height = "
                        + mPhotoView.getHeight());
        mPhotoView.loadImages(this);
        mPhotoView.invalidate();
        if (mClickedShareButton) {
            mClickedShareButton = false;

        } else if (mClickedSaveButton) {
            mClickedSaveButton = false;

        }
    }

    private void createQuickAction() {
        QuickActionsItem editItem = new QuickActionsItem(ID_EDIT, getString(R.string.edit), getResources().getDrawable(R.drawable.icon_menu_edit));
        QuickActionsItem deleteItem = new QuickActionsItem(ID_DELETE, getString(R.string.delete), getResources().getDrawable(R.drawable.icon_menu_delete));
        QuickActionsItem cancelItem = new QuickActionsItem(ID_CANCEL, getString(R.string.cancel), getResources().getDrawable(R.drawable.icon_menu_cancel));

        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
        editItem.setSticky(true);
        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
//        mTextQuickAction = new QuickAction(this, QuickAction.HORIZONTAL);
        mStickerQuickAction = new QuickActions(this, QuickActions.HORIZONTAL);
        //add action items into QuickAction
//        mTextQuickAction.addActionItem(editItem);
//        mTextQuickAction.addActionItem(deleteItem);
//        mTextQuickAction.addActionItem(cancelItem);
        mStickerQuickAction.addActionItem(deleteItem);
        mStickerQuickAction.addActionItem(cancelItem);
        //Set listener for action item clicked
//        mTextQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
//            @Override
//            public void onItemClick(QuickAction source, int pos, int actionId) {
//                QuickActionItem quickActionItem = mTextQuickAction.getActionItem(pos);
//                mTextQuickAction.dismiss();
//                //here we can filter which action item was clicked with pos or actionId parameter
//                if (actionId == ID_DELETE) {
//                    mPhotoView.removeImageEntity(mSelectedEntity);
//                } else if (actionId == ID_EDIT) {
//                    if (mSelectedEntity instanceof TextEntity) {
//                        TextDrawable textDrawable = (TextDrawable) ((TextEntity) mSelectedEntity).getDrawable();
//                        editTextItem(textDrawable.getText(), textDrawable.getTypefacePath(), textDrawable.getTextColor());
//                    }
//                } else if (actionId == ID_CANCEL) {
//
//                }
//            }
//        });
        //Set listener for action item clicked
        mStickerQuickAction.setOnActionItemClickListener(new QuickActions.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickActions source, int pos, int actionId) {
                QuickActionsItem quickActionItem = mStickerQuickAction.getActionItem(pos);
                mStickerQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    mPhotoView.removeImageEntity(mSelectedEntity);
                } else if (actionId == ID_CANCEL) {

                }
            }
        });
        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
        //by clicking the area outside the dialog.
//        mTextQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//            }
//        });

    }

    @Override
    public void onPhotoViewDoubleClick(PhotosView view, MultiTouchEntityHelper entity) {
        mSelectedEntity = (ImageEntityHelper) entity;
        /*if (mSelectedEntity instanceof TextEntity) {
            mTextQuickAction.show(view, (int) mSelectedEntity.getCenterX(), (int) mSelectedEntity.getCenterY());
        } else {*/
        mStickerQuickAction.show(view, (int) mSelectedEntity.getCenterX(), (int) mSelectedEntity.getCenterY());
//        }
    }

    @Override
    public void onBackgroundDoubleClick() {

    }

    @Override
    public void onPreviewTemplateClick(int position) {
        TemplateItemModel item = mTemplateItemList.get(position);
        mSelectedTemplateItem.setSelected(false);
        for (int idx = 0; idx < mSelectedTemplateItem.getPhotoItemList().size(); idx++) {
            PhotosItem photoItem = mSelectedTemplateItem.getPhotoItemList().get(idx);
            if (photoItem.imagePath != null && photoItem.imagePath.length() > 0) {
                if (idx < mSelectedPhotoPaths.size()) {
                    mSelectedPhotoPaths.add(idx, photoItem.imagePath);
                } else {
                    mSelectedPhotoPaths.add(photoItem.imagePath);
                }
            }
        }

        final int size = Math.min(mSelectedPhotoPaths.size(), item.getPhotoItemList().size());
        for (int idx = 0; idx < size; idx++) {
            PhotosItem photoItem = item.getPhotoItemList().get(idx);
            if (photoItem.imagePath == null || photoItem.imagePath.length() < 1) {
                photoItem.imagePath = mSelectedPhotoPaths.get(idx);
            }
        }

        mSelectedTemplateItem = item;
        mSelectedTemplateItem.setSelected(true);
        mTemplateAdapter.notifyItemChanged(position);

        buildLayout(item);
        //show ads
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_template_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
//            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseTemplateDetailActivity.this);
            Bundle bundle = new Bundle();
            bundle.putString(com.photo.photography.secure_vault.helper.Constants.CollageSave, com.photo.photography.secure_vault.helper.Constants.CollageSave);
//            mFirebaseAnalytics.logEvent(Constants.Collage, bundle);

            mClickedSaveButton = true;
            asyncSaveAndShare();
            return true;

        } else if (item.getItemId() == R.id.action_add) {
            mAddImageDialog.show();
            return true;

        } else if (item.getItemId() == R.id.action_ratio) {
            if (mRatioDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustomRation);
                String[] layoutRatioName = new String[]{getString(R.string.photo_editor_square), getString(R.string.fit), getString(R.string.golden_ratio)};

                builder.setTitle(R.string.select_ratio);
                builder.setSingleChoiceItems(layoutRatioName, mPref.getInt(RATIO_KEY, 0),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPref.edit().putInt(RATIO_KEY, which).commit();
                                mLayoutRatio = which;
                                dialog.dismiss();
                                buildLayout(mSelectedTemplateItem);
                            }
                        });
                mRatioDialog = builder.create();
            }
            mRatioDialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void asyncSaveAndShare() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            Dialog dialog;
            String errMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(ActBaseTemplateDetail.this, "", getString(R.string.creating));
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    Bitmap image = createOutputImage();
                    String fileName = DateTimeUtil.getCurrentDateTime().replaceAll(":", "-").concat(".png");
                    File collageFolder = new File(com.photo.photography.collage.util.ImageUtil.OUTPUT_COLLAGE_FOLDER);
                    if (!collageFolder.exists()) {
                        collageFolder.mkdirs();
                    }
                    File photoFile = new File(collageFolder, fileName);
                    image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
                    String savedImageUri = ImageUtil.moveFile(photoFile);
                    return savedImageUri;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errMsg = ex.getMessage();
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                    errMsg = err.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String file) {
                super.onPostExecute(file);
                try {
                    dialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (file != null) {
                    if (mClickedSaveButton) {
                        Intent resultAct = new Intent(ActBaseTemplateDetail.this, ActCollageView.class);
                        resultAct.putExtra("reslutUri", file);
                        resultAct.putExtra("from", getString(R.string.collage));

                        if (MyApp.getInstance().needToShowAd()) {
                            MyApp.getInstance().showInterstitial(ActBaseTemplateDetail.this, resultAct, true, -1, null);
                        } else {
                            startActivity(resultAct);
                            finish();
                        }

                        //Toast.makeText(BaseTemplateDetailActivity.this, getString(R.string.txt_collage_create_successfully), Toast.LENGTH_SHORT).show();
                        //showCollageCreated(file.getAbsolutePath());
                        //  finish();

                    } else {
                        try {
                            File newFile = new File(file);
                            Uri uri = null;
                            if (Build.VERSION.SDK_INT >= 24) {
                                uri = FileProvider.getUriForFile(ActBaseTemplateDetail.this, BuildConfig.APPLICATION_ID + ".provider", newFile);
                            } else {
                                uri = Uri.fromFile(newFile);
                            }

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/png");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(share, getString(R.string.photo_editor_share_image)));
                        } catch (Exception e) {
                            Log.e("TAG", "Error : " + e.getMessage());
                        }
                    }
                } else if (errMsg != null) {
                    Toast.makeText(ActBaseTemplateDetail.this, errMsg, Toast.LENGTH_LONG).show();
                }
                //log
                Bundle bundle = new Bundle();
                if (mIsFrameImage) {
                    String[] layoutRatioName = new String[]{getString(R.string.photo_editor_square), getString(R.string.fit), getString(R.string.golden_ratio)};
                    String ratio = "";
                    if (mLayoutRatio < layoutRatioName.length)
                        ratio = layoutRatioName[mLayoutRatio];
                    // bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "share/frame_".concat(ratio).concat("_").concat(mSelectedTemplateItem.getTitle()));
                } else {
                    //  bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "share/template_".concat(mSelectedTemplateItem.getTitle()));
                }
                //bundle.putString(FirebaseAnalytics.Param.ITEM_ID, mSelectedTemplateItem.getTitle());
                // mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public float calculateScaleRatio(int imageWidth, int imageHeight) {
        float ratioWidth = ((float) imageWidth) / getPhotoViewWidth();
        float ratioHeight = ((float) imageHeight) / getPhotoViewHeight();
        return Math.max(ratioWidth, ratioHeight);
    }

    public int[] calculateThumbnailSize(int imageWidth, int imageHeight) {
        int[] size = new int[2];
        float ratioWidth = ((float) imageWidth) / getPhotoViewWidth();
        float ratioHeight = ((float) imageHeight) / getPhotoViewHeight();
        float ratio = Math.max(ratioWidth, ratioHeight);
        if (ratio == ratioWidth) {
            size[0] = getPhotoViewWidth();
            size[1] = (int) (imageHeight / ratio);
        } else {
            size[0] = (int) (imageWidth / ratio);
            size[1] = getPhotoViewHeight();
        }

        return size;
    }

    private int getPhotoViewWidth() {
        return mContainerLayout.getWidth();
    }

    private int getPhotoViewHeight() {
        return mContainerLayout.getHeight();
    }

//    @Override
//    public void onStickerButtonClick() {
//        mItemType = Constant.STICKER_ITEM;
//        pickSticker();
//        try {
//            mAddImageDialog.dismiss();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

//    @Override
//    public void onTextButtonClick() {
//        mItemType = Constant.TEXT_ITEM;
//        addTextItem();
//        try {
//            mAddImageDialog.dismiss();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @Override
    public void onBackgroundColorButtonClick() {

    }

    @Override
    public void onBackgroundPhotoButtonClick() {
        mItemType = Constants.BACKGROUND_ITEM;
        pickBackground();
    }

    @Override
    public void resultStickers(Uri[] uri) {
        super.resultPickMultipleImages(uri);
        final int size = uri.length;

        for (int idx = 0; idx < size; idx++) {
            float angle = (float) (idx * Math.PI / 20);

            ImageEntityHelper entity = new ImageEntityHelper(uri[idx], getResources());
            entity.setInitScaleFactor(0.25);
            entity.load(this,
                    (mPhotoView.getWidth() - entity.getWidth()) / 2,
                    (mPhotoView.getHeight() - entity.getHeight()) / 2, angle);
            mPhotoView.addImageEntity(entity);
            if (ResultContainers.getInstance().getImageEntities() != null) {
                ResultContainers.getInstance().getImageEntities().add(entity);
            }
        }
    }

    @Override
    protected void resultAddTextItem(String text, int color, String fontPath) {
        final TextEntityHelper entity = new TextEntityHelper(text, getResources());
        entity.setTextColor(color);
        entity.setTypefacePath(fontPath);
        entity.load(this,
                (mPhotoView.getWidth() - entity.getWidth()) / 2,
                (mPhotoView.getHeight() - entity.getHeight()) / 2);
        entity.setSticker(false);
        entity.setDrawImageBorder(true);
        mPhotoView.addImageEntity(entity);
        if (ResultContainers.getInstance().getImageEntities() != null) {
            ResultContainers.getInstance().getImageEntities().add(entity);
        }
    }

    @Override
    protected void resultEditTextItem(String text, int color, String fontPath) {
        if (mSelectedEntity instanceof TextEntityHelper) {
            TextEntityHelper textEntity = (TextEntityHelper) mSelectedEntity;
            textEntity.setTextColor(color);
            textEntity.setTypefacePath(fontPath);
            textEntity.setText(text);
        }
    }

    public void pickSticker() {
        Intent intent = new Intent(this, ActDownloadedPackage.class);
        intent.putExtra(FragDownloadedPackage.EXTRA_PACKAGE_TYPE, ItemPackageTables.STICKER_TYPE);
        startActivityForResult(intent, PICK_STICKER_REQUEST_CODE);
    }
}
