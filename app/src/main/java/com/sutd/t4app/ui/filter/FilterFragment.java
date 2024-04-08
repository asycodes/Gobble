package com.sutd.t4app.ui.filter;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.slider.Slider;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.databinding.FilterBottomUpBinding;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.ui.reviews.ReviewViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class FilterFragment extends Fragment {

//    private CheckBox topRatedCheckbox, recommendedCheckbox, nearMeCheckbox;
    private CheckBox americanCheckbox, asianCheckbox, asianFusionCheckbox, breakfastCheckbox, chineseCheckbox, mexicanCheckbox, italianCheckbox, thaiCheckbox, indianCheckbox, japaneseCheckBox;
    private CheckBox glutenFreeCheckbox, halalCheckbox, veganFriendlyCheckbox, vegetarianCheckbox, seafoodCheckBox, healthyCheckBox;
    private CheckBox centralCheckbox, NorthCheckBox, NorthEastCheckBox, WestCheckBox, EastCheckBox;
    private Slider priceSlider;
    private ImageView filterStar1, filterStar2, filterStar3, filterStar4, filterStar5;
    private Button showResultsButton;

    private List<String> selectedFilter = new ArrayList<>();
    private int selectedPrice = 0;
    private int overallStarRating = 0;
    private ReviewViewModel viewModel;
    private FilterBottomUpBinding binding;
    private Realm realm;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FilterBottomUpBinding binding= FilterBottomUpBinding.inflate(inflater,container,false);
        View root= binding.getRoot();
        realm= Realm.getDefaultInstance();





        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Initialize views
        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
//        topRatedCheckbox = view.findViewById(R.id.topRatedCheckbox);
//        recommendedCheckbox = view.findViewById(R.id.RecommendedCheckbox);
//        nearMeCheckbox = view.findViewById(R.id.nearmeCheckbox);
        americanCheckbox = view.findViewById(R.id.AmericanCheckbox);
        asianCheckbox = view.findViewById(R.id.AsianCheckbox);
        asianFusionCheckbox = view.findViewById(R.id.AsianFusionCheckbox);
        breakfastCheckbox = view.findViewById(R.id.BreakfastCheckbox);
        chineseCheckbox = view.findViewById(R.id.ChineseCheckbox);
        italianCheckbox=view.findViewById(R.id.ItalianCheckbox);
        mexicanCheckbox= view.findViewById(R.id.MexicanCheckbox);
        thaiCheckbox=view.findViewById(R.id.ThaiCheckbox);
        indianCheckbox=view.findViewById(R.id.IndianCheckbox);
        japaneseCheckBox=view.findViewById(R.id.JapaneseCheckbox);

        glutenFreeCheckbox = view.findViewById(R.id.GlutenFreeCheckbox);
        halalCheckbox = view.findViewById(R.id.HalalCheckbox);
        veganFriendlyCheckbox = view.findViewById(R.id.VeganFriendlyCheckbox);
        vegetarianCheckbox = view.findViewById(R.id.VegetarianCheckbox);
        seafoodCheckBox= view.findViewById(R.id.SeafoodCheckbox);
        healthyCheckBox=view.findViewById((R.id.HealthyCheckbox));


        priceSlider = view.findViewById(R.id.priceSlider);
        filterStar1 = view.findViewById(R.id.filterStar1);
        filterStar2 = view.findViewById(R.id.filterStar2);
        filterStar3 = view.findViewById(R.id.filterStar3);
        filterStar4 = view.findViewById(R.id.filterStar4);
        filterStar5 = view.findViewById(R.id.filterStar5);

        centralCheckbox=view.findViewById(R.id.CentralCheckbox);
        NorthCheckBox=view.findViewById(R.id.NorthCheckbox);
        NorthEastCheckBox=view.findViewById(R.id.NorthEastCheckbox);
        WestCheckBox=view.findViewById(R.id.WestCheckbox);
        EastCheckBox=view.findViewById(R.id.EastCheckbox);

        showResultsButton = view.findViewById(R.id.showResultsButton);

        // Set onClickListener for checkboxes
//        setupCheckbox(topRatedCheckbox, selectedFilter);
//        setupCheckbox(recommendedCheckbox, selectedFilter);
//        setupCheckbox(nearMeCheckbox, selectedFilter);
//        setupCheckbox(americanCheckbox, selectedFilter);
//        setupCheckbox(asianCheckbox, selectedFilter);
//        setupCheckbox(asianFusionCheckbox, selectedFilter);
//        setupCheckbox(breakfastCheckbox, selectedFilter);
//        setupCheckbox(chineseCheckbox, selectedFilter);
//        setupCheckbox(italianCheckbox,selectedFilter);
//        setupCheckbox(mexicanCheckbox,selectedFilter);
//        setupCheckbox(thaiCheckbox,selectedFilter);
//        setupCheckbox(indianCheckbox,selectedFilter);
//        setupCheckbox(japaneseCheckBox,selectedFilter);
//
//        setupCheckbox(glutenFreeCheckbox, selectedFilter);
//        setupCheckbox(halalCheckbox, selectedFilter);
//        setupCheckbox(vegetarianCheckbox, selectedFilter);
//        setupCheckbox(veganFriendlyCheckbox, selectedFilter);
//        setupCheckbox(seafoodCheckBox,selectedFilter);
//        setupCheckbox(healthyCheckBox,selectedFilter);
//
//        setupCheckbox(centralCheckbox,selectedFilter);
//        setupCheckbox(NorthCheckBox,selectedFilter);
//        setupCheckbox(NorthEastCheckBox,selectedFilter);
//        setupCheckbox(WestCheckBox,selectedFilter);
//        setupCheckbox(EastCheckBox,selectedFilter);


        // Set listener for price slider
        priceSlider.addOnChangeListener((slider, value, fromUser) -> selectedPrice = (int) value);

        // Set onClickListener for star rating
        filterStar1.setOnClickListener(v -> setRating(filterStar1, 1, "filter"));
        filterStar2.setOnClickListener(v -> setRating(filterStar2, 2, "filter"));
        filterStar3.setOnClickListener(v -> setRating(filterStar3, 3, "filter"));
        filterStar4.setOnClickListener(v -> setRating(filterStar4, 4, "filter"));
        filterStar5.setOnClickListener(v -> setRating(filterStar5, 5, "filter"));

        // Set onClickListener for show results button
        showResultsButton.setOnClickListener(v -> {
            List<Restaurant> filteredRestaurants = applyFilters();
            Toast.makeText(getContext(), "Filtered Restaurants: " + filteredRestaurants.size(), Toast.LENGTH_SHORT).show();
            //add navigation back to home later
        });
    }

