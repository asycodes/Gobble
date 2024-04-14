package com.sutd.t4app.ui.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.ui.home.RestaurantExploreAdapter;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder>{
    private List<Review> reviewList;
    private int layoutID;
    public ReviewListAdapter(List<Review> reviewList, int layoutID) {
        this.reviewList = reviewList;
        this.layoutID = layoutID;
    }


    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.layoutID, parent, false);
        Log.d("INflated or what","yes");
        return new ReviewListAdapter.ViewHolder(view, reviewList);
    }
    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ViewHolder holder, int position) {
        Log.d("We are reading position","index " + position);
        Log.d("Review state","RES: " + reviewList);
        if(reviewList.size() > 0){

            Review review = reviewList.get(position);
            Log.d("AdapterDebug", "Binding restaurant at position " + position + ": " + review.getReview());

            holder.Username.setText(review.getUsername());
            Log.d("DEBUGGING"," resName " + holder.Username.getText() );

            if(holder.UserReview != null) {
                holder.UserReview.setText(review.getReview());
                Log.d("DEBUGGING"," resLandmark " + holder.UserReview.getText() );
            }
            if(holder.UserRatings != null) {
                holder.UserRatings.setRating((float) review.getRating());
                Log.d("DEBUGGING", " resCuisine " + holder.UserRatings.getRating());
            }

            if(holder.ReviewLikes != null){
                holder.ReviewLikes.setText(review.getLikes());
                Log.d("DEBUGGING"," resLocation " + holder.ReviewLikes.getText() );
            }
            Picasso.get().setLoggingEnabled(true);


            if(review.getUser_img_link()!= ""){
                Picasso.get()
                        .load(review.getUser_img_link()) // Assuming `getImageUrl()` is a method in your `Review` class
                        .into(holder.imageViewUser);
                holder.imageViewUser.setVisibility(View.VISIBLE);
            }

            if(review.getImg_post_link()!= ""){
                Picasso.get()
                        .load(review.getImg_post_link()) // Assuming `getImageUrl()` is a method in your `Review` class
                        .into(holder.postImageView);
                holder.postImageView.setVisibility(View.VISIBLE);
            }else{
                // make the postImageView invisible
                holder.postImageView.setVisibility(View.GONE);

            }
        }

    }
    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public void updateData(List<Review> newReviewlist) {
        Log.d("AdapterUpdate", "Updating data with " + newReviewlist.size() + " restaurants.");
        this.reviewList = newReviewlist;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Username;
        TextView UserReview;
        RatingBar UserRatings;
        ImageView imageViewUser;
        TextView ReviewLikes;
        ImageView postImageView;




        ViewHolder(View view, List<Review> reviewList) {
            super(view);

            Username = view.findViewById(R.id.Username);
            UserReview = view.findViewById(R.id.User_review);
            UserRatings = view.findViewById(R.id.user_rating);
            ReviewLikes = view.findViewById(R.id.likeCountTextView);
            imageViewUser = view.findViewById(R.id.imageViewUser);
            postImageView = view.findViewById(R.id.postImageView);


        }
    }
}
