package com.photo.photography.data_helper.filters_mode;

import java.io.File;
import java.io.FileFilter;

public class FoldersFileFilters implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }
}