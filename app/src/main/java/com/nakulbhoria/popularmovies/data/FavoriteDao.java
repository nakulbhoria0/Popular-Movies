package com.nakulbhoria.popularmovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nakulbhoria.popularmovies.Model.FavoriteMovie;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavoriteMovie favoriteMovie);

    @Query("DELETE FROM favorite_table WHERE mId = :id")
    void delete(int id);

    @Query("SELECT * FROM favorite_table")
    LiveData<List<FavoriteMovie>> getList();

    @Query("SELECT * FROM favorite_table WHERE mId = :id")
    FavoriteMovie getMovie(int id);
}
