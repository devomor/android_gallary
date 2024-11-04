package com.photo.photography.data_helper.metadata_helper;

import android.content.Context;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.photo.photography.R;
import com.photo.photography.data_helper.Media;
import com.photo.photography.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MetadataHelpers {

    public MediaDetailMap<String, String> getMainDetails(Context context, Media m) {
        MediaDetailMap<String, String> details = new MediaDetailMap<>();
        details.put(context.getString(R.string.path), m.getDisplayPath());
        details.put(context.getString(R.string.type), m.getMimeType());
        if (m.getSize() != -1)
            details.put(context.getString(R.string.size), StringUtil.humanReadableByteCount(m.getSize(), true));
        // TODO should i add this always?
        details.put(context.getString(R.string.orientation), m.getOrientation() + "");
        MetaDataItems metadata = MetaDataItems.getMetadata(context, m.getUri());
        details.put(context.getString(R.string.resolution), metadata.getResolution());
        details.put(context.getString(R.string.date), SimpleDateFormat.getDateTimeInstance().format(new Date(m.getDateModified())));
        Date dateOriginal = metadata.getDateOriginal();
        if (dateOriginal != null)
            details.put(context.getString(R.string.date_taken), SimpleDateFormat.getDateTimeInstance().format(dateOriginal));

        String tmp;
        if ((tmp = metadata.getCameraInfo()) != null)
            details.put(context.getString(R.string.camera), tmp);
        if ((tmp = metadata.getExifInfo()) != null)
            details.put(context.getString(R.string.exif), tmp);
        GeoLocation location;
        if ((location = metadata.getLocation()) != null)
            details.put(context.getString(R.string.location), location.toDMSString());


        return details;
    }

    public MediaDetailMap<String, String> getAllDetails(Context context, Media media) {
        MediaDetailMap<String, String> data = new MediaDetailMap<String, String>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(context.getContentResolver().openInputStream(media.getUri()));
            for (Directory directory : metadata.getDirectories()) {

                for (Tag tag : directory.getTags()) {
                    data.put(tag.getTagName(), directory.getObject(tag.getTagType()) + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
