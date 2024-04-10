package com.sutd.t4app.ui.home;
/*
 * The `RestaurantExploreAdapter` class is a RecyclerView adapter used to display a list of restaurants
 * with their details and images in an Android app.
 */
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

import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;

import java.util.List;
import com.squareup.picasso.Picasso;

public class RestaurantExploreAdapter extends RecyclerView.Adapter<RestaurantExploreAdapter.ViewHolder>{

    private List<Restaurant> restaurantList;
    private int layoutID;
    public RestaurantExploreAdapter(List<Restaurant> restaurantList, int layoutID) {
        this.restaurantList = restaurantList;
        this.layoutID = layoutID;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.layoutID, parent, false);
        Log.d("INflated or what","yes");
        return new ViewHolder(view, restaurantList);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("We are reading position","index " + position);
        Log.d("Restaurant state","RES: " + restaurantList);
        if(restaurantList.size() > 0){

            Restaurant restaurant = restaurantList.get(position);
            Log.d("AdapterDebug", "Binding restaurant at position " + position + ": " + restaurant.getName());

            holder.textViewName.setText(restaurant.getName());
            Log.d("DEBUGGING"," resName " + holder.textViewName.getText() );

            if(holder.textViewClosetLandmark != null) {
                holder.textViewClosetLandmark.setText(restaurant.getClosestLandmark());
                Log.d("DEBUGGING"," resLandmark " + holder.textViewClosetLandmark.getText() );
            }
            if(holder.textViewRestaurantCuisine != null) {
                holder.textViewRestaurantCuisine.setText(restaurant.getCuisine());
                Log.d("DEBUGGING", " resCuisine " + holder.textViewRestaurantCuisine.getText());
            }

            if(holder.textViewRestaurantLocation != null){
                holder.textViewRestaurantLocation.setText(restaurant.getAddress());
                Log.d("DEBUGGING"," resLocation " + holder.textViewRestaurantLocation.getText() );
            }
            Picasso.get().setLoggingEnabled(true);




            //add restImage update imageView
            Picasso.get()
                .load(restaurant.getImgMainURL()) // Assuming `getImageUrl()` is a method in your `Restaurant` class
                .into(holder.restImageHolder);


            Log.d("DEBUGGING"," resImage " +restaurant.getImgMainURL());

            // Bind other restaurant details as needed
        }

    }
    @Override
    public int getItemCount() {
        return restaurantList != null ? restaurantList.size() : 0;
    }

    public void updateData(List<Restaurant> newRestaurantList) {
        Log.d("AdapterUpdate", "Updating data with " + newRestaurantList.size() + " restaurants.");
        this.restaurantList = newRestaurantList;
        notifyDataSetChanged();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewRestaurantCuisine;
        TextView textViewClosetLandmark;
        TextView textViewRestaurantLocation;
        ImageView restImageHolder;
        RatingBar Ratings;//Overall
        TextView Menu1;
        TextView Menu2;
        TextView Menu3;
        TextView Menu4;
        RatingBar foodRating;
        RatingBar serviceRating;
        RatingBar atmosphereRating;
        TextView User1;
        TextView User1Review;
        RatingBar User1Ratings;
        TextView User2;
        TextView User2Review;
        RatingBar User2Ratings;



        ViewHolder(View view, List<Restaurant> restaurantList) {
            super(view);
            textViewName = view.findViewById(R.id.textViewRestaurantName);
            textViewRestaurantCuisine = view.findViewById(R.id.textViewRestaurantCuisine);
            textViewClosetLandmark = view.findViewById(R.id.textViewRestaurantClosestLandmark);
            textViewRestaurantLocation = view.findViewById(R.id.textViewRestaurantLocation);
            restImageHolder = view.findViewById(R.id.restImage);




            //attach onClickListener to restaurantItemView
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    int pos = getBindingAdapterPosition();
                    Restaurant restaurant = restaurantList.get(pos);
                    bundle.putParcelable("restaurant", restaurant); // replace "key" and "value" with your actual key and value
                    if (pos != RecyclerView.NO_POSITION){
                        Navigation.findNavController(v).navigate(R.id.torestaurantfragment, bundle);
                    }


                }
            });
        }
    }
}