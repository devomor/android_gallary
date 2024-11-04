package com.photo.photography.collage.poster;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.photo.photography.R;
import com.photo.photography.collage.helper.HelperALog;
import com.photo.photography.collage.view.TransitionImagesView;
import com.photo.photography.collage.model.DataImageTemplate;
import com.photo.photography.collage.action.ActionQuick;
import com.photo.photography.collage.action.ActionQuickItem;
import com.photo.photography.collage.util.ImageDecoders;
import com.photo.photography.collage.util.ImageUtil;
import com.photo.photography.collage.util.PhotoUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PhotoLayoutCustom extends RelativeLayout implements ItemImageViewCustom.OnImageClickListener {
    private static final String TAG = PhotoLayoutCustom.class.getSimpleName();
    //action id
    private static final int ID_EDIT = 1;
    private static final int ID_CHANGE = 2;
    private static final int ID_DELETE = 3;
    private static final int ID_CANCEL = 4;
    View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    HelperALog.i("Drag Event", "Entered: x=" + event.getX() + ", y=" + event.getY());
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    HelperALog.i("Drag Event", "Exited: x=" + event.getX() + ", y=" + event.getY());
                    break;

                case DragEvent.ACTION_DROP:
                    ItemImageViewCustom target = (ItemImageViewCustom) v;
                    ItemImageViewCustom dragged = (ItemImageViewCustom) event.getLocalState();
                    String targetPath = "", draggedPath = "";
                    if (target.getPhotoItem() != null)
                        targetPath = target.getPhotoItem().imagePath;
                    if (dragged.getPhotoItem() != null)
                        draggedPath = dragged.getPhotoItem().imagePath;
                    if (targetPath == null) targetPath = "";
                    if (draggedPath == null) draggedPath = "";
                    if (!targetPath.equals(draggedPath))
                        target.swapImage(dragged);
                    break;
            }

            return true;
        }
    };
    private ActionQuick mQuickAction;
    private ActionQuick mBackgroundQuickAction;
    private List<PhotoItemCustom> mPhotoItems;
    private int mImageWidth, mImageHeight;
    private List<ItemImageViewCustom> mItemImageViews;
    private TransitionImagesView mBackgroundImageView;
    private int mViewWidth, mViewHeight;
    private float mInternalScaleRatio = 1;
    private float mOutputScaleRatio = 1;
    private Bitmap mTemplateImage;
    private OnQuickActionClickListener mQuickActionClickListener;
    private ProgressBar mProgressBar;
    private Bitmap mBackgroundImage;

    public PhotoLayoutCustom(Context context, DataImageTemplate template) {
        super(context);
        Bitmap templateImage = PhotoUtil.decodePNGImage(context, template.getTemplate());
        List<PhotoItemCustom> photoItems = parseImageTemplate(template);
        init(photoItems, templateImage);
    }

    public PhotoLayoutCustom(Context context, List<PhotoItemCustom> photoItems, Bitmap templateImage) {
        super(context);
        init(photoItems, templateImage);
    }

    public static List<PhotoItemCustom> parseImageTemplate(DataImageTemplate template) {
        List<PhotoItemCustom> photoItems = new ArrayList<>();
        try {
            String[] childTexts = template.getChild().split(";");
            if (childTexts != null) {
                for (String child : childTexts) {
                    String[] properties = child.split(",");
                    if (properties != null) {
                        PhotoItemCustom item = new PhotoItemCustom();
                        item.index = Integer.parseInt(properties[0]);
                        item.x = Integer.parseInt(properties[1]);
                        item.y = Integer.parseInt(properties[2]);
                        item.maskPath = properties[3];
                        photoItems.add(item);
                    }
                }
                //Sort via index
                Collections.sort(photoItems, new Comparator<PhotoItemCustom>() {
                    @Override
                    public int compare(PhotoItemCustom lhs, PhotoItemCustom rhs) {
                        return rhs.index - lhs.index;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return photoItems;
    }

    private void init(List<PhotoItemCustom> photoItems, Bitmap templateImage) {
        mPhotoItems = photoItems;
        mTemplateImage = templateImage;
        mImageWidth = mTemplateImage.getWidth();
        mImageHeight = mTemplateImage.getHeight();
        mItemImageViews = new ArrayList<>();
        setLayerType(LAYER_TYPE_HARDWARE, null);
        createQuickAction();
    }

    public void setQuickActionClickListener(OnQuickActionClickListener quickActionClickListener) {
        mQuickActionClickListener = quickActionClickListener;
    }

    private void createQuickAction() {
//        QuickActionItem editItem = new QuickActionItem(ID_EDIT, getContext().getString(R.string.edit), getResources().getDrawable(R.drawable.menu_edit));
        ActionQuickItem changeItem = new ActionQuickItem(ID_CHANGE, getContext().getString(R.string.change), getResources().getDrawable(R.drawable.icon_menu_change));
        ActionQuickItem deleteItem = new ActionQuickItem(ID_DELETE, getContext().getString(R.string.delete), getResources().getDrawable(R.drawable.icon_menu_delete));
        ActionQuickItem cancelItem = new ActionQuickItem(ID_CANCEL, getContext().getString(R.string.cancel), getResources().getDrawable(R.drawable.icon_menu_cancel));

        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
//        editItem.setSticky(true);
//        changeItem.setSticky(true);

        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        mQuickAction = new ActionQuick(getContext(), ActionQuick.HORIZONTAL);

        //add action items into QuickAction
        mQuickAction.addActionItem(changeItem);
//        mQuickAction.addActionItem(editItem);
        mQuickAction.addActionItem(deleteItem);
        mQuickAction.addActionItem(cancelItem);

        //Set listener for action item clicked
        mQuickAction.setOnActionItemClickListener(new ActionQuick.OnActionItemClickListener() {
            @Override
            public void onItemClick(ActionQuick source, int pos, int actionId) {
                ActionQuickItem quickActionItem = mQuickAction.getActionItem(pos);
                mQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    ItemImageViewCustom v = (ItemImageViewCustom) mQuickAction.getAnchorView();
                    v.clearMainImage();
                } else if (actionId == ID_EDIT) {
                    if (mQuickActionClickListener != null) {
                        mQuickActionClickListener.onEditActionClick((ItemImageViewCustom) mQuickAction.getAnchorView());
                    }
                } else if (actionId == ID_CHANGE) {
                    if (mQuickActionClickListener != null) {
                        mQuickActionClickListener.onChangeActionClick((ItemImageViewCustom) mQuickAction.getAnchorView());
                    }
                }
            }
        });

        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
        //by clicking the area outside the dialog.
//        mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//            }
//        });
        //Background quick action
        changeItem = new ActionQuickItem(ID_CHANGE, getContext().getString(R.string.change), getResources().getDrawable(R.drawable.icon_menu_change));
        deleteItem = new ActionQuickItem(ID_DELETE, getContext().getString(R.string.delete), getResources().getDrawable(R.drawable.icon_menu_delete));
        cancelItem = new ActionQuickItem(ID_CANCEL, getContext().getString(R.string.cancel), getResources().getDrawable(R.drawable.icon_menu_cancel));
        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        mBackgroundQuickAction = new ActionQuick(getContext(), ActionQuick.HORIZONTAL);

        //add action items into QuickAction
        mBackgroundQuickAction.addActionItem(changeItem);
        mBackgroundQuickAction.addActionItem(deleteItem);
        mBackgroundQuickAction.addActionItem(cancelItem);

        //Set listener for action item clicked
        mBackgroundQuickAction.setOnActionItemClickListener(new ActionQuick.OnActionItemClickListener() {
            @Override
            public void onItemClick(ActionQuick source, int pos, int actionId) {
                ActionQuickItem quickActionItem = mBackgroundQuickAction.getActionItem(pos);
                mBackgroundQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    TransitionImagesView v = (TransitionImagesView) mBackgroundQuickAction.getAnchorView();
                    v.recycleImages();
                } else if (actionId == ID_CHANGE) {
                    if (mQuickActionClickListener != null) {
                        mQuickActionClickListener.onChangeBackgroundActionClick((TransitionImagesView) mBackgroundQuickAction.getAnchorView());
                    }
                }
            }
        });
    }

    public Bitmap getTemplateImage() {
        return mTemplateImage;
    }

    public TransitionImagesView getBackgroundImageView() {
        return mBackgroundImageView;
    }

    public Bitmap getBackgroundImage() {
        return mBackgroundImageView.getImage();
    }

    public void setBackgroundImage(Bitmap image) {
        mBackgroundImage = image;
    }

    private void asyncCreateBackgroundImage(final String path) {
        HelperALog.d(TAG, "asyncCreateBackgroundImage");
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(VISIBLE);
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    Bitmap image = ImageDecoders.decodeFileToBitmap(path);
                    if (image != null) {
//                        Bitmap result = PhotoUtils.blurImage(image, 10);
//                        if (image != result) {
//                            image.recycle();
//                            image = null;
//                            System.gc();
//                        }
//                        return result;
                    }
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                mProgressBar.setVisibility(GONE);
                if (result != null)
                    mBackgroundImageView.init(result, mViewWidth, mViewHeight, mOutputScaleRatio);
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void build(final int viewWidth, final int viewHeight, final float outputScaleRatio) {
        if (viewWidth < 1 || viewHeight < 1) {
            return;
        }
        //add children views
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mOutputScaleRatio = outputScaleRatio;
        mItemImageViews.clear();
        mInternalScaleRatio = 1.0f / PhotoUtil.calculateScaleRatio(mImageWidth, mImageHeight, viewWidth, viewHeight);
        for (PhotoItemCustom item : mPhotoItems) {
            mItemImageViews.add(addPhotoItemView(item, mInternalScaleRatio, mOutputScaleRatio));
        }
        //add template image
        final ImageView templateImageView = new ImageView(getContext());
        if (Build.VERSION.SDK_INT >= 16) {
            templateImageView.setBackground(new BitmapDrawable(getResources(), mTemplateImage));
        } else {
            templateImageView.setBackgroundDrawable(new BitmapDrawable(getResources(), mTemplateImage));
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(templateImageView, params);
        //Create progress bar
        mProgressBar = new ProgressBar(getContext());
        params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setVisibility(View.GONE);
        addView(mProgressBar, params);
        //add background image
        mBackgroundImageView = new TransitionImagesView(getContext());
        params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mBackgroundImageView, 0, params);
        //Create background
        mBackgroundImageView.setOnImageClickListener(new TransitionImagesView.OnImageClickListener() {
            @Override
            public void onLongClickImage(TransitionImagesView view) {

            }

            @Override
            public void onDoubleClickImage(TransitionImagesView v) {
                if ((v.getImage() == null || v.getImage().isRecycled()) && mQuickActionClickListener != null) {
                    mQuickActionClickListener.onChangeBackgroundActionClick(v);
                } else {
                    mBackgroundQuickAction.show(v, (int) (v.getWidth() / 2.0), (int) (v.getHeight() / 2.0));
                    mBackgroundQuickAction.setAnimStyle(ActionQuick.ANIM_REFLECT);
                }
            }
        });

        if (mBackgroundImage == null || mBackgroundImage.isRecycled()) {
            if (mPhotoItems.size() > 0 && mPhotoItems.get(0).imagePath != null && mPhotoItems.get(0).imagePath.length() > 0) {
                asyncCreateBackgroundImage(mPhotoItems.get(0).imagePath);
            }
        } else {
            mBackgroundImageView.init(mBackgroundImage, mViewWidth, mViewHeight, mOutputScaleRatio);
        }
    }

    private ItemImageViewCustom addPhotoItemView(PhotoItemCustom item, float internalScale, float outputScaleRatio) {
        if (item == null || item.maskPath == null) {
            return null;
        }
        HelperALog.d("PhotoLayout", "addPhotoItemView, item.x=" + item.x + ", item.y=" + item.y + ", scale=" + internalScale);
        final ItemImageViewCustom imageView = new ItemImageViewCustom(getContext(), item);
        final float viewWidth = internalScale * imageView.getMaskImage().getWidth();
        final float viewHeight = internalScale * imageView.getMaskImage().getHeight();
        imageView.init(viewWidth, viewHeight, outputScaleRatio);
        imageView.setOnImageClickListener(this);
        if (mPhotoItems.size() > 1)
            imageView.setOnDragListener(mOnDragListener);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
        params.leftMargin = (int) (internalScale * item.x);
        params.topMargin = (int) (internalScale * item.y);
        imageView.setOriginalLayoutParams(params);
        addView(imageView, params);
        return imageView;
    }

    public Bitmap createImage() {
        Bitmap template = Bitmap.createBitmap((int) (mOutputScaleRatio * mViewWidth), (int) (mOutputScaleRatio * mViewHeight), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(template);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (mBackgroundImageView.getImage() != null && !mBackgroundImageView.getImage().isRecycled()) {
            canvas.drawBitmap(mBackgroundImageView.getImage(), mBackgroundImageView.getScaleMatrix(), paint);
        }

        canvas.saveLayer(0, 0, template.getWidth(), template.getHeight(), paint, Canvas.ALL_SAVE_FLAG);

        for (ItemImageViewCustom view : mItemImageViews)
            if (view.getImage() != null && !view.getImage().isRecycled()) {
                final int left = (int) (view.getLeft() * mOutputScaleRatio);
                final int top = (int) (view.getTop() * mOutputScaleRatio);
                final int width = (int) (view.getWidth() * mOutputScaleRatio);
                final int height = (int) (view.getHeight() * mOutputScaleRatio);
                canvas.saveLayer(left, top, left + width, top + height, paint, Canvas.ALL_SAVE_FLAG);
                //draw image
                canvas.save();
                canvas.translate(left, top);
                canvas.clipRect(0, 0, width, height);
                canvas.drawBitmap(view.getImage(), view.getScaleMatrix(), paint);
                canvas.restore();
                //draw mask
                canvas.save();
                canvas.translate(left, top);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                canvas.drawBitmap(view.getMaskImage(), view.getScaleMaskMatrix(), paint);
                paint.setXfermode(null);
                canvas.restore();
                canvas.restore();
            }
        //draw frame
        if (mTemplateImage != null) {
            canvas.drawBitmap(mTemplateImage,
                    ImageUtil.createMatrixToDrawImageInCenterView(mOutputScaleRatio * mViewWidth, mOutputScaleRatio * mViewHeight,
                            mTemplateImage.getWidth(), mTemplateImage.getHeight()), paint);
        }

        canvas.restore();

        return template;
    }

    public void recycleImages(final boolean recycleBackground) {
        HelperALog.d(TAG, "recycleImages, recycleBackground=" + recycleBackground);
        if (recycleBackground) {
            mBackgroundImageView.recycleImages();
        }

        for (ItemImageViewCustom view : mItemImageViews) {
            view.recycleImages(recycleBackground);
        }
        if (mTemplateImage != null && !mTemplateImage.isRecycled()) {
            mTemplateImage.recycle();
            mTemplateImage = null;
        }
        System.gc();
    }

    @Override
    public void onLongClickImage(ItemImageViewCustom v) {
        if (mPhotoItems.size() > 1) {
            v.setTag("x=" + v.getPhotoItem().x + ",y=" + v.getPhotoItem().y + ",path=" + v.getPhotoItem().imagePath);
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
            v.startDrag(dragData, myShadow, v, 0);
        }
    }

    @Override
    public void onDoubleClickImage(ItemImageViewCustom v) {
        if ((v.getImage() == null || v.getImage().isRecycled()) && mQuickActionClickListener != null) {
            mQuickActionClickListener.onChangeActionClick(v);
        } else {
            mQuickAction.show(v);
            mQuickAction.setAnimStyle(ActionQuick.ANIM_REFLECT);
        }
    }

    public interface OnQuickActionClickListener {
        void onEditActionClick(ItemImageViewCustom v);

        void onChangeActionClick(ItemImageViewCustom v);

        void onChangeBackgroundActionClick(TransitionImagesView v);
    }
}
