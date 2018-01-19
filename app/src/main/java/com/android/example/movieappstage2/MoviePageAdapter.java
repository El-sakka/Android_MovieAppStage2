package com.android.example.movieappstage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;



public class MoviePageAdapter extends RecyclerView.Adapter<MoviePageAdapter.MoviePageViewHolder> {
    private String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private Context mContext;
    private List<MovieDetail> list;

    public MoviePageAdapter(Context context) {
        mContext = context;
    }

    public void setMovieTrailers(List<MovieDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public MoviePageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cutom_trailer_list, parent, false);
        MoviePageViewHolder viewHolder = new MoviePageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviePageViewHolder holder, int position) {
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


    public class MoviePageViewHolder extends RecyclerView.ViewHolder {
        ImageView mTrailerImageView;

        MoviePageViewHolder(View itemView) {
            super(itemView);
            mTrailerImageView = (ImageView) itemView.findViewById(R.id.tv_trailer_textview);
        }

        void bind(final int position) {
            final String Movie_id = list.get(position).getMovieKey();
            final String url = YOUTUBE_URL + Movie_id;
            String img_url = "http://img.youtube.com/vi/" + Movie_id + "/0.jpg";
            Picasso.with(mContext).load(img_url)
                    .into(mTrailerImageView);
            mTrailerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
