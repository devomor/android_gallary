package com.photo.photography.collage.screen.frag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.photo.photography.R;
import com.photo.photography.collage.helper.HelperALog;
import com.photo.photography.collage.helper.HelperConstant;
import com.photo.photography.collage.view.colorpick.ColorPickDialog;
import com.photo.photography.collage.callback.CallbackOnChooseColor;
import com.photo.photography.collage.callback.CallbackOnShareImage;
import com.photo.photography.collage.multipletouch.control.ImageEntitys;
import com.photo.photography.collage.multipletouch.control.MultiTouchEntitys;
import com.photo.photography.collage.multipletouch.control.TextEntitys;
import com.photo.photography.collage.multipletouch.view.CallbackOnDoubleClick;
import com.photo.photography.collage.multipletouch.view.PhotoViewCustom;
import com.photo.photography.collage.action.ActionQuick;
import com.photo.photography.collage.action.ActionQuickItem;
import com.photo.photography.collage.screen.ActPhotoCollage;
import com.photo.photography.collage.util.DateTimeUtil;
import com.photo.photography.collage.util.DialogUtil;
import com.photo.photography.collage.util.DialogUtil.OnAddImageButtonClickListener;
import com.photo.photography.collage.util.DialogUtil.OnBorderShadowOptionListener;
import com.photo.photography.collage.util.DialogUtil.OnEditImageMenuClickListener;
import com.photo.photography.collage.util.ImageUtil;
import com.photo.photography.collage.util.PhotoUtil;
import com.photo.photography.collage.util.ResultContainers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

//

