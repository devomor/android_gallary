package com.photo.photography.duplicatephotos.frag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.repeater.RepeaterIndividualGroup;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.callback.CallbackMarked;
import com.photo.photography.duplicatephotos.util.PopUpDialogs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class FragSimilarDuplicates extends Fragment implements CallbackMarked {
    public static List<IndividualGroups> groupOfDupes = null;
    public static int index = -1;
    public static TextView tvNoDuplicatesFound = null;
    public static RecyclerView recyclerViewForIndividualGrp = null;
    public static int top = -1;
    private final ImageLoader imageLoader = GlobalVarsAndFunction.getImageLoadingInstance();
    private final DisplayImageOptions options = GlobalVarsAndFunction.getOptions();
    RepeaterIndividualGroup individualGroupAdapter;
    LinearLayoutManager mLayoutManager;
    Activity sActivity;
    Activity sDContext;
    View view;
    private TextView marked;

    @Override
    public void photosCleanedExact(int i) {
    }

    @Override
    public void photosCleanedSimilar(int i) {
    }

    @Override
    public void updateDuplicateFoundExact(int i) {
    }

    @Override
    public void updateDuplicateFoundSimilar(int i) {
    }

    @Override
    public void updateMarkedExact() {
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.frag_similar_photos, viewGroup, false);
        this.sActivity = getActivity();
        this.sDContext = getActivity();
        this.marked = (TextView) this.sActivity.findViewById(R.id.marked);
        GlobalVarsAndFunction.configureImageLoader(this.imageLoader, getActivity());
        if (GlobalVarsAndFunction.getSortBy(this.sDContext).equalsIgnoreCase(GlobalVarsAndFunction.DATE_UP)) {
            groupOfDupes = PopUpDialogs.sortByDateDescending(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar());
        } else if (GlobalVarsAndFunction.getSortBy(this.sDContext).equalsIgnoreCase(GlobalVarsAndFunction.DATE_DOWN)) {
            groupOfDupes = PopUpDialogs.sortByDateAscending(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar());
        } else if (GlobalVarsAndFunction.getSortBy(this.sDContext).equalsIgnoreCase(GlobalVarsAndFunction.SIZE_UP)) {
            groupOfDupes = PopUpDialogs.sortBySizeDescending(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar());
        } else if (GlobalVarsAndFunction.getSortBy(this.sDContext).equalsIgnoreCase(GlobalVarsAndFunction.SIZE_DOWN)) {
            groupOfDupes = PopUpDialogs.sortBySizeAscending(GlobalVarsAndFunction.getGroupOfDuplicatesSimilar());
        }
        this.individualGroupAdapter = new RepeaterIndividualGroup(this.sDContext, this.sActivity, setCheckBox(false), this, this.imageLoader, this.options);
        tvNoDuplicatesFound = view.findViewById(R.id.tvNoDuplicatesFound);
        recyclerViewForIndividualGrp = (RecyclerView) this.view.findViewById(R.id.recycler_view_similar);
        this.mLayoutManager = new LinearLayoutManager(this.sDContext);
        recyclerViewForIndividualGrp.setLayoutManager(this.mLayoutManager);
        recyclerViewForIndividualGrp.setAdapter(this.individualGroupAdapter);
        if (individualGroupAdapter.getItemCount() == 0) {
            tvNoDuplicatesFound.setVisibility(View.VISIBLE);
        } else {
            tvNoDuplicatesFound.setVisibility(View.GONE);
        }
        setHasOptionsMenu(true);
        return this.view;
    }

    @Override
    public void updateMarkedSimilar() {
        this.marked.setText(getString(R.string.Marked) + " " + GlobalVarsAndFunction.file_To_Be_Deleted_Similar.size() + " (" + GlobalVarsAndFunction.sizeInString() + ")");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.settings_2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_selectall) {
            similarSelectAllAndDeselectAll(true);
            return true;
        } else if (itemId == R.id.action_deselectall) {
            similarSelectAllAndDeselectAll(false);
            return true;
        } else if (itemId == R.id.action_setting) {
            new PopUpDialogs(this.sDContext, this.sActivity).matchingLevelPopUpDialog(1);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void similarSelectAllAndDeselectAll(boolean z) {
        this.individualGroupAdapter = new RepeaterIndividualGroup(this.sDContext, this.sActivity, setCheckBox(z), this, this.imageLoader, this.options);
        recyclerViewForIndividualGrp = (RecyclerView) this.view.findViewById(R.id.recycler_view_similar);
        recyclerViewForIndividualGrp.setLayoutManager(new LinearLayoutManager(this.sDContext));
        recyclerViewForIndividualGrp.setAdapter(this.individualGroupAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        index = this.mLayoutManager.findFirstVisibleItemPosition();
        int i = 0;
        View childAt = recyclerViewForIndividualGrp.getChildAt(0);
        if (childAt != null) {
            i = childAt.getTop() - recyclerViewForIndividualGrp.getPaddingTop();
        }
        top = i;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (index != -1) {
            this.mLayoutManager.scrollToPositionWithOffset(index, top);
        }
    }

    private List<IndividualGroups> setCheckBox(boolean z) {
        ArrayList arrayList = new ArrayList();
        GlobalVarsAndFunction.file_To_Be_Deleted_Similar.clear();
        GlobalVarsAndFunction.size_Of_File_Similar = 0;
        if (groupOfDupes != null) {
            for (int i = 0; i < groupOfDupes.size(); i++) {
                IndividualGroups individualGroup = groupOfDupes.get(i);
                individualGroup.setGroupSetCheckBox(z);
                ArrayList arrayList2 = new ArrayList();
                for (int i2 = 0; i2 < individualGroup.getIndividualGrpOfDupes().size(); i2++) {
                    ImagesItem imageItem = individualGroup.getIndividualGrpOfDupes().get(i2);
                    if (i2 == 0) {
                        imageItem.setImageCheckBox(false);
                        arrayList2.add(imageItem);
                    } else {
                        if (z) {
                            GlobalVarsAndFunction.file_To_Be_Deleted_Similar.add(imageItem);
                            GlobalVarsAndFunction.addSizeSimilar(imageItem.getSizeOfTheFile());
                            updateMarkedSimilar();
                        } else {
                            updateMarkedSimilar();
                        }
                        imageItem.setImageCheckBox(z);
                        arrayList2.add(imageItem);
                    }
                }
                individualGroup.setIndividualGrpOfDupes(arrayList2);
                arrayList.add(individualGroup);
            }
        }
        return arrayList;
    }
}
