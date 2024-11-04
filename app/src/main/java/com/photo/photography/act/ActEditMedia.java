package com.photo.photography.act;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.repeater.RepeaterBubble;
import com.photo.photography.repeater.RepeaterColor;
import com.photo.photography.repeater.RepeaterEditorOptionList;
import com.photo.photography.repeater.RepeaterFiltersNew;
import com.photo.photography.repeater.RepeaterFont;
import com.photo.photography.repeater.GPUImageFilterTool;
import com.photo.photography.repeater.RepeaterMainEditorOptionList;
import com.photo.photography.repeater.RepeaterSticker;
import com.photo.photography.repeater.RepeaterStickerCategoryList;
import com.photo.photography.edit_views.BrushViewEdit.DrawingViews;
import com.photo.photography.edit_views.BrushViewEdit.CallbackStartDrawing;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.edit_views.StickerViewEdit.BubbleStickers;
import com.photo.photography.edit_views.StickerViewEdit.DrawableStickers;
import com.photo.photography.edit_views.StickerViewEdit.Sticker;
import com.photo.photography.edit_views.StickerViewEdit.StickerView;
import com.photo.photography.callbacks.CallbackCustomItemClick;
import com.photo.photography.callbacks.CallbackCustomItemClickGallery;
import com.photo.photography.callbacks.CallbackFilterItemClick;
import com.photo.photography.callbacks.CallbackMainOptionItemClick;
import com.photo.photography.models.OptionDatasModel;
import com.photo.photography.models.StickerItemParentModelMan;
import com.photo.photography.models.StickerModelMan;
import com.photo.photography.models.TextBubblesModel;
import com.photo.photography.util.DeviceUtil;
import com.photo.photography.util.Measures;
import com.photo.photography.util.utils.DateTimeUtil;
import com.photo.photography.util.utils.ImageUtil;
import com.photo.photography.util.utilsEdit.PathUtils;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.utils.VaultFileUtil;
import com.photo.photography.BuildConfig;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;

@SuppressWarnings("ResourceAsColor")
public class ActEditMedia extends ActBase implements View.OnClickListener {

    // load native image filters library
    static {
        try {
            System.loadLibrary("NativeImageProcessor");
        } catch (Exception exception) {

        }
    }

    public ArrayList<String> arrImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    boolean isBrushOpacity = false, isBrushColorSelection = false, isCurrentOptionEdited = false, isEditMode = false;
    int rotateImage, REQUEST_CODE_CROPPING = 4398, screenImgEditorWidth = 0, screenImgEditorHeight = 0,
    // Main Features Position
    POS_DEFAULT = 2, POS_CROP_ROTATE = 0, POS_TEXT = 1, POS_STICKER = 2, POS_BRUSH = 3, POS_FILTER = 4, POS_ADJUST = 5,
    // Adjustment Option Position
    POS_BRIGHTNESS = 0, POS_CONTRAST = 1, POS_SHARPNESS = 2, POS_SATURATION = 3, POS_BLUR = 4, POS_GAMMA = 5, POS_SEPIA = 6, POS_VIGNETTE = 7;
    String oldSavedFileName;
    StickerView stickerView;
    AppCompatImageView mainUserImage, ivEditingClose, ivEditingApply, ivClose, ivCheckMark;
    RecyclerView recyclerMainAppOption, recyclerSticker, recyclerStickerCategory, recyclerAdjust, recyclerStickerTextOption, recyclerStickerImgOption, recyclerBrushOption, recyclerFilter, recyclerFont, recyclerColor, recyclerBubble;
    SeekBar seekBar;
    LinearLayout linThirdDivisionOption, linImgStickerView, linSeekBarView;
    RelativeLayout rel_image, relTextEditingPanel;
    EditText etAddTextSticker;
    ArrayList<TextBubblesModel> textBubbleList;
    //ArrayList<StickerItemParentModel> stickerParentList;
    ArrayList<StickerItemParentModelMan> stickerParentList;
    Typeface[] typeface;
    String[] fontNameArr, colorArray, arrMainOption, arrAdjustOption, arrFilterName;
    RepeaterColor colorAdapter;
    RepeaterFont fontAdapter;
    DrawingViews drawingView;
    ProgressDialog progressDialog;
    Bitmap bm, filteredImageBitmap, originalbitmap;
    GPUImage gpuImage;
    GPUImageFilter mFilter;
    GPUImageFilterTool.FilterAdjuster mFilterAdjuster;
    Animation slideUpAnimation, slideDownAnimation;
    Sticker currentSticker;
    String[] arrBrushOptionText = {"Brush", "Eraser", "Size", "Opacity", "Undo", "Redo"};
    int[] arrBrushOptionIcon = {
            R.drawable.e_edit_pencil,
            R.drawable.e_eraser_white,
            R.drawable.e_exposure_white,
            R.drawable.e_contrast_white,
            R.drawable.e_undo_white,
            R.drawable.e_redo_white};
    Integer[] arrBrushOptionSelectablePos = {0, 1};
    Integer[] arrBrushOptionDeviderPos = {1, 3};
    String[] arrStickerTextOptionText = {"Edit", "Font", "Bubble", "Flip H", "Flip V", "Delete"};
    int[] arrStickerTextOptionIcon = {
            R.drawable.e_edit_pencil,
            R.drawable.icon_text_24dp,
            R.drawable.icon_cloud_white_24dp,
            R.drawable.icon_sticker_flip_horizontal_white_18dp,
            R.drawable.icon_sticker_flip_vertical_white_18dp,
            R.drawable.e_delete_white};
    Integer[] arrStickerTextOptionDeviderPos = {0, 2, 4};
    String[] arrStickerImgOptionText = {"Flip H", "Flip V", "Delete"};
    int[] arrStickerImgOptionIcon = {
            R.drawable.icon_sticker_flip_horizontal_white_18dp,
            R.drawable.icon_sticker_flip_vertical_white_18dp,
            R.drawable.e_delete_white};
    Integer[] arrStickerImgOptionDeviderPos = {1};
    RepeaterMainEditorOptionList mAdapter;
    int oldSelectedOption;
    int lastTimeInertedSticker;
    RepeaterSticker stickerAdapter;
    //    List<StickerModel> stickerChildList = new ArrayList<>();
    List<StickerModelMan> stickerChildList = new ArrayList<>();
    boolean isSetProgrammmatically = false;
    String savedImageUri;
    String[] stickerListTitle = new String[]{};
    String[] stickerUrl = new String[]{};
    String[] stickerFileName = new String[]{};
    Integer[] stickerListIcon = new Integer[]{};
    Integer[] stickerImagesSize = new Integer[]{};
    String typeMask = "";

    static public void notifyMediaScannerService(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, new String[]{"image/jpeg"}, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_media);

        ButterKnife.bind(this);

        initUi();

        init();

        if (progressDialog == null)
            progressDialog = new ProgressDialog(ActEditMedia.this);
        progressDialog.setMessage(getString(R.string.txtPleaseWait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                String action = getIntent().getAction();

                Uri selectedImageUri;
                String selectedImagePath = "";

                if (action != null && getIntent().getAction().equals(Intent.ACTION_SEND)) {
                    selectedImageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
                    try {
                        selectedImagePath = PathUtils.getPath(ActEditMedia.this, selectedImageUri); // /external/images/media/19426
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    selectedImagePath = getIntent().getExtras().getString("selectedImagePath");
                    selectedImageUri = Uri.parse(selectedImagePath);
                }

                rotateImage = SupportClass.getCameraPhotoOrientation(ActEditMedia.this, selectedImageUri);
                checkBitmap(selectedImagePath);

                initMainOption();
                initAdjustOption();
                initBrushOption();
                initStickerTextOption();
                initStickerImgOption();
                initFontOption();
                initColorOption();
                initBubbleOption();
                initStickerOption();
            }
        }, 500);

