package com.nakulbhoria.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nakulbhoria.popularmovies.DetailActivity;
import com.nakulbhoria.popularmovies.Model.FavoriteMovie;
import com.nakulbhoria.popularmovies.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    ArrayList<FavoriteMovie> movie;
    private Context context;

    public FavoriteAdapter(ArrayList<FavoriteMovie> movie, Context context) {
        this.context = context;
        this.movie = movie;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent
                , false);
        FavoriteViewHolder holder = new FavoriteViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        FavoriteMovie currentMovie = movie.get(position);
        holder.title.setText(currentMovie.getmTitle());
        holder.date.setText(currentMovie.getmReleaseDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("favoriteMovie", currentMovie);
                context.startActivity(intent);
            }
        });
    }

    public void setData(ArrayList<FavoriteMovie> movie) {
        this.movie = movie;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movie.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_tv);
            date = itemView.findViewById(R.id.release_date_tv);
        }
    }
}
