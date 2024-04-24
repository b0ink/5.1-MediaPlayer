package com.example.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "extra_username";

    public EditText etUsername;
    public EditText etPassword;
    public Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_USERNAME)){
            String username = intent.getStringExtra(EXTRA_USERNAME);
            if(username != null && !username.isEmpty()){
                etUsername.setText(username);
                Toast.makeText(this, "Successfully create account, you may now login", Toast.LENGTH_LONG).show();
            }
        }
    }
}