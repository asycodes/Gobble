package com.sutd.t4app.ui.restaurant;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sutd.t4app.R;

public class CompareFragment extends Fragment {

    private CompareViewModel mViewModel;

    public static CompareFragment newInstance() {
        return new CompareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_compare, container, false);
        TextView llmText = root.findViewById(R.id.llmOutput);


        //call openAI API - add prompt "How are you"


        //update llmText with the response of openAI
        llmText.setText("abc");


        //set the value of llmOutput to "abc"
        //TextView llmText = root.findViewById(R.id.restaurantName);
        
        //instantiate TextView llmText



        return root;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        // TODO: Use the ViewModel
    }

}