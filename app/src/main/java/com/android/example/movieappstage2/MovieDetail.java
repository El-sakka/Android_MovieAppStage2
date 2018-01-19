package com.android.example.movieappstage2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class MovieDetail implements Parcelable {
    private String title;
    private String releaseData;
    private String descrption;
    private double voteAverage;
    private String posterUrl;
    private String movieKey;
    private int movieId;
    private String trailerNumber;
    private String contentReview;
    private String autherReview;


    public MovieDetail(String title, String posterUrl, String releaseData, double voteAverage, String descrption, int movieId) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.releaseData = releaseData;
        this.voteAverage = voteAverage;
        this.descrption = descrption;
        this.movieId = movieId;
    }

    public MovieDetail(String movieKey, String trailerNumber) {
        this.movieKey = movieKey;
        this.trailerNumber = trailerNumber;
    }

    public MovieDetail(String autherReview, String contentReview, String title) {
        this.contentReview = contentReview;
        this.autherReview = autherReview;
    }


    protected MovieDetail(Parcel in) {
        title = in.readString();
        releaseData = in.readString();
        descrption = in.readString();
        voteAverage = in.readDouble();
        posterUrl = in.readString();
        movieKey = in.readString();
        movieId = in.readInt();
        trailerNumber = in.readString();
        contentReview = in.readString();
        autherReview = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseData);
        dest.writeString(descrption);
        dest.writeDouble(voteAverage);
        dest.writeString(posterUrl);
        dest.writeString(movieKey);
        dest.writeInt(movieId);
        dest.writeString(trailerNumber);
        dest.writeString(contentReview);
        dest.writeString(autherReview);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    public String getTitle() {
        return this.title;
    }

    public String getReleaseData() {
        return this.releaseData;
    }

    public String getPosterUrl() {
        return this.posterUrl;
    }

    public String getDescrption() {
        return this.descrption;
    }

    public double getVoteAverage() {
        return this.voteAverage;
    }

    public String getAutherReview() {
        return autherReview;
    }

    public String getContentReview() {
        return contentReview;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieKey() {
        return movieKey;
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setMovieId(int id) {
        this.movieId = id;
    }

}
