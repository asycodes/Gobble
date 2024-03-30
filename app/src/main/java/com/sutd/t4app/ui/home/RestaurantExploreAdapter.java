package com.sutd.t4app.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

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
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("We are reading position","index " + position);
        Log.d("Restaurant state","RES: " + restaurantList);
        if(restaurantList.size() > 0){

            Restaurant restaurant = restaurantList.get(position);
            Log.d("CHeck",restaurant.getName() + "  ADDED");

            holder.textViewName.setText(restaurant.getName());
            Log.d("DEBUGGING"," res " + holder.textViewName.getText() );

            if(holder.textViewClosetLandmark != null) {
                holder.textViewClosetLandmark.setText(restaurant.getClosestLandmark());
                Log.d("DEBUGGING"," res " + holder.textViewClosetLandmark.getText() );
            }
            if(holder.textViewRestaurantCuisine != null) {
                holder.textViewRestaurantCuisine.setText(restaurant.getCuisine());
                Log.d("DEBUGGING", " res " + holder.textViewRestaurantCuisine.getText());
            }
            holder.textViewRestaurantLocation.setText(restaurant.getAddress());
            Log.d("DEBUGGING"," res " + holder.textViewRestaurantLocation.getText() );


            //add restImage update imageView
            Picasso.get()
                .load("https://images.pexels.com/photos/6277500/pexels-photo-6277500.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load") // Assuming `getImageUrl()` is a method in your `Restaurant` class
                .into(holder.restImageHolder);

            // Bind other restaurant details as needed
        }

    }
    @Override
    public int getItemCount() {
        return restaurantList != null ? restaurantList.size() : 0;
    }

    public void updateData(List<Restaurant> newRestaurantList) {
        this.restaurantList = newRestaurantList;
        notifyDataSetChanged();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewRestaurantCuisine;
        TextView textViewClosetLandmark;
        TextView textViewRestaurantLocation;
        ImageView restImageHolder;

        ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewRestaurantName);
            textViewRestaurantCuisine = view.findViewById(R.id.textViewRestaurantCuisine);
            textViewClosetLandmark = view.findViewById(R.id.textViewRestaurantClosestLandmark);
            textViewRestaurantLocation = view.findViewById(R.id.textViewRestaurantLocation);
            restImageHolder = view.findViewById(R.id.restImage);
        }
    }
}