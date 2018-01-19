package com.android.example.movieappstage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.movieappstage2.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by mahmoud on 28/11/17.
 */

public class FavoriteActivity extends AppCompatActivity {

    TextView mTitleTextView;
    ImageView mPosterTextView;
    TextView mRateTextView;
    TextView mDateTextView;
    TextView mContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_layout);

        Intent intent = getIntent();

        String title = intent.getStringExtra(MovieContract.MovieEntry.COLUMN_TITLE);
        String poster = intent.getStringExtra(MovieContract.MovieEntry.COLUMN_POSTER);
        double rate = intent.getDoubleExtra(MovieContract.MovieEntry.COLUMN_RATE, 0.0);
        String date = intent.getStringExtra(MovieContract.MovieEntry.COLUMN_DATE);
        String content = intent.getStringExtra(MovieContract.MovieEntry.COLUMN_CONTENT);

        mTitleTextView = (TextView) findViewById(R.id.fv_movie_title_textView);
        mRateTextView = (TextView) findViewById(R.id.fv_vote_average_textView);
        mDateTextView = (TextView) findViewById(R.id.fv_release_date_textView);
        mContentTextView = (TextView) findViewById(R.id.fv_descrption_textView);
        mPosterTextView = (ImageView) findViewById(R.id.fv_movie_imgaeView);

        Picasso.with(this).load(poster).into(mPosterTextView);
        mTitleTextView.setText(title);
        mContentTextView.setText(content);
        mRateTextView.setText(rate + "");
        mDateTextView.setText(date);

    }

}
