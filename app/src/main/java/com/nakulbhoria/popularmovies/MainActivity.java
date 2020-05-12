package com.nakulbhoria.popularmovies;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nakulbhoria.popularmovies.Model.Movie;
import com.nakulbhoria.popularmovies.data.DatabaseHelper;
import com.nakulbhoria.popularmovies.data.MovieProvider;
import com.nakulbhoria.popularmovies.data.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movies = new ArrayList<>();
    RecyclerView recyclerView;
    MainRecyclerAdapter adapter;
    int sortIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> list = new ArrayList<>();
        list.add("Most Popular");
        list.add("Top Rated");
        list.add("Favorites");
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    sortIndex = 0;
                    new FetchMoviesData().execute();
                }else if(position == 1){
                    sortIndex = 1;
                    new FetchMoviesData().execute();
                } else if (position == 2) {
                    sortIndex = 2;
                    movies.clear();
                    Cursor cursor = getContentResolver().query(MovieProvider.CONTENT_URI, null, null, null, null);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            int tempId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                            String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                            String rating = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RATING));
                            String releaseDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RELEASE_DATE));
                            movies.add(new Movie(name, description, rating, "", releaseDate, tempId));
                            cursor.moveToNext();
                        }
                        cursor.close();
                    }
                    adapter = new MainRecyclerAdapter(movies, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerView = findViewById(R.id.main_recycler_view);
        adapter = new MainRecyclerAdapter(movies, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    class FetchMoviesData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String apiKey = "e8b360e6cb85c4a1add9aac77aaf7ef8";
            String popularURL = "https://api.themoviedb.org/3/movie/popular?&api_key=" + apiKey;
            String topRated = "https://api.themoviedb.org/3/movie/top_rated?&api_key=" + apiKey;


            if (sortIndex == 0) {
                JSONObject object = NetworkUtils.getData(topRated);
                movies = NetworkUtils.getMoviesList(object);
            } else if (sortIndex == 1) {
                JSONObject object = NetworkUtils.getData(popularURL);
                movies = NetworkUtils.getMoviesList(object);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new MainRecyclerAdapter(movies, MainActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
