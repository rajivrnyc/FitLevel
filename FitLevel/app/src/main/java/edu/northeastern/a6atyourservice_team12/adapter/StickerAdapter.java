// References:
// RecyclerView Adapter pattern: https://developer.android.com/develop/ui/views/layout/recyclerview

package edu.northeastern.a6atyourservice_team12.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.a6atyourservice_team12.R;
import edu.northeastern.a6atyourservice_team12.model.StickerItem;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    public interface OnStickerClickListener {
        void onStickerClicked(StickerItem sticker);
    }

    private final List<StickerItem> stckrs;
    private final OnStickerClickListener listenr;
    private int selPos = -1;

    public StickerAdapter(List<StickerItem> stckrs, OnStickerClickListener listenr) {
        this.stckrs = stckrs;
        this.listenr = listenr;
    }

    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sticker, parent, false);
        return new StickerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        StickerItem sticker = stckrs.get(position);
        holder.img.setImageResource(sticker.getDrawableResId());


        if (selPos == position) {
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setScaleX(1.1f);
            holder.itemView.setScaleY(1.1f);
        } else {
            holder.itemView.setAlpha(0.55f);
            holder.itemView.setScaleX(1.0f);
            holder.itemView.setScaleY(1.0f);
        }

        holder.itemView.setOnClickListener(v -> {
            if (selPos == position) {

                selPos = -1;
                notifyDataSetChanged();
                listenr.onStickerClicked(null);
            } else {
                // Different sticker tapped → select
                selPos = position;
                notifyDataSetChanged();
                listenr.onStickerClicked(sticker);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stckrs.size();
    }

    public void clearSelection() {
        selPos = -1;
        notifyDataSetChanged();
    }

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        StickerViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgStickerItem);
        }
    }
}