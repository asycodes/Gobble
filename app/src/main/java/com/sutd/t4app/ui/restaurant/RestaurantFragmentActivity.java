package com.sutd.t4app.ui.restaurant;

import android.content.pm.PackageManager;
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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import android.Manifest;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.databinding.FragmentDashboardBinding;
import com.sutd.t4app.databinding.FragmentRestuarantProfileBinding;
import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;

/**
 * The `RestaurantFragmentActivity` class is responsible for displaying restaurant details and allowing
 * users to compare restaurants in an Android app.
 */
public class RestaurantFragmentActivity extends Fragment implements OnMapReadyCallback {
    private FragmentRestuarantProfileBinding binding;
    private TextView textViewRestaurantLocation;
    private ImageView restImageHolder;
    private RatingBar Ratings;//Overall
    private TextView Menu1;
    private TextView Menu2;
    private TextView restaurantNameTextView;
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
    private MapView mapView;
    private GoogleMap googleMap;
    private String RestaurantId;
    private HomeFragmentViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentRestuarantProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(HomeFragmentViewModel.class);


        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        initialiseUI(root);

        Bundle arguments = getArguments();
        String value = null;
        if (arguments != null) {
            restaurant = arguments.getParcelable("restaurant");
            if (restaurant != null) {

                displayRestaurantDetails(restaurant);

            }

            else {
                String restaurantId = arguments.getString("restaurantId");
                if (restaurantId != null) {
                    // Fetch the restaurant details using the provided ID
                    observeSpecificRestaurant(restaurantId);
                }

            }


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

    public void initialiseUI(View root){
        restaurantNameTextView = root.findViewById(R.id.textViewRestaurantName);
        Ratings = root.findViewById(R.id.ratingRest);
        Menu1 = root.findViewById(R.id.Menu1);
        Menu2 = root.findViewById(R.id.Menu2);
        Menu3 = root.findViewById(R.id.Menu3);
        Menu4 = root.findViewById(R.id.Menu4);
        foodRating = root.findViewById(R.id.foodRatingBar);
        serviceRating = root.findViewById(R.id.serivceRatingBar);
        atmosphereRating = root.findViewById(R.id.atmosphereRatingBar);
        User1 = root.findViewById(R.id.User1);
        User1Review = root.findViewById(R.id.User1_review);
        User1Ratings = root.findViewById(R.id.User1_rating);
        User2 = root.findViewById(R.id.User2);
        User2Review = root.findViewById(R.id.User2_review);
        User2Ratings = root.findViewById(R.id.user2_rating);
        restaurantProfileImage = root.findViewById(R.id.restaurantProfileImage);
    }

    public void displayRestaurantDetails(Restaurant restaurant) {
        if (restaurantNameTextView != null && Ratings != null && Menu1 != null) {
            restaurantNameTextView.setText(restaurant.getName());
            Menu1.setText(restaurant.getTopMenu1());
            Menu2.setText(restaurant.getTopMenu2());
            Menu3.setText(restaurant.getTopMenu3());
            Menu4.setText(restaurant.getTopMenu4());
            Ratings.setRating((float) restaurant.getRatings().doubleValue());
            foodRating.setRating((float) restaurant.getFoodRating().doubleValue());
            serviceRating.setRating((float) restaurant.getServiceRating().doubleValue());
            atmosphereRating.setRating((float) restaurant.getAmbienceRating().doubleValue());
            User1.setText(restaurant.getUserId1());
            User1Review.setText(restaurant.getReview1());
            User1Ratings.setRating((float) restaurant.getReviewRating1().doubleValue());
            User2.setText(restaurant.getUserId2());
            User2Review.setText(restaurant.getReview2());
            User2Ratings.setRating((float) restaurant.getReviewRating2().doubleValue());
            Picasso.get()
                    .load(restaurant.getImgMainURL())
                    .resize(1024, 768) // You can adjust these values as needed
                    .centerInside()
                    .into(restaurantProfileImage);
            Log.e("RestaurantFragmentActivity", "UI components successful.");
        } else {
            Log.e("RestaurantFragmentActivity", "UI components are not initialized.");
        }
    }

    //    private void observeRestaurantLiveData() {
//        viewModel.getSpecificRestaurantLiveData().observe(getViewLifecycleOwner(), fetchedRestaurant -> {
//            if (fetchedRestaurant != null) {
//                this.restaurant = fetchedRestaurant;
//                displayRestaurantDetails(fetchedRestaurant);
//            } else {
//                Log.e("RestaurantFragmentActivity", "No restaurant details available.");
//                // Optionally show an error message or a placeholder
//            }
//        });
//    }
public void observeSpecificRestaurant(String restaurantId) {
    viewModel.getRestaurantById(restaurantId).observe(getViewLifecycleOwner(), new Observer<Restaurant>() {
        @Override
        public void onChanged(Restaurant restaurant) {
            if (restaurant != null) {
                displayRestaurantDetails(restaurant);
            } else {
                Log.e("RestaurantFragmentActivity", "No restaurant details available.");
                // Optionally show an error message or a placeholder
            }
        }
    });
}


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // This method is automatically called when the map is ready
        this.googleMap = googleMap; // Save a reference to the GoogleMap object
        setUpMap(); // Setup your map UI and functionality here

    }

    private void setUpMap() {
        if (restaurant != null) {
            double lat = Double.parseDouble(restaurant.getLat());
            double lng = Double.parseDouble(restaurant.getLng());
            LatLng location = new LatLng(lat, lng);

            Log.d("MapDebug", "Adding marker at location: " + location.toString());

            if (googleMap != null) {
                googleMap.addMarker(new MarkerOptions().position(location).title(restaurant.getName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15)); // Adjust the zoom level as needed
            } else {
                Log.e("MapError", "GoogleMap object is null");
            }
        } else {
            Log.e("MapError", "Restaurant object is null");
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
//    }
//    private void fetchAndDisplayRestaurant(String restaurantId) {
//        // Assuming you have a method to fetch a Restaurant by ID
//        viewModel.fetchRestaurantById(restaurantId).observe(getViewLifecycleOwner(), fetchedRestaurant -> {
//            if (fetchedRestaurant != null) {
//                this.restaurant = fetchedRestaurant;
//                displayRestaurantDetails(fetchedRestaurant);
//            } else {
//                Log.e("RestaurantFragment", "Failed to load restaurant details for ID: " + restaurantId);
//            }
//        });
//    }

}
