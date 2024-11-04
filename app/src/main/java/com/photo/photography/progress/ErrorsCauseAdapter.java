package com.photo.photography.progress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;

import java.util.ArrayList;
import java.util.List;

public class ErrorsCauseAdapter extends RecyclerView.Adapter<ErrorsCauseViewHolder> {

    private List<ErrorsCause> errors;

    public ErrorsCauseAdapter(Context context, List<ErrorsCause> errors) {
//        super(context);
        this.errors = errors;
    }

    public void setErrors(ArrayList<ErrorsCause> errors) {
        this.errors = errors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ErrorsCauseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ErrorsCauseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_error_causes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ErrorsCauseViewHolder holder, int position) {
        holder.load(errors.get(position));
//        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return errors.size();
    }
}
