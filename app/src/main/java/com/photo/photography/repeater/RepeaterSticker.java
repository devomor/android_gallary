package com.photo.photography.repeater;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.photo.photography.R;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.edit_views.SquareImagesView;
import com.photo.photography.models.StickerModelMan;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class RepeaterSticker extends RecyclerView.Adapter<RepeaterSticker.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<StickerModelMan> stickerList;
    private ItemClickListener mClickListener;
    private String typeMask;

    public RepeaterSticker(Context mContext, List<StickerModelMan> stickerList, String typeMask) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.stickerList = stickerList;
        this.typeMask = typeMask;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_stickers_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {

        final int position = holder.getAbsoluteAdapterPosition();
        if (position < stickerList.size()) {
            if (SupportClass.checkConnection(mContext)) {
                String fileNameFormat = null;
                if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_1)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_1, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_2)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_2, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_3)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_3, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_4)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_4, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_5)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_5, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_6)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_6, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_7)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_7, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_8)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_8, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_9)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_9, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_10)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_10, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_11)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_11, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_12)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_12, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_13)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_13, "");
                } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_14)) {
                    fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_14, "");
                }

                String fileName = fileNameFormat.replace(Constants.KEY_PNG, "");
                File existFile = new File(new VaultFileUtil(mContext).getFilePath(mContext.getString(R.string.app_folder_sticker)), fileName);
                boolean check = existFile.exists();
                String pathImageLoad = existFile.getAbsolutePath();
                if (check) {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(pathImageLoad)
                            .placeholder(R.drawable.e_placeholder)
                            .error(R.drawable.e_placeholder)
                            .into(holder.ivSticker);

                } else {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .asBitmap()
                            .placeholder(R.drawable.e_placeholder)
                            .error(R.drawable.e_placeholder)
                            .load(stickerList.get(position).getStickerImage())
                            .addListener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.progressBar.setVisibility(View.GONE);
                                    holder.ivSticker.setImageBitmap(resource);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();
                                    String fileNameFormat = null;
                                    if (stickerList.size() > position) {
                                        if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_1)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_1, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_2)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_2, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_3)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_3, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_4)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_4, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_5)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_5, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_6)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_6, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_7)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_7, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_8)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_8, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_9)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_9, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_10)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_10, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_11)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_11, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_12)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_12, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_13)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_13, "");
                                        } else if (typeMask.equalsIgnoreCase(Constants.KEY_CATEGORY_14)) {
                                            fileNameFormat = stickerList.get(position).getStickerImage().replace(Constants.IMAGE_URL_14, "");
                                        }
                                        String fileName = fileNameFormat.replace(Constants.KEY_PNG, "");
                                        new saveArrayImage(stickerList.get(position).getStickerImage(), byteArray, fileName).execute();
                                    }
                                    return false;
                                }
                            })
                            .into(holder.ivSticker)
                            ;
                }
            } else {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.e_placeholder)
                        .error(R.drawable.e_placeholder)
                        .load(stickerList.get(position).getStickerImage())
                        .into(holder.ivSticker);
            }
        }

    }


    public List<StickerModelMan> getOffLineList() {
        return stickerList;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public void notifyDataListChanged(List<StickerModelMan> overlayChildList, String typeMask) {
        this.stickerList = overlayChildList;
        this.typeMask = typeMask;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private class saveArrayImage extends AsyncTask<String, String, Exception> {
        String imageString;
        byte[] byteArrayImage;
        String newfileName;

        public saveArrayImage(String s, byte[] byteArray, String fileName) {
            imageString = s;
            byteArrayImage = byteArray;
            newfileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Exception doInBackground(String... strings) {
            if (byteArrayImage != null) {

                File myDir = new VaultFileUtil(mContext).getFile(mContext.getString(R.string.app_folder_sticker));
                if (!myDir.exists())
                    myDir.mkdirs();

                File file = new File(myDir, newfileName);

                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(byteArrayImage);

                    fos.close();
                    return null;
                } catch (Exception e) {
                    return new Exception(mContext.getString(R.string.txt_image_not_Detacted));
                }

            } else {
                return new Exception(mContext.getString(R.string.txt_image_not_Detacted));
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
            if (e == null) {
                // Toast.makeText(mContext, getString(R.string.txt_creation_saved_successfully), Toast.LENGTH_SHORT).show();
               /* Intent intent = new Intent(DisplayOptionMan.this, ShareActivityMan.class);
                intent.putExtra("uriImage", savedImageUri.toString());
                startActivity(intent);*/
            } else {
                // Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SquareImagesView ivSticker;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSticker = (SquareImagesView) itemView.findViewById(R.id.ivSticker);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}