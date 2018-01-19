package com.android.example.movieappstage2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.movieappstage2.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by mahmoud on 27/11/17.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.ItemViewHolder> {
    private Cursor mCursor;
    private Context mContext;

    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.rv_movie_item, parent, false
        );
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int posterIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
        int voteIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATE);
        int dateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE);
        int contentIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_CONTENT);
        mCursor.moveToPosition(position);

        final String title = mCursor.getString(titleIndex);
        final String poster = mCursor.getString(posterIndex);
        final double vote = mCursor.getDouble(voteIndex);
        final String date = mCursor.getString(dateIndex);
        final String content = mCursor.getString(contentIndex);

        Picasso.with(mContext).load(poster).into(holder.mPosterImageView);
        holder.mPosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FavoriteActivity.class);
                intent.putExtra(MovieContract.MovieEntry.COLUMN_TITLE, title);
                intent.putExtra(MovieContract.MovieEntry.COLUMN_POSTER, poster);
                intent.putExtra(MovieContract.MovieEntry.COLUMN_RATE, vote);
                intent.putExtra(MovieContract.MovieEntry.COLUMN_DATE, date);
                intent.putExtra(MovieContract.MovieEntry.COLUMN_CONTENT, content);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mPosterImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_poster_movie);
        }


    }
}
