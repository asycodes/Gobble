package com.sutd.t4app.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;

import java.util.List;

public class RestaurantExploreAdapter extends RecyclerView.Adapter<RestaurantExploreAdapter.ViewHolder>{

    private List<Restaurant> restaurantList;
    public RestaurantExploreAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);
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
            holder.textViewClosetLandmark.setText(restaurant.getClosestLandmark());
            Log.d("DEBUGGING"," res " + holder.textViewClosetLandmark.getText() );

            holder.textViewRestaurantLocation.setText(restaurant.getAddress());
            Log.d("DEBUGGING"," res " + holder.textViewRestaurantLocation.getText() );

            holder.textViewRestaurantCuisine.setText(restaurant.getCuisine());
            Log.d("DEBUGGING"," res " + holder.textViewRestaurantCuisine.getText() );

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

        ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewRestaurantName);
            textViewRestaurantCuisine = view.findViewById(R.id.textViewRestaurantCuisine);
            textViewClosetLandmark = view.findViewById(R.id.textViewRestaurantClosestLandmark);
            textViewRestaurantLocation = view.findViewById(R.id.textViewRestaurantLocation);

        }
    }
}