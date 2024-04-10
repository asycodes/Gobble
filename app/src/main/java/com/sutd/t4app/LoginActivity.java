package com.sutd.t4app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.auth.GoogleAuthType;
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject App realmApp;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> resultLauncher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sign_in);
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("608019695893-le4ojn1imiute9040pj9mulgnhe6gkjt.apps.googleusercontent.com").requestEmail()
//                .build();
//        GoogleSignInAccount googleSignInAcc = GoogleSignIn.getLastSignedInAccount(this);
//        // check if theres any existing account previously
//        if(googleSignInAcc != null){
//            Log.d("we go next", "not null");
//            onLoginSuccess();
//        }
//        googleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        resultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    Log.d("LoginActivity", "ActivityResultLauncher triggered with result code: " + result.getResultCode());
//
//
//                    if(result.getResultCode() == -1){
//                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
//                        handleSignInResult(task);
//                    }
//
//                    else{
//                        // TODO: what happens if its not -1
//
//                    }
//
//
//                });
//
//        findViewById(R.id.sign_in_button).setOnClickListener(view -> signInWithGoogle());
        onLoginSuccess();
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        // Launch the intent using the registered launcher
        resultLauncher.launch(signInIntent);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            if (completedTask.isSuccessful()) {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                final String getFullName = account.getDisplayName();
                final String getEmail = account.getEmail();
                Log.d("NAME", ""+getFullName);
                Log.d("EMAIL", ""+getEmail);

                String token = account.getIdToken();
                Log.d("CEHCKING1","" +token);
                Credentials creds = Credentials.jwt(token);
                this.realmApp.loginAsync(creds, it -> {
                    if (it.isSuccess()) {
                        Log.v("AUTH",
                                "Successfully logged in to MongoDB Realm using Google OAuth.");
                        onLoginSuccess();
                    } else {
                        Log.e("AUTH",
                                "Failed to log in to MongoDB Realm: ", it.getError());
                    }
                });
            } else {
                Log.e("AUTH", "Google Auth failed: "
                        + completedTask.getException().toString());
            }
        } catch (ApiException e) {
            Log.w("AUTH", "Failed to log in with Google OAuth: " + e.getMessage());
        }
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the LoginActivity so the user can't go back to it
    }

    private void proceedToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

}
