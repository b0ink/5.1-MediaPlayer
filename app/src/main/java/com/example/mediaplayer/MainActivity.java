package com.example.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "extra_user_id";

    public DatabaseHelper db;

    public TextView tvFullname;
    public EditText etYoutubeUrl;

    public Button btnPlay;
    public Button btnAddToPlaylist;
    public Button btnViewPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvFullname = findViewById(R.id.textViewFullname);
        etYoutubeUrl = findViewById(R.id.editTextYoutubeURL);

        btnPlay = findViewById(R.id.buttonPlay);
        btnAddToPlaylist = findViewById(R.id.buttonSaveToPlaylist);
        btnViewPlaylist = findViewById(R.id.buttonViewPlaylist);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_USER_ID)) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        int userId = intent.getIntExtra(EXTRA_USER_ID, -1);


        db = DatabaseHelper.getInstance(this);

        UserData user = db.retrieveUser(userId);
        tvFullname.setText("Welcome back,\n" + user.fullName + "!");

        btnPlay.setOnClickListener(view -> {
            String url = etYoutubeUrl.getText().toString();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter in a YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent mediaIntent = new Intent(this, MediaActivity.class);
            mediaIntent.putExtra(MediaActivity.EXTRA_YOUTUBE_URL, url);
            startActivity(mediaIntent);
        });

        btnAddToPlaylist.setOnClickListener(view -> {
            String url = etYoutubeUrl.getText().toString();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter in a YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.addToPlaylist(userId, url)) {
                Toast.makeText(this, "Successfully saved video to your playlist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "An error occurred saving video to your playlist, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewPlaylist.setOnClickListener(view -> {
            Intent playlistIntent = new Intent(this, PlaylistActivity.class);
            playlistIntent.putExtra(PlaylistActivity.EXTRA_USER_ID, userId);
            startActivity(playlistIntent);
        });
    }
}