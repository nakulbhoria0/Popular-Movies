package com.nakulbhoria.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movies = new ArrayList<>();
    GridView gridView;
    MovieAdapter adapter;
    boolean isSortByRating = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Most Popular");
        list.add("Top Rated");
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    isSortByRating = false;
                    new FetchMoviesData().execute();
                }else if(position == 1){
                    isSortByRating = true;
                    new FetchMoviesData().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        gridView = findViewById(R.id.grid_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie currentMovie = movies.get(position);

                Bundle bundle = new Bundle();

                bundle.putString("title", currentMovie.getmTitle());
                bundle.putString("description", currentMovie.getmOverview());
                bundle.putString("rating", currentMovie.getmVoteAverage());
                bundle.putString("poster", currentMovie.getmPoster());
                bundle.putString("releaseDate", currentMovie.getmReleaseDate());

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    class FetchMoviesData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String popularURL = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=e8b360e6cb85c4a1add9aac77aaf7ef8";
            String topRated = "https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=e8b360e6cb85c4a1add9aac77aaf7ef8";


            if(isSortByRating){
                movies = NetworkUtils.getData(topRated);
            }else{
                movies = NetworkUtils.getData(popularURL);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new MovieAdapter(movies, MainActivity.this);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
