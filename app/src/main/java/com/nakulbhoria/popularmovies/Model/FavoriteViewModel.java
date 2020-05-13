package com.nakulbhoria.popularmovies.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nakulbhoria.popularmovies.data.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteRepository repository;
    private LiveData<List<FavoriteMovie>> movies;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteRepository(application);
        movies = repository.getAllMovies();
    }

    public LiveData<List<FavoriteMovie>> getAllMovies() {
        return movies;
    }

    public void insert(FavoriteMovie movie) {
        repository.insert(movie);
    }

    public void delete(int movie) {
        repository.delete(movie);
    }

    public FavoriteMovie getMovie(int id) {
        return repository.getMovie(id);
    }
}
