package com.nakulbhoria.popularmovies.Model;

import java.io.Serializable;

public class Movie implements Serializable {
    String mTitle, mOverview, mVoteAverage, mPoster, mReleaseDate ;
    int mId;

    public Movie(String title, String overview, String voteAverage, String poster, String releaseDate, int id) {

        mTitle = title;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mPoster = poster;
        mReleaseDate = releaseDate;
        mId = id;
    }

    public int getmId() {
        return mId;
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

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setmId(int id) {
        mId = id;
    }
}
