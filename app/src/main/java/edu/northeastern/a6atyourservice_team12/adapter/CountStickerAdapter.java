package edu.northeastern.a6atyourservice_team12.adapter;

//https://developer.android.com/develop/ui/views/layout/recyclerview
//https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.a6atyourservice_team12.R;
import edu.northeastern.a6atyourservice_team12.SendStickersActivity;
import edu.northeastern.a6atyourservice_team12.StickerCount;

import java.util.ArrayList;
import java.util.List;

public class CountStickerAdapter extends RecyclerView.Adapter<CountStickerAdapter.CountViewHolder> {
    private List<StickerCount> countStickerList;
    private Context stickerContext;

    public CountStickerAdapter(Context stickerContext, List<StickerCount> countStickerList) {
        this.stickerContext = stickerContext;
        this.countStickerList = countStickerList != null ? countStickerList : new ArrayList<>();
    }

    @NonNull
    @Override
    public CountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sticker_count, parent, false);
        return new CountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountViewHolder holder, int position) {
        StickerCount stickerCount = countStickerList.get(position);
        holder.tvStickerName.setText(stickerCount.getStringID());
        holder.tvCount.setText(String.valueOf(stickerCount.getCount()));
        int stickerRes = SendStickersActivity.getDrawableForStickerId(stickerCount.getStringID());
        holder.imgStickerCount.setImageResource(stickerRes);
    }

    @Override
    public int getItemCount() {
        return countStickerList != null ? countStickerList.size() : 0;
    }

    public void updateData(List<StickerCount> newStickerCountList) {
        this.countStickerList = newStickerCountList != null ? newStickerCountList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class CountViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvStickerName;
        TextView tvCount;
        ImageView imgStickerCount;

        public CountViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardSticker);
            tvStickerName = itemView.findViewById(R.id.tvStickerName);
            tvCount = itemView.findViewById(R.id.tvCount);
            imgStickerCount = itemView.findViewById(R.id.imgStickerCount);
        }
    }
}