package edu.northeastern.a6atyourservice_team12;

//https://www.baeldung.com/java-map-entries-methods
//https://docs.oracle.com/javase/8/docs/api/java/util/Map.Entry.html

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.a6atyourservice_team12.adapter.CountStickerAdapter;

import edu.northeastern.a6atyourservice_team12.firebase.FirebaseHelper;
import edu.northeastern.a6atyourservice_team12.util.SessionManager;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public class StickersSent extends AppCompatActivity {

    private RecyclerView recyclerSentStickers;
    private CountStickerAdapter countStickerAdapter;
    private List<StickerCount> countStickerList;
    private FirebaseHelper firebaseHelper;
    private SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stickers_sent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseHelper = FirebaseHelper.getInstance();
        sessionManager = new SessionManager(this);
        countStickerList = new ArrayList<>();

        recyclerSentStickers = findViewById(R.id.recyclerStickerSent);
        setupRecyclerView();
        showSentStickers();

    }


    private void setupRecyclerView() {
        countStickerAdapter = new CountStickerAdapter(this, countStickerList);
        recyclerSentStickers.setLayoutManager(new LinearLayoutManager(this));
        recyclerSentStickers.setAdapter(countStickerAdapter);
    }

    public void showSentStickers() {
        String username = sessionManager.getUsername();

        firebaseHelper.getSentStickerCounts(username, new FirebaseHelper.StickerCountCallback() {
            @Override
            public void onCountsLoaded(Map<String, Integer> stickerCounts) {
                List<StickerCount> stickerCountList = new ArrayList<>();

                for (Map.Entry<String, Integer> map : stickerCounts.entrySet()) {
                    stickerCountList.add(new StickerCount(map.getKey(), map.getValue()));
                }
                countStickerAdapter.updateData(stickerCountList);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}

