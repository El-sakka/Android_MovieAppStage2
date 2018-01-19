package com.android.example.movieappstage2;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;

import com.android.example.movieappstage2.data.MovieContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String MOVIE_API = "http://api.themoviedb.org/3/movie/";

    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String API_KEY = "?api_key=3c15dbf274c6641b91228b202383fdc9";
    private static final String SORT_CRITERION = "sort";
    private static final int MOVIE_LOADER_ID = 0;
    private static final String TAG = MainActivity.class.getSimpleName() + "LOG";

    private Parcelable listState;
    private int CURRENT_SCROLL_POSITION;
    private String CURRENT_STATE = "current_state";
    public static int index = -1;
    public static int top = -1;
    //LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerViewMovie;
    MovieAdapter mMovieAdapter;
    GridLayoutManager gridLayoutManager;
    private CustomCursorAdapter mCursorAdapter;
    private List<MovieDetail> mMovieDetailList;
    private String SAVEINstANCE_ARRAYLIST_MOVIES = "movie-list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieDetailList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(MainActivity.this);
        mCursorAdapter = new CustomCursorAdapter(this);

        int noOfColumns = calculateNoOfColumns(this);
        mRecyclerViewMovie = (RecyclerView) findViewById(R.id.rv_movie_list);
        gridLayoutManager = new GridLayoutManager(this, noOfColumns);
        mRecyclerViewMovie.setLayoutManager(gridLayoutManager);

        MovieAppAsyncTask movieAppAsyncTask = new MovieAppAsyncTask();

        SharedPreferences sh = getSharedPreferences("save", Context.MODE_PRIVATE);
        String sort_order = sh.getString(SORT_CRITERION, SORT_POPULAR);
        if (savedInstanceState == null)
            movieAppAsyncTask.execute(sort_order);
        else {
            mMovieDetailList = savedInstanceState.getParcelableArrayList(SAVEINstANCE_ARRAYLIST_MOVIES);
            mMovieAdapter.setMovieData(mMovieDetailList);
        }

        mRecyclerViewMovie.setAdapter(mMovieAdapter);

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutStat = savedInstanceState.getParcelable(CURRENT_STATE);
            mRecyclerViewMovie.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutStat);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVEINstANCE_ARRAYLIST_MOVIES, (ArrayList<? extends Parcelable>) mMovieDetailList);
        outState.putParcelable(CURRENT_STATE, mRecyclerViewMovie.getLayoutManager().onSaveInstanceState());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPref = getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int id = item.getItemId();

        if (id == R.id.top_rated) {

            editor.putString(SORT_CRITERION, SORT_TOP_RATED);
            editor.apply();
            new MovieAppAsyncTask().execute(SORT_TOP_RATED);
            mRecyclerViewMovie.setAdapter(mMovieAdapter);
            return true;


        } else if (id == R.id.popular) {

            /*SharedPreferences sharedPref = getSharedPreferences("save",Context.MODE_PRIVATE);
            String sortBy = sharedPref.getString(SORT_CRITERION_POPULAR, SORT_POPULAR);
            */

            editor.putString(SORT_CRITERION, SORT_POPULAR);
            editor.apply();
            new MovieAppAsyncTask().execute(SORT_POPULAR);
            mRecyclerViewMovie.setAdapter(mMovieAdapter);


            return true;
        } else if (id == R.id.favorite) {
            mRecyclerViewMovie.setAdapter(mCursorAdapter);
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MovieAppAsyncTask extends AsyncTask<String, Void, List<MovieDetail>> {
        @Override
        protected List<MovieDetail> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            String MOVIE_URL = MOVIE_API + strings[0] + API_KEY;
            List<MovieDetail> result = MovieUtils.fetchMovieAppData(MOVIE_URL);
            return result;
        }

        @Override
        protected void onPostExecute(List<MovieDetail> movieDetails) {
            mMovieDetailList = (ArrayList<MovieDetail>) movieDetails;
            mMovieAdapter.setMovieData(mMovieDetailList);
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
