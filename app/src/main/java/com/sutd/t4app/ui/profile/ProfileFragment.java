package com.sutd.t4app.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.sutd.t4app.LoginActivity;
import com.sutd.t4app.MainActivity;
import com.sutd.t4app.R;
import com.sutd.t4app.SignUpActivity;
import com.sutd.t4app.databinding.FragmentNotificationsBinding;

public class ProfileFragment extends Fragment {
    private GoogleSignInClient mGoogleSignInClient;

    private FragmentNotificationsBinding binding;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button logout = root.findViewById(R.id.Logout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("608019695893-le4ojn1imiute9040pj9mulgnhe6gkjt.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        Log.d("work?","Profile page");


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("work?","yes working cliking");
                signOut();
            }
        });




        return root;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    // Sign-out was successful, redirect to LoginActivity
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();  // Call finish() to close the current activity
                });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}