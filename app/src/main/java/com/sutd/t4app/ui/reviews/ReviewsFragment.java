package com.sutd.t4app.ui.reviews;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sutd.t4app.R;
import com.sutd.t4app.databinding.FragmentDashboardBinding;
import java.io.IOException;
/**
 * The ReviewsFragment class in an Android app allows users to submit reviews with ratings and images.
 */
public class ReviewsFragment extends Fragment {

    private ReviewViewModel viewModel;
    private ImageView foodStar1, foodStar2, foodStar3, foodStar4, foodStar5;
    private ImageView serviceStar1, serviceStar2, serviceStar3, serviceStar4, serviceStar5;
    private ImageView atmosphereStar1, atmosphereStar2, atmosphereStar3, atmosphereStar4, atmosphereStar5;
    private int currentFoodRating = 0;
    private int currentServiceRating = 0;
    private int currentAtmosphereRating = 0;
    private Button uploadImageButton;
    private Uri imageUri;
    private ImageView selectedImage;
    private EditText reviews;
    private ActivityResultLauncher<String> imagePickerLauncher;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        uploadImageButton=root.findViewById(R.id.uploadimage);
        uploadImageButton.setOnClickListener(v -> openImageChooser());
        selectedImage=root.findViewById(R.id.selectedImage);
        reviews=root.findViewById(R.id.user_review);
        Button postreviewbutton= root.findViewById(R.id.post_review);
        postreviewbutton.setOnClickListener(v -> submitReview());



        // Initialize food rating stars
        foodStar1 = root.findViewById(R.id.foodStar1);
        foodStar2 = root.findViewById(R.id.foodStar2);
        foodStar3 = root.findViewById(R.id.foodStar3);
        foodStar4 = root.findViewById(R.id.foodStar4);
        foodStar5 = root.findViewById(R.id.foodStar5);

        // Set onClickListener for food stars
        foodStar1.setOnClickListener(v -> setRating(foodStar1, 1, "food"));
        foodStar2.setOnClickListener(v -> setRating(foodStar2, 2, "food"));
        foodStar3.setOnClickListener(v -> setRating(foodStar3, 3, "food"));
        foodStar4.setOnClickListener(v -> setRating(foodStar4, 4, "food"));
        foodStar5.setOnClickListener(v -> setRating(foodStar5, 5, "food"));

        // Initialize service rating stars
        serviceStar1 = root.findViewById(R.id.serviceStar1);
        serviceStar2 = root.findViewById(R.id.serviceStar2);
        serviceStar3 = root.findViewById(R.id.serviceStar3);
        serviceStar4 = root.findViewById(R.id.serviceStar4);
        serviceStar5 = root.findViewById(R.id.serviceStar5);

        // Set onClickListener for service stars
        serviceStar1.setOnClickListener(v -> setRating(serviceStar1, 1, "service"));
        serviceStar2.setOnClickListener(v -> setRating(serviceStar2, 2, "service"));
        serviceStar3.setOnClickListener(v -> setRating(serviceStar3, 3, "service"));
        serviceStar4.setOnClickListener(v -> setRating(serviceStar4, 4, "service"));
        serviceStar5.setOnClickListener(v -> setRating(serviceStar5, 5, "service"));

        // Initialize atmosphere rating stars
        atmosphereStar1 = root.findViewById(R.id.atmosphereStar1);
        atmosphereStar2 = root.findViewById(R.id.atmosphereStar2);
        atmosphereStar3 = root.findViewById(R.id.atmosphereStar3);
        atmosphereStar4 = root.findViewById(R.id.atmosphereStar4);
        atmosphereStar5 = root.findViewById(R.id.atmosphereStar5);

        // Set onClickListener for atmosphere stars
        atmosphereStar1.setOnClickListener(v -> setRating(atmosphereStar1, 1, "atmosphere"));
        atmosphereStar2.setOnClickListener(v -> setRating(atmosphereStar2, 2, "atmosphere"));
        atmosphereStar3.setOnClickListener(v -> setRating(atmosphereStar3, 3, "atmosphere"));
        atmosphereStar4.setOnClickListener(v -> setRating(atmosphereStar4, 4, "atmosphere"));
        atmosphereStar5.setOnClickListener(v -> setRating(atmosphereStar5, 5, "atmosphere"));

        return root;
    }

    private void openImageChooser() {
        // Launch the image picker using the Activity Result API
        imagePickerLauncher.launch("image/*");
    }

    private void setRating(ImageView star, int rating, String category) {
        switch (category) {
            case "food":
                currentFoodRating = updateRating(currentFoodRating, rating, star, foodStar1, foodStar2, foodStar3, foodStar4, foodStar5);
                viewModel.setFoodRating(currentFoodRating);
                break;
            case "service":
                currentServiceRating = updateRating(currentServiceRating, rating, star, serviceStar1, serviceStar2, serviceStar3, serviceStar4, serviceStar5);
                viewModel.setServiceRating(currentServiceRating);
                break;
            case "atmosphere":
                currentAtmosphereRating = updateRating(currentAtmosphereRating, rating, star, atmosphereStar1, atmosphereStar2, atmosphereStar3, atmosphereStar4, atmosphereStar5);
                viewModel.setAtmosphereRating(currentAtmosphereRating);
                break;
        }
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

    private void submitReview() {
        // Calculate average rating
        double averageRating = (currentFoodRating + currentServiceRating + currentAtmosphereRating) / 3.0;

        // Get review text
        String reviewText = reviews.getText().toString().trim();

        // TODO: 23/3/24 store and process averagerating, imageURI and reviews to save to database 
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ActivityResultLauncher for image picking
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        selectedImage.setImageURI(imageUri);
                    }
                });
    }



}
