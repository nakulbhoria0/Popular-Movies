package com.nakulbhoria.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        String titleString = b.getString("title");
        String descriptionString = b.getString("description");
        String releaseDateString = b.getString("releaseDate");
        String posterString = b.getString("poster");
        String ratingString = b.getString("rating");

        ImageView imageView = findViewById(R.id.image_iv);
        TextView title = findViewById(R.id.title_tv);
        TextView description = findViewById(R.id.description_tv);
        TextView releaseDate = findViewById(R.id.release_date_tv);
        TextView rating = findViewById(R.id.user_rating_tv);

        String imageUrl = "http://image.tmdb.org/t/p/w300//" + posterString;
        Picasso.get().load(imageUrl).into(imageView);

        title.setText(titleString);
        description.setText(descriptionString);
        releaseDate.setText(releaseDateString);
        rating.setText(ratingString);
    }
}
