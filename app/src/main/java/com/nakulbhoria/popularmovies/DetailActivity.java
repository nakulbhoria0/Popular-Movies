package com.nakulbhoria.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nakulbhoria.popularmovies.Model.Movie;
import com.nakulbhoria.popularmovies.Model.Trailer;
import com.nakulbhoria.popularmovies.data.DatabaseHelper;
import com.nakulbhoria.popularmovies.data.MovieProvider;
import com.nakulbhoria.popularmovies.data.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    ArrayList<Trailer> videoList = new ArrayList<>();
    ArrayList<Trailer> reviewList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView reviewRecyclerView;
    VideoAdapter videoAdapter;
    ReviewAdapter reviewAdapter;
    boolean isFavorite;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        id = movie.getmId();

        final Button favoriteButton = findViewById(R.id.favorites_button);
        Cursor cursor = getContentResolver().query(MovieProvider.CONTENT_URI, null, DatabaseHelper.COLUMN_ID + "=" + id, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            isFavorite = false;
        } else {
            cursor.close();
            isFavorite = true;
        }
        if (isFavorite) {
            favoriteButton.setText(R.string.remove_favorite);
        } else {
            favoriteButton.setText(R.string.add_favorite);
        }

        new FetchData().execute();

        recyclerView = findViewById(R.id.videos_recycler_view);
        reviewRecyclerView = findViewById(R.id.reviews_recycler_view);
        reviewAdapter = new ReviewAdapter(reviewList);
        videoAdapter = new VideoAdapter(videoList);
        reviewRecyclerView.setAdapter(reviewAdapter);
        recyclerView.setAdapter(videoAdapter);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView imageView = findViewById(R.id.image_iv);
        TextView title = findViewById(R.id.title_tv);
        final TextView description = findViewById(R.id.description_tv);
        TextView releaseDate = findViewById(R.id.release_date_tv);
        TextView rating = findViewById(R.id.user_rating_tv);


        String imageUrl = "http://image.tmdb.org/t/p/w300//" + movie.getmPoster();
        Picasso.get().load(imageUrl).into(imageView);

        final String name = movie.getmTitle();
        final String descriptionString = movie.getmOverview();
        final String releaseDateString = movie.getmReleaseDate();
        final String ratingString = movie.getmVoteAverage();
        title.setText(name);
        description.setText(descriptionString);
        releaseDate.setText(releaseDateString);
        rating.setText(ratingString);


        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    int deleted = getContentResolver().delete(MovieProvider.CONTENT_URI, DatabaseHelper.COLUMN_ID + "=" + id, null);
                    favoriteButton.setText(R.string.add_favorite);
                    finish();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.COLUMN_ID, id);
                    contentValues.put(DatabaseHelper.COLUMN_NAME, name);
                    contentValues.put(DatabaseHelper.COLUMN_DESCRIPTION, descriptionString);
                    contentValues.put(DatabaseHelper.COLUMN_RATING, ratingString);
                    contentValues.put(DatabaseHelper.COLUMN_RELEASE_DATE, releaseDateString);
                    getContentResolver().insert(MovieProvider.CONTENT_URI, contentValues);
                    favoriteButton.setText(R.string.remove_favorite);
                    isFavorite = true;
                }
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public Button button;
        public TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button_play);
            textView = itemView.findViewById(R.id.text_video);
        }
    }

    public static class MyReviewHolder extends RecyclerView.ViewHolder {

        public TextView textViewAuthor;
        public TextView textView;

        public MyReviewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.text_author_name);
            textView = itemView.findViewById(R.id.text_review);
        }
    }

    private class VideoAdapter extends RecyclerView.Adapter<MyViewHolder> {

        ArrayList<Trailer> videoList;

        public VideoAdapter(ArrayList<Trailer> videoList) {

            this.videoList = videoList;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            final Trailer trailer = videoList.get(position);
            holder.textView.setText(trailer.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = trailer.getValue();
                    String url = "https://www.youtube.com/watch?v=" + key;
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        DetailActivity.this.startActivity(intent);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<MyReviewHolder> {

        ArrayList<Trailer> reviewList;

        public ReviewAdapter(ArrayList<Trailer> videoList) {

            this.reviewList = videoList;

        }

        @NonNull
        @Override
        public MyReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
            MyReviewHolder holder = new MyReviewHolder(view);
            return holder;
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

    class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String apiKey = "e8b360e6cb85c4a1add9aac77aaf7ef8";
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
        }
    }
}
