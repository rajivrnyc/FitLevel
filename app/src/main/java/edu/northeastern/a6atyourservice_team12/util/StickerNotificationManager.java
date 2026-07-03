package edu.northeastern.a6atyourservice_team12.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edu.northeastern.a6atyourservice_team12.R;
import edu.northeastern.a6atyourservice_team12.model.StickerMessage;

// Notification pattern based on:
// https://developer.android.com/guide/topics/ui/notifiers/notifications

public class StickerNotificationManager {
    private static final String CHANNEL_ID = "sticker_channel";
    private int notifCount = 0;
    private final Context context;
    private final long appStart;
    private ChildEventListener listener;
    private Query query;

    public StickerNotificationManager(Context context) {
        this.context = context;
        this.appStart = System.currentTimeMillis();
        createNotificationChannel();
    }

    // Firebase ChildEventListener message update from:
    // https://firebase.google.com/docs/database/android/lists-of-data
    public void beginListen(String currentUsername) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("messages");

        query = ref.orderByChild("receiverUsername").equalTo(currentUsername);
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String prevChildName) {
                StickerMessage msg = snapshot.getValue(StickerMessage.class);
                if (msg != null && msg.getTimestamp() > appStart) {
                    sendNotification(msg.getSenderUsername(), msg.getStickerId());
                }
            }

            @Override public void onChildChanged(DataSnapshot s, String p) {}
            @Override public void onChildRemoved(DataSnapshot s) {}
            @Override public void onChildMoved(DataSnapshot s, String p) {}
            @Override public void onCancelled(DatabaseError e) {}
        };
        query.addChildEventListener(listener);
    }

    public void stopListen() {
        if (query != null && listener != null) {
            query.removeEventListener(listener);
        }
    }


    // Notification Structure (large, small) referenced:
    // https://developer.android.com/develop/ui/views/notifications
    private void sendNotification(String sender, String stickerId) {
        int stickerResId = StickerConfig.getDrawableForSticker(stickerId);
        if (stickerResId == 0) {
            stickerResId = R.mipmap.ic_launcher;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), stickerResId))
                .setContentTitle("Sticker Received!")
                .setContentText(sender + " sent you a sticker")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat mgr = NotificationManagerCompat.from(context);
        if (mgr.areNotificationsEnabled()) {
            try {
                mgr.notify(notifCount++, builder.build());
            } catch (SecurityException e) {
                // Permission not granted, skip notification
            }
        }
    }

    // Notification channel creation from:
    // https://developer.android.com/training/notify-user/build-notification
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sticker Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for received stickers");
            NotificationManager mgr = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            mgr.createNotificationChannel(channel);
        }
    }
}