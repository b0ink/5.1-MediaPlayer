package com.example.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MediaActivity extends AppCompatActivity {

    public static final String EXTRA_YOUTUBE_URL = "extra_youtube_url";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_media);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_YOUTUBE_URL)) {
            finish();
            return;
        }

        String ytUrl = intent.getStringExtra(EXTRA_YOUTUBE_URL);
        if (ytUrl.isEmpty()) {
            finish();
            return;
        }

        webView = findViewById(R.id.webView);

        webView.loadUrl(getIframeJavascriptEmbed(ytUrl));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

    }

    public String getIframeJavascriptEmbed(String videoID) {
        videoID = videoID.replace("https://", "");
        videoID = videoID.replace("http://", "");
        videoID = videoID.replace("www.youtube.com/watch?v=", "");

        String embed = "https://www.youtube.com/embed/" + videoID + "?enablejsapi=1";
        return embed;
    }

}