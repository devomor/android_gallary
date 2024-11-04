package com.photo.photography.act;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.util.StringUtil;

import java.util.List;


public class ActPalette extends ActBase {

    /*** - PALETTE ITEM ON CLICK - ***/
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        /** Copies the selected color to the clipboard. */
        @Override
        public void onClick(View view) {
            String text = ((TextView) view.findViewById(R.id.palette_item_text)).getText().toString();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Palette Color", text);
            clipboard.setPrimaryClip(clip);
            StringUtil.showToast(getApplicationContext(), getString(R.string.color) + ": " + text + " " + getString(R.string.copy_clipboard));
        }
    };
    private ImageView paletteImg;
    private Uri uri;
    private Palette palette;
    private RecyclerView rvPalette;
    private PaletteAdapter paletteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_palette);
        paletteImg = (ImageView) findViewById(R.id.palette_image);

        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        uri = getIntent().getData();
        paletteImg.setImageURI(null);
        paletteImg.setImageURI(uri);
        //showFullScreenAdsNow(PaletteActivity.this);
        setPalette();
    }

    public void setPalette() {
        try {
            Bitmap myBitmap = ((BitmapDrawable) paletteImg.getDrawable()).getBitmap();
            ((TextView) findViewById(R.id.palette_image_title)).setText(uri.getPath().substring(uri.getPath().lastIndexOf("/") + 1));
            ((TextView) findViewById(R.id.palette_image_caption)).setText(uri.getPath());
            palette = Palette.from(myBitmap).generate();
            rvPalette = (RecyclerView) findViewById(R.id.paletteRecycler);
            rvPalette.setLayoutManager(new LinearLayoutManager(this));
            rvPalette.setNestedScrollingEnabled(false);
            paletteAdapter = new PaletteAdapter(palette.getSwatches());
            rvPalette.setAdapter(paletteAdapter);
        }catch (Exception e){
            Toast.makeText(this, R.string.something_went_wrong_please_try_again, Toast.LENGTH_SHORT).show();
        }
    }

    /*** - PALETTE ADAPTER - ***/
    private class PaletteAdapter extends RecyclerView.Adapter<ActPalette.PaletteAdapter.ViewHolder> {

        private final List<Palette.Swatch> swatches;

        private PaletteAdapter(List<Palette.Swatch> sws) {
            this.swatches = sws;
        }

        public ActPalette.PaletteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.palette_items, parent, false);
            v.setOnClickListener(onClickListener);
            return new ActPalette.PaletteAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ActPalette.PaletteAdapter.ViewHolder holder, final int position) {
            Palette.Swatch sw = swatches.get(position);
            holder.txtColor.setTextColor(sw.getTitleTextColor());
            holder.txtColor.setText(String.format("#%06X", (0xFFFFFF & sw.getRgb())));
            holder.itemBackground.setBackgroundColor(sw.getRgb());
        }

        public int getItemCount() {
            return null != swatches ? swatches.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtColor;
            LinearLayout itemBackground;

            ViewHolder(View itemView) {
                super(itemView);
                txtColor = (TextView) itemView.findViewById(R.id.palette_item_text);
                itemBackground = (LinearLayout) itemView.findViewById(R.id.ll_palette_item);
            }
        }
    }
}