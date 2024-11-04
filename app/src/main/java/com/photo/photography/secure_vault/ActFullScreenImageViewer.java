package com.photo.photography.secure_vault;

import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.util.utils.Util;
import com.photo.photography.secure_vault.repeater.RepeaterFullScreenImage;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.view.HackeyViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActFullScreenImageViewer extends ActBase {
    private static final String ISLOCKED_ARG = "isLocked";
    private static final int SLIDE_SHOW_INTERVAL = 5000;
    @BindView(R.id.pager)
    HackeyViewPager mPager;
    @BindView(R.id.relToolbar)
    RelativeLayout relToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<VaultFile> mGalleryModelList = new ArrayList<>();
    private boolean fullScreenMode;
    private int position = 0;
//    private Handler handler = new Handler();
//    private final Runnable slideShowRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
////                mPager.setCurrentItem((mPager.getCurrentItem() + 1) % album.getCount());
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                handler.postDelayed(this, SLIDE_SHOW_INTERVAL);
//            }
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pager_layout);
        ButterKnife.bind(this);

        showSystemBars();
        relToolbar.setPaddingRelative(0, Util.getStatusBarHeight(getResources()), 0, 0);

        mGalleryModelList = (ArrayList<VaultFile>) getIntent().getSerializableExtra("data");

        if (savedInstanceState != null) {
            mPager.setLocked(savedInstanceState.getBoolean(ISLOCKED_ARG, false));
        }

        initUi();

    }

    private void prepareSharedElementTransition() {
        TransitionSet transitionSet = new TransitionSet();
        Transition bound = new ChangeBounds();
        transitionSet.addTransition(bound);
        Transition changeImageTransform = new ChangeImageTransform();
        transitionSet.addTransition(changeImageTransform);
        transitionSet.setDuration(375);
        getWindow().setSharedElementEnterTransition(transitionSet);
        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                // Locate the image view at the primary fragment (the ImageFragment that is currently
                // visible). To locate the fragment, call instantiateItem with the selection position.
                // At this stage, the method will simply return the fragment at the position and will
                // not create a new one.
                Fragment currentFragment = (Fragment) mPager.getAdapter().instantiateItem(mPager, position);
                View view = currentFragment.getView();
                if (view == null) {
                    return;
                }
                sharedElements.put(names.get(0), view.findViewById(R.id.imageView));
            }
        });
    }

    private void initUi() {
        setSupportActionBar(toolbar);

        toolbar.bringToFront();
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(ActFullScreenImageViewer.this, R.color.white));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getSupportActionBar().setTitle(getIntent().getBooleanExtra("isVideo", false) ? "Videos" : "Images");

        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_fadein);
        mAnimation.setDuration(800);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        toolbar.setAnimation(mAnimation);

        mPager.setAdapter(new RepeaterFullScreenImage(this, mGalleryModelList, getIntent().getBooleanExtra("isVideo", false), this::toggleSystemUI));
        mPager.setCurrentItem(getIntent().getIntExtra("position", 0));
//        mPager.setCurrentItem(position);
        prepareSharedElementTransition();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override

            public void onPageSelected(int pos) {
                position = pos;

//                updatePageTitle(position);

                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation() == Surface.ROTATION_90) {
            Configuration configuration = new Configuration();
            configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;
            onConfigurationChanged(configuration);
        }
    }

//    @Override
//    public void onViewTapped() {
//        toggleSystemUI();
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getApplicationContext()).clearMemory();
        Glide.get(getApplicationContext()).trimMemory(TRIM_MEMORY_COMPLETE);
        System.gc();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        relToolbar.setPaddingRelative(0, Util.getStatusBarHeight(getResources()), 0, 0);
    }

    public void hideSystemUI() {
        runOnUiThread(() -> {
            hideSystem_Bars();
            relToolbar.animate().translationY(-relToolbar.getHeight()).setInterpolator(new AccelerateInterpolator()).setDuration(200).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    relToolbar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    Animator.AnimatorListener.super.onAnimationStart(animation, isReverse);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();

            fullScreenMode = true;
        });
    }

    private void hideSystem_Bars() {

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    private void showSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        int orientation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        if (orientation == 1) {
            // Hide both the status bar and the navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
            return;
        }
        // Hide both the status bar and the navigation bar
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars());
    }

    public void showSystemUI() {
        runOnUiThread(() -> {
            showSystemBars();
            relToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).setDuration(240).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    relToolbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            }).start();

            fullScreenMode = false;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mPager != null) {
            outState.putBoolean(ISLOCKED_ARG, mPager.isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    public void toggleSystemUI() {
        if (fullScreenMode) showSystemUI();
        else hideSystemUI();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        handler.removeCallbacks(slideShowRunnable);
//        handler = null;
//    }
}
