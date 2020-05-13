package com.nakulbhoria.popularmovies.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favorite_table")
public class FavoriteMovie implements Serializable {

    @ColumnInfo(name = "title")
    public String mTitle;
    @ColumnInfo(name = "overview")
    public String mOverview;
    @ColumnInfo(name = "vote")
    public String mVoteAverage;
    @ColumnInfo(name = "release_date")
    public String mReleaseDate;
    @PrimaryKey
    public int mId;

    public FavoriteMovie(String title, String overview, String voteAverage, String releaseDate, int id) {

        mTitle = title;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
        mId = id;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        mId = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
}
