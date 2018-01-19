package com.android.example.movieappstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CompoundButton;

/**
 * Created by mahmoud on 27/11/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "movie.db";
    public final static int DATABASE_VERSION = 1;

    public final static String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
                    + "(" + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_RATE + " DOUBLE, "
                    + MovieContract.MovieEntry.COLUMN_CONTENT + " TEXT, "
                    + MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL);";

    public final static String SQL_DELETE_ENTERIES =
            "DROP TABLE IF EXITS " + MovieContract.MovieEntry.TABLE_NAME;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTERIES);
        onCreate(sqLiteDatabase);
    }
}
