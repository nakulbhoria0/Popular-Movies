package com.nakulbhoria.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nakulbhoria.popularmovies.DetailActivity;
import com.nakulbhoria.popularmovies.Model.Movie;
import com.nakulbhoria.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyMainHolder> {


    private ArrayList<Movie> movies;
    private Context context;

    public MainRecyclerAdapter(ArrayList<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MyMainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MyMainHolder holder = new MyMainHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyMainHolder holder, int position) {

        final Movie movie = movies.get(position);
        String poster = movie.getmPoster();


        if (poster.isEmpty()) {
            holder.imageView.setVisibility(View.GONE);
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(movie.getmTitle());
        } else {
            String imageUrl = "http://image.tmdb.org/t/p/w500//" + poster;
            Picasso.get().load(imageUrl).into(holder.imageView);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyMainHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyMainHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }
}
