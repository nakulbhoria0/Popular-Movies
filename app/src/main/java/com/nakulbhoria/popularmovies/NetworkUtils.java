package com.nakulbhoria.popularmovies;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.internal.Util;

public class NetworkUtils {

    public static ArrayList<Movie> getData(String string){
        ArrayList<Movie> movies = new ArrayList<>();
        try{
            URL url = new URL(string);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            String data = "";

            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                data = scanner.next();
            }

            if(data == null || data.isEmpty()){
                return movies;
            }
            JSONObject object = new JSONObject(data);
            JSONArray results = object.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);

                String poster = movie.getString("poster_path");
                String title = movie.getString("title");
                String voteAverage = movie.getString("vote_average");
                String overview = movie.getString("overview");
                String releaseDate = movie.getString("release_date");

                Movie movieObject = new Movie(title, overview, voteAverage, poster, releaseDate);

                movies.add(movieObject);
            }
            connection.disconnect();


            return movies;


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

}