        //  showFullScreenAdsNow(EditMediaActivity.this);


    }

    public void init() {

        drawingView = findViewById(R.id.wachi_drawing_view);
        drawingView.setUserTouchListener(new CallbackStartDrawing() {
            @Override
            public void onDrawStart() {
                isCurrentOptionEdited = true;
            }
        });

        fontNameArr = getResources().getStringArray(R.array.textFontNameArr);
        colorArray = getResources().getStringArray(R.array.textFontColorArr);
        arrMainOption = getResources().getStringArray(R.array.mainOptionArr);
        arrAdjustOption = getResources().getStringArray(R.array.adjustOptionArr);
        arrFilterName = getResources().getStringArray(R.array.filterArr);

        mainUserImage = (AppCompatImageView) findViewById(R.id.mainUserImage);
        mainUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerStickerImgOption.setVisibility(View.GONE);
                if (mAdapter.getCurrentSelectedOption() == POS_STICKER)
                    recyclerStickerCategory.setVisibility(View.VISIBLE);
                else if (mAdapter.getCurrentSelectedOption() == POS_TEXT)
                    recyclerStickerTextOption.setVisibility(View.VISIBLE);
                stickerView.setLocked(true);
            }
        });
        stickerView = (StickerView) findViewById(R.id.sticker_view);
        initMainStickerView();

        relTextEditingPanel = (RelativeLayout) findViewById(R.id.relTextEditingPanel);
        relTextEditingPanel.setVisibility(View.GONE);
        ivEditingClose = (AppCompatImageView) findViewById(R.id.ivEditingClose);
        ivEditingClose.setOnClickListener(this);
        ivEditingApply = (AppCompatImageView) findViewById(R.id.ivEditingApply);
        ivEditingApply.setOnClickListener(this);
        ivClose = (AppCompatImageView) findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
        ivCheckMark = (AppCompatImageView) findViewById(R.id.ivCheckMark);
        ivCheckMark.setOnClickListener(this);
        etAddTextSticker = (EditText) findViewById(R.id.etAddTextSticker);

        linSeekBarView = (LinearLayout) findViewById(R.id.linSeekBarView);
        linSeekBarView.setVisibility(View.GONE);

        linThirdDivisionOption = (LinearLayout) findViewById(R.id.linThirdDivisionOption);
        linThirdDivisionOption.setVisibility(View.GONE);
        linImgStickerView = (LinearLayout) findViewById(R.id.linImgStickerView);
        rel_image = (RelativeLayout) findViewById(R.id.rel_image);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (recyclerStickerTextOption.getVisibility() == View.VISIBLE) {
                    setBubbleTextSize(progress);

                } else if (recyclerBrushOption.getVisibility() == View.VISIBLE) {
                    if (isBrushOpacity) {
                        drawingView.setOpacity(progress);
                    } else {
                        drawingView.setStrokeWidth(progress);
                    }
                } else {
                    if (isSetProgrammmatically) {
                        isSetProgrammmatically = false;
                    } else {
                        if (mFilterAdjuster != null) {
                            mFilterAdjuster.adjust(progress);
                        }
                        gpuImage.requestRender();
                        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
                        if (myBmp != null)
                            mainUserImage.setImageBitmap(myBmp);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (recyclerBrushOption.getVisibility() == View.VISIBLE) {
                    seekbarViewAnimation(false, "");
                }
            }
        });

        recyclerMainAppOption = (RecyclerView) findViewById(R.id.recyclerMainAppOption);
        recyclerAdjust = (RecyclerView) findViewById(R.id.recyclerAdjust);
        recyclerBrushOption = (RecyclerView) findViewById(R.id.recyclerBrushOption);
        recyclerStickerTextOption = (RecyclerView) findViewById(R.id.recyclerStickerTextOption);
        recyclerStickerImgOption = (RecyclerView) findViewById(R.id.recyclerStickerImgOption);
        recyclerFilter = (RecyclerView) findViewById(R.id.recyclerFilter);
        recyclerStickerCategory = (RecyclerView) findViewById(R.id.recyclerStickerCategory);
        recyclerSticker = (RecyclerView) findViewById(R.id.recyclerSticker);
        recyclerFont = (RecyclerView) findViewById(R.id.recyclerFont);
        recyclerColor = (RecyclerView) findViewById(R.id.recyclerColor);
        recyclerBubble = (RecyclerView) findViewById(R.id.recyclerBubble);

        recyclerMainAppOption.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerAdjust.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerBrushOption.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerStickerTextOption.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerStickerImgOption.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerStickerCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        int spanCount = 4;
        recyclerSticker.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerFont.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerBubble.setLayoutManager(new GridLayoutManager(this, spanCount));

        gpuImage = new GPUImage(this);
    }

    private void initMainStickerView() {
        stickerView.setLocked(false);
        stickerView.setConstrained(true);
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.e("TAG", "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                Log.e("TAG", "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                removeStickerWithDeleteIcon();
                Log.e("TAG", "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.e("TAG", "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull final Sticker sticker) {
                if (recyclerBrushOption.getVisibility() == View.VISIBLE && isCurrentOptionEdited) {
                    showConfirmationDialog(getString(R.string.txtBrush), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    applyBrushAndSave(drawingView.getViewBitmap());
                                    stickerOptionTaskPerform(sticker);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    stickerOptionTaskPerform(sticker);
                                    break;
                            }
                        }
                    });

                } else {
                    stickerOptionTaskPerform(sticker);
                }
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.e("TAG", "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.e("TAG", "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.e("TAG", "onDoubleTapped: double tap will be with two click");
            }
        });
    }

    private void initMainOption() {
        ArrayList<OptionDatasModel> mainOptionDataList = new ArrayList<>();
        for (int p = 0; p < arrMainOption.length; p++) {
            mainOptionDataList.add(new OptionDatasModel(arrMainOption[p], SupportClass.arrMainOptionIcon[p],
                    SupportClass.arrMainOptionIconSelected[p], true, false, false));
        }
        mainOptionDataList.get(2).setSelected(true);
        mAdapter = new RepeaterMainEditorOptionList(ActEditMedia.this, mainOptionDataList, new CallbackMainOptionItemClick() {
            @Override
            public void onItemClick(int newPosition) {
                if (recyclerBrushOption.getVisibility() == View.VISIBLE && isCurrentOptionEdited) {
                    showConfirmationDialog(getString(R.string.txtBrush), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    applyBrushAndSave(drawingView.getViewBitmap());
                                    setMainOptionSelection(newPosition);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    setMainOptionSelection(newPosition);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                } else if (recyclerFilter.getVisibility() == View.VISIBLE && isCurrentOptionEdited) {
                    showConfirmationDialog(getString(R.string.txtFilters), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    setMainOptionSelection(newPosition);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    mainUserImage.setImageBitmap(originalbitmap);
                                    setMainOptionSelection(newPosition);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                } else if (recyclerStickerTextOption.getVisibility() == View.VISIBLE) {
                    stickerView.setLocked(true);
                    setMainOptionSelection(newPosition);

                } else if (recyclerStickerImgOption.getVisibility() == View.VISIBLE) {
                    stickerView.setLocked(true);
                    setMainOptionSelection(newPosition);

                } else {
                    setMainOptionSelection(newPosition);
                }
            }
        });
        recyclerMainAppOption.setAdapter(mAdapter);
    }

    private void initStickerOption() {
       /* stickerParentList = createStickerParentList();
        StickerCategoryListAdapter stickerCategoryListAdapter = new StickerCategoryListAdapter(EditMediaActivity.this, stickerParentList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                lastTimeInertedSticker = 0;
                stickerChildList = stickerParentList.get(position).getChildList();
                stickerAdapter.notifyDataListChanged(stickerChildList);
                hideAllThirdLevelSubOption();
                recyclerViewMoveFirstPosition(recyclerSticker, 0);
                itemSelectFromList(linThirdDivisionOption, recyclerSticker, true);
//                recyclerSticker.setVisibility(View.VISIBLE);
//                linThirdDivisionOption.setVisibility(View.VISIBLE);
            }
        });
        recyclerStickerCategory.setAdapter(stickerCategoryListAdapter);*/


        stickerParentList = createStickerParentList();
        RepeaterStickerCategoryList stickerCategoryListAdapter = new RepeaterStickerCategoryList(ActEditMedia.this, stickerParentList, new CallbackCustomItemClickGallery() {
            @Override
            public void onItemClick(View v, int pos) {
                lastTimeInertedSticker = 0;
                hideAllThirdLevelSubOption();
                // recyclerViewMoveFirstPosition(recyclerSticker, 0);
                setStickerAndView(pos);
                itemSelectFromList(linThirdDivisionOption, recyclerSticker, true);
            }
        });

        recyclerStickerCategory.setAdapter(stickerCategoryListAdapter);

        /*stickerAdapter = new StickerAdapter(EditMediaActivity.this, stickerChildList);
        stickerAdapter.setClickListener(new StickerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemSelectFromList(linThirdDivisionOption, recyclerSticker, false);
                lastTimeInertedSticker = lastTimeInertedSticker + 1;
                Drawable drawable = ContextCompat.getDrawable(EditMediaActivity.this, stickerChildList.get(position).getSticker());
                stickerView.addSticker(new DrawableSticker(drawable));
            }
        });
        recyclerSticker.setAdapter(stickerAdapter);*/
        stickerAdapter = new RepeaterSticker(ActEditMedia.this, stickerChildList, typeMask);

        stickerAdapter.setClickListener(new RepeaterSticker.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String fileNameFormat = null;
                itemSelectFromList(linThirdDivisionOption, recyclerSticker, false);
                lastTimeInertedSticker = lastTimeInertedSticker + 1;

                if (SupportClass.checkConnection(ActEditMedia.this)) {
                    if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_1)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_1, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_2)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_2, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_3)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_3, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_4)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_4, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_5)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_5, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_6)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_6, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_7)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_7, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_8)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_8, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_9)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_9, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_10)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_10, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_11)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_11, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_12)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_12, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_13)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_13, "");
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_14)) {
                        fileNameFormat = arrImage.get(position).replace(Constants.IMAGE_URL_14, "");
                    }

                    String fileName = fileNameFormat.replace(Constants.KEY_PNG, "");
                    File existFile = new File(new VaultFileUtil(ActEditMedia.this).getFile(getString(R.string.app_folder_sticker)), fileName);
                    boolean check = existFile.exists();
                    if (check) {

                        String imagePath = existFile.getAbsolutePath();
                        Uri pathUri = Uri.fromFile(new File(imagePath));
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(pathUri);
                            Drawable drawable = Drawable.createFromStream(inputStream, pathUri.toString());
                            stickerView.addSticker(new DrawableStickers(drawable));
                        } catch (FileNotFoundException e) {
                            Toast.makeText(ActEditMedia.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ActEditMedia.this, getString(R.string.txt_image_is_not_load_yet), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String imagePath = null;
                    if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_1)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_2)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_3)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_4)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_5)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_6)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_7)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_8)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_9)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_10)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_11)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_12)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_13)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_14)) {
                        imagePath = stickerAdapter.getOffLineList().get(position).getStickerImage();
                    }

                    Uri pathUri = Uri.fromFile(new File(imagePath));
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(pathUri);
                        Drawable drawable = Drawable.createFromStream(inputStream, pathUri.toString());
                        stickerView.addSticker(new DrawableStickers(drawable));
                    } catch (FileNotFoundException e) {
                        Toast.makeText(ActEditMedia.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        recyclerSticker.setAdapter(stickerAdapter);
    }

    public void setStickerAndView(int positionCategory) {

        typeMask = stickerParentList.get(positionCategory).getParentText();

        arrImage = new ArrayList<>();

        for (int i = 1; i <= stickerImagesSize[positionCategory]; i++) {
            arrImage.add(stickerUrl[positionCategory] + stickerFileName[positionCategory] + i + Constants.KEY_PNG);
        }

        if (SupportClass.checkConnection(ActEditMedia.this)) {
            ArrayList<StickerModelMan> stickerList2 = new ArrayList<>();
            StickerItemParentModelMan stickerParentMode2 = new StickerItemParentModelMan();
            for (int i = 1; i <= stickerImagesSize[positionCategory]; i++) {
                StickerModelMan stickerMode2 = new StickerModelMan();
                stickerMode2.setStickerImage(stickerUrl[positionCategory] + stickerFileName[positionCategory] + i + Constants.KEY_PNG);
                stickerList2.add(stickerMode2);
            }
            stickerParentMode2.setChildItemList(stickerList2);
            stickerParentMode2.setParentIcon(getResources().getDrawable(stickerListIcon[positionCategory]));
            stickerParentMode2.setParentText(stickerListTitle[positionCategory]);
            stickerParentList.set(positionCategory, stickerParentMode2);

        } else {
            String fileNameFormat = null;
            ArrayList<String> noInternetImage = new ArrayList<>();
            ArrayList<StickerModelMan> stickerList2 = new ArrayList<>();
            StickerItemParentModelMan stickerParentMode2 = new StickerItemParentModelMan();
            for (int i = 0; i < arrImage.size(); i++) {
                fileNameFormat = arrImage.get(i).replace(stickerUrl[positionCategory], "");

                String fileName = fileNameFormat.replace(Constants.KEY_PNG, "");
                File existFile = new File(new VaultFileUtil(ActEditMedia.this).getFile(getString(R.string.app_folder_sticker)), fileName);
                boolean check = existFile.exists();
                String pathImageLoad = existFile.getAbsolutePath();
                if (check) {
                    noInternetImage.add(pathImageLoad);
                    StickerModelMan stickerMode2 = new StickerModelMan();
                    stickerMode2.setStickerImage(pathImageLoad);
                    stickerList2.add(stickerMode2);
                }
            }
            if (noInternetImage.size() == 0) {
                Toast.makeText(ActEditMedia.this, getString(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT).show();
            }

            stickerParentMode2.setChildItemList(stickerList2);
            stickerParentMode2.setParentIcon(getResources().getDrawable(stickerListIcon[positionCategory]));
            stickerParentMode2.setParentText(stickerListTitle[positionCategory]);
            stickerParentList.set(positionCategory, stickerParentMode2);

        }
        stickerAdapter.notifyDataListChanged(stickerParentList.get(positionCategory).getChildList(), typeMask);


    }


    private void initStickerTextOption() {
        ArrayList<OptionDatasModel> stickerTextOptionDataList = new ArrayList<>();
        for (int p = 0; p < arrStickerTextOptionText.length; p++) {
            stickerTextOptionDataList.add(new OptionDatasModel(arrStickerTextOptionText[p], arrStickerTextOptionIcon[p], false, false, isOptionContainPosition(p, arrStickerTextOptionDeviderPos)));
        }
        RepeaterEditorOptionList stickerTextOptionAdapter = new RepeaterEditorOptionList(ActEditMedia.this, stickerTextOptionDataList, new CallbackCustomItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                //"Edit", "Font", "Bubble", "Flip H", "Flip V", "Delete"
                // 0       1       2         3         4         5
                if (currentSticker == null)
                    currentSticker = stickerView.getCurrentSticker();

                if (currentSticker == null) {
                    Toast.makeText(ActEditMedia.this, getString(R.string.pls_click_on_sticker), Toast.LENGTH_SHORT).show();

                } else {
                    if (position == 0 && currentSticker instanceof BubbleStickers) {
                        isEditMode = true;
                        addTextAnimation(true, ((BubbleStickers) currentSticker).getText().trim());

                    } else if (position == 1) {
                        hideAllThirdLevelSubOption();
                        recyclerViewMoveFirstPosition(recyclerFont, 0);
                        itemSelectFromList(linThirdDivisionOption, recyclerFont, true);
//                        recyclerFont.setVisibility(View.VISIBLE);
//                        linThirdDivisionOption.setVisibility(View.VISIBLE);

                    } else if (position == 2) {
                        hideAllThirdLevelSubOption();
                        recyclerViewMoveFirstPosition(recyclerBubble, 0);
                        itemSelectFromList(linThirdDivisionOption, recyclerBubble, true);
//                        recyclerBubble.setVisibility(View.VISIBLE);
//                        linThirdDivisionOption.setVisibility(View.VISIBLE);

                    } else if (position == 3) {
                        stickerView.flip(currentSticker, StickerView.FLIP_HORIZONTALLY);

                    } else if (position == 4) {
                        stickerView.flip(currentSticker, StickerView.FLIP_VERTICALLY);

                    } else if (position == 5) {
                        removeStickerWithDeleteIcon();
                    }
                }
            }
        });
        recyclerStickerTextOption.setAdapter(stickerTextOptionAdapter);
    }

    private void initStickerImgOption() {
        ArrayList<OptionDatasModel> stickerImgOptionDataList = new ArrayList<>();
        for (int p = 0; p < arrStickerImgOptionText.length; p++) {
            stickerImgOptionDataList.add(new OptionDatasModel(arrStickerImgOptionText[p], arrStickerImgOptionIcon[p], false, false, isOptionContainPosition(p, arrStickerImgOptionDeviderPos)));
        }

        RepeaterEditorOptionList stickerImgOptionAdapter = new RepeaterEditorOptionList(ActEditMedia.this, stickerImgOptionDataList, new CallbackCustomItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                //"Flip H", "Flip V", "Delete"
                // 0         1         2
                if (currentSticker == null)
                    currentSticker = stickerView.getCurrentSticker();

                if (currentSticker != null) {
                    if (position == 0) {
                        stickerView.flip(currentSticker, StickerView.FLIP_HORIZONTALLY);
                    } else if (position == 1) {
                        stickerView.flip(currentSticker, StickerView.FLIP_VERTICALLY);
                    } else if (position == 2) {
                        removeStickerWithDeleteIcon();
                    }
                } else {
                    Toast.makeText(ActEditMedia.this, getString(R.string.pls_click_on_sticker), Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerStickerImgOption.setAdapter(stickerImgOptionAdapter);
    }

    private void initBubbleOption() {
        try {
            textBubbleList = getLoadedXmlValues();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final RepeaterBubble bubbleAdapter = new RepeaterBubble(ActEditMedia.this, textBubbleList);
        bubbleAdapter.setClickListener(new RepeaterBubble.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Draw sticker canvas
                if (currentSticker == null)
                    currentSticker = stickerView.getCurrentSticker();
                if (currentSticker instanceof BubbleStickers) {
//                    selectedBubblePos = position;
                    ((BubbleStickers) currentSticker).setCurrBubblePosition(position);
                    setSeekBarProgressProgrammatically(getBubbleStickerTextSize());
                    Bitmap bmpSticker = generateDynamicStickerBitmap(
                            ((BubbleStickers) currentSticker).getText(),
                            ((BubbleStickers) currentSticker).getTextSize(),
                            position,
                            ((BubbleStickers) currentSticker).getCurrentFontColorPosition(),
                            ((BubbleStickers) currentSticker).getCurrentFontPosition());
                    Drawable d = new BitmapDrawable(getResources(), bmpSticker);
                    ((BubbleStickers) currentSticker).setDrawable(d);
                    stickerView.replace(currentSticker);
                    stickerView.invalidate();
                    itemSelectFromList(linThirdDivisionOption, recyclerBubble, false);
                }
            }
        });
        recyclerBubble.setAdapter(bubbleAdapter);
    }

    private void initBrushOption() {
        ArrayList<OptionDatasModel> brushOptionDataList = new ArrayList<>();
        for (int p = 0; p < arrBrushOptionText.length; p++) {
            brushOptionDataList.add(new OptionDatasModel(arrBrushOptionText[p], arrBrushOptionIcon[p], isOptionContainPosition(p, arrBrushOptionSelectablePos), false, isOptionContainPosition(p, arrBrushOptionDeviderPos)));
        }
        brushOptionDataList.get(0).setSelected(true);
        RepeaterEditorOptionList brushOptionAdapter = new RepeaterEditorOptionList(ActEditMedia.this, brushOptionDataList, new CallbackCustomItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                //"Brush", "Eraser", "Size", "Opacity", "Undo", "Redo"
                // 0        1         2       3          4       5
                if (position == 0) {
                    drawingView.change2Brush();
                } else if (position == 1) {
                    drawingView.change2Eraser();
                } else if (position == 2) {
                    isBrushOpacity = false;
                    seekbarViewAnimation(true, getString(R.string.txtBrushSize));
                } else if (position == 3) {
                    isBrushOpacity = true;
                    seekbarViewAnimation(true, getString(R.string.txtBrushOpacity));
                } else if (position == 4) {
                    drawingView.undo();
                } else if (position == 5) {
                    drawingView.redo();
                }
            }
        });
        recyclerBrushOption.setAdapter(brushOptionAdapter);
    }

    private void initColorOption() {
        colorAdapter = new RepeaterColor(ActEditMedia.this, colorArray);
        colorAdapter.setClickListener(new RepeaterColor.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isBrushColorSelection) {
                    drawingView.setColor(Color.parseColor(colorArray[position]));

                } else {
                    if (currentSticker == null)
                        currentSticker = stickerView.getCurrentSticker();
                    if (currentSticker instanceof BubbleStickers) {
                        ((BubbleStickers) currentSticker).setCurrentFontColorPosition(position);
                        Bitmap bmpSticker = generateDynamicStickerBitmap(
                                ((BubbleStickers) currentSticker).getText(),
                                ((BubbleStickers) currentSticker).getTextSize(),
                                ((BubbleStickers) currentSticker).getCurrBubblePosition(),
                                position,
                                ((BubbleStickers) currentSticker).getCurrentFontPosition());
                        Drawable d = new BitmapDrawable(getResources(), bmpSticker);
                        ((BubbleStickers) currentSticker).setDrawable(d);
                        stickerView.replace(currentSticker);
                        stickerView.invalidate();
                    }
                }
            }
        });
        recyclerColor.setAdapter(colorAdapter);
    }

    private void initFontOption() {
        typeface = new Typeface[fontNameArr.length];
        for (int tf = 0; tf < fontNameArr.length; tf++) {
            typeface[tf] = SupportClass.getTypefaceFromAsset(ActEditMedia.this, fontNameArr[tf]);
        }
        fontAdapter = new RepeaterFont(ActEditMedia.this, typeface);
        fontAdapter.setClickListener(new RepeaterFont.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (currentSticker == null)
                    currentSticker = stickerView.getCurrentSticker();
                if (currentSticker instanceof BubbleStickers) {
                    ((BubbleStickers) currentSticker).setCurrentFontPosition(position);
                    Bitmap bmpSticker = generateDynamicStickerBitmap(
                            ((BubbleStickers) currentSticker).getText(),
                            ((BubbleStickers) currentSticker).getTextSize(),
                            ((BubbleStickers) currentSticker).getCurrBubblePosition(),
                            ((BubbleStickers) currentSticker).getCurrentFontColorPosition(),
                            position);
                    Drawable d = new BitmapDrawable(getResources(), bmpSticker);
                    ((BubbleStickers) currentSticker).setDrawable(d);
                    stickerView.replace(currentSticker);
                    stickerView.invalidate();
                    itemSelectFromList(linThirdDivisionOption, recyclerFont, false);
                }
            }
        });
        recyclerFont.setAdapter(fontAdapter);
    }

    private void initAdjustOption() {
        ArrayList<OptionDatasModel> adjustOptionDataList = new ArrayList<>();
        for (int p = 0; p < arrAdjustOption.length; p++) {
            adjustOptionDataList.add(new OptionDatasModel(arrAdjustOption[p], SupportClass.arrAdjustOptionIcon[p], true, false, false));
        }
        RepeaterEditorOptionList adjustAdapter = new RepeaterEditorOptionList(ActEditMedia.this, adjustOptionDataList, new CallbackCustomItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                getImage();
//                addToUndoList(originalbitmap);
                if (originalbitmap != null) {
                    if (position == POS_BRIGHTNESS) {
                        Brightness();
                    } else if (position == POS_CONTRAST) {
                        Contrast();
                    } else if (position == POS_SHARPNESS) {
                        Sharpness();
                    } else if (position == POS_SATURATION) {
                        Saturation();
                    } else if (position == POS_BLUR) {
                        Blur();
                    } else if (position == POS_GAMMA) {
                        Gamma();
                    } else if (position == POS_SEPIA) {
                        Sepia();
                    } else if (position == POS_VIGNETTE) {
                        Vignette();
                    }
                }
            }
        });
        recyclerAdjust.setAdapter(adjustAdapter);
    }

    public void stickerOptionTaskPerform(Sticker sticker) {
        lastTimeInertedSticker = 0;
        stickerView.setLocked(false);
        currentSticker = sticker;
        stickerView.sendToLayer(stickerView.getStickerPosition(currentSticker));
        if (currentSticker instanceof DrawableStickers) {
            hideAllOptionRecycler(true, true);
            recyclerViewMoveFirstPosition(recyclerMainAppOption, POS_STICKER);
            recyclerStickerImgOption.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvText)).setText(getString(R.string.txtSticker));
            mAdapter.notifySelection(POS_STICKER);

        } else if (currentSticker instanceof BubbleStickers) {
            hideAllOptionRecycler(false, false);
            recyclerViewMoveFirstPosition(recyclerColor, 0);
            if (linSeekBarView.getVisibility() != View.VISIBLE)
                seekbarViewAnimation(true, getString(R.string.txtText));
            if (recyclerColor.getVisibility() != View.VISIBLE)
                colorSelectionAnimation(true);
            recyclerViewMoveFirstPosition(recyclerMainAppOption, POS_TEXT);
            recyclerStickerTextOption.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvText)).setText(getString(R.string.txtText));
            mAdapter.notifySelection(POS_TEXT);
        }
        Log.e("TAG", "onStickerTouchedDown");
    }

    public void recyclerViewMoveFirstPosition(RecyclerView recyclerView, int position) {
        recyclerView.scrollToPosition(position);
    }

    private void setBubbleTextSize(int textSize) {
        if (currentSticker instanceof BubbleStickers) {
            ((BubbleStickers) currentSticker).setTextSize(textSize * 5);
            Bitmap bmpSticker = generateDynamicStickerBitmap(
                    ((BubbleStickers) currentSticker).getText(),
                    ((BubbleStickers) currentSticker).getTextSize(),
                    ((BubbleStickers) currentSticker).getCurrBubblePosition(),
                    ((BubbleStickers) currentSticker).getCurrentFontColorPosition(),
                    ((BubbleStickers) currentSticker).getCurrentFontPosition());
            Drawable d = new BitmapDrawable(getResources(), bmpSticker);
            ((BubbleStickers) currentSticker).setDrawable(d);
            stickerView.replace(currentSticker);
            stickerView.invalidate();
        }
    }

    public void hideAllOptionRecycler(boolean hideAllWithSeekbar, boolean hideAllWithColorBar) {
        isBrushColorSelection = false;
        if (hideAllWithSeekbar)
            linSeekBarView.setVisibility(View.GONE);
        drawingView.setVisibility(View.GONE);
        drawingView.clearDrawingBoard();
        recyclerAdjust.setVisibility(View.GONE);
        recyclerStickerTextOption.setVisibility(View.GONE);
        recyclerStickerImgOption.setVisibility(View.GONE);
        recyclerBrushOption.setVisibility(View.GONE);
        recyclerFilter.setVisibility(View.GONE);
        if (hideAllWithColorBar)
            recyclerColor.setVisibility(View.GONE);
        recyclerStickerCategory.setVisibility(View.GONE);
    }

    public void hideAllThirdLevelSubOption() {
        recyclerFont.setVisibility(View.GONE);
        recyclerBubble.setVisibility(View.GONE);
        recyclerSticker.setVisibility(View.GONE);
    }

    public void setMainOptionSelection(int newPosition) {
        isCurrentOptionEdited = false;
        if (newPosition != POS_CROP_ROTATE)
            mAdapter.notifySelection(newPosition);

        getImage();
        if (originalbitmap != null) {
            if (newPosition == POS_CROP_ROTATE) {
                Uri crpUri = getImageUri(originalbitmap);
                if (crpUri != null)
                    startActivityForResult(ActCropImage.createIntent(ActEditMedia.this, crpUri), REQUEST_CODE_CROPPING);
                else
                    Toast.makeText(this, getString(R.string.something_went_wrong_please_try_again), Toast.LENGTH_SHORT).show();

            } else if (newPosition == POS_ADJUST) {
                hideAllOptionRecycler(true, true);
                recyclerViewMoveFirstPosition(recyclerAdjust, 0);
                recyclerAdjust.setVisibility(View.VISIBLE);

            } else if (newPosition == POS_FILTER) {
                ((RepeaterFiltersNew) recyclerFilter.getAdapter()).setSelectedFirstPos();
                hideAllOptionRecycler(true, true);
                getImage();
                recyclerViewMoveFirstPosition(recyclerFilter, 0);
                recyclerFilter.setVisibility(View.VISIBLE);

            } else if (newPosition == POS_BRUSH) {
                hideAllOptionRecycler(true, true);
                recyclerViewMoveFirstPosition(recyclerBrushOption, 0);
                isBrushColorSelection = true;
                if (recyclerColor.getVisibility() != View.VISIBLE)
                    colorSelectionAnimation(true);
                recyclerBrushOption.setVisibility(View.VISIBLE);
                drawingView.setVisibility(View.VISIBLE);
                drawingView.change2Brush();

            } else if (newPosition == POS_TEXT) {
                oldSelectedOption = mAdapter.getCurrentSelectedOption();
                addTextAnimation(true, "");

            } else if (newPosition == POS_STICKER) {
                hideAllOptionRecycler(true, true);
                recyclerViewMoveFirstPosition(recyclerStickerCategory, 0);
                recyclerStickerCategory.setVisibility(View.VISIBLE);

            }
        }
    }

    private void removeStickerWithDeleteIcon() {
        stickerView.remove(currentSticker);
        currentSticker = null;
        if (stickerView.getStickerCount() == 0) {
            hideAllOptionRecycler(true, true);
            recyclerViewMoveFirstPosition(recyclerMainAppOption, POS_DEFAULT);
            setMainOptionSelection(POS_DEFAULT);
        } else {
            currentSticker = stickerView.getLastSticker();
        }
    }

    private void showConfirmationDialog(String optionName, DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle(getString(R.string.txt_msg_changes_confirmation_dialog_title));
        builder.setMessage(getString(R.string.txt_msg_changes_confirmation_dialog_msg1) + " " + optionName + " " + getString(R.string.txt_msg_changes_confirmation_dialog_msg2));
        AlertDialog alertDialog = builder.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.txt_apply), onClickListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.txt_discard), onClickListener);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

                Button button2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                button2.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }
        });
        alertDialog.show();
    }

    private boolean isOptionContainPosition(int position, Integer[] arrOptionDeviderPos) {
        List<Integer> intList = Arrays.asList(arrOptionDeviderPos);
        return intList.contains(position);
    }

    private ArrayList<Integer> getAnchorTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 27; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_anchor" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_anchor" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getAnimalTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_animal" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_animal" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getArtTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 23; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_art" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_art" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getButterflyTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_butterfly" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_butterfly" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getDragonTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 51; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_dragon" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_dragon" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getFlowerTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_flower" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_flower" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getLoveTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_love" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_love" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getReligiousTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 57; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_religious" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_religious" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    private ArrayList<Integer> getTribleTatoo() {
        ArrayList<Integer> arrSticker = new ArrayList<>();
        for (int i = 1; i <= 53; i++) {
            arrSticker.add(getResources().getIdentifier("tatoo_trible" + i, "drawable", BuildConfig.APPLICATION_ID));
//            arrSticker.add(getResources().getIdentifier("tatoo_trible" + i, "drawable", getPackageName()));
        }
        return arrSticker;
    }

    /* private ArrayList<StickerItemParentModel> createStickerParentList() {
         ArrayList<Integer> list1 = getAnchorTatoo();
         ArrayList<Integer> list2 = getAnimalTatoo();
         ArrayList<Integer> list3 = getArtTatoo();
         ArrayList<Integer> list4 = getButterflyTatoo();
         ArrayList<Integer> list5 = getDragonTatoo();
         ArrayList<Integer> list6 = getFlowerTatoo();
         ArrayList<Integer> list7 = getLoveTatoo();
         ArrayList<Integer> list8 = getReligiousTatoo();
         ArrayList<Integer> list9 = getTribleTatoo();

         int[] stickerListIcon = new int[]{
                 list1.get(16),
                 list2.get(0),
                 list3.get(20),
                 list4.get(13),
                 list5.get(13),
                 list6.get(20),
                 list7.get(7),
                 list8.get(38),
                 list9.get(49)
         };

         String[] stickerListTitle = new String[]{"Anchor", "Animal", "Art", "Butterfly", "Dragon",
                 "Flower", "Love", "Religious", "Trible"};
         ArrayList<ArrayList<Integer>> arrListAllSticker = new ArrayList<>();
         arrListAllSticker.add(list1);
         arrListAllSticker.add(list2);
         arrListAllSticker.add(list3);
         arrListAllSticker.add(list4);
         arrListAllSticker.add(list5);
         arrListAllSticker.add(list6);
         arrListAllSticker.add(list7);
         arrListAllSticker.add(list8);
         arrListAllSticker.add(list9);

         ArrayList<StickerItemParentModel> stickerParentList = new ArrayList<>();
         for (int p = 0; p < arrListAllSticker.size(); p++) {
             StickerItemParentModel stickerParentModel = new StickerItemParentModel();
             ArrayList<StickerModel> stickerList = new ArrayList<>();
             ArrayList<Integer> intArr = arrListAllSticker.get(p);
             for (int i = 0; i < intArr.size(); i++) {
                 StickerModel stickerModel = new StickerModel();
                 stickerModel.setSticker(intArr.get(i));
                 stickerList.add(stickerModel);
             }
             stickerParentModel.setChildItemList(stickerList);
             stickerParentModel.setParentIcon(ContextCompat.getDrawable(this, stickerListIcon[p]));
             stickerParentModel.setParentText(stickerListTitle[p]);
             stickerParentList.add(stickerParentModel);
         }
         return stickerParentList;
     }*/
    private ArrayList<StickerItemParentModelMan> createStickerParentList() {


        stickerListTitle = new String[]{Constants.KEY_CATEGORY_1, Constants.KEY_CATEGORY_2, Constants.KEY_CATEGORY_3, Constants.KEY_CATEGORY_4, Constants.KEY_CATEGORY_5, Constants.KEY_CATEGORY_6, Constants.KEY_CATEGORY_7, Constants.KEY_CATEGORY_8,
                Constants.KEY_CATEGORY_9, Constants.KEY_CATEGORY_10, Constants.KEY_CATEGORY_11, Constants.KEY_CATEGORY_12, Constants.KEY_CATEGORY_13, Constants.KEY_CATEGORY_14};
        stickerUrl = new String[]{Constants.IMAGE_URL_1, Constants.IMAGE_URL_2, Constants.IMAGE_URL_3, Constants.IMAGE_URL_4, Constants.IMAGE_URL_5, Constants.IMAGE_URL_6, Constants.IMAGE_URL_7, Constants.IMAGE_URL_8, Constants.IMAGE_URL_9, Constants.IMAGE_URL_10,
                Constants.IMAGE_URL_11, Constants.IMAGE_URL_12, Constants.IMAGE_URL_13, Constants.IMAGE_URL_14};
        stickerFileName = new String[]{Constants.KEY_FILE_NAME_1, Constants.KEY_FILE_NAME_2, Constants.KEY_FILE_NAME_3, Constants.KEY_FILE_NAME_4, Constants.KEY_FILE_NAME_5, Constants.KEY_FILE_NAME_6, Constants.KEY_FILE_NAME_7, Constants.KEY_FILE_NAME_8, Constants.KEY_FILE_NAME_9, Constants.KEY_FILE_NAME_10,
                Constants.KEY_FILE_NAME_11, Constants.KEY_FILE_NAME_12, Constants.KEY_FILE_NAME_13, Constants.KEY_FILE_NAME_14};
        stickerListIcon = new Integer[]{R.drawable.icon_category_1, R.drawable.icon_category_2, R.drawable.icon_category_3, R.drawable.icon_category_4, R.drawable.icon_category_5, R.drawable.icon_category_6, R.drawable.icon_category_7, R.drawable.icon_category_8, R.drawable.icon_category_9, R.drawable.icon_category_10,
                R.drawable.icon_category_11, R.drawable.icon_category_12, R.drawable.icon_category_13, R.drawable.icon_category_14};
        stickerImagesSize = new Integer[]{Constants.IMAGE_SIZE_1, Constants.IMAGE_SIZE_2, Constants.IMAGE_SIZE_3, Constants.IMAGE_SIZE_4, Constants.IMAGE_SIZE_5, Constants.IMAGE_SIZE_6, Constants.IMAGE_SIZE_7, Constants.IMAGE_SIZE_8, Constants.IMAGE_SIZE_9, Constants.IMAGE_SIZE_10,
                Constants.IMAGE_SIZE_11, Constants.IMAGE_SIZE_12, Constants.IMAGE_SIZE_13, Constants.IMAGE_SIZE_14};


        ArrayList<StickerItemParentModelMan> stickerParentList = new ArrayList<>();

        for (int k = 0; k < stickerListIcon.length; k++) {
            StickerItemParentModelMan stickerParentMode3 = new StickerItemParentModelMan();
            stickerParentMode3.setChildItemList(getStickerList(k));
            stickerParentMode3.setParentIcon(getResources().getDrawable(stickerListIcon[k]));
            stickerParentMode3.setParentText(stickerListTitle[k]);
            stickerParentList.add(stickerParentMode3);
        }
        return stickerParentList;
    }

    public ArrayList<StickerModelMan> getStickerList(int pos) {
        ArrayList<StickerModelMan> stickerList = new ArrayList<>();
        for (int i = 1; i <= stickerImagesSize[pos]; i++) {
            StickerModelMan stickerModel3 = new StickerModelMan();
            stickerModel3.setStickerImage(stickerUrl[pos] + stickerFileName[pos] + i + Constants.KEY_PNG);
            stickerList.add(stickerModel3);
        }

        return stickerList;
    }


    private int getDetermineMaxTextSize(String strText, float maxWidth) {
        int size = 0;
        Paint paint = new Paint();
        //Finally, Draw the text on the canvas at the horizontal and vertical center position
        String[] strMsgLine = strText.split("\n");
        int maximumCharLinePos = 0;
        for (int i = 0; i < strMsgLine.length; i++) {
            if (strMsgLine[i].length() > strMsgLine[maximumCharLinePos].length())
                maximumCharLinePos = i;
        }
        do {
            paint.setTextSize(++size);
        } while (paint.measureText(strMsgLine[maximumCharLinePos]) < maxWidth);

        return size;
    }

    private Bitmap generateDynamicStickerBitmap(String strText, int textSize, int bubblePosition, int colorPosition, int fontPosition) {
        TextBubblesModel bubbleModel = textBubbleList.get(bubblePosition);
        Bitmap bitmap;
        if (bubblePosition == 0) {
            bitmap = Bitmap.createBitmap(
                    500, // Width
                    500, // Height
                    Bitmap.Config.ARGB_8888 // Config
            );
        } else {
            String imagePath = SupportClass.filePath + bubbleModel.getKEY_BUBBLE_IMAGE();
            Bitmap imutableBitmap = SupportClass.getBitmapFromAsset(ActEditMedia.this, imagePath);
            bitmap = imutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }

        String[] strRect = bubbleModel.getKEY_TEXT_RECT().split(",");
        // calculate the y position of canvas to draw the text
        Rect rectangle = new Rect(
                Integer.parseInt(strRect[0]),
                Integer.parseInt(strRect[1]),
                Integer.parseInt(strRect[2]),
                Integer.parseInt(strRect[3]));

        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(bitmap);

        // Initialize a new Paint instance to draw the text
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        if (colorPosition != -1)
            paint.setColor(Color.parseColor(colorArray[colorPosition]));
        else
            paint.setColor(Color.parseColor(bubbleModel.getKEY_TEXT_COLOR()));
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        String selecteFont = "";
        if (fontPosition != -1)
            selecteFont = fontNameArr[fontPosition];
        else
            selecteFont = bubbleModel.getKEY_FONT();
        if (selecteFont.length() > 0) {
            Typeface typeface = SupportClass.getTypefaceFromAsset(ActEditMedia.this, selecteFont);
            paint.setTypeface(typeface);
        }

        //Finally, Draw the text on the canvas at the horizontal and vertical center position
        String[] strMsgLine = strText.split("\n");
        float textHeight = getTextHeight(strText, paint);
        Log.e("PANTAG", bubbleModel.getKEY_GUID() + "\nX => " + rectangle.exactCenterX() * 2 + " And\nY => " + rectangle.exactCenterY() * 2);
        if (strMsgLine.length == 1) {
            canvas.drawText(strText, // Text to draw
                    rectangle.exactCenterX() * 2, // x
                    rectangle.exactCenterY() * 2, // y
                    paint // Paint
            );
            Log.e("PANTAG", "Line 1 => " + rectangle.exactCenterY() * 2);
        } else if (strMsgLine.length == 2) {
            float p = ((textHeight / 3) * 2);
            float line1 = (rectangle.exactCenterY() * 2) - p;
            float line2 = (rectangle.exactCenterY() * 2) + p;
            canvas.drawText(strMsgLine[0], // Text to draw
                    rectangle.exactCenterX() * 2, // x
                    line1, // y
                    paint // Paint
            );
            canvas.drawText(strMsgLine[1], // Text to draw
                    rectangle.exactCenterX() * 2, // x
                    line2, // y
                    paint // Paint
            );
            Log.e("PANTAG", "Line 1 => " + line1 + " Line 2 => " + line2 + " Diff => " + p);
        } else {
            float p = ((textHeight / 3) * 4);
            float line1 = (rectangle.exactCenterY() * 2) - p;
            float line2 = rectangle.exactCenterY() * 2;
            float line3 = (rectangle.exactCenterY() * 2) + p;
            canvas.drawText(strMsgLine[0], // Text to draw
                    rectangle.exactCenterX() * 2, // x
                    line1, // y
                    paint // Paint
            );
            canvas.drawText(strMsgLine[1], // Text to draw
                    rectangle.exactCenterX() * 2, // x
                    line2, // y
                    paint // Paint
            );
            canvas.drawText(strMsgLine[2], // Text to draw
                    rectangle.exactCenterX() * 2, // x
                    line3,
                    paint // Paint
            );
            Log.e("PANTAG", "Line 1 => " + line1 + " Line 2 => " + line2 + " Line 3 => " + line3 + " Diff => " + p);
        }
        return bitmap;
    }

    private float getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    private InputStream bubbleXmlInputStream(String fileName) throws IOException {
        InputStream is = this.getAssets().open("textbubble/" + fileName);
        return is;
    }

    private ArrayList<TextBubblesModel> getLoadedXmlValues() throws XmlPullParserException, IOException {
        ArrayList<TextBubblesModel> arrListData = new ArrayList<>();
        for (int p = 0; p < SupportClass.xmlFileName.length; p++) {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            // access the xml file and convert it to input stream
            String fileName = SupportClass.xmlFileName[p];
            InputStream is = bubbleXmlInputStream(fileName);
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            TextBubblesModel bubbleModel = new TextBubblesModel();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("bubble")) {
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attributeName = parser.getAttributeName(i);
                            String attributeValue = parser.getAttributeValue(i);
                            if (attributeName.equals(SupportClass.KEY_BUBBLE_IMAGE)) {
                                bubbleModel.setKEY_BUBBLE_IMAGE(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_BUBBLE_OPACITY)) {
                                bubbleModel.setKEY_BUBBLE_OPACITY(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_FONT)) {
                                bubbleModel.setKEY_FONT(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_GUID)) {
                                bubbleModel.setKEY_GUID(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_SHADOW)) {
                                bubbleModel.setKEY_SHADOW(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_STROKE_COLOR)) {
                                bubbleModel.setKEY_STROKE_COLOR(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_TEXT_COLOR)) {
                                bubbleModel.setKEY_TEXT_COLOR(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_TEXT_SIZE)) {
                                bubbleModel.setKEY_TEXT_SIZE(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_TEXT_MATRIX)) {
                                bubbleModel.setKEY_TEXT_MATRIX(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_TEXT_OPACITY)) {
                                bubbleModel.setKEY_TEXT_OPACITY(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_TEXT_RECT)) {
                                bubbleModel.setKEY_TEXT_RECT(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_THUMB_IMAGE)) {
                                bubbleModel.setKEY_THUMB_IMAGE(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_TYPE)) {
                                bubbleModel.setKEY_TYPE(attributeValue);
                            } else if (attributeName.equals(SupportClass.KEY_VERSION)) {
                                bubbleModel.setKEY_VERSION(attributeValue);
                            }
                        }
                    }
                }
                eventType = parser.next();
            }
            arrListData.add(bubbleModel);
        }
        return arrListData;
    }

    private void checkBitmap(String selectedImagePath) {
        try {
            bm = VaultFileUtil.decodeFile(selectedImagePath);

            ViewTreeObserver vto = linImgStickerView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        linImgStickerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        linImgStickerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    screenImgEditorWidth = linImgStickerView.getMeasuredWidth();
                    screenImgEditorHeight = linImgStickerView.getMeasuredHeight();
                    if (bm != null) {
                        manageBitmapWiseView(bm);
                    }

                }
            });

            Bitmap thumbBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImagePath), 128, 128);
            if (thumbBitmap != null)
                prepareThumbnail(thumbBitmap);
