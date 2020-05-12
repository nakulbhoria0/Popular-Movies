package com.nakulbhoria.popularmovies.data;


import com.nakulbhoria.popularmovies.Model.Movie;
import com.nakulbhoria.popularmovies.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {

    public static JSONObject getData(String string) {
        JSONObject object = null;
        try {
            URL url = new URL(string);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            String data = "";

            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                data = scanner.next();
            }

            if (data == null || data.isEmpty()) {
                return object;
            }
            object = new JSONObject(data);

            connection.disconnect();


            return object;


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static ArrayList<Movie> getMoviesList(final JSONObject object) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONArray results = object.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);

                String poster = movie.getString("poster_path");
                String title = movie.getString("title");
                String voteAverage = movie.getString("vote_average");
                String overview = movie.getString("overview");
                String releaseDate = movie.getString("release_date");
                int id = movie.getInt("id");

                Movie movieObject = new Movie(title, overview, voteAverage, poster, releaseDate, id);

                movies.add(movieObject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return movies;
    }

    public static ArrayList<Trailer> getTrailers(final JSONObject object) {
        ArrayList<Trailer> trailer = new ArrayList<>();

        try {
            JSONArray results = object.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                if (movie.getString("type").equals("Trailer")) {
                    String key = movie.getString("key");
                    String name = movie.getString("name");
                    Trailer trailerObject = new Trailer(name, key);
                    trailer.add(trailerObject);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return trailer;
    }

    public static ArrayList<Trailer> getReviews(final JSONObject object) {
        ArrayList<Trailer> reviews = new ArrayList<>();

        try {
            JSONArray results = object.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                String key = movie.getString("content");
                key = key.replaceAll("//s+", "");
                String name = movie.getString("author");
                name = name.replaceAll("//s+", "");
                Trailer trailerObject = new Trailer(name, key);
                reviews.add(trailerObject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reviews;
    }

}
