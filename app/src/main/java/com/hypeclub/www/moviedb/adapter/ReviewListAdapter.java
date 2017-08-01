package com.hypeclub.www.moviedb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypeclub.www.moviedb.R;
import com.hypeclub.www.moviedb.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jo on 30-Jul-17.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewHolder>{

    private ArrayList<Review> reviewData;

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.review_item,parent,false);
        return new ReviewListAdapter.ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.author.setText(reviewData.get(position).getAuthor());
        holder.content.setText(reviewData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (reviewData != null) {
            return reviewData.size();
        }
        return 0;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author) TextView author;
        @BindView(R.id.review_content) TextView content;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setReviewData(ArrayList<Review> reviewData) {
        this.reviewData = reviewData;
        notifyDataSetChanged();
    }
}
