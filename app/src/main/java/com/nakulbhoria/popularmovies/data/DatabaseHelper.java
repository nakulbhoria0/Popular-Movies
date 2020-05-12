package com.nakulbhoria.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "favorites.db";
    public static String TABLE_NAME = "favorite_movies";
    public static int DATABASE_VERSION = 1;
    public static String COLUMN_NAME = "name";
    public static String COLUMN_ID = "id";
    public static String COLUMN_DESCRIPTION = "description";
    public static String COLUMN_RELEASE_DATE = "release_date";
    public static String COLUMN_RATING = "rating";
    public static String[] ALL_COLUMNS = {COLUMN_RATING, COLUMN_RELEASE_DATE, COLUMN_DESCRIPTION, COLUMN_ID, COLUMN_NAME};


    public static String createTable = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            COLUMN_RATING + " TEXT NOT NULL );";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
