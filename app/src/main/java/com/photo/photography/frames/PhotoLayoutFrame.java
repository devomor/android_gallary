package com.photo.photography.frames;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.photo.photography.R;
import com.photo.photography.util.configs.AppLog;
import com.photo.photography.util.actions.QuickActions;
import com.photo.photography.util.actions.QuickActionsItem;
import com.photo.photography.templates.PhotosItem;
import com.photo.photography.util.utils.ImageUtil;
import com.photo.photography.util.utils.editorUtil.ImageDecoders;

import java.util.ArrayList;
import java.util.List;

public class PhotoLayoutFrame extends RelativeLayout implements ImageViewFrame.OnImageClickListener {
    private static final String TAG = PhotoLayoutFrame.class.getSimpleName();
    //action id
    private static final int ID_EDIT = 1;
    private static final int ID_CHANGE = 2;
    private static final int ID_DELETE = 3;
    private static final int ID_CANCEL = 4;
    private final List<PhotosItem> mPhotoItems;
    private final List<ImageViewFrame> mItemImageViews;
    private QuickActions mQuickAction;
    private int mViewWidth, mViewHeight;
    OnDragListener mOnDragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    AppLog.i("Drag Event", "Entered: x=" + event.getX() + ", y=" + event.getY());
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    AppLog.i("Drag Event", "Exited: x=" + event.getX() + ", y=" + event.getY());
                    break;

