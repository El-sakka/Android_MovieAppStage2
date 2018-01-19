package com.android.example.movieappstage2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud on 26/11/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final static String LOG_TAG = MovieAdapter.class.getSimpleName();
    List<MovieDetail> movieList = new ArrayList<>();
    Context context;
    private static final String TITLE = "title";
    private static final String DECRIPTION = "decription";
    private static final String RELEASEDATE = "release_date";
    private static final String POSTERURL = "poster_url";
    private static final String VOTEAVERAGE = "vote_average";
    private static final String ID = "id";


    MovieAdapter(Context context) {
        this.context = context;
    }

    /*public interface ListItemClickListener{
        void onListItemClick(ImageView imageView,int position);
    }*/

    public void setMovieData(List<MovieDetail> movieData) {
        movieList = movieData;
        notifyDataSetChanged();
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_movie_item, parent, false);

        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = movieList.size();

        } catch (NullPointerException e) {
            Log.e(LOG_TAG, e + "");
        }
        return count;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mPosterImageView;

        //TextView mTextView;
        MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_poster_movie);
            // mTextView = (TextView) itemView.findViewById(R.id.iv_poster_movie);

        }

        void bind(final int position) {
            Picasso.with(context).load(movieList.get(position).getPosterUrl())
                    .fit()
                    .into(mPosterImageView);

            mPosterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MoviePage.class);
                    int posotion = getAdapterPosition();
                    String title = movieList.get(position).getTitle();
                    String releaseDate = movieList.get(position).getReleaseData();
                    String decription = movieList.get(position).getDescrption();
                    double voteAverage = movieList.get(position).getVoteAverage();
                    String posterUrl = movieList.get(position).getPosterUrl();
                    int id = movieList.get(position).getMovieId();
                    intent.putExtra(TITLE, title);
                    intent.putExtra(RELEASEDATE, releaseDate);
                    intent.putExtra(DECRIPTION, decription);
                    intent.putExtra(VOTEAVERAGE, voteAverage);
                    intent.putExtra(POSTERURL, posterUrl);
                    intent.putExtra(ID, id);
                    context.startActivity(intent);
                }
            });
        }


    }
}
