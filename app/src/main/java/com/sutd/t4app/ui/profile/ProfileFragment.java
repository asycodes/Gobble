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

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.mongodb.App;
import io.realm.mongodb.User;


@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    @Inject
    App realmApp;
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

                    Log.d("Log out", "Logged off from Google");
                });

        User user = realmApp.currentUser();
        if (user!=null){
            Log.d("signup check","all valid and user is NOT NULL");
            // that means we already this fella in the database naturally coz we have log him in
            // now we log out and use wtv he is providing
            user.logOutAsync(result -> {
                if (result.isSuccess()) {
                    Log.v("User", "Successfully logged out.");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().finish();  // Call finish() to close the current activity
                    startActivity(intent);
                    // At this point, currentUser() will be null if there are no other users logged in.
                } else {
                    Log.e("User", "Failed to log out, reason: " + result.getError());
                }
            });

        }

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}