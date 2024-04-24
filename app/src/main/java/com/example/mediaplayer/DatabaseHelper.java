package com.example.mediaplayer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

import android.content.ContentValues;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "MediaPlayerApp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, fullname VARCHAR(128), username VARCHAR(25), password VARCHAR(255))";
        String CREATE_TABLE_PLAYLIST = "CREATE TABLE IF NOT EXISTS playlist (id INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, url VARCHAR(128))";

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PLAYLIST);

        Log.d("DatabaseHelper", "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS playlist");

        // Create tables again
        onCreate(db);
    }

    @SuppressLint("Range")
    public UserData retrieveUser(String username, String passwordRaw) {
        username = username.toLowerCase();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? LIMIT 1", new String[]{username});
        UserData user = null;

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String fullName = cursor.getString(cursor.getColumnIndex("fullname"));
            String passwordHashed = cursor.getString(cursor.getColumnIndex("password"));

            if (BCrypt.checkpw(passwordRaw, passwordHashed)) {
                user = new UserData(id, fullName, username);
            }

        }
        if (cursor != null) {
            cursor.close();
        }
        return user;
    }

    @SuppressLint("Range")
    public UserData retrieveUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id=? LIMIT 1", new String[]{String.valueOf(userId)});
        UserData user = null;

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String fullName = cursor.getString(cursor.getColumnIndex("fullname"));

            user = new UserData(userId, fullName, username);
        }

        if (cursor != null) {
            cursor.close();
        }
        return user;
    }

    public Boolean saveNewUser(String fullName, String username, String passwordRaw) {
        username = username.toLowerCase();

        String passwordHashed = BCrypt.hashpw(passwordRaw, BCrypt.gensalt());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("fullname", fullName);
        contentValues.put("username", username);
        contentValues.put("password", passwordHashed);

        long result = db.insert("users", null, contentValues);

        if (result != -1) {
            Log.d("DatabaseHelper", "Added user successfully");
        }

        return result != -1;
    }

    @SuppressLint("Range")
    public ArrayList<String> retrievePlaylist(int userid) {
        ArrayList<String> urls = new ArrayList<>();
        String sUserId = String.valueOf(userid);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM playlist WHERE userid=?", new String[]{sUserId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndex("url"));
                urls.add(url);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        return urls;
    }

    public boolean addToPlaylist(int userId, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("userid", userId);
        contentValues.put("url", filterYoutubeUrl(url));

        long result = db.insert("playlist", null, contentValues);

        if (result != -1) {
            Log.d("DatabaseHelper", "Added to playlist successfully");
        }

        return result != -1;
    }

    public String filterYoutubeUrl(String url) {
        String filteredUrl = url.replace("https://", "");
        return filteredUrl;
    }

}
