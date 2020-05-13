package com.nakulbhoria.popularmovies.data;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.nakulbhoria.popularmovies.Model.FavoriteMovie;

import java.util.List;

public class FavoriteRepository {

    public Cursor cursor;
    public FavoriteMovie favoriteMovie;
    private FavoriteDao favoriteDao;
    private LiveData<List<FavoriteMovie>> movies;

    public FavoriteRepository(Application application) {
        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);

        favoriteDao = db.favoriteDao();
        movies = favoriteDao.getList();
    }

    public LiveData<List<FavoriteMovie>> getAllMovies() {
        return movies;
    }

    public void insert(FavoriteMovie movie) {
        FavoriteRoomDatabase.databaseWriteExecutor.execute(() ->
                favoriteDao.insert(movie));
    }

    public void delete(int movie) {
        FavoriteRoomDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDao.delete(movie);
        });
    }

    public FavoriteMovie getMovie(int id) {

        FavoriteRoomDatabase.databaseWriteExecutor.execute(() -> {
            favoriteMovie = favoriteDao.getMovie(id);
        });

        return favoriteMovie;
    }
}
