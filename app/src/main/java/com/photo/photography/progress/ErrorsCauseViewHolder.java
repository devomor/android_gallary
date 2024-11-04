package com.photo.photography.progress;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrorsCauseViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.error_title)
    TextView title;

    @BindView(R.id.error_causes)
    LinearLayout causes;

    ErrorsCauseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void load(ErrorsCause errorCause) {
        title.setText(errorCause.getTitle());

        causes.removeAllViews();
        for (String c : errorCause.getCauses()) {
            TextView textView = new TextView(itemView.getContext());
            textView.setText(c);
            causes.addView(textView);
        }
    }
}
