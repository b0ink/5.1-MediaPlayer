package com.example.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "extra_user_id";


    private DatabaseHelper db;

    public TextView tvUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvUrls = findViewById(R.id.textViewURLs);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_USER_ID)) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        int userId = intent.getIntExtra(EXTRA_USER_ID, -1);


        db = DatabaseHelper.getInstance(this);

        UserData user = db.retrieveUser(userId);
        if(user == null){
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        ArrayList<String> urls = db.retrievePlaylist(userId);
        StringBuilder urlList = new StringBuilder();
        for(String url : urls){
            String filteredUrl = url.replace("https://", "");
            filteredUrl = filteredUrl.replace("http://", "");

            // remove any possible duplicates, also done prior to db save
            if(urlList.indexOf(filteredUrl) == -1){
                urlList.append(filteredUrl).append("\n");
            }
        }

        tvUrls.setText(urlList);
    }
}