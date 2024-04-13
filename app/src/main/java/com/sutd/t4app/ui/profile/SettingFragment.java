package com.sutd.t4app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sutd.t4app.databinding.EditProfileBinding;
import com.sutd.t4app.databinding.ProfileSettingsBinding;

public class SettingFragment extends Fragment {
    private ProfileSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ProfileSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
}
