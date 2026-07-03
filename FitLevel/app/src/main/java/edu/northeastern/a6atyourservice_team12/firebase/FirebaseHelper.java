// Firebase read/write patterns based on:
// https://firebase.google.com/docs/database/android/read-and-write
// https://firebase.google.com/docs/database/android/lists-of-data

package edu.northeastern.a6atyourservice_team12.firebase;

import androidx.annotation.NonNull;

import edu.northeastern.a6atyourservice_team12.model.StickerMessage;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    private static final String USERS_NODE = "users";
    private static final String MESSAGES_NODE = "messages";

    private final DatabaseReference databaseRef;
    private static FirebaseHelper instance;

    private FirebaseHelper() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }


    public interface LoginCallback {
        void onSuccess(String username);
        void onFailure(String errorMessage);
    }

    public interface UsersCallback {
        void onUsersLoaded(List<String> usernames);
        void onError(String errorMessage);
    }

    public interface SendCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface MessagesCallback {
        void onMessagesLoaded(List<StickerMessage> messages);
        void onError(String errorMessage);
    }

    public interface StickerCountCallback {
        void onCountsLoaded(Map<String, Integer> stickerCounts);
        void onError(String errorMessage);
    }

    public interface NewStickerCallback {
        void onNewSticker(StickerMessage message);
    }


    public void loginUser(String username, LoginCallback callback) {
        if (username == null || username.trim().isEmpty()) {
            callback.onFailure("Username cannot be empty");
            return;
        }

        String cleanUsername = username.trim().toLowerCase();

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", cleanUsername);

        databaseRef.child(USERS_NODE).child(cleanUsername)
                .setValue(userData)
                .addOnSuccessListener(unused -> callback.onSuccess(cleanUsername))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }


    public void getAllUsers(UsersCallback callback) {
        databaseRef.child(USERS_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> usernames = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String uname = child.getKey();
                            if (uname != null) {
                                usernames.add(uname);
                            }
                        }
                        callback.onUsersLoaded(usernames);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }



    public void sendSticker(String senderUsername, String receiverUsername,
                            String stickerId, SendCallback callback) {

        DatabaseReference newMsgRef = databaseRef.child(MESSAGES_NODE).push();

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("senderUsername", senderUsername);
        messageData.put("receiverUsername", receiverUsername);
        messageData.put("stickerId", stickerId);
        // Server timestamp usage from Firebase docs:
// https://firebase.google.com/docs/database/android/offline-capabilities#server-timestamps
        messageData.put("timestamp", ServerValue.TIMESTAMP);

        newMsgRef.setValue(messageData)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }



    public void getReceivedStickers(String username, MessagesCallback callback) {
        databaseRef.child(MESSAGES_NODE)
                .orderByChild("receiverUsername")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<StickerMessage> messages = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            StickerMessage msg = child.getValue(StickerMessage.class);
                            if (msg != null) {
                                msg.setMessageId(child.getKey());
                                messages.add(msg);
                            }
                        }
                        callback.onMessagesLoaded(messages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }


    public void getSentStickerCounts(String username, StickerCountCallback callback) {
        databaseRef.child(MESSAGES_NODE)
                .orderByChild("senderUsername")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Integer> counts = new HashMap<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String stickerId = child.child("stickerId")
                                    .getValue(String.class);
                            if (stickerId != null) {
                                counts.put(stickerId,
                                        counts.getOrDefault(stickerId, 0) + 1);
                            }
                        }
                        callback.onCountsLoaded(counts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }


    private ChildEventListener incomingListener;

    public void listenForIncomingStickers(String username,
                                          NewStickerCallback callback) {
        stopListeningForIncomingStickers();

        incomingListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,
                                     String previousChildName) {
                StickerMessage msg = snapshot.getValue(StickerMessage.class);
                if (msg != null && username.equals(msg.getReceiverUsername())) {
                    msg.setMessageId(snapshot.getKey());
                    callback.onNewSticker(msg);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot,
                                     String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        databaseRef.child(MESSAGES_NODE)
                .orderByChild("receiverUsername")
                .equalTo(username)
                .addChildEventListener(incomingListener);
    }

    public void stopListeningForIncomingStickers() {
        if (incomingListener != null) {
            databaseRef.child(MESSAGES_NODE)
                    .removeEventListener(incomingListener);
            incomingListener = null;
        }
    }
}