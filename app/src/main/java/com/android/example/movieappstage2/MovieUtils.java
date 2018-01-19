package com.android.example.movieappstage2;

import android.graphics.Movie;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MovieUtils {

    private static final String LOG_TAG = MovieUtils.class.getSimpleName();
    private static String SIZE_PARAM = "w342";

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String TRAILER_URL = "https://api.themoviedb.org/3/movie/";
    private static String VIDEO_PARAM = "/videos?api_key=3c15dbf274c6641b91228b202383fdc9";

    private static URL makeUrl(String stringUrl) {
        URL url = null;
        try {

            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Throw Execption in makeUrl", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuffer output = new StringBuffer();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        if (url == null) {
            return jsonResponse;
        }
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }


    private static List<MovieDetail> extractMovieData(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }
        List<MovieDetail> movieList = new ArrayList<>();
        String moviePosterPath;
        String movieDescription;
        String movieTitle;
        String movieReleaseDate;
        int id;
        double movieVoteAverage;

        try {
            JSONObject root = new JSONObject(movieJson);
            JSONArray resultArray = root.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);
                id = resultObject.getInt("id");
                moviePosterPath = resultObject.getString("poster_path");
                movieTitle = resultObject.getString("original_title");
                movieReleaseDate = resultObject.getString("release_date");
                movieDescription = resultObject.getString("overview");
                movieVoteAverage = resultObject.getDouble("vote_average");
                moviePosterPath = buildUrl(moviePosterPath).toString();
                MovieDetail movie = new MovieDetail(movieTitle,
                        moviePosterPath,
                        movieReleaseDate,
                        movieVoteAverage,
                        movieDescription,
                        id
                );
                movieList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    private static List<MovieDetail> extractMovieKeys(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }
        List<MovieDetail> movieDetailsList = new ArrayList<>();
        String movieKey;
        String nameTailer;
        try {
            JSONObject root = new JSONObject(movieJson);
            JSONArray resultArray = root.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);

                movieKey = resultObject.getString("key");
                nameTailer = resultObject.getString("name");
                MovieDetail movieDetailObject = new MovieDetail(movieKey, nameTailer);
                movieDetailsList.add(movieDetailObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDetailsList;
    }

    private static List<MovieDetail> extractMovieReviews(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }
        List<MovieDetail> list = new ArrayList<>();
        String contentReview;
        String autherReview;
        try {
            JSONObject root = new JSONObject(movieJson);
            JSONArray resultArray = root.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);
                contentReview = resultObject.getString("content");
                autherReview = resultObject.getString("author");

                MovieDetail movieDetail = new MovieDetail(autherReview, contentReview, "title");
                list.add(movieDetail);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static URL buildUrl(String posterUrl) {
        Uri builtUri = Uri.parse(IMAGE_URL).buildUpon()
                .appendEncodedPath(SIZE_PARAM)
                .appendEncodedPath(posterUrl)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static List<MovieDetail> fetchMovieAppData(String requestUrl) {
        URL url = makeUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MovieDetail> movieDetails = extractMovieData(jsonResponse);
        return movieDetails;
    }


    public static List<MovieDetail> fetchMovieKeys(String requestUrl) {
        URL url = makeUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MovieDetail> movieDetails = extractMovieKeys(jsonResponse);
        return movieDetails;
    }

    public static List<MovieDetail> fetchMovieReview(String requestUrl) {
        URL url = makeUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MovieDetail> list = extractMovieReviews(jsonResponse);
        return list;
    }
}