public class FragPhotoCollage extends FragBase implements View.OnClickListener, CallbackOnDoubleClick,
        OnEditImageMenuClickListener, OnAddImageButtonClickListener, OnBorderShadowOptionListener,
        ColorPickDialog.OnColorChangedListener {
    //action id
    private static final int ID_EDIT = 1;
    private static final int ID_CHANGE = 2;
    private static final int ID_DELETE = 3;
    private static final int ID_CANCEL = 4;

    private CallbackOnShareImage mShareImageListener;
    private ImageView mDeletePhotoView;
    private ImageView mBorderView;
    private PhotoViewCustom mPhotoView;
    private ViewGroup mPhotoLayout;

    private int mItemType = HelperConstant.NORMAL_IMAGE_ITEM;
    // edit image
    private ImageEntitys mSelectedEntity = null;
    // use for animation, these views are found from dialogs
    private Animation mAnimation;
    private View mAddImageView;
    private View mGuideView;
    private View mStickerView;
    private View mItemView;
    private View mSelectPhotoView;
    // Dialogs
    private Dialog mItemDialog;
    private Dialog mStickerDialog;
    private Dialog mAddImageDialog;
    private Dialog mBorderShadowOptionDialog;
    private Dialog mSelectPhotoDialog;
    private int mPhotoViewWidth;
    private int mPhotoViewHeight;
    private SharedPreferences mPreferences;
    private CallbackOnChooseColor mChooseColorListener;
    private int mCurrentColor = Color.WHITE;
    private ColorPickDialog mColorPickerDialog;

    //    private QuickAction mTextQuickAction;
    private ActionQuick mStickerQuickAction;
    private ActionQuick mPhotoQuickAction;

    private void createQuickAction() {
        ActionQuickItem editItem = new ActionQuickItem(ID_EDIT, mActivity.getString(R.string.edit), getResources().getDrawable(R.drawable.icon_menu_edit));
        ActionQuickItem deleteItem = new ActionQuickItem(ID_DELETE, mActivity.getString(R.string.delete), mActivity.getResources().getDrawable(R.drawable.icon_menu_delete));
        ActionQuickItem cancelItem = new ActionQuickItem(ID_CANCEL, mActivity.getString(R.string.cancel), mActivity.getResources().getDrawable(R.drawable.icon_menu_cancel));

        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
        editItem.setSticky(true);
        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
//        mTextQuickAction = new QuickAction(mActivity, QuickAction.HORIZONTAL);
        mStickerQuickAction = new ActionQuick(mActivity, ActionQuick.HORIZONTAL);
        mPhotoQuickAction = new ActionQuick(mActivity, ActionQuick.HORIZONTAL);
        //add action items into QuickAction
//        mTextQuickAction.addActionItem(editItem);
//        mTextQuickAction.addActionItem(deleteItem);
//        mTextQuickAction.addActionItem(cancelItem);
        mStickerQuickAction.addActionItem(deleteItem);
        mStickerQuickAction.addActionItem(cancelItem);
        mPhotoQuickAction.addActionItem(editItem);
        mPhotoQuickAction.addActionItem(deleteItem);
        mPhotoQuickAction.addActionItem(cancelItem);
        //Set listener for action item clicked
        mPhotoQuickAction.setOnActionItemClickListener(new ActionQuick.OnActionItemClickListener() {
            @Override
            public void onItemClick(ActionQuick source, int pos, int actionId) {
                ActionQuickItem quickActionItem = mPhotoQuickAction.getActionItem(pos);
                mPhotoQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    onRemoveButtonClick();
                } else if (actionId == ID_EDIT) {
                    onEditButtonClick();
                } else if (actionId == ID_CANCEL) {

                }
            }
        });
        //Set listener for action item clicked
//        mTextQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
//            @Override
//            public void onItemClick(QuickAction source, int pos, int actionId) {
//                QuickActionItem quickActionItem = mTextQuickAction.getActionItem(pos);
//                mTextQuickAction.dismiss();
        //here we can filter which action item was clicked with pos or actionId parameter
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
        mStickerQuickAction.setOnActionItemClickListener(new ActionQuick.OnActionItemClickListener() {
            @Override
            public void onItemClick(ActionQuick source, int pos, int actionId) {
                ActionQuickItem quickActionItem = mStickerQuickAction.getActionItem(pos);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPreferences = activity.getSharedPreferences(HelperConstant.PREF_NAME, Context.MODE_PRIVATE);
        try {
            mShareImageListener = (CallbackOnShareImage) activity;
            if (activity instanceof CallbackOnChooseColor) {
                mChooseColorListener = (CallbackOnChooseColor) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            ResultContainers.getInstance().restoreFromBundle(savedInstanceState);
            mPhotoViewWidth = savedInstanceState.getInt(HelperConstant.PHOTO_VIEW_WIDTH_KEY);
            mPhotoViewHeight = savedInstanceState.getInt(HelperConstant.PHOTO_VIEW_HEIGHT_KEY);
        }
        mAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.anim_slide_in_bottom);
        createQuickAction();

        if (mActivity instanceof ActPhotoCollage) {
            ((ActPhotoCollage) mActivity).iv1.setOnClickListener(this);
            ((ActPhotoCollage) mActivity).iv2.setOnClickListener(this);
            ((ActPhotoCollage) mActivity).iv3.setOnClickListener(this);
            ((ActPhotoCollage) mActivity).iv4.setOnClickListener(this);
            ((ActPhotoCollage) mActivity).iv5.setOnClickListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ResultContainers.getInstance().saveToBundle(outState);
        outState.putInt(HelperConstant.PHOTO_VIEW_WIDTH_KEY, mPhotoViewWidth);
        outState.putInt(HelperConstant.PHOTO_VIEW_HEIGHT_KEY, mPhotoViewHeight);
        outState.putParcelableArrayList("imageEntities", mPhotoView.getImageEntities());
        outState.putParcelable("backgroundUri", mPhotoView.getPhotoBackgroundUri());
        outState.putParcelable("mSelectedEntity", mSelectedEntity);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HelperALog.d("PhotoCollageFragment.onCreateView", "onCreateView");
        View rootView = inflater.inflate(R.layout.frag_photocollage, container, false);
        mPhotoLayout = (ViewGroup) rootView.findViewById(R.id.photoLayout);
        mDeletePhotoView = (ImageView) rootView.findViewById(R.id.deleteView);
        mDeletePhotoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickDeleteCurrentPhotoView();
            }
        });

        mBorderView = (ImageView) rootView.findViewById(R.id.borderView);
        mBorderView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickBorderView();
            }
        });

        mPhotoView = new PhotoViewCustom(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mPhotoLayout.addView(mPhotoView, params);
        mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        mPhotoViewWidth = mPhotoView.getWidth();
                        mPhotoViewHeight = mPhotoView.getHeight();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mPhotoView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });

        mPhotoView.setOnDoubleClickListener(this);

        if (savedInstanceState != null) {
            ArrayList<MultiTouchEntitys> entities = savedInstanceState.getParcelableArrayList("imageEntities");
            if (entities != null) {
                mPhotoView.setImageEntities(entities);
            }
            Uri backgroundUri = savedInstanceState.getParcelable("backgroundUri");
            if (backgroundUri != null) {
                mPhotoView.setPhotoBackground(backgroundUri);
            }
            ImageEntitys entity = savedInstanceState.getParcelable("mSelectedEntity");
            if (entity != null) {
                mSelectedEntity = entity;
            }
        } else {
            mPhotoView.setImageEntities(ResultContainers.getInstance().copyImageEntities());

            if (ResultContainers.getInstance().getPhotoBackgroundImage() != null) {
                mPhotoView.setPhotoBackground(ResultContainers.getInstance().getPhotoBackgroundImage());
            }
        }

        mItemDialog = DialogUtil.createEditImageDialog(getActivity(), this, DialogUtil.ITEM_DIALOG_TYPE, false);
        mStickerDialog = DialogUtil.createEditImageDialog(getActivity(), this, DialogUtil.STICKER_DIALOG_TYPE, false);
        mAddImageDialog = DialogUtil.createAddImageDialog(getActivity(), this, false);
