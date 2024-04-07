package com.sutd.t4app.ui.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.databinding.FragmentDashboardBinding;
import com.sutd.t4app.databinding.FragmentRestuarantProfileBinding;

public class RestaurantFragmentActivity extends Fragment {
    private FragmentRestuarantProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentRestuarantProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle arguments = getArguments();
        String value = null;
        if (arguments != null) {
            Restaurant restaurant = arguments.getParcelable("restaurant");
            TextView restaurantNameTextView = root.findViewById(R.id.restaurantName);
            restaurantNameTextView.setText(restaurant.getName());
        }

        // button compare instantiation
        Button btnCompare = root.findViewById(R.id.compareButton);
        
        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add your code here to be executed when the button is clicked
                Navigation.findNavController(v).navigate(R.id.compare_fragment);
            }
        });
        //add onClickListener to btnCompare


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
