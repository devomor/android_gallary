package com.photo.photography.data_helper.filters_mode;

import java.io.File;
import java.io.FileFilter;

public class NotHiddenFolderFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return file.isDirectory() && !file.isHidden() && !new File(file, ".nomedia").exists();
    }
}