//    private void setupCheckbox(CheckBox checkBox, List<String> selectedFilter) {
//        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            // Handle checkbox state change
//            if (isChecked) {
//                selectedFilter.add(checkBox.getText().toString());
//            } else {
//                selectedFilter.remove(checkBox.getText().toString());
//            }
//        });
//    }

    private int updateRating(int currentRating, int rating, ImageView clickedStar, ImageView... stars) {
        // Toggle the clicked star and update UI
        if (currentRating == rating) {
            currentRating = 0;
        } else {
            currentRating = rating;
        }

        // Reset all stars to empty
        for (ImageView star : stars) {
            star.setImageResource(R.drawable.star_empty);
        }

        // Fill stars up to the selected rating
        for (ImageView star : stars) {
            if (Integer.parseInt(star.getTag().toString()) <= currentRating) {
                star.setImageResource(R.drawable.star_fill);
            }
        }

        return currentRating;
    }
    private void setRating(ImageView star, int rating, String category) {
        switch (category) {
            case "filter":
                overallStarRating = updateRating(overallStarRating, rating, star, filterStar1, filterStar2, filterStar3, filterStar4, filterStar5);
                viewModel.setFoodRating(overallStarRating);
                break;
            
        }
    }

    private List<Restaurant> applyFilters() {
        RealmQuery<Restaurant> query = realm.where(Restaurant.class);
        RealmResults<Restaurant> allRestaurants = query.findAll(); // Get all restaurants before applying filters

        // Log the initial size of the restaurant list
        Log.d("InitialData", "Initial number of restaurants: " + allRestaurants.size());
        // TODO: Problem: it seems like it is not pulling any data  "Initial number of restaurants: 0"



        // Cuisine filters: Checking if the 'Cuisine' string contains any of the selected cuisines
        List<String> selectedCuisines = new ArrayList<>();
        if (americanCheckbox.isChecked()) selectedCuisines.add("Western");
        if (asianCheckbox.isChecked()) selectedCuisines.add("Asian");
        if (asianFusionCheckbox.isChecked()) selectedCuisines.add("Asian Fusion");
        if(breakfastCheckbox.isChecked()) selectedCuisines.add("BreakFast");
        if(chineseCheckbox.isChecked()) selectedCuisines.add("Chinese");
        if(italianCheckbox.isChecked()) selectedCuisines.add("Italian");
        if(indianCheckbox.isChecked()) selectedCuisines.add("Indian");
        if(mexicanCheckbox.isChecked()) selectedCuisines.add("Mexican");
        if(thaiCheckbox.isChecked()) selectedCuisines.add("Thai");
        if(japaneseCheckBox.isChecked()) selectedCuisines.add("Japanese");


        // Adding query condition for cuisines
        if (selectedCuisines.size() > 0) {
            query.beginGroup();
            for (int i = 0; i < selectedCuisines.size(); i++) {
                if (i > 0) query.or(); // Only add 'or' condition if there's more than one cuisine selected
                query.contains("Cuisine", selectedCuisines.get(i));
            }
            query.endGroup();
        }

        // Dietary options filters
        List<String> selectedDietaryOptions = new ArrayList<>();
        if (glutenFreeCheckbox.isChecked()) selectedDietaryOptions.add("Gluten Free");
        if (halalCheckbox.isChecked()) selectedDietaryOptions.add("Halal");
        if(veganFriendlyCheckbox.isChecked()) selectedDietaryOptions.add("Vegan");
        if(vegetarianCheckbox.isChecked()) selectedDietaryOptions.add("Vegetarian");
        if(seafoodCheckBox.isChecked()) selectedDietaryOptions.add("Seafood");
        if(healthyCheckBox.isChecked()) selectedDietaryOptions.add("Healthy");

        // Adding query condition for dietary options
        if (selectedDietaryOptions.size() > 0) {
            query.beginGroup();
            for (int i = 0; i < selectedDietaryOptions.size(); i++) {
                if (i > 0) query.or(); // Only add 'or' condition if there's more than one dietary option selected
                query.contains("DietaryOptions", selectedDietaryOptions.get(i));
            }
            query.endGroup();
        }

        //Location Options filter
        List<String> selectedLocations= new ArrayList<>();
        if(centralCheckbox.isChecked()) selectedLocations.add("Central");
        if(NorthCheckBox.isChecked()) selectedLocations.add("North");
        if(NorthEastCheckBox.isChecked()) selectedLocations.add("North-East");
        if(WestCheckBox.isChecked()) selectedLocations.add("West");
        if(EastCheckBox.isChecked()) selectedLocations.add("East");

        if(selectedLocations.size()>0){
            query.beginGroup();
            for(int i=0; i< selectedLocations.size();i++){
                if(i>0) query.or();
                query.contains("Area",selectedLocations.get(i));
            }
        }


        // Overall star rating filter
        if (overallStarRating > 0) {
            query.greaterThanOrEqualTo("Ratings", (double) overallStarRating);
        }

        if (selectedPrice > 0) {
            query.beginGroup();
            for (int price = selectedPrice; price <= 300; price += 10) {
                String priceString = "$" + price;
                query.or().equalTo("PriceRange", priceString);
            }
            query.endGroup();
        }




        RealmResults<Restaurant> result = query.findAll();
        Log.d("FilterDebug", "Filtered Query Result Size: " + result.size());

        // Before returning the filtered list, log the size of the list to debug
        List<Restaurant> filteredList = realm.copyFromRealm(result);
        Log.d("FilterDebug", "Number of filtered restaurants: " + filteredList.size());




        return filteredList; // Convert RealmResults to List
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close(); // Close Realm instance to avoid memory leaks
    }

}