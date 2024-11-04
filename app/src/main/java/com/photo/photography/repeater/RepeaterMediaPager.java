package com.photo.photography.repeater;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.photo.photography.data_helper.Media;
import com.photo.photography.frag.FragGif;
import com.photo.photography.frag.FragImage;
import com.photo.photography.frag.FragVideo;

import java.util.ArrayList;

public class RepeaterMediaPager extends FragmentStatePagerAdapter {

    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private ArrayList<Media> media;

    public RepeaterMediaPager(FragmentManager fm, ArrayList<Media> media) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.media = media;
    }

    @Override
    public Fragment getItem(int pos) {
        Media media = this.media.get(pos);
        if (media.isVideo()) return FragVideo.newInstance(media, pos);
        if (media.isGif()) return FragGif.newInstance(media, pos);
        else return FragImage.newInstance(media, pos);
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public void swapDataSet(ArrayList<Media> media) {
        this.media = media;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (media == null)
            return 0;
        return media.size();
    }
}