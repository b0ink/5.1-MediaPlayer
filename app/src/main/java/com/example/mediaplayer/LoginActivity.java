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


    public DatabaseHelper db;

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

        db = DatabaseHelper.getInstance(this);

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

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if(username.isEmpty()){
                Toast.makeText(this, "Please enter in a username", Toast.LENGTH_SHORT).show();
                return;
            }

            if(password.isEmpty()){
                Toast.makeText(this, "Please enter in a password", Toast.LENGTH_SHORT).show();
                return;
            }

            UserData user = db.retrieveUser(username, password);

            if(user == null){
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
                etPassword.setText("");
                return;
            }

            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.putExtra(MainActivity.EXTRA_USER_ID, user.userId);
            startActivity(homeIntent);
            finish();

        });
    }
}