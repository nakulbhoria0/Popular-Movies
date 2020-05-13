package com.nakulbhoria.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nakulbhoria.popularmovies.Adapters.FavoriteAdapter;
import com.nakulbhoria.popularmovies.Adapters.MainRecyclerAdapter;
import com.nakulbhoria.popularmovies.Model.FavoriteMovie;
import com.nakulbhoria.popularmovies.Model.FavoriteViewModel;
import com.nakulbhoria.popularmovies.Model.Movie;
import com.nakulbhoria.popularmovies.data.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movies = new ArrayList<>();
    ArrayList<FavoriteMovie> mFavoriteMovies = new ArrayList<>();
    RecyclerView recyclerView;
    MainRecyclerAdapter adapter;
    int sortIndex = 0;
    FavoriteAdapter favoriteAdapter;
    Spinner spinner;
    private FavoriteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        ArrayList<String> list = new ArrayList<>();
        list.add("Most Popular");
        list.add("Top Rated");
        list.add("Favorites");
        spinner = findViewById(R.id.spinner);

        viewModel.getAllMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                mFavoriteMovies.clear();
                mFavoriteMovies.addAll(favoriteMovies);
            }
        });

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

                    viewModel.getAllMovies().observe(MainActivity.this, new Observer<List<FavoriteMovie>>() {
                        @Override
                        public void onChanged(List<FavoriteMovie> favoriteMovies) {
                            mFavoriteMovies.clear();
                            mFavoriteMovies.addAll(favoriteMovies);
                            favoriteAdapter = new FavoriteAdapter(mFavoriteMovies, MainActivity.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(favoriteAdapter);
                        }
                    });
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

    @Override
    protected void onResume() {
        super.onResume();
        if (spinner != null) {
            int position = spinner.getSelectedItemPosition();
            if (position == 2) {
                favoriteAdapter = new FavoriteAdapter(mFavoriteMovies, MainActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(favoriteAdapter);
            } else {
                adapter = new MainRecyclerAdapter(movies, MainActivity.this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    class FetchMoviesData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String apiKey = "key";
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
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
