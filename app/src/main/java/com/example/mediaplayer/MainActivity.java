package com.example.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        Intent intent = getIntent();
        if(intent == null || !intent.hasExtra(EXTRA_USER_ID)){
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        int userId = intent.getIntExtra(EXTRA_USER_ID, -1);


        db = DatabaseHelper.getInstance(this);

        UserData user = db.retrieveUser(userId);
        tvFullname.setText("Welcome back,\n" + user.fullName + "!");

    }
}