package com.sutd.t4app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sutd.t4app.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.mongodb.App;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private HomeFragmentViewModel viewModel;
    private RestaurantExploreAdapter adapter;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Existing binding setup
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.i("test","running FRAGMENT ACTIVITY");

        // Initialize the RecyclerView and its adapter
        adapter = new RestaurantExploreAdapter(new ArrayList<>());
        binding.recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRestaurants.setAdapter(adapter);

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        // Observe the LiveData from the ViewModel
        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            // Update the adapter with the list of restaurants
            adapter.updateData(restaurants); // See note below about adapter

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}