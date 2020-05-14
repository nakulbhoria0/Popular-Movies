package com.nakulbhoria.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nakulbhoria.popularmovies.Model.FavoriteMovie;
import com.nakulbhoria.popularmovies.Model.FavoriteViewModel;
import com.nakulbhoria.popularmovies.Model.Movie;
import com.nakulbhoria.popularmovies.Model.Trailer;
import com.nakulbhoria.popularmovies.data.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private static final String BUNDLE_RECYCLER_LAYOUT = "recyclerView";
    private static final String BUNDLE_REVIEW_RECYCLER_LAYOUT = "reviewRecyclerView";
    ArrayList<Trailer> videoList = new ArrayList<>();
    ArrayList<Trailer> reviewList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView reviewRecyclerView;
    VideoAdapter videoAdapter;
    ReviewAdapter reviewAdapter;
    FavoriteViewModel viewModel;
    boolean isFavorite;
    ImageView imageView;
    TextView title;
    TextView description;
    TextView releaseDate;
    TextView rating;
    Movie movie = null;
    Button favoriteButton;
    Bundle state = null;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        recyclerView = findViewById(R.id.videos_recycler_view);
        reviewRecyclerView = findViewById(R.id.reviews_recycler_view);
        if (savedInstanceState != null) {
            state = savedInstanceState;
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            Parcelable savedReviewRecyclerState = savedInstanceState.getParcelable(BUNDLE_REVIEW_RECYCLER_LAYOUT);
            reviewAdapter = new ReviewAdapter(reviewList);
            videoAdapter = new VideoAdapter(videoList);
            reviewRecyclerView.setAdapter(reviewAdapter);
            recyclerView.setAdapter(videoAdapter);
            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            reviewRecyclerView.getLayoutManager().onRestoreInstanceState(savedReviewRecyclerState);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        favoriteButton = findViewById(R.id.favorites_button);
        FavoriteMovie favoriteMovie;


        movie = (Movie) intent.getSerializableExtra("movie");
        favoriteMovie = (FavoriteMovie) intent.getSerializableExtra("favoriteMovie");


        if (null != movie) {
            id = movie.getmId();
        } else {
            id = favoriteMovie.mId;
        }

        FavoriteMovie tempFavoriteMovie = viewModel.getMovie(id);
        new FetchData().execute();
        if (favoriteMovie != null) {

            isFavorite = true;
            favoriteButton.setText(R.string.remove_favorite);
        } else {
            favoriteMovie = tempFavoriteMovie;
            favoriteButton.setText(R.string.add_favorite);
            isFavorite = false;
        }
        imageView = findViewById(R.id.image_iv);
        title = findViewById(R.id.title_tv);
        description = findViewById(R.id.description_tv);
        releaseDate = findViewById(R.id.release_date_tv);
        rating = findViewById(R.id.user_rating_tv);


        reviewAdapter = new ReviewAdapter(reviewList);
        videoAdapter = new VideoAdapter(videoList);
        reviewRecyclerView.setAdapter(reviewAdapter);
        recyclerView.setAdapter(videoAdapter);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (movie != null) {
            String imageUrl = "https://image.tmdb.org/t/p/w300//" + movie.getmPoster();
            Picasso.get().load(imageUrl).into(imageView);

            final String name = movie.getmTitle();
            final String descriptionString = movie.getmOverview();
            final String releaseDateString = movie.getmReleaseDate();
            final String ratingString = movie.getmVoteAverage();
            title.setText(name);
            description.setText(descriptionString);
            releaseDate.setText(releaseDateString);
            rating.setText(ratingString);
        } else if (favoriteMovie != null) {
            final String name = favoriteMovie.mTitle;
            final String descriptionString = favoriteMovie.mOverview;
            final String releaseDateString = favoriteMovie.mReleaseDate;
            final String ratingString = favoriteMovie.mVoteAverage;
            title.setText(name);
            description.setText(descriptionString);
            releaseDate.setText(releaseDateString);
            rating.setText(ratingString);
        }

        Movie finalMovie = movie;
        favoriteButton.setOnClickListener(v -> {
            if (isFavorite) {
                viewModel.delete(id);
                favoriteButton.setText(R.string.add_favorite);
                isFavorite = false;
                finish();
            } else {
                final String name = finalMovie.getmTitle();
                final String descriptionString = finalMovie.getmOverview();
                final String releaseDateString = finalMovie.getmReleaseDate();
                final String ratingString = finalMovie.getmVoteAverage();
                FavoriteMovie tempFavoriteMovie1 = new FavoriteMovie(name, descriptionString, ratingString, releaseDateString, id);

                favoriteButton.setText(R.string.remove_favorite);
                viewModel.insert(tempFavoriteMovie1);
                isFavorite = true;
            }
        });


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_REVIEW_RECYCLER_LAYOUT, reviewRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        Button button;
        TextView textView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button_play);
            textView = itemView.findViewById(R.id.text_video);
        }
    }

    static class MyReviewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textView;

        MyReviewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.text_author_name);
            textView = itemView.findViewById(R.id.text_review);
        }
    }

    private static class ReviewAdapter extends RecyclerView.Adapter<MyReviewHolder> {

        ArrayList<Trailer> reviewList;

        ReviewAdapter(ArrayList<Trailer> videoList) {

            this.reviewList = videoList;

        }

        @NonNull
        @Override
        public MyReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
            return new MyReviewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyReviewHolder holder, int position) {

            Trailer review = reviewList.get(position);
            holder.textView.setText(review.getValue());
            holder.textViewAuthor.setText(review.getName());

        }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }
    }

    private class VideoAdapter extends RecyclerView.Adapter<MyViewHolder> {

        ArrayList<Trailer> videoList;

        VideoAdapter(ArrayList<Trailer> videoList) {

            this.videoList = videoList;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            final Trailer trailer = videoList.get(position);
            holder.textView.setText(trailer.getName());

            holder.itemView.setOnClickListener(v -> {
                String key = trailer.getValue();
                String url = "https://www.youtube.com/watch?v=" + key;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    DetailActivity.this.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }
    }

    class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String apiKey = "key";
            String trailersUrl = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + apiKey;
            String reviewsUrl = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + apiKey;


            JSONObject trailersObject = NetworkUtils.getData(trailersUrl);
            videoList = NetworkUtils.getTrailers(trailersObject);
            JSONObject reviewsObject = NetworkUtils.getData(reviewsUrl);
            reviewList = NetworkUtils.getReviews(reviewsObject);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            videoAdapter = new VideoAdapter(videoList);
            recyclerView.setAdapter(videoAdapter);
            videoAdapter.notifyDataSetChanged();
            reviewAdapter = new ReviewAdapter(reviewList);
            reviewRecyclerView.setAdapter(reviewAdapter);
            reviewAdapter.notifyDataSetChanged();
            if (state != null) {
                Parcelable savedRecyclerLayoutState = state.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                Parcelable savedReviewRecyclerState = state.getParcelable(BUNDLE_REVIEW_RECYCLER_LAYOUT);
                reviewRecyclerView.getLayoutManager().onRestoreInstanceState(savedReviewRecyclerState);
            }
        }
    }
}
