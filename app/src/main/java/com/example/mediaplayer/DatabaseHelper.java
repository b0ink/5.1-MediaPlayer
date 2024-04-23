package com.example.mediaplayer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper  {
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

    // TODO: retrieveUser(string username) -> verify password : return UserData

    // TODO: saveNewUser(UserData user)

    // TODO: retrievePlaylist(string username)

    // TODO: addToPlaylist
}
