package com.photo.photography.frag;

import static com.androidquery.util.AQUtility.getHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.photo.photography.R;
import com.photo.photography.act.ActMain;
import com.photo.photography.act.ActSingleMedia;
import com.photo.photography.data_helper.Album;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class FragRvAllMedia extends FragBase {

    public static final String TAG = "RvMediaFragment";
    final Handler handler = getHandler();
    public ViewPager viewPager;
    public String[] titleArr = new String[]{"Photos", "Albums", /*"Videos",*/ "Explore"};
    public ViewPagerAdapter pagerAdapter;
    public int currTab = 0;
    private TabLayout tabLayout;
    private FrameLayout frameBannerAds;

    public static FragRvAllMedia make() {
        FragRvAllMedia fragment = new FragRvAllMedia();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_rv_all_media, container, false);
        ButterKnife.bind(this, v);

        viewPager = v.findViewById(R.id.viewpager);
        createViewPager(viewPager);

        frameBannerAds = v.findViewById(R.id.adContainerView);
        loadBannerAds(requireActivity(), frameBannerAds);

        tabLayout = v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment fragment = pagerAdapter.getItem(currTab);
                if (fragment instanceof FragRvAllMediaPhotos) {
                    ((FragRvAllMediaPhotos) fragment).clearSelected();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        createTabIcons();
        setCurrentTab(0);
        return v;
    }

    private void createTabIcons() {

        View tabOne = LayoutInflater.from(mActivity).inflate(R.layout.apk_tabs_home, null);
        TextView textViewOne = tabOne.findViewById(R.id.tab);
        textViewOne.setText(titleArr[0]);
        ImageView ivTabThumb1 = tabOne.findViewById(R.id.ivTabThumb);
        ivTabThumb1.setImageResource(R.drawable.e_tab_photos);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View tabTwo = LayoutInflater.from(mActivity).inflate(R.layout.apk_tabs_home, null);
        TextView textViewTwo = tabTwo.findViewById(R.id.tab);
        textViewTwo.setText(titleArr[1]);
        ImageView ivTabThumb2 = tabTwo.findViewById(R.id.ivTabThumb);
        ivTabThumb2.setImageResource(R.drawable.e_tab_album);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        View tabThree = LayoutInflater.from(mActivity).inflate(R.layout.apk_tabs_home, null);
        TextView textViewThree = tabThree.findViewById(R.id.tab);
        textViewThree.setText(titleArr[2]);
        ImageView ivTabThumb3 = tabThree.findViewById(R.id.ivTabThumb);
        ivTabThumb3.setImageResource(R.drawable.e_tab_explore);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setCurrentTab(int tabPos) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tabOne = tabLayout.getTabAt(i).getCustomView();

            TextView textViewOne = tabOne.findViewById(R.id.tab);
            textViewOne.setTextColor(ContextCompat.getColor(mActivity, i == tabPos ? R.color.tab_selected_new : R.color.tab_unselected_new));

            ImageView ivTabThumb = tabOne.findViewById(R.id.ivTabThumb);
            ivTabThumb.setColorFilter(ContextCompat.getColor(mActivity,i == tabPos ? R.color.tab_selected_new : R.color.tab_unselected_new));

            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    private void createViewPager(ViewPager viewPager) {
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFrag(new FragRvAllMediaPhotos().make(Album.getAllMediaAlbum()), titleArr[0]);
        pagerAdapter.addFrag(new FragAlbums(), titleArr[1]);
        pagerAdapter.addFrag(new FragRvAllMediaExplore(), titleArr[2]);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    handler.postDelayed(() -> {
                        Fragment fragment1 = pagerAdapter.getItem(currTab);
                        if (fragment1 instanceof FragRvAllMediaPhotos) {
                            ((FragRvAllMediaPhotos) fragment1).notifyAds();
                        }
                    }, 300);
                } else if (position == 1) {
                    handler.postDelayed(() -> {
                        Fragment fragment1 = pagerAdapter.getItem(currTab);
                        if (fragment1 instanceof FragAlbums && fragment1.isAdded()) {
                            ((FragAlbums) fragment1).notifyAds();
                        }
                    }, 300);
                }  else {
                    handler.postDelayed(() -> {
                        Fragment fragment1 = pagerAdapter.getItem(currTab);
                        if (fragment1 instanceof FragRvAllMediaExplore) {
                            ((FragRvAllMediaExplore) fragment1).loadNativeAds();
                            ((FragRvAllMediaExplore) fragment1).add();
                        }
                    }, 300);
                }

                if (currTab == 0) {
                    Fragment fragment = pagerAdapter.getItem(currTab);
                    if (fragment instanceof FragRvAllMediaPhotos) {
                        ((FragRvAllMediaPhotos) fragment).clearSelected();
                    }
                }
                currTab = position;
                ((ActMain) mActivity).updateToolbar(titleArr[currTab]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void refreshViewPager() {
        if (pagerAdapter != null && pagerAdapter.getCount() > 3) {
            ((FragRvAllMediaPhotos) pagerAdapter.getItem(0)).reInItMedia();
            ((FragAlbums) pagerAdapter.getItem(1)).reInItAlbums();
//            ((FragRvAllMediaVideos) pagerAdapter.getItem(2)).reInItMedia();
        }
    }

    public void refreshSpecificMediaFragment(Intent result) {
        if (viewPager != null && pagerAdapter != null) {
            if (pagerAdapter.getItem(0) instanceof FragRvAllMediaPhotos) {
                Log.e("TRACE_DELETE", "refreshSpecificMediaFragment if call for AllMediaFragment => PhotosFragment");
                if (result != null && result.hasExtra(ActSingleMedia.DELETED_POS_LIST))
                    ((FragRvAllMediaPhotos) pagerAdapter.getItem(0)).reInItMedia(result);
                else
                    ((FragRvAllMediaPhotos) pagerAdapter.getItem(0)).reInItMedia();
            }
//            if (pagerAdapter.getItem(2) instanceof FragRvAllMediaVideos) {
//                Log.e("TRACE_DELETE", "refreshSpecificMediaFragment if call for AllMediaFragment => VideosFragment");
//                if (result != null && result.hasExtra(ActSingleMedia.DELETED_POS_LIST))
//                    ((FragRvAllMediaVideos) pagerAdapter.getItem(2)).reInItMedia(result);
//                else
//                    ((FragRvAllMediaVideos) pagerAdapter.getItem(2)).reInItMedia();
//            }
        }else{
            if (result != null && result.hasExtra(ActSingleMedia.DELETED_POS_LIST))
                new FragRvAllMediaVideos().reInItMedia(result);
            else
                new FragRvAllMediaVideos().reInItMedia();
        }
    }

    public boolean isMediaSelected() {
        if (viewPager.getCurrentItem() == 0) {
            Fragment fragment = pagerAdapter.getItem(viewPager.getCurrentItem());
            if (fragment instanceof FragRvAllMediaPhotos) {
                return ((FragRvAllMediaPhotos) fragment).isMediaSelected();
            }
        } /*else if (viewPager.getCurrentItem() == 2) {
            Fragment fragment = pagerAdapter.getItem(viewPager.getCurrentItem());
            if (fragment instanceof FragRvAllMediaVideos) {
                return ((FragRvAllMediaVideos) fragment).isMediaSelected();
            }
        }*/
        return false;
    }

    public void writePermissionAction() {
        if (pagerAdapter != null && viewPager.getCurrentItem() == 0 && pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof FragRvAllMediaPhotos) {
            ((FragRvAllMediaPhotos) pagerAdapter.getItem(viewPager.getCurrentItem())).doUserAction();
        } else if (pagerAdapter != null && viewPager.getCurrentItem() == 1 && pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof FragAlbums) {
            ((FragAlbums) pagerAdapter.getItem(viewPager.getCurrentItem())).doUserAction();
        } /*else if (pagerAdapter != null && viewPager.getCurrentItem() == 2 && pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof FragRvAllMediaVideos) {
            ((FragRvAllMediaVideos) pagerAdapter.getItem(viewPager.getCurrentItem())).doUserAction();
        }*/else {
            ((FragRvAllMediaVideos) pagerAdapter.getItem(viewPager.getCurrentItem())).doUserAction();
        }
    }

    public void onSuccessGive11Permission(int requestCode, int resultCode) {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            if (resultCode == Activity.RESULT_OK) {
                if (pagerAdapter != null && viewPager.getCurrentItem() == 0 && pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof FragRvAllMediaPhotos) {
                    ((FragRvAllMediaPhotos) pagerAdapter.getItem(viewPager.getCurrentItem())).deletePendingMedia(requestCode, resultCode);

                } /*else if (pagerAdapter != null && viewPager.getCurrentItem() == 2 && pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof FragRvAllMediaVideos) {
                    ((FragRvAllMediaVideos) pagerAdapter.getItem(viewPager.getCurrentItem())).deletePendingMedia(requestCode, resultCode);
                }*/else{
                    ((FragRvAllMediaVideos) pagerAdapter.getItem(viewPager.getCurrentItem())).deletePendingMedia(requestCode, resultCode);
                }
            } else {
                if (SupportClass.userAction == SupportClass.USER_ACTION.DELETE) {
                    Toast.makeText(mActivity, "Permission required for Delete media", Toast.LENGTH_SHORT).show();
                } else if (SupportClass.userAction == SupportClass.USER_ACTION.MOVE_TO_VAULT) {
                    Toast.makeText(mActivity, "Permission required for Move to vault media", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "Permission required for this action", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
