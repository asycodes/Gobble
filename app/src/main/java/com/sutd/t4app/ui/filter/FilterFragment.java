package com.sutd.t4app.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.slider.Slider;
import com.sutd.t4app.R;
import com.sutd.t4app.databinding.FilterBottomUpBinding;
import com.sutd.t4app.ui.reviews.ReviewViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    private CheckBox topRatedCheckbox, recommendedCheckbox, nearMeCheckbox;
    private CheckBox americanCheckbox, asianCheckbox, asianFusionCheckbox, breakfastCheckbox, chineseCheckbox;
    private CheckBox glutenFreeCheckbox, halalCheckbox, veganFriendlyCheckbox, vegetarianCheckbox;
    private Slider priceSlider;
    private ImageView filterStar1, filterStar2, filterStar3, filterStar4, filterStar5;
    private Button showResultsButton;

    private List<String> selectedFilter = new ArrayList<>();
    private int selectedPrice = 0;
    private int overallStarRating = 0;
    private ReviewViewModel viewModel;
    private FilterBottomUpBinding binding;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FilterBottomUpBinding binding= FilterBottomUpBinding.inflate(inflater,container,false);
        View root= binding.getRoot();

        // Inflate the layout for this fragment

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Initialize views
        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        topRatedCheckbox = view.findViewById(R.id.topRatedCheckbox);
        recommendedCheckbox = view.findViewById(R.id.RecommendedCheckbox);
        nearMeCheckbox = view.findViewById(R.id.nearmeCheckbox);
        americanCheckbox = view.findViewById(R.id.AmericanCheckbox);
        asianCheckbox = view.findViewById(R.id.AsianCheckbox);
        asianFusionCheckbox = view.findViewById(R.id.AsianFusionCheckbox);
        breakfastCheckbox = view.findViewById(R.id.BreakfastCheckbox);
        chineseCheckbox = view.findViewById(R.id.ChineseCheckbox);
        glutenFreeCheckbox = view.findViewById(R.id.GlutenFreeCheckbox);
        halalCheckbox = view.findViewById(R.id.HalalCheckbox);
        veganFriendlyCheckbox = view.findViewById(R.id.VeganFriendlyCheckbox);
        vegetarianCheckbox = view.findViewById(R.id.VegetarianCheckbox);
        priceSlider = view.findViewById(R.id.priceSlider);
        filterStar1 = view.findViewById(R.id.filterStar1);
        filterStar2 = view.findViewById(R.id.filterStar2);
        filterStar3 = view.findViewById(R.id.filterStar3);
        filterStar4 = view.findViewById(R.id.filterStar4);
        filterStar5 = view.findViewById(R.id.filterStar5);
        showResultsButton = view.findViewById(R.id.showResultsButton);

        // Set onClickListener for checkboxes
        setupCheckbox(topRatedCheckbox, selectedFilter);
        setupCheckbox(recommendedCheckbox, selectedFilter);
        setupCheckbox(nearMeCheckbox, selectedFilter);
        setupCheckbox(americanCheckbox, selectedFilter);
        setupCheckbox(asianCheckbox, selectedFilter);
        setupCheckbox(asianFusionCheckbox, selectedFilter);
        setupCheckbox(breakfastCheckbox, selectedFilter);
        setupCheckbox(chineseCheckbox, selectedFilter);
        setupCheckbox(glutenFreeCheckbox, selectedFilter);
        setupCheckbox(halalCheckbox, selectedFilter);
        setupCheckbox(vegetarianCheckbox, selectedFilter);
        setupCheckbox(veganFriendlyCheckbox, selectedFilter);

        // Set listener for price slider
        priceSlider.addOnChangeListener((slider, value, fromUser) -> selectedPrice = (int) value);

        // Set onClickListener for star rating
        filterStar1.setOnClickListener(v -> setRating(filterStar1, 1, "filter"));
        filterStar2.setOnClickListener(v -> setRating(filterStar2, 2, "filter"));
        filterStar3.setOnClickListener(v -> setRating(filterStar3, 3, "filter"));
        filterStar4.setOnClickListener(v -> setRating(filterStar4, 4, "filter"));
        filterStar5.setOnClickListener(v -> setRating(filterStar5, 5, "filter"));

        // Set onClickListener for show results button
        showResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_home);

            }
            // TODO: 22/3/24 LINK TO THE EXPLORE PAGE ALGORITHM
        });
    }

    private void setupCheckbox(CheckBox checkBox, List<String> selectedFilter) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle checkbox state change
            if (isChecked) {
                selectedFilter.add(checkBox.getText().toString());
            } else {
                selectedFilter.remove(checkBox.getText().toString());
            }
        });
    }

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
}