//        mAddImageDialog.findViewById(R.id.dividerTextView).setVisibility(View.VISIBLE);
        mAddImageDialog.findViewById(R.id.alterBackgroundView).setVisibility(View.VISIBLE);
        mBorderShadowOptionDialog = DialogUtil.createBorderAndShadowOptionDialog(getActivity(), this, false);
        mSelectPhotoDialog = DialogUtil.createSelectPhotoDialog(mActivity, this, this, false);
        // find content view of dialogs
        mAddImageView = mAddImageDialog.findViewById(R.id.dialogAddImage);
        mItemView = mItemDialog.findViewById(R.id.dialogEditImage);
        mStickerView = mStickerDialog.findViewById(R.id.dialogEditImage);
        mSelectPhotoView = mSelectPhotoDialog.findViewById(R.id.dialogSelectPhoto);
        // set title
        mActivity = getActivity();
        mActivity.setTitle(R.string.create_from_photo);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        HelperALog.d("PhotoCollageFragment.onPause", "onPause: width=" + mPhotoView.getWidth() + ", height = "
                + mPhotoView.getHeight());
        mPhotoView.unloadImages();
    }

    @Override
    public void onResume() {
        super.onResume();
        HelperALog.d("PhotoCollageFragment.onResume", "onResume: width=" + mPhotoView.getWidth() + ", height = "
                + mPhotoView.getHeight());
        mPhotoView.loadImages(getActivity());
        mPhotoView.invalidate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_photocollage, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        HelperALog.d("PhotoCollageFragment.onOptionsItemSelected", "max memory=" + Runtime.getRuntime().maxMemory() + ", used="
                + ImageUtil.getUsedMemorySize());
        if (id == R.id.action_add) {
            if (mAddImageView != null) {
                mAddImageView.startAnimation(mAnimation);
            }
            mAddImageDialog.show();
        } else if (id == R.id.action_trash) {
            clickDeleteCurrentPhotoView();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorChanged(int color) {
        mCurrentColor = color;
        drawImageBounds(mCurrentColor);
    }

    public void clickDeleteCurrentPhotoView() {
        if (!already()) {
            return;
        }

        DialogUtil.showConfirmDialog(getActivity(), R.string.confirm, R.string.confirm_delete_photo,
                new DialogUtil.ConfirmDialogOnClickListener() {

                    @Override
                    public void onOKButtonOnClick() {
                        ResultContainers.getInstance().clearAll();
                        mPhotoView.clearAllImageEntities();
                        mPhotoView.destroyBackground();
                    }

                    @Override
                    public void onCancelButtonOnClick() {

                    }
                });
    }

    public void clickBorderView() {
        if (!already()) {
            return;
        }

        if (mColorPickerDialog == null) {
            mColorPickerDialog = new ColorPickDialog(mActivity, mCurrentColor);
            mColorPickerDialog.setOnColorChangedListener(this);
        }

        mColorPickerDialog.setOldColor(mCurrentColor);
        if (!mColorPickerDialog.isShowing()) {
            mColorPickerDialog.show();
        }
    }

    @Override
    protected void resultEditTextItem(String text, int color, String fontPath) {
        if (mSelectedEntity instanceof TextEntitys) {
            TextEntitys textEntity = (TextEntitys) mSelectedEntity;
            textEntity.setTextColor(color);
            textEntity.setTypefacePath(fontPath);
            textEntity.setText(text);
        }
    }

    @Override
    protected void resultAddTextItem(String text, int color, String fontPath) {
        final TextEntitys entity = new TextEntitys(text, getResources());
        entity.setTextColor(color);
        entity.setTypefacePath(fontPath);
        entity.load(getActivity(), (mPhotoView.getWidth() - entity.getWidth()) / 2,
                (mPhotoView.getHeight() - entity.getHeight()) / 2);
        entity.setSticker(false);
        entity.setDrawImageBorder(true);
        mPhotoView.addImageEntity(entity);
        if (ResultContainers.getInstance().getImageEntities() != null) {
            ResultContainers.getInstance().getImageEntities().add(entity);
        }
    }

    @Override
    public void resultFromPhotoEditor(Uri uri) {
        super.resultFromPhotoEditor(uri);

        HelperALog.d("PhotoCollageFragment.resultFromPhotoEditor", "uri=" + uri.toString());
        if (!already()) {
            return;
        }

        if (mItemType != HelperConstant.BACKGROUND_ITEM) {
            ImageEntitys entity = new ImageEntitys(uri, getResources());
            entity.setSticker(false);
            entity.load(getActivity(), (mPhotoViewWidth - entity.getWidth()) / 2,
                    (mPhotoViewHeight - entity.getHeight()) / 2);
            mPhotoView.addImageEntity(entity);
            if (ResultContainers.getInstance().getImageEntities() != null) {
                ResultContainers.getInstance().getImageEntities().add(entity);
            }

        } else {
            mPhotoView.setPhotoBackground(uri);
            ResultContainers.getInstance().setPhotoBackgroundImage(uri);
        }
    }

    @Override
    protected void resultSticker(Uri uri) {
        super.resultSticker(uri);
        ImageEntitys entity = new ImageEntitys(uri, getResources());
        entity.load(getActivity(), (mPhotoView.getWidth() - entity.getWidth()) / 2,
                (mPhotoView.getHeight() - entity.getHeight()) / 2);
        entity.setSticker(true);
        mPhotoView.addImageEntity(entity);
        if (ResultContainers.getInstance().getImageEntities() != null) {
            ResultContainers.getInstance().getImageEntities().add(entity);
        }
    }

    @Override
    protected void resultBackground(Uri uri) {
        super.resultBackground(uri);
        mPhotoView.setPhotoBackground(uri);
        ResultContainers.getInstance().setPhotoBackgroundImage(uri);
        mItemType = HelperConstant.NORMAL_IMAGE_ITEM;
    }

    @Override
    protected void resultEditImage(Uri uri) {
        super.resultEditImage(uri);
        mSelectedEntity.setImageUri(getActivity(), uri);
        mPhotoView.invalidate();
    }

    @Override
    protected void resultStickers(Uri[] uri) {
        super.resultStickers(uri);
        if (!already()) {
            return;
        }
        final int size = uri.length;

        for (int idx = 0; idx < size; idx++) {
            float angle = (float) (idx * Math.PI / 20);

            ImageEntitys entity = new ImageEntitys(uri[idx], getResources());
            entity.load(getActivity(), (mPhotoViewWidth - entity.getWidth()) / 2,
                    (mPhotoViewHeight - entity.getHeight()) / 2, angle);
            mPhotoView.addImageEntity(entity);
            if (ResultContainers.getInstance().getImageEntities() != null) {
                ResultContainers.getInstance().getImageEntities().add(entity);
            }
        }
    }

    @Override
    public void resultPickMultipleImages(Uri[] uri) {
        super.resultPickMultipleImages(uri);
        if (!already()) {
            return;
        }
        final int size = uri.length;

        for (int idx = 0; idx < size; idx++) {
            float angle = (float) (idx * Math.PI / 20);

            ImageEntitys entity = new ImageEntitys(uri[idx], getResources());
            entity.setInitScaleFactor(0.5f);
            entity.setSticker(false);
            entity.load(getActivity(), (mPhotoViewWidth - entity.getWidth()) / 2,
                    (mPhotoViewHeight - entity.getHeight()) / 2, angle);
            mPhotoView.addImageEntity(entity);
            if (ResultContainers.getInstance().getImageEntities() != null) {
                ResultContainers.getInstance().getImageEntities().add(entity);
            }
        }
    }

    public void clickShareView() {
        if (!already()) {
            return;
        }
        mActivity = getActivity();
        final Bitmap image = mPhotoView.getImage(ImageUtil.calculateOutputScaleFactor(mPhotoView.getWidth(), mPhotoView.getHeight()));
        AsyncTask<Void, Void, File> task = new AsyncTask<Void, Void, File>() {
            Dialog dialog;
            String errMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(mActivity, getString(R.string.app_name_new), getString(R.string.creating));
            }

            @Override
            protected File doInBackground(Void... params) {
                try {
                    String fileName = DateTimeUtil.getCurrentDateTime().replaceAll(":", "-").concat(".png");
                    File collageFolder = new File(ImageUtil.OUTPUT_COLLAGE_FOLDER);
                    if (!collageFolder.exists()) {
                        collageFolder.mkdirs();
                    }
                    File photoFile = new File(collageFolder, fileName);
                    image.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(photoFile));
                    PhotoUtil.addImageToGallery(photoFile.getAbsolutePath(), mActivity);
                    return photoFile;
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
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                dialog.dismiss();
                if (file != null) {
                    if (mShareImageListener != null) {
                        mShareImageListener.onShareImage(file.getAbsolutePath());
                    }
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    startActivity(Intent.createChooser(share, getString(R.string.photo_editor_share_image)));
                } else if (errMsg != null) {
                    Toast.makeText(mActivity, errMsg, Toast.LENGTH_LONG).show();
                }
                //log
//                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "share/create_freely");
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "create_freely");
//                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onPhotoViewDoubleClick(PhotoViewCustom view, MultiTouchEntitys entity) {
        if (!already()) {
            return;
        }
        mSelectedEntity = (ImageEntitys) entity;
        if (mSelectedEntity.isSticker()) {
            mStickerQuickAction.show(mPhotoView, (int) entity.getCenterX(), (int) entity.getCenterY());
//        } else if (mSelectedEntity instanceof TextEntity) {
//            mTextQuickAction.show(mPhotoView, (int) entity.getCenterX(), (int) entity.getCenterY());
        } else {
            mPhotoQuickAction.show(mPhotoView, (int) entity.getCenterX(), (int) entity.getCenterY());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HelperALog.d("PhotoCollageFragment.onDestroyView", "Destroy view");
        mPhotoView.unloadImages();
        mPhotoView.setImageEntities(null);
        mPhotoView.destroyBackground();
    }

    private void drawImageBounds(int color) {
        mPhotoView.setBorderColor(color);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv1:
                onCameraButtonClick();
                break;

            case R.id.iv2:
                onGalleryButtonClick();
                break;

//            case R.id.iv3:
//                onStickerButtonClick();
//                break;

//            case R.id.iv4:
//                onTextButtonClick();
//                break;

            case R.id.iv5:
                onAlterBackgroundButtonClick();
                break;

        }
    }

    @Override
    public void onCameraButtonClick() {
        mItemType = HelperConstant.NORMAL_IMAGE_ITEM;
        getImageFromCamera();
        mAddImageDialog.dismiss();
    }

    @Override
    public void onGalleryButtonClick() {
        mItemType = HelperConstant.NORMAL_IMAGE_ITEM;
        // pickImageFromGallery();
        pickMultipleImageFromGallery();
        mAddImageDialog.dismiss();
    }

    @Override
    public void onRemoveButtonClick() {
        if (mSelectedEntity != null) {
            mPhotoView.removeImageEntity(mSelectedEntity);
            // save result
            ResultContainers.getInstance().removeImageEntity(mSelectedEntity);
        }

        if (mItemDialog.isShowing()) {
            mItemDialog.dismiss();
        }

        if (mStickerDialog.isShowing()) {
            mStickerDialog.dismiss();
        }
    }

    @Override
    public void onAlterBackgroundButtonClick() {
        if (!already()) {
            return;
        }
        mItemType = HelperConstant.BACKGROUND_ITEM;
        pickBackground();
        if (mItemDialog.isShowing()) {
            mItemDialog.dismiss();
        }

        if (mStickerDialog.isShowing()) {
            mStickerDialog.dismiss();
        }
    }

    @Override
    public void onBorderAndShaderButtonClick() {
        mBorderShadowOptionDialog.show();
        if (mItemDialog.isShowing()) {
            mItemDialog.dismiss();
        }

        if (mStickerDialog.isShowing()) {
            mStickerDialog.dismiss();
        }
    }

    @Override
    public void onEditButtonClick() {
        requestEditingImage(mSelectedEntity.getImageUri());
        mItemDialog.dismiss();
    }

    @Override
    public void onColorBorderButtonClick() {
        mItemDialog.dismiss();
        clickBorderView();
    }

    @Override
    public void onBorderSizeChange(float borderSize) {
        mPhotoView.setBorderSize(borderSize);
    }

    @Override
    public void onShadowSizeChange(float shadowSize) {
        if (shadowSize > 1) {
            mPhotoView.setDrawShadow(true);
            mPhotoView.setShadowSize((int) shadowSize);
        } else {
            mPhotoView.setDrawShadow(false);
            mPhotoView.setShadowSize((int) shadowSize);
        }
    }

    @Override
    public void onCancelEdit() {

    }

    @Override
    public void onBackgroundDoubleClick() {
        if (mSelectPhotoView != null) {
            mSelectPhotoView.startAnimation(mAnimation);
        }
        mSelectPhotoDialog.show();
    }

    @Override
    public void onBackgroundColorButtonClick() {

    }

    @Override
    public void onBackgroundPhotoButtonClick() {
        onAlterBackgroundButtonClick();
    }
}
