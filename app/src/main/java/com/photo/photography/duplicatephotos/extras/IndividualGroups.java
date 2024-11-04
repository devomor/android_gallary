package com.photo.photography.duplicatephotos.extras;

import java.util.List;

public class IndividualGroups {

    private long dbId = -1;
    private boolean groupSetCheckBox;
    private int groupTag;
    private List<ImagesItem> individualGrpOfDupes;

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public boolean isGroupSetCheckBox() {
        return this.groupSetCheckBox;
    }

    public void setGroupSetCheckBox(boolean z) {
        this.groupSetCheckBox = z;
    }

    public int getGroupTag() {
        return this.groupTag;
    }

    public void setGroupTag(int i) {
        this.groupTag = i;
    }

    public List<ImagesItem> getIndividualGrpOfDupes() {
        return this.individualGrpOfDupes;
    }

    public void setIndividualGrpOfDupes(List<ImagesItem> list) {
        this.individualGrpOfDupes = list;
    }

}
