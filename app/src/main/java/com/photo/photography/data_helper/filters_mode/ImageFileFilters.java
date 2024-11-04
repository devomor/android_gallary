package com.photo.photography.data_helper.filters_mode;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;


public class ImageFileFilters implements FilenameFilter {

    private final Pattern pattern;

    public ImageFileFilters(boolean includeVideo) {
        pattern = includeVideo
                ? Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp|mp4|mkv|webm|avi)$", Pattern.CASE_INSENSITIVE)
                : Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp)$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean accept(File dir, String filename) {
        return new File(dir, filename).isFile() && pattern.matcher(filename).find();
    }
}