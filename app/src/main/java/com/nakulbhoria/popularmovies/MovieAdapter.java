package com.nakulbhoria.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {

    ArrayList<Movie> mList;
    Context mContext;

    public MovieAdapter(ArrayList<Movie> list, Context context){

        mList = list;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Movie movie = mList.get(position);
        String poster = movie.getmPoster();
        if(!poster.isEmpty()){
            String imageUrl = "http://image.tmdb.org/t/p/w500//" + poster;
            Picasso.get().load(imageUrl).into(viewHolder.iv);
        }


        return convertView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        ImageView iv;
        public ViewHolder(View view){
            iv = (ImageView) view.findViewById(R.id.imageView);
        }
    }

}
