package edu.northeastern.a6atyourservice_team12;

import android.os.Bundle;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.a6atyourservice_team12.adapter.ReceivedStickersAdapter;
import edu.northeastern.a6atyourservice_team12.model.StickerMessage;
import edu.northeastern.a6atyourservice_team12.firebase.FirebaseHelper;
import edu.northeastern.a6atyourservice_team12.util.SessionManager;


import java.util.ArrayList;
import java.util.List;



public class ReceivedStickers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_received_stickers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseHelper = FirebaseHelper.getInstance();
        sessionManager = new SessionManager(this);
        receivedStickerList = new ArrayList<>();
        recyclerReceivedStickers = findViewById(R.id.recyclerStickerReceived);
        setupRecyclerView();
        showReceivedStickers();


    }

    private RecyclerView recyclerReceivedStickers;
    private ReceivedStickersAdapter receivedStickerAdapter;
    private List<StickerMessage> receivedStickerList;
    private FirebaseHelper firebaseHelper;
    private SessionManager sessionManager;



    private void setupRecyclerView() {
        receivedStickerAdapter = new ReceivedStickersAdapter(this, receivedStickerList);
        recyclerReceivedStickers.setLayoutManager(new LinearLayoutManager(this));
        recyclerReceivedStickers.setAdapter(receivedStickerAdapter);
    }

    public void showReceivedStickers() {
        String username = sessionManager.getUsername();

        firebaseHelper.getReceivedStickers(username, new FirebaseHelper.MessagesCallback() {
            @Override
            public void onMessagesLoaded(List<StickerMessage> messages) {
                receivedStickerAdapter.updateData(messages);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }
}