package com.sutd.t4app.ui.restaurant;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.databinding.FragmentDashboardBinding;
import com.sutd.t4app.databinding.FragmentRestuarantProfileBinding;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.ui.home.RestaurantExploreAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantFragment extends Fragment {
    private FragmentRestuarantProfileBinding binding;
    private TextView textViewRestaurantLocation;
    private ImageView restImageHolder;
    private RatingBar Ratings;//Overall
    private TextView Menu1;
    private TextView Menu2;
    private TextView Menu3;
    private TextView Menu4;
    private RatingBar foodRating;
    private RatingBar serviceRating;
    private RatingBar atmosphereRating;
    private TextView User1;
    private TextView User1Review;
    private RatingBar User1Ratings;
    private TextView User2;
    private TextView User2Review;
    private RatingBar User2Ratings;
    private Restaurant restaurant;
    private ImageView restaurantProfileImage;
    private RestaurantFragmentViewModel viewModel;
    private ReviewListAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentRestuarantProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle arguments = getArguments();
        adapter = new ReviewListAdapter(new ArrayList<>(), R.layout.review_item );
        binding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reviewRecyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(RestaurantFragmentViewModel.class);

        String value = null;
        if (arguments != null) {
            restaurant = arguments.getParcelable("restaurant");
            viewModel.setcurrRes(restaurant);
            viewModel.getreviewsLiveData().observe(getViewLifecycleOwner(), reviews -> {
                // Update the adapter with the list of restaurants
                Log.d("HomeFragment", "Number of restaurants received: " + reviews.size());

                //take the first two of restaurants
                if (reviews.size() >= 1) {
                    adapter.updateData(reviews); // See note below about adapter
                }
                else {
                    // TODO: meaning empty, SHOW EMPTY UI
                }

            });
            TextView restaurantNameTextView = root.findViewById(R.id.textViewRestaurantName);
            //TextView restaurantNameTextView = root.findViewById(R.id.restaurantName);
            // if the tripadId is empty we find it first
            // likewise for yelpId
            // we run the find id using lat, lng and name
            // pick the first result then run the find reviews
            // once we have gained the reviews, we insert into the review collection if it doesnt exist
            // show in- house reviews first


/*

            restaurantNameTextView.setText(restaurant.getName());
            Log.d("RestaurantData", "Restaurant name: " + restaurant.getName());
            Ratings=root.findViewById(R.id.ratingRest);
            Menu1= root.findViewById(R.id.Menu1);
            Menu2= root.findViewById(R.id.Menu2);
            Menu3= root.findViewById(R.id.Menu3);
            Menu4= root.findViewById(R.id.Menu4);
            foodRating=root.findViewById(R.id.foodRatingBar);
            serviceRating=root.findViewById(R.id.serivceRatingBar);
            atmosphereRating=root.findViewById(R.id.atmosphereRatingBar);
            Menu1.setText(restaurant.getTopMenu1());
            Log.d("RestaurantData", "Top Menu1: " + restaurant.getTopMenu1());
            Menu2.setText(restaurant.getTopMenu2());
            Menu3.setText(restaurant.getTopMenu3());
            Menu4.setText(restaurant.getTopMenu4());
            Ratings.setRating((float) restaurant.getRatings().doubleValue());
            foodRating.setRating((float) restaurant.getFoodRating().doubleValue());
            Log.d("RestaurantData", "foodrating: " + restaurant.getFoodRating());
            serviceRating.setRating((float) restaurant.getServiceRating().doubleValue());
            atmosphereRating.setRating((float) restaurant.getAmbienceRating().doubleValue());
            User1.setText(restaurant.getUserId1());
            User1Review.setText(restaurant.getReview1());
            User1Ratings.setRating((float) restaurant.getReviewRating1().doubleValue());
            User2.setText(restaurant.getUserId2());
            User2Review.setText(restaurant.getReview2());
            User2Ratings.setRating((float) restaurant.getReviewRating2().doubleValue());

            restaurantProfileImage = root.findViewById(R.id.restaurantProfileImage);
            Picasso.get()
                    .load(restaurant.getImgMainURL()) // Assuming `getImageUrl()` is a method in your `Restaurant` class
                    .into(restaurantProfileImage);
*/


        }
        Button btnCompare = root.findViewById(R.id.compareButton);

        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("restaurant", restaurant);
                // TODO: Add your code here to be executed when the button is clicked
                Navigation.findNavController(v).navigate(R.id.compare_fragment, bundle);
            }
        });
        //update restaurantName textview values with value from restaurant.getName()

//        CardView fuelPlus1Card= root.findViewById(R.id.FuelPlus1);
//
//        fuelPlus1Card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.torestaurantfragment);
//            }
//        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
