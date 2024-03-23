package com.sutd.t4app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.sutd.t4app.R;
import com.sutd.t4app.databinding.FragmentHomeBinding;
import com.sutd.t4app.ui.feed.FeedFragment;
import com.sutd.t4app.ui.filter.FilterFragment;

public class HomeFragmentActivity extends Fragment {

    private FragmentHomeBinding binding;
    private SwitchCompat pageSwitch;
    private ImageView filterIcon;
    private boolean isExplorePage= true; // initial is explore page

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView fuelPlus1Card= root.findViewById(R.id.FuelPlus1);

        fuelPlus1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.torestaurantfragment);
            }
        });

        filterIcon = root.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.tofilterfragment);
            }
        });

        pageSwitch= root.findViewById(R.id.pageSwitch);
//        pageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    showFeedPage();
//                }else{
//                    showExplorePage();
//                }
//            }
//        });
//
//        // Initially show the explore page
//        showExplorePage();


        return root;



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//    private void showFeedPage() {
//        isExplorePage=false;
//        // Inflate the feed page layout
////        View feedPageView = LayoutInflater.from(requireContext()).inflate(R.layout.feed_page, null);
//
//        replaceContent(new FeedFragment());
//    }
//
//    private void showExplorePage() {
//        isExplorePage = true;
//        // Inflate the explore page layout
////        View explorePageView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home, null);
//
//        // Replace the content in the FrameLayout with the explore page layout
//        replaceContent(new HomeFragmentActivity());
//    }
//
//    private void replaceContent(Fragment fragment) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragmentContainer, fragment);
//        transaction.commit();}


}