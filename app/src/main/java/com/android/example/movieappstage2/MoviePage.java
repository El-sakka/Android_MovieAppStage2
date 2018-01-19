package com.android.example.movieappstage2;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.movieappstage2.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviePage extends AppCompatActivity {
    private static final String TITLE = "title";
    private static final String DECRIPTION = "decription";
    private static final String RELEASEDATE = "release_date";
    private static final String POSTERURL = "poster_url";
    private static final String VOTEAVERAGE = "vote_average";
    private static final String ID = "id";
    private static final String TRAILER_URL = "https://api.themoviedb.org/3/movie/";
    private static String VIDEO_PARAM = "/videos?api_key=3c15dbf274c6641b91228b202383fdc9";
    private static String REVIEW_PARAM = "/reviews?api_key=3c15dbf274c6641b91228b202383fdc9";

    private static String SAVE_INSTANCE_ARRAYLIST_REVIEW = "review";
    private static String SAVE_INSTANCE_ARRAYLIST_VIDEO = "video";
    private List<MovieDetail> mMovieDetailList;
    private List<MovieDetail> mMovieDetailListTrailer;
    private String CURRENT_STATE = "current_state";
    private String CURRENT_STATE_VIDEO = "current_state_video";

    String title;
    String releaseDate;
    String posterUrl;
    double voteAverage;
    String decription;
    RecyclerView recyclerViewMoviePage;
    RecyclerView recyclerViewMovieReview;
    MoviePageAdapter mAdapter;
    MoviePageReviewAdapter mMoviePageReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);

        mMovieDetailList = new ArrayList<>();

        Intent intent = getIntent();


        recyclerViewMoviePage = (RecyclerView) findViewById(R.id.rv_trailers);
        mAdapter = new MoviePageAdapter(this);

        recyclerViewMovieReview = (RecyclerView) findViewById(R.id.rv_movie_reviews);
        recyclerViewMovieReview.setLayoutManager(new LinearLayoutManager(this));
        mMoviePageReviewAdapter = new MoviePageReviewAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewMoviePage.setLayoutManager(linearLayoutManager);

        title = intent.getStringExtra(TITLE);
        decription = intent.getStringExtra(DECRIPTION);
        releaseDate = intent.getStringExtra(RELEASEDATE);
        posterUrl = intent.getStringExtra(POSTERURL);
        voteAverage = intent.getDoubleExtra(VOTEAVERAGE, 0.0);
        int id = intent.getIntExtra(ID, 0);

        String movieUrl = buildVideoUrl(id);
        String reviewUrl = buildReviewUrl(id);

        TextView originalTitle = (TextView) findViewById(R.id.movie_title_textView);
        originalTitle.setText(title);

        ImageView posterImage = (ImageView) findViewById(R.id.movie_imgaeView);
        Picasso.with(getApplicationContext()).load(posterUrl).into(posterImage);

        TextView releaseDateTextView = (TextView) findViewById(R.id.release_date_textView);
        releaseDateTextView.setText(releaseDate);

        TextView voteAverageTextView = (TextView) findViewById(R.id.vote_average_textView);
        voteAverageTextView.setText(String.valueOf(voteAverage));

        TextView descriptionTextView = (TextView) findViewById(R.id.descrption_textView);
        descriptionTextView.setText(decription);

        if (savedInstanceState == null) {
            new MoviePageAsyncTaskReview().execute(reviewUrl);
            new MoviePageAsyncTask().execute(movieUrl);
        } else {
            mMovieDetailList = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_ARRAYLIST_REVIEW);
            mMovieDetailListTrailer = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_ARRAYLIST_VIDEO);
            mAdapter.setMovieTrailers(mMovieDetailListTrailer);
            mMoviePageReviewAdapter.setDataContent(mMovieDetailList);
        }
        recyclerViewMovieReview.setAdapter(mMoviePageReviewAdapter);
        recyclerViewMoviePage.setAdapter(mAdapter);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable parcelable = savedInstanceState.getParcelable(CURRENT_STATE);
            Parcelable parcelable1Video = savedInstanceState.getParcelable(CURRENT_STATE_VIDEO);
            recyclerViewMovieReview.getLayoutManager().onRestoreInstanceState(parcelable);
            recyclerViewMoviePage.getLayoutManager().onRestoreInstanceState(parcelable1Video);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_INSTANCE_ARRAYLIST_REVIEW, (ArrayList<? extends Parcelable>) mMovieDetailList);
        outState.putParcelableArrayList(SAVE_INSTANCE_ARRAYLIST_VIDEO, (ArrayList<? extends Parcelable>) mMovieDetailListTrailer);
        outState.putParcelable(CURRENT_STATE, recyclerViewMovieReview.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(CURRENT_STATE_VIDEO, recyclerViewMoviePage.getLayoutManager().onSaveInstanceState());
    }

    private String buildVideoUrl(int id) {
        String stringUrl = TRAILER_URL + id + VIDEO_PARAM;
        return stringUrl;
    }

    private String buildReviewUrl(int id) {
        String stringUrl = TRAILER_URL + id + REVIEW_PARAM;
        return stringUrl;
    }

    public class MoviePageAsyncTask extends AsyncTask<String, Void, List<MovieDetail>> {
        @Override
        protected List<MovieDetail> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            List<MovieDetail> details = MovieUtils.fetchMovieKeys(strings[0]);
            return details;
        }

        @Override
        protected void onPostExecute(List<MovieDetail> list) {
            mMovieDetailListTrailer = list;
            mAdapter.setMovieTrailers(list);
        }
    }

    public class MoviePageAsyncTaskReview extends AsyncTask<String, Void, List<MovieDetail>> {
        @Override
        protected List<MovieDetail> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            List<MovieDetail> list = MovieUtils.fetchMovieReview(strings[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<MovieDetail> list) {
            mMovieDetailList = list;
            mMoviePageReviewAdapter.setDataContent(list);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.moviepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bt_favorite) {
            addFavoriteMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFavoriteMovie() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER, posterUrl);
        cv.put(MovieContract.MovieEntry.COLUMN_DATE, releaseDate);
        cv.put(MovieContract.MovieEntry.COLUMN_RATE, voteAverage);
        cv.put(MovieContract.MovieEntry.COLUMN_CONTENT, decription);
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