                case DragEvent.ACTION_DROP:
                    AppLog.i("Drag Event", "Dropped: x=" + event.getX() + ", y=" + event.getY());
                    ImageViewFrame target = (ImageViewFrame) v;
                    ImageViewFrame selectedView = getSelectedFrameImageView(target, event);
                    if (selectedView != null) {
                        target = selectedView;
                        ImageViewFrame dragged = (ImageViewFrame) event.getLocalState();
                        if (target.getPhotoItem() != null && dragged.getPhotoItem() != null) {
                            String targetPath = target.getPhotoItem().imagePath;
                            String draggedPath = dragged.getPhotoItem().imagePath;
                            if (targetPath == null) targetPath = "";
                            if (draggedPath == null) draggedPath = "";
                            if (!targetPath.equals(draggedPath))
                                target.swapImage(dragged);
                        }
                    }
                    break;
            }

            return true;
        }
    };
    private float mOutputScaleRatio = 1;
    private OnQuickActionClickListener mQuickActionClickListener;

    public PhotoLayoutFrame(Context context, List<PhotosItem> photoItems) {
        super(context);
        mItemImageViews = new ArrayList<>();
        setLayerType(LAYER_TYPE_HARDWARE, null);
        createQuickAction();
        mPhotoItems = photoItems;
    }

    private ImageViewFrame getSelectedFrameImageView(ImageViewFrame target, DragEvent event) {
        AppLog.d(TAG, "getSelectedFrameImageView");
        ImageViewFrame dragged = (ImageViewFrame) event.getLocalState();
        int leftMargin = (int) (mViewWidth * target.getPhotoItem().bound.left);
        int topMargin = (int) (mViewHeight * target.getPhotoItem().bound.top);
        final float globalX = leftMargin + event.getX();
        final float globalY = topMargin + event.getY();
        for (int idx = mItemImageViews.size() - 1; idx >= 0; idx--) {
            ImageViewFrame view = mItemImageViews.get(idx);
            float x = globalX - mViewWidth * view.getPhotoItem().bound.left;
            float y = globalY - mViewHeight * view.getPhotoItem().bound.top;
            if (view.isSelected(x, y)) {
                if (view == dragged) {
                    return null;
                } else {
                    return view;
                }
            }
        }
        return null;
    }

    public void saveInstanceState(Bundle outState) {
        if (mItemImageViews != null)
            for (ImageViewFrame view : mItemImageViews)
                view.saveInstanceState(outState);
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        if (mItemImageViews != null)
            for (ImageViewFrame view : mItemImageViews)
                view.restoreInstanceState(savedInstanceState);
    }

    public void setQuickActionClickListener(OnQuickActionClickListener quickActionClickListener) {
        mQuickActionClickListener = quickActionClickListener;
    }

    private void createQuickAction() {
//        QuickActionItem editItem = new QuickActionItem(ID_EDIT, getContext().getString(R.string.edit), getResources().getDrawable(R.drawable.menu_edit));
        QuickActionsItem changeItem = new QuickActionsItem(ID_CHANGE, getContext().getString(R.string.change), getResources().getDrawable(R.drawable.icon_menu_change));
        QuickActionsItem deleteItem = new QuickActionsItem(ID_DELETE, getContext().getString(R.string.delete), getResources().getDrawable(R.drawable.icon_menu_delete));
        QuickActionsItem cancelItem = new QuickActionsItem(ID_CANCEL, getContext().getString(R.string.cancel), getResources().getDrawable(R.drawable.icon_menu_cancel));

        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
//        editItem.setSticky(true);
//        changeItem.setSticky(true);

        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        mQuickAction = new QuickActions(getContext(), QuickActions.HORIZONTAL);

        //add action items into QuickAction
        mQuickAction.addActionItem(changeItem);
//        mQuickAction.addActionItem(editItem);
        mQuickAction.addActionItem(deleteItem);
        mQuickAction.addActionItem(cancelItem);

        //Set listener for action item clicked
        mQuickAction.setOnActionItemClickListener(new QuickActions.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickActions source, int pos, int actionId) {
                QuickActionsItem quickActionItem = mQuickAction.getActionItem(pos);
                mQuickAction.dismiss();
                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_DELETE) {
                    ImageViewFrame v = (ImageViewFrame) mQuickAction.getAnchorView();
                    v.clearMainImage();
                } else if (actionId == ID_EDIT) {
                    if (mQuickActionClickListener != null) {
                        mQuickActionClickListener.onEditActionClick((ImageViewFrame) mQuickAction.getAnchorView());
                    }
                } else if (actionId == ID_CHANGE) {
                    if (mQuickActionClickListener != null) {
                        mQuickActionClickListener.onChangeActionClick((ImageViewFrame) mQuickAction.getAnchorView());
                    }
                }
            }
        });

        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
        //by clicking the area outside the dialog.
        mQuickAction.setOnDismissListener(new QuickActions.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

    }

    private boolean isNotLargeThan1Gb() {
        ImageUtil.MemoryInfo memoryInfo = ImageUtil.getMemoryInfo(getContext());
        return memoryInfo.totalMem > 0 && (memoryInfo.totalMem / 1048576.0 <= 1024);
    }

    public void build(final int viewWidth, final int viewHeight, final float outputScaleRatio, final float space, final float corner) {
        if (viewWidth < 1 || viewHeight < 1) {
            return;
        }
        //add children views
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mOutputScaleRatio = outputScaleRatio;
        mItemImageViews.clear();
        //A circle view always is on top
        if (mPhotoItems.size() > 4 || isNotLargeThan1Gb()) {
            ImageDecoders.SAMPLER_SIZE = 256;
        } else {
            ImageDecoders.SAMPLER_SIZE = 512;
        }
        AppLog.d(TAG, "build, SAMPLER_SIZE = " + ImageDecoders.SAMPLER_SIZE);
        for (PhotosItem item : mPhotoItems) {
            ImageViewFrame imageView = addPhotoItemView(item, mOutputScaleRatio, space, corner);
            mItemImageViews.add(imageView);
        }
    }

    public void build(final int viewWidth, final int viewHeight, final float outputScaleRatio) {
        build(viewWidth, viewHeight, outputScaleRatio, 0, 0);
    }

    public void setSpace(float space, float corner) {
        for (ImageViewFrame img : mItemImageViews)
            img.setSpace(space, corner);
    }

    private ImageViewFrame addPhotoItemView(PhotosItem item, float outputScaleRatio, final float space, final float corner) {
        final ImageViewFrame imageView = new ImageViewFrame(getContext(), item);
        int leftMargin = (int) (mViewWidth * item.bound.left);
        int topMargin = (int) (mViewHeight * item.bound.top);
        int frameWidth = 0, frameHeight = 0;
        if (item.bound.right == 1) {
            frameWidth = mViewWidth - leftMargin;
        } else {
            frameWidth = (int) (mViewWidth * item.bound.width() + 0.5f);
        }

        if (item.bound.bottom == 1) {
            frameHeight = mViewHeight - topMargin;
        } else {
            frameHeight = (int) (mViewHeight * item.bound.height() + 0.5f);
        }

        imageView.init(frameWidth, frameHeight, outputScaleRatio, space, corner);
        imageView.setOnImageClickListener(this);
        if (mPhotoItems.size() > 1)
            imageView.setOnDragListener(mOnDragListener);

        LayoutParams params = new LayoutParams(frameWidth, frameHeight);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        imageView.setOriginalLayoutParams(params);
        addView(imageView, params);
        return imageView;
    }

    public Bitmap createImage() throws OutOfMemoryError {
        try {
            Bitmap template = Bitmap.createBitmap((int) (mOutputScaleRatio * mViewWidth), (int) (mOutputScaleRatio * mViewHeight), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(template);
            for (ImageViewFrame view : mItemImageViews)
                if (view.getImage() != null && !view.getImage().isRecycled()) {
                    final int left = (int) (view.getLeft() * mOutputScaleRatio);
                    final int top = (int) (view.getTop() * mOutputScaleRatio);
                    final int width = (int) (view.getWidth() * mOutputScaleRatio);
                    final int height = (int) (view.getHeight() * mOutputScaleRatio);
                    //draw image
                    canvas.saveLayer(left, top, left + width, top + height, new Paint(), Canvas.ALL_SAVE_FLAG);
                    canvas.translate(left, top);
                    canvas.clipRect(0, 0, width, height);
                    view.drawOutputImage(canvas);
                    canvas.restore();
                }

            return template;
        } catch (OutOfMemoryError error) {
            throw error;
        }
    }

    public void recycleImages() {
        AppLog.d(TAG, "recycleImages");
        for (ImageViewFrame view : mItemImageViews) {
            view.recycleImage();
        }
        System.gc();
    }

    @Override
    public void onLongClickImage(ImageViewFrame v) {
        if (mPhotoItems.size() > 1) {
            v.setTag("x=" + v.getPhotoItem().x + ",y=" + v.getPhotoItem().y + ",path=" + v.getPhotoItem().imagePath);
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
            DragShadowBuilder myShadow = new DragShadowBuilder(v);
            v.startDrag(dragData, myShadow, v, 0);
        }
    }

    @Override
    public void onDoubleClickImage(ImageViewFrame v) {
        if ((v.getImage() == null || v.getImage().isRecycled()) && mQuickActionClickListener != null) {
            mQuickActionClickListener.onChangeActionClick((ImageViewFrame) mQuickAction.getAnchorView());
        } else {
            PointF center = v.getCenterPolygon();
            if (center == null) center = new PointF(v.getWidth() / 2.0f, v.getHeight() / 2.0f);
            mQuickAction.show(v, (int) center.x, (int) center.y);
            mQuickAction.setAnimStyle(QuickActions.ANIM_REFLECT);
        }
    }

    public interface OnQuickActionClickListener {
        void onEditActionClick(ImageViewFrame v);

        void onChangeActionClick(ImageViewFrame v);
    }
}
