package com.sutd.t4app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.databinding.FragmentHomeBinding;
import com.sutd.t4app.ui.ProfileQuestions.UserProfile;
import com.sutd.t4app.ui.ProfileQuestions.UserProfileViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private SwitchCompat pageSwitch;
    private ImageView filterIcon;
    private boolean isExplorePage = true; // initial is explore page
    private HomeFragmentViewModel viewModel;
    private RestaurantExploreAdapter adapter;
    private RestaurantExploreAdapter hotAdapter;

    private FragmentHomeBinding binding;

    private ImageView questionnaire;
    private UserProfileViewModel userProfileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Existing binding setup
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.i("test","running FRAGMENT ACTIVITY");

        // Initialize the RecyclerView and its adapter
        adapter = new RestaurantExploreAdapter(new ArrayList<>(), R.layout.restaurant_item );
        binding.recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRestaurants.setAdapter(adapter);

        hotAdapter = new RestaurantExploreAdapter(new ArrayList<>(), R.layout.restaurant_hot_item);
        binding.recyclerViewHot.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        binding.recyclerViewHot.setAdapter(hotAdapter);


        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
//        userProfileViewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        // Observe the LiveData from the ViewModel
        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            // Update the adapter with the list of restaurants
            Log.d("HomeFragment", "Number of restaurants received: " + restaurants.size());
            adapter.updateData(restaurants); // See note below about adapter
            hotAdapter.updateData(restaurants);

        });

//        // Observe Ranked LiveData from ViewModel
//        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), rankedRestaurants -> {
//            // Update adapter with ranked restaurants
//            adapter.updateData(rankedRestaurants);
//        });




        //TextView fuelPlus1Card = root.findViewById(R.id.FuelPlus1);

        /*
        fuelPlus1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("work?","yes working cliking");
                Navigation.findNavController(v).navigate(R.id.torestaurantfragment);
            }
        });
        */

        questionnaire= root.findViewById(R.id.questionsicon);
        questionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.toQuestionspage);
            }
        });

        filterIcon = root.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.tofilterfragment);
            }
        });

        pageSwitch = root.findViewById(R.id.pageSwitch);
        pageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showFeedPage();
                } else {
                    showExplorePage();
                }
            }
        });

        // Initially show the explore page
        showExplorePage();

        return root;
    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Fetch and rank restaurants when user profiles are available
//        userProfileViewModel.getUserProfilesLiveData().observe(getViewLifecycleOwner(), userProfiles -> {
//            if (!userProfiles.isEmpty()) {
//                UserProfile userProfile = userProfiles.get(2); // Use the third profile for simplicity
//                // TODO: 8/4/24 make it only for the current profile
//                viewModel.rankAndUpdateRestaurants(userProfile);;
//            }
//        });
//    }
//    private void fetchAndRankRestaurants(UserProfile userProfile) {
//        // Fetch unranked restaurants here (static list, database, API, etc.)
//        List<Restaurant> unrankedRestaurants = getUnrankedRestaurants(); // Implement this according to your app logic
//
//        // Rank restaurants based on user profile
//        viewModel.rankAndUpdateRestaurants(unrankedRestaurants, userProfile);
//    }


    private void showFeedPage() {
        isExplorePage = false;

        // Hide Explore layout and show Feed layout
        binding.exploreLayout.setVisibility(View.GONE);
        binding.feedLayout.setVisibility(View.VISIBLE);

    }

    private void showExplorePage() {
        isExplorePage = true;

        binding.feedLayout.setVisibility(View.GONE);
        binding.exploreLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}