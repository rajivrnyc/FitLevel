package edu.northeastern.a6atyourservice_team12.adapter;

//https://docs.oracle.com/javase/8/docs/api/java/sql/Timestamp.html

import android.annotation.SuppressLint;
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
import edu.northeastern.a6atyourservice_team12.model.StickerMessage;

import java.util.ArrayList;
import java.util.List;

public class ReceivedStickersAdapter extends RecyclerView.Adapter<ReceivedStickersAdapter.ReceivedViewHolder> {
    private List<StickerMessage> receivedStickerList;
    private int lastAnimatedPositionSticker = -1;
    private Context stickerContext;

    public ReceivedStickersAdapter(Context stickerContext, List<StickerMessage> receivedStickerList) {
        this.stickerContext = stickerContext;
        this.receivedStickerList = receivedStickerList != null ? receivedStickerList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ReceivedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stickers_received, parent, false);
        return new ReceivedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedViewHolder holder, int position) {
        StickerMessage stickerMessage = receivedStickerList.get(position);
        holder.tvSenderName.setText(stickerMessage.getSenderUsername());
        holder.tvTimestamp.setText(formatTimestamp(stickerMessage.getTimestamp()));
        int stickerRes = SendStickersActivity.getDrawableForStickerId(stickerMessage.getStickerId());
        holder.imgReceivedSticker.setImageResource(stickerRes);

        if (position > lastAnimatedPositionSticker) {
            setFadeInAnimation(holder.itemView, position);
            lastAnimatedPositionSticker = position;
        }
    }

    @Override
    public int getItemCount() {
        return receivedStickerList != null ? receivedStickerList.size() : 0;
    }

    public void updateData(List<StickerMessage> newStickerList) {
        this.receivedStickerList = newStickerList != null ? newStickerList : new ArrayList<>();
        this.lastAnimatedPositionSticker = -1;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.receivedStickerList.clear();
        this.lastAnimatedPositionSticker = -1;
        notifyDataSetChanged();
    }

    @SuppressLint("SimpleDateFormat")
    private String formatTimestamp(long timeStamp) {
        if (timeStamp <= 0) {
            return "";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd h:mm a",
                java.util.Locale.getDefault()).format(new java.util.Date(timeStamp));
    }

    public void resetAnimation() {
        lastAnimatedPositionSticker = -1;
    }

    private void setFadeInAnimation(View view, int position) {
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(position * 50L)
                .start();
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvSenderName;
        TextView tvTimestamp;
        ImageView imgReceivedSticker;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardSticker);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            imgReceivedSticker = itemView.findViewById(R.id.imgReceivedSticker);
        }
    }
}