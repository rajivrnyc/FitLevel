// References:
// GridLayoutManager usage: https://developer.android.com/develop/ui/views/layout/recyclerview
// Spinner with ArrayAdapter: https://developer.android.com/guide/topics/ui/controls/spinner
// Intent flags for activity navigation: https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_CLEAR_TOP

package edu.northeastern.a6atyourservice_team12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.a6atyourservice_team12.adapter.StickerAdapter;
import edu.northeastern.a6atyourservice_team12.firebase.FirebaseHelper;
import edu.northeastern.a6atyourservice_team12.model.StickerItem;
import edu.northeastern.a6atyourservice_team12.util.SessionManager;
import edu.northeastern.a6atyourservice_team12.util.StickerNotificationManager;

public class SendStickersActivity extends AppCompatActivity {
    private StickerAdapter stickerAdapter;
    private Spinner friendSpinner;
    private TextView statusText;
    private FirebaseHelper firebaseHelper;
    private SessionManager sessionManager;
    private StickerItem chosenSticker = null;
    private StickerNotificationManager notificationManager;

    private List<StickerItem> buildStickerList() {
        return Arrays.asList(
                new StickerItem("sticker_happy",     R.drawable.sticker_happy),
                new StickerItem("sticker_sad",       R.drawable.sticker_sad),
                new StickerItem("sticker_love",      R.drawable.sticker_love),
                new StickerItem("sticker_laugh",     R.drawable.sticker_laugh),
                new StickerItem("sticker_angry",     R.drawable.sticker_angry),
                new StickerItem("sticker_cool",      R.drawable.sticker_cool),
                new StickerItem("sticker_surprised", R.drawable.sticker_surprised),
                new StickerItem("sticker_thumbsup",  R.drawable.sticker_thumbsup)
        );
    }

    public static int getDrawableForStickerId(String stickerId) {
        switch (stickerId) {
            case "sticker_happy":     return R.drawable.sticker_happy;
            case "sticker_sad":       return R.drawable.sticker_sad;
            case "sticker_love":      return R.drawable.sticker_love;
            case "sticker_laugh":     return R.drawable.sticker_laugh;
            case "sticker_angry":     return R.drawable.sticker_angry;
            case "sticker_cool":      return R.drawable.sticker_cool;
            case "sticker_surprised": return R.drawable.sticker_surprised;
            case "sticker_thumbsup":  return R.drawable.sticker_thumbsup;
            default:                  return R.drawable.sticker_unknown;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_stickers);

        firebaseHelper = FirebaseHelper.getInstance();
        sessionManager = new SessionManager(this);

        friendSpinner = findViewById(R.id.spinnerFriends);
        statusText = findViewById(R.id.textStatus);
        Button sendButton = findViewById(R.id.buttonSend);
        Button historyButton = findViewById(R.id.buttonStickerHistory);
        Button countButton = findViewById(R.id.buttonStickerCount);
        Button logoutButton = findViewById(R.id.buttonLogout);

        setupStickerGrid();
        loadFriendsList();

        sendButton.setOnClickListener(v -> attemptSend());

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(SendStickersActivity.this, ReceivedStickers.class);
            startActivity(intent);
        });

        countButton.setOnClickListener(v -> {
            Intent intent = new Intent(SendStickersActivity.this, StickersSent.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            sessionManager.clearSession();
            Intent intent = new Intent(SendStickersActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupStickerGrid() {
        RecyclerView grid = findViewById(R.id.recyclerStickers);
        grid.setLayoutManager(new GridLayoutManager(this, 4));

        stickerAdapter = new StickerAdapter(buildStickerList(), sticker -> {
            chosenSticker = sticker;
        });

        grid.setAdapter(stickerAdapter);
    }

    private void loadFriendsList() {
        String currentUser = sessionManager.getUsername();

        firebaseHelper.getAllUsers(new FirebaseHelper.UsersCallback() {
            @Override
            public void onUsersLoaded(List<String> usernames) {
                List<String> friends = new ArrayList<>(usernames);
                friends.remove(currentUser);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        SendStickersActivity.this,
                        android.R.layout.simple_spinner_item,
                        friends
                ) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ((TextView) view).setTextColor(0xFFFFFFFF);
                        ((TextView) view).setTextSize(16);
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        view.setBackgroundColor(0xFF2A2A2A);
                        ((TextView) view).setTextColor(0xFFFFFFFF);
                        ((TextView) view).setTextSize(16);
                        ((TextView) view).setPadding(32, 24, 32, 24);
                        return view;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                friendSpinner.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                statusText.setText("Could not load friends: " + errorMessage);
            }
        });
    }

    private void attemptSend() {
        String sender = sessionManager.getUsername();
        String receiver = (String) friendSpinner.getSelectedItem();

        if (chosenSticker == null) {
            Toast.makeText(this, "Pick a sticker first!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (receiver == null) {
            Toast.makeText(this, "No friends to send to yet", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.buttonSend).setEnabled(false);
        statusText.setText("Sending....");

        firebaseHelper.sendSticker(sender, receiver, chosenSticker.getStickerId(),
                new FirebaseHelper.SendCallback() {
                    @Override
                    public void onSuccess() {
                        statusText.setText("Sticker sent to " + receiver + "!!");
                        chosenSticker = null;
                        stickerAdapter.clearSelection();
                        findViewById(R.id.buttonSend).setEnabled(true);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        statusText.setText("Failed to send - " + errorMessage);
                        findViewById(R.id.buttonSend).setEnabled(true);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = sessionManager.getUsername();
        notificationManager = new StickerNotificationManager(this);
        notificationManager.beginListen(username);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (notificationManager != null) {
            notificationManager.stopListen();
        }
    }
}