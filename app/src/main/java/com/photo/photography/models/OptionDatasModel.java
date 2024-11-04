package com.photo.photography.models;

/**
 * Created on 21-03-2018, 05:12:06 PM.
 */

public class OptionDatasModel {

    String optionName;
    int optionIcon;
    int optionIconSelected;
    boolean isOptionSelectable;
    boolean isSelected;
    boolean withDevider;

    public OptionDatasModel(String optionName, int optionIcon, boolean isOptionSelectable, boolean isSelected, boolean withDevider) {
        this.optionName = optionName;
        this.optionIcon = optionIcon;
        this.isOptionSelectable = isOptionSelectable;
        this.isSelected = isSelected;
        this.withDevider = withDevider;
    }

    public OptionDatasModel(String optionName, int optionIcon, int optionIconSelected, boolean isOptionSelectable, boolean isSelected, boolean withDevider) {
        this.optionName = optionName;
        this.optionIcon = optionIcon;
        this.optionIconSelected = optionIconSelected;
        this.isOptionSelectable = isOptionSelectable;
        this.isSelected = isSelected;
        this.withDevider = withDevider;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionIcon() {
        return optionIcon;
    }

    public void setOptionIcon(int optionIcon) {
        this.optionIcon = optionIcon;
    }

    public int getOptionIconSelected() {
        return optionIconSelected;
    }

    public void setOptionIconSelected(int optionIconSelected) {
        this.optionIconSelected = optionIconSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isOptionSelectable() {
        return isOptionSelectable;
    }

    public void setOptionSelectable(boolean isOptionSelectable) {
        isOptionSelectable = isOptionSelectable;
    }

    public boolean withDevider() {
        return withDevider;
    }

    public void setDevider(boolean withDevider) {
        withDevider = withDevider;
    }

}
