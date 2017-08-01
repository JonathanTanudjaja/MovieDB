package com.hypeclub.www.moviedb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypeclub.www.moviedb.R;
import com.hypeclub.www.moviedb.model.MovieVideo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jo on 30-Jul-17.
 */

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoHolder> {

    private final MovieVideoOnClickListener clickListener;
    private ArrayList<MovieVideo> movieVideo;

    public MovieVideoAdapter(MovieVideoOnClickListener videoOnClickListener) {
        this.clickListener = videoOnClickListener;
    }

    @Override
    public MovieVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.movie_video,parent,false);
        return new MovieVideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieVideoHolder holder, int position) {
        holder.videoName.setText(movieVideo.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (movieVideo != null) {
            return movieVideo.size();
        }
        return 0;
    }

    public interface MovieVideoOnClickListener {
        void onVideoClick(MovieVideo movieVideo);
    }

    class MovieVideoHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        @BindView(R.id.video_name) TextView videoName;

        MovieVideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onVideoClick(movieVideo.get(position));
        }
    }

    public void setMovieVideo(ArrayList<MovieVideo> movieVideo) {
        this.movieVideo = movieVideo;
        notifyDataSetChanged();
    }
}
