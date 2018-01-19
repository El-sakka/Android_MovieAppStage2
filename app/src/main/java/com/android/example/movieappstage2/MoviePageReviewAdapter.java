package com.android.example.movieappstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mahmoud on 27/11/17.
 */

public class MoviePageReviewAdapter extends RecyclerView.Adapter<MoviePageReviewAdapter.MovieViewHolderReview> {

    List<MovieDetail> list;

    MoviePageReviewAdapter() {

    }

    public void setDataContent(List<MovieDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolderReview onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_reviews_list, parent, false);
        MovieViewHolderReview viewHolder = new MovieViewHolderReview(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolderReview holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = list.size();
            return count;
        } catch (NullPointerException e) {

        }
        return count;
    }

    public class MovieViewHolderReview extends RecyclerView.ViewHolder {
        TextView mAuhterTextView;
        TextView mContentTextView;

        MovieViewHolderReview(View itemView) {
            super(itemView);
            mAuhterTextView = (TextView) itemView.findViewById(R.id.tv_review_auther);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

        void bind(int position) {
            mAuhterTextView.setText(list.get(position).getAutherReview());
            mContentTextView.setText(list.get(position).getContentReview());
        }
    }
}
