package com.android.example.movieappstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mahmoud on 27/11/17.
 */

public final  class MovieContract {

    public static final String AUTHORITY="com.android.example.movieappstage2";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIE="movie";

    public final static class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME="movie";
        public static final String COLUMN_TITLE  = "title";
        public static final String COLUMN_RATE="rate";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_POSTER="poster";
        public static final String COLUMN_CONTENT="content";
    }
}