//            else
//                Toast.makeText(this, "Thumb not detect", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getImage() {
        BitmapDrawable originaldraw = (BitmapDrawable) mainUserImage.getDrawable();
        if (originaldraw != null) {
            originalbitmap = originaldraw.getBitmap();
        } /*else {
            Toast.makeText(this, "Image not detacted", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void manageBitmapWiseView(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int sWidth;
        int sHeight;
        if (bitmapWidth < bitmapHeight) {
            // 810 1555
            // 540  ?
            sWidth = screenImgEditorWidth;
            sHeight = (bitmapHeight * screenImgEditorWidth) / bitmapWidth;
            int pW = sWidth;
            int qH = sHeight;
            if (sHeight > screenImgEditorHeight) {
                sHeight = screenImgEditorHeight;
                sWidth = (pW * screenImgEditorHeight) / qH;
            }

        } else {
            // 800 1080
            // 506  ?
            sWidth = screenImgEditorWidth;
            sHeight = (bitmapHeight * screenImgEditorWidth) / bitmapWidth;
        }
        rel_image.getLayoutParams().width = sWidth;
        rel_image.getLayoutParams().height = sHeight;
        rel_image.requestLayout();
        rel_image.setDrawingCacheEnabled(true);
        mainUserImage.setImageBitmap(bitmap);
        drawingView.setConfig(sWidth, sHeight);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "IMG_" + System.currentTimeMillis(), null);
        return TextUtils.isEmpty(path) ? null : Uri.parse(path);
    }

    public int getBubbleStickerTextSize() {
        if (currentSticker == null)
            currentSticker = stickerView.getCurrentSticker();
        int currSize = ((BubbleStickers) currentSticker).getTextSize();
        return ((BubbleStickers) currentSticker).getTextSize() / 5;
    }

    public int getDefaultProgress(String selectedOption) {
        (findViewById(R.id.seekbarLineCenter)).setVisibility(View.GONE);
        int progress = 0;
        if (selectedOption.equalsIgnoreCase(getString(R.string.txtBrightness)) ||
                selectedOption.equalsIgnoreCase(getString(R.string.txtContrast)) ||
                selectedOption.equalsIgnoreCase(getString(R.string.txtSharpness)) ||
                selectedOption.equalsIgnoreCase(getString(R.string.txtSaturation)) ||
                selectedOption.equalsIgnoreCase(getString(R.string.txtGamma)) ||
                selectedOption.equalsIgnoreCase(getString(R.string.txtVignette))) {
            setSeekBarProgressProgrammatically(0);
            progress = 50;
            (findViewById(R.id.seekbarLineCenter)).setVisibility(View.VISIBLE);

        } else if (selectedOption.equalsIgnoreCase(getString(R.string.txtSepia)) ||
                selectedOption.equalsIgnoreCase(getString(R.string.txtToon))) {
            setSeekBarProgressProgrammatically(0);
            progress = 0;

        } else if (selectedOption.equalsIgnoreCase(getString(R.string.txtBlur))) {
            setSeekBarProgressProgrammatically(0);
            progress = 0;

        } else if (selectedOption.equalsIgnoreCase(getString(R.string.txtBrushSize))) {
            progress = drawingView.getStrokeWidth();

        } else if (selectedOption.equalsIgnoreCase(getString(R.string.txtBrushOpacity))) {
            progress = drawingView.getOpacity();
        }
        return progress;
    }

    public void setSeekBarProgressProgrammatically(int progress) {
        isSetProgrammmatically = true;
        seekBar.setProgress(progress);
    }

    public void seekbarViewAnimation(boolean upAnimation, String selectedOption) {
        if (upAnimation) {
            linSeekBarView.setVisibility(View.VISIBLE);
            setSeekBarProgressProgrammatically(getDefaultProgress(selectedOption));
            slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
            linSeekBarView.startAnimation(slideUpAnimation);
            ((TextView) findViewById(R.id.tvText)).setText(selectedOption);

        } else {
            slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down_edit);
            linSeekBarView.startAnimation(slideDownAnimation);
            slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linSeekBarView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void saveImage() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(ActEditMedia.this);
        progressDialog.setMessage(getString(R.string.txtPleaseWait));
        progressDialog.setCancelable(false);

        new saveImageTask().execute();
    }

    public Uri addImageToGallery(final String filePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void Brightness() {
        seekbarViewAnimation(true, getString(R.string.txtBrightness));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        mFilter = new GPUImageBrightnessFilter();
        ((GPUImageBrightnessFilter) mFilter).setBrightness(0.0f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    public void Contrast() {
        seekbarViewAnimation(true, getString(R.string.txtContrast));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        mFilter = new GPUImageContrastFilter();
        ((GPUImageContrastFilter) mFilter).setContrast(1.0f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    public void Sharpness() {
        seekbarViewAnimation(true, getString(R.string.txtSharpness));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        mFilter = new GPUImageSharpenFilter();
        ((GPUImageSharpenFilter) mFilter).setSharpness(0.0f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    public void Saturation() {
        seekbarViewAnimation(true, getString(R.string.txtSaturation));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        mFilter = new GPUImageSaturationFilter();
        ((GPUImageSaturationFilter) mFilter).setSaturation(1.0f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    public void Blur() {
        seekbarViewAnimation(true, getString(R.string.txtBlur));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        mFilter = new GPUImageGaussianBlurFilter();
        ((GPUImageGaussianBlurFilter) mFilter).setBlurSize(0.0f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    public void Gamma() {
        seekbarViewAnimation(true, getString(R.string.txtGamma));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        mFilter = new GPUImageGammaFilter();
        ((GPUImageGammaFilter) mFilter).setGamma(1.0f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    public void Sepia() {
        seekbarViewAnimation(true, getString(R.string.txtSepia));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        mFilter = new GPUImageSepiaFilter(0.0f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    public void Vignette() {
        seekbarViewAnimation(true, getString(R.string.txtVignette));
        BitmapDrawable bmpDrawable = (BitmapDrawable) mainUserImage.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        PointF centerPoint = new PointF();
        centerPoint.x = 0.5f;
        centerPoint.y = 0.5f;
        mFilter = new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
        gpuImage.setImage(bmp);
        gpuImage.setFilter(mFilter);
        Bitmap myBmp = gpuImage.getBitmapWithFilterApplied();
        if (myBmp != null)
            mainUserImage.setImageBitmap(myBmp);
        mFilterAdjuster = new GPUImageFilterTool.FilterAdjuster(mFilter);
    }

    /**
     * Renders thumbnails in horizontal list
     * loads default image from Assets if passed param is null
     *
     * @param thumbBitmap
     */
    public void prepareThumbnail(final Bitmap thumbBitmap) {
        Runnable r = new Runnable() {
            public void run() {

                ThumbnailsManager.clearThumbs();
                final List<ThumbnailItem> thumbnailItemList = new ArrayList<>();

                // add normal bitmap first
                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbBitmap;
                thumbnailItem.filterName = getString(R.string.filter_normal);
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(ActEditMedia.this);
                for (int i = 0; i < filters.size() - 3; i++) {
                    Filter filter = filters.get(i);
                    ThumbnailItem tI = new ThumbnailItem();
                    tI.image = thumbBitmap;
                    tI.filter = filter;
                    tI.filterName = filter.getName();
                    ThumbnailsManager.addThumb(tI);
                }
                thumbnailItemList.addAll(ThumbnailsManager.processThumbs(ActEditMedia.this));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RepeaterFiltersNew filtersAdapterNew = new RepeaterFiltersNew(thumbnailItemList, rotateImage, new CallbackFilterItemClick() {
                            @Override
                            public void onFilterClicked(Filter filter) {
                                isCurrentOptionEdited = true;
                                // applying the selected filter
                                filteredImageBitmap = originalbitmap.copy(Bitmap.Config.ARGB_8888, true);
                                // preview filtered image
                                mainUserImage.setImageBitmap(filter.processFilter(filteredImageBitmap));
                            }
                        });
                        recyclerFilter.setAdapter(filtersAdapterNew);
                    }
                });
            }
        };

        new Thread(r).start();
    }

    public void addTextAnimation(boolean upAnimation, final String strText) {
        if (upAnimation) {
            etAddTextSticker.setText(strText);
            relTextEditingPanel.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvText)).setText(getString(R.string.txtText));
            slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
            etAddTextSticker.startAnimation(slideUpAnimation);

        } else {
            slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down_edit);
            etAddTextSticker.startAnimation(slideDownAnimation);
            slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ((TextView) findViewById(R.id.tvText)).setText(getString(R.string.txtEditor));
                    relTextEditingPanel.setVisibility(View.GONE);
                    hideKeyboard();
                    etAddTextSticker.setText(strText);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    public void itemSelectFromList(final LinearLayout linLayout, final RecyclerView recyclerView, boolean upAnimation) {
        recyclerView.setVisibility(View.VISIBLE);
        if (upAnimation) {
            linLayout.setVisibility(View.VISIBLE);
            slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
            linLayout.startAnimation(slideUpAnimation);

        } else {
            slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down_edit);
            linLayout.startAnimation(slideDownAnimation);
            slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void hideKeyboard() {
        try {
            RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.activity_main);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onBackPressed() {
        if (linThirdDivisionOption.getVisibility() == View.VISIBLE) {
            if (currentSticker == null)
                currentSticker = stickerView.getCurrentSticker();

            if (currentSticker != null && recyclerFont.getVisibility() == View.VISIBLE) {
                int previousFontPosition = ((BubbleStickers) currentSticker).getCurrentFontPosition();
                if (currentSticker instanceof BubbleStickers) {
                    Bitmap bmpSticker = generateDynamicStickerBitmap(
                            ((BubbleStickers) currentSticker).getText(),
                            ((BubbleStickers) currentSticker).getTextSize(),
                            ((BubbleStickers) currentSticker).getCurrBubblePosition(),
                            ((BubbleStickers) currentSticker).getCurrentFontColorPosition(),
                            previousFontPosition);
                    Drawable d = new BitmapDrawable(getResources(), bmpSticker);
                    ((BubbleStickers) currentSticker).setDrawable(d);
                    stickerView.replace(currentSticker);
                    stickerView.invalidate();
                }
                itemSelectFromList(linThirdDivisionOption, recyclerFont, false);

            } else if (currentSticker != null && recyclerBubble.getVisibility() == View.VISIBLE) {
                int previousBubbleWiseTxtSize = ((BubbleStickers) currentSticker).getTextSize();
                int previousBubblePosition = ((BubbleStickers) currentSticker).getCurrBubblePosition();
                if (currentSticker instanceof BubbleStickers) {
                    Bitmap bmpSticker = generateDynamicStickerBitmap(
                            ((BubbleStickers) currentSticker).getText(),
                            previousBubbleWiseTxtSize,
                            previousBubblePosition,
                            ((BubbleStickers) currentSticker).getCurrentFontColorPosition(),
                            ((BubbleStickers) currentSticker).getCurrentFontPosition());
                    Drawable d = new BitmapDrawable(getResources(), bmpSticker);
                    ((BubbleStickers) currentSticker).setDrawable(d);
                    stickerView.replace(currentSticker);
                    stickerView.invalidate();
                }
                itemSelectFromList(linThirdDivisionOption, recyclerBubble, false);

            } else if (recyclerSticker.getVisibility() == View.VISIBLE) {
                for (int i = 0; i < lastTimeInertedSticker; i++) {
                    Sticker sticker = stickerView.getLastSticker();
                    stickerView.remove(sticker);
                }
                itemSelectFromList(linThirdDivisionOption, recyclerSticker, false);
            }

        } else if (relTextEditingPanel.getVisibility() == View.VISIBLE) {
            isEditMode = false;
            mAdapter.notifySelection(oldSelectedOption);
            recyclerViewMoveFirstPosition(recyclerMainAppOption, oldSelectedOption);
            addTextAnimation(false, "");

        } else {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.txt_msg_confirmation_dialog_cancel_editing))
                    .setPositiveButton(getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.txt_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivCheckMark:
                if (linThirdDivisionOption.getVisibility() == View.VISIBLE) {
                    if (currentSticker == null)
                        currentSticker = stickerView.getCurrentSticker();

                    if (currentSticker != null && recyclerFont.getVisibility() == View.VISIBLE) {
                        itemSelectFromList(linThirdDivisionOption, recyclerFont, false);

                    } else if (currentSticker != null && recyclerBubble.getVisibility() == View.VISIBLE) {
                        itemSelectFromList(linThirdDivisionOption, recyclerBubble, false);

                    } else if (recyclerSticker.getVisibility() == View.VISIBLE) {
                        itemSelectFromList(linThirdDivisionOption, recyclerSticker, false);
                    }
                }
                break;

            case R.id.ivClose:
                onBackPressed();
                break;

            case R.id.ivEditingClose:
                onBackPressed();
                break;

            case R.id.ivEditingApply:
                addTextAnimation(false, "");
                if (etAddTextSticker.getText().toString().trim().length() > 0) {
                    if (isEditMode) {
                        isEditMode = false;

                        int textSize = getDetermineMaxTextSize(etAddTextSticker.getText().toString().trim(), 450);
                        Bitmap bmpSticker = generateDynamicStickerBitmap(
                                etAddTextSticker.getText().toString().trim(),
                                textSize,
                                ((BubbleStickers) currentSticker).getCurrBubblePosition(),
                                ((BubbleStickers) currentSticker).getCurrentFontColorPosition(),
                                ((BubbleStickers) currentSticker).getCurrentFontPosition());
                        Drawable d = new BitmapDrawable(getResources(), bmpSticker);
                        ((BubbleStickers) currentSticker).setTextSize(textSize);
                        ((BubbleStickers) currentSticker).setText(etAddTextSticker.getText().toString().trim());
                        ((BubbleStickers) currentSticker).setDrawable(d);
                        setSeekBarProgressProgrammatically(getBubbleStickerTextSize());
                        stickerView.replace(currentSticker);
                        stickerView.invalidate();

                    } else {
                        int textSize = getDetermineMaxTextSize(etAddTextSticker.getText().toString().trim(), 450);
                        Bitmap bmpSticker = generateDynamicStickerBitmap(
                                etAddTextSticker.getText().toString().trim(),
                                textSize,
                                0,
                                -1,
                                -1);
                        Drawable d = new BitmapDrawable(getResources(), bmpSticker);
                        BubbleStickers bubbleSticker = new BubbleStickers(d);
                        bubbleSticker.setText(etAddTextSticker.getText().toString().trim());
                        bubbleSticker.setTextSize(textSize);
                        stickerView.addSticker(bubbleSticker);

                        hideAllOptionRecycler(true, false);
                        if (recyclerColor.getVisibility() != View.VISIBLE)
                            colorSelectionAnimation(true);
                        seekbarViewAnimation(true, getString(R.string.txtText));
                        recyclerViewMoveFirstPosition(recyclerStickerTextOption, 0);
                        recyclerStickerTextOption.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    private void applyBrushAndSave(Bitmap brushBitmap) {
        //getImage before applying Brush
        getImage();

        Bitmap mutableBitmap = brushBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap resizedMutableBitmap = Bitmap.createScaledBitmap(mutableBitmap, originalbitmap.getWidth(), originalbitmap.getHeight(), false);

        Bitmap bitmapResult = getBitmapOverlay(originalbitmap, resizedMutableBitmap);
        mainUserImage.setImageBitmap(bitmapResult);
    }

    private Bitmap getBitmapOverlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public void colorSelectionAnimation(boolean upAnimation) {
        if (upAnimation) {
            recyclerViewMoveFirstPosition(recyclerColor, 0);
            recyclerColor.setVisibility(View.VISIBLE);
            slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
            recyclerColor.startAnimation(slideUpAnimation);

        } else {
            slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down_edit);
            recyclerColor.startAnimation(slideDownAnimation);
            slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    recyclerColor.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void initUi() {

        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
//        toolbar.setTitleTextColor(Color.WHITE);


        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getApplicationContext()).clearMemory();
        Glide.get(getApplicationContext()).trimMemory(TRIM_MEMORY_COMPLETE);
        System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (!SupportClass.isExternalStoragePermissionGranted(ActEditMedia.this)) {
                SupportClass.showTakeWritePermissionDialog(ActEditMedia.this);
            } else {
                saveImage();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_CODE_CROPPING && resultCode == RESULT_OK && resultData != null) {
            if (resultData.hasExtra("croppedBitmapByteArr")) {
                Bitmap croppedBitmap = BitmapFactory.decodeByteArray(
                        resultData.getByteArrayExtra("croppedBitmapByteArr"),
                        0,
                        resultData.getByteArrayExtra("croppedBitmapByteArr").length);

                originalbitmap = croppedBitmap;
                if (croppedBitmap != null) {
                    manageBitmapWiseView(croppedBitmap);
                } else {
                    Toast.makeText(ActEditMedia.this, "Sorry for inconvenience, Try again", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 590) {
            if (SupportClass.isExternalStoragePermissionGranted(ActEditMedia.this)) {
                saveImage();
            } else {
                Toast.makeText(ActEditMedia.this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 591) {// permission
            if (SupportClass.isExternalStoragePermissionGranted(ActEditMedia.this)) {
                saveImage();
            } else {
                Toast.makeText(ActEditMedia.this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (DeviceUtil.isLandscape(getResources()))
            params.setMargins(0, 0, Measures.getNavigationBarSize(ActEditMedia.this).x, 0);
        else
            params.setMargins(0, 0, 0, 0);

        toolbar.setLayoutParams(params);
    }

    private class saveImageTask extends AsyncTask<String, String, Exception> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stickerView.setLocked(true);
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected Exception doInBackground(String... strings) {
            //get Filter applied image
            getImage();
            if (originalbitmap != null) {

                rel_image.setDrawingCacheEnabled(true);

                rel_image.buildDrawingCache();

                Bitmap bitmap = rel_image.getDrawingCache();

                String fileName = DateTimeUtil.getCurrentDateTime().replaceAll(":", "-").concat(Constants.KEY_PNG);
                File editForlder = new File(ImageUtil.OUTPUT_EDIT_FOLDER);
                if (!editForlder.exists()) {
                    editForlder.mkdirs();
                }
                File file = new File(editForlder, fileName);
                oldSavedFileName = fileName;
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    savedImageUri = ImageUtil.moveFile(file);

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.parse(savedImageUri));
                    sendBroadcast(intent);
                    return null;
                } catch (Exception e) {
                    return e;
                }
            } else {
                return new Exception("Image not Detacted");
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
            if (ActEditMedia.this.isFinishing() || ActEditMedia.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                return;
            }
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            if (e == null) {
                Toast.makeText(ActEditMedia.this, getString(R.string.txt_creation_saved_successfully), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ActEditMedia.this, ActCollageView.class);
                intent.putExtra("reslutUri", savedImageUri);
                intent.putExtra("from", R.string.share);
                if (MyApp.getInstance().needToShowAd()) {
                    MyApp.getInstance().showInterstitial(ActEditMedia.this, intent, true, -1, null);
                } else {
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(ActEditMedia.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

