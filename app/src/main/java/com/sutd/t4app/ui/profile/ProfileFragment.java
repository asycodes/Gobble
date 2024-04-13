package com.sutd.t4app.ui.profile;
/**
 * The ProfileFragment class in an Android app handles user profile information and includes
 * functionality for logging out using Google Sign-In.
 */
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

import java.util.Set;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.mongodb.App;


@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    @Inject
    App realmApp;
    private GoogleSignInClient mGoogleSignInClient;

    private FragmentNotificationsBinding binding;
    private Button questions;
    private Button EditProfile;
    private Button Settings;




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

        questions=root.findViewById(R.id.ProfileQuestions);
        questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.toQuestionspage);
            }
        });
        EditProfile=root.findViewById(R.id.editprofilebutton);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.toeditProfile);
            }
        });

        Settings=root.findViewById(R.id.Settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.tosettings);
            }
        });




        return root;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    // Sign-out was successful, redirect to LoginActivity
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().finish();  // Call finish() to close the current activity
                    startActivity(intent);

                });

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}