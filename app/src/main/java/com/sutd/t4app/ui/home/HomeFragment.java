package com.sutd.t4app.ui.home;
/*
 * The HomeFragment class in an Android app displays a list of restaurants and allows users to switch
 * between explore and feed pages.
 */
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
import com.sutd.t4app.ui.filter.FilterViewModel;

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
    private FilterViewModel filterViewModel;

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
        filterViewModel= new ViewModelProvider(requireActivity()).get(FilterViewModel.class);


        // Observe the LiveData from the ViewModel
        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            Log.d("HomeFragment", "Number of unranked restaurants received: " + restaurants.size());
            // Use this data for something that doesn't need ranked data, for example, for `hotAdapter`
            if (restaurants.size() >= 1) {
                List<Restaurant> hotRestaurants = restaurants.subList(0, Math.min(2, restaurants.size()));
                hotAdapter.updateData(hotRestaurants);
            }
        });

        // Observe the LiveData for ranked restaurants
        viewModel.getRankedRestaurantsLiveData().observe(getViewLifecycleOwner(), rankedRestaurants -> {

            if (rankedRestaurants.size() >= 1) {
                List<Restaurant> hotRestaurants = rankedRestaurants.subList(0, Math.min(2, rankedRestaurants.size()));
            // Update the adapter with the ranked list of restaurants
                 Log.d("HomeFragment", "Number of ranked restaurants received: " + rankedRestaurants.size());
                 adapter.updateData(rankedRestaurants);}
        });


        questionnaire= root.findViewById(R.id.questionsicon);
        questionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.cleanUp();
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
    private void triggerRanking() {
        if (!viewModel.getRestaurantsLiveData().getValue().isEmpty() && viewModel.getUserProfilesLiveData().getValue() != null) {
            viewModel.rankAndUpdateRestaurants(viewModel.getUserProfilesLiveData().getValue());
        }
    }
    private void fetchData(){
        viewModel.fetchRestaurantandUser();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            if (!restaurants.isEmpty()) {
                triggerRanking();
            }
        });

        viewModel.getUserProfilesLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                // Use the user profile to rank restaurants, for example
                viewModel.rankAndUpdateRestaurants(userProfile);
            }
        });

        // Observe LiveData for ranked restaurants
        viewModel.getRankedRestaurantsLiveData().observe(getViewLifecycleOwner(), rankedRestaurants -> {
            // Update the adapter with the ranked list of restaurants
            Log.d("HomeFragment", "Ranked restaurants updated.");
            adapter.updateData(rankedRestaurants);
        });

        filterViewModel.getFilteredRestaurantsLiveData().observe(getViewLifecycleOwner(),filteredRestaurants ->{
            // Update the adapter with the filtered list of restaurants
            if (filteredRestaurants != null) {
                Log.d("HomeFragmentFilterWidget", "Filtered restaurants received: " + filteredRestaurants.size());
                adapter.updateData(filteredRestaurants);
            }

        });


    }
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