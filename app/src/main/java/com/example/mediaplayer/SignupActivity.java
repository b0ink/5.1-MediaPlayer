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

public class SignupActivity extends AppCompatActivity {


    public EditText etUsername;
    public EditText etFullname;
    public EditText etPassword;
    public EditText etConfirmPassword;

    public Button btnCreateAccount;

    public DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = DatabaseHelper.getInstance(this);

        etUsername = findViewById(R.id.editTextUsername);
        etFullname = findViewById(R.id.editTextFullname);
        etPassword = findViewById(R.id.editTextPassword);
        etConfirmPassword = findViewById(R.id.editTextPasswordConfirm);
        btnCreateAccount = findViewById(R.id.buttonCreateAccount);

        btnCreateAccount.setOnClickListener(view -> {

            String fullname = etFullname.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String passwordConfirm = etConfirmPassword.getText().toString();

            if (username.isEmpty() || fullname.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                Toast.makeText(this, "Fields can not be left blank", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fullname.length() > 128 || fullname.length() < 3) {
                Toast.makeText(this, "Full name must be at least 3 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.length() > 25 || fullname.length() < 5) {
                Toast.makeText(this, "Username must between 5-25 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() > 255 || password.length() < 5) {
                Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(passwordConfirm)) {
                Toast.makeText(this, "Passwords do not match, please try again", Toast.LENGTH_SHORT).show();
                etPassword.setText("");
                etConfirmPassword.setText("");
                return;
            }

            if (db.saveNewUser(fullname, username, password)) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_USERNAME, username.toLowerCase());
                startActivity(intent);
                finish();
                return;
            }

            Toast.makeText(this, "An error occurred, please try again", Toast.LENGTH_LONG).show();

            etPassword.setText("");
            etConfirmPassword.setText("");

        });
    }
}