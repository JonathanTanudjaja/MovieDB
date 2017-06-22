package com.hypeclub.www.moviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.utilities.NetworkUtils;

/**
 * Created by Jo on 18-Jun-17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListHolder> {

    private final MoviePosterOnClickListener clickListener;
    private Movie[] movieData;

    public MovieListAdapter(MoviePosterOnClickListener movieOnClickListener) {
        this.clickListener = movieOnClickListener;
    }

    @Override
    public MovieListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.movie_poster,parent,false);
        return new MovieListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieListHolder holder, int position) {
        String posterPath = NetworkUtils.getMoviePosterBaseUri() + movieData[position].getPosterPath();
        Glide.with(holder.poster.getContext())
                .asDrawable()
                .load(posterPath)
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        if (movieData != null) {
            return movieData.length;
        }
        return 0;
    }

    public interface MoviePosterOnClickListener {
        void onMovieClick(Movie movie);
    }

    public class MovieListHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        final ImageView poster;

        MovieListHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onMovieClick(movieData[position]);
        }
    }

    public void setMovieData(Movie[] movieData) {
        this.movieData = movieData;
        notifyDataSetChanged();
    }
}
