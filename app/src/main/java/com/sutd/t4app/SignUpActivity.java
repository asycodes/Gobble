package com.sutd.t4app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputLayout;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.utility.RealmUtility;

import org.bson.types.ObjectId;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;


@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {
    @Inject
    App realmApp;

    private Realm realm;

    private RealmResults<UserProfile> realmResults;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditText firstName = findViewById(R.id.firstNameEditText);
        TextInputLayout firstNameInputLayout = findViewById(R.id.firstNameTextInputLayout);

        EditText lastName = findViewById(R.id.lastNameEditText);
        TextInputLayout lastNameTextInputLayout = findViewById(R.id.lastNameEditTextTextInputLayout);

        EditText email = findViewById(R.id.emailEditText);
        TextInputLayout emailTextInputLayout = findViewById(R.id.emailTextInputLayout);

        EditText pass = findViewById(R.id.passwordEditText);
        TextInputLayout passTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        Button submit = findViewById(R.id.signUpButton);

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameValue = firstName.getText().toString();
                String lastNameValue = lastName.getText().toString();
                String emailValue = email.getText().toString();
                String passValue = pass.getText().toString();
                boolean isValid = true; // default true

                // OKOK now we will need to do validation!!!!
                if (firstNameValue.isEmpty()) {
                    firstName.setError("First name is required");
                    isValid = false;
                }else{
                    firstName.setError(null);
                }

                if (lastNameValue.isEmpty()) {
                    lastName.setError("Last name is required");
                    isValid = false;
                }else{
                    lastName.setError(null);
                }
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                    // The email address is in a valid format.
                    emailTextInputLayout.setError(null);
                } else {
                    //
                    emailTextInputLayout.setError("Invalid email format");
                    isValid = false;

                }

                if (passValue.matches(passwordPattern)) {
                    // The password is considered strong.
                    passTextInputLayout.setError(null);
                } else {
                    // The password does not meet the criteria.
                    passTextInputLayout.setError("Password is WEAK");
                    isValid = false;

                }

                if (isValid) {
                    // All inputs are valid, proceed with the sign-up process
                    User user = realmApp.currentUser();
                    if (user!=null){
                        Log.d("signup check","all valid and user is NOT NULL");
                        // that means we already this fella in the database naturally coz we have log him in
                        // now we log out and use wtv he is providing
                        user.logOutAsync(result -> {
                            if (result.isSuccess()) {
                                Log.v("User", "Successfully logged out.");
                                // At this point, currentUser() will be null if there are no other users logged in.
                            } else {
                                Log.e("User", "Failed to log out, reason: " + result.getError());
                            }
                        });

                    }
                    // now we have made sure current user is null, we gonna sign up this dood
                    Log.d("signup check","all valid and user is NULL");
                    Log.d("email and pass ","email: " + emailValue +
                            "\n pass: " + passValue
                            );

                    realmApp.getEmailPassword().registerUserAsync(emailValue, passValue, result -> {
                        if (result.isSuccess()) {
                            // Step 2: Log in the user
                            Credentials credentials = Credentials.emailPassword(emailValue, passValue);
                            realmApp.loginAsync(credentials, loginResult -> {
                                if (loginResult.isSuccess()) {
                                    // The user is successfully logged in
                                    Log.d("SUCCESS","SUCCESS");
                                    initializeRealm();
                                } else {
                                    Log.d("FAILED","FAILED" +  result.getError().getErrorMessage());
                                }
                            });
                        } else {
                            Log.d("FAILED","FAILED" +  result.getError().getErrorMessage());

                        }
                    });




                    }

                    // do a check if theres existing user

                    // maybe do an email verification? not a must but itll be good
                    // captcha to prevent traffic abuse
                    // once that is done we create a userProfile
                    // go to profilequestion fragment
                    //
                }
            });
        }

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                Log.d("get instance","checking");

                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {

                    @Override
                    public void onSuccess(Realm realm) {
                        SignUpActivity.this.realm = realm;
                        Log.d("YourViewModel", "Realm instance has been initialized successfully.");
                        checkusers(); // check if the fella alr exist or not
                    }
                });
            }
            @Override
            public void onError(Exception e) {
                // Handle any errors, such as login failure
                Log.e("YourViewModel", "Error obtaining Realm configuration", e);
            }
        });
    }

    public void checkusers(){

            Log.v("CHECK 2", "we have called obsercerUserProfile ");
            User user = realmApp.currentUser();
            // Perform your Realm query
            RealmQuery<UserProfile> query = realm.where(UserProfile.class).equalTo("userId", user.getId());
            if (query.count() > 0) {
                Log.v("DB_CHECK", "User data exists.");
                // User data exists, proceed accordingly
            } else {
                Log.v("DB_CHECK", "User data does not exist.");
                // User data does not exist, create the user
                UserProfile userProfile = createUserProfileFromForm();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        UserProfile item = realm.createObject(UserProfile.class,new ObjectId());
                        item.setUserId(userProfile.getUserId());
                        item.setUsername(userProfile.getUsername());
                        item.setEmail(userProfile.getEmail());
                        item.setPassword(userProfile.getPassword());
                        Log.v("UserProfile", "Saving UserProfile with data: " +
                                "\nUsername: " + userProfile.getUsername() +
                                "\nEmail: " + userProfile.getEmail() +
                                "\nCuisine Preferences: " + userProfile.getUserId() +
                                "\nEmail: " + userProfile.getPassword())

                        ;
                    }
                }, () -> {
                    // Transaction was a success.
                    Log.v("UserProfile", "User profile saved successfully,");
                    // PROCEED TO BE QUESTIONED
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);

                }, error -> {
                    // Transaction failed and was automatically canceled.
                    Log.e("UserProfile", "Error saving user profile", error);
                });
            }


    }

    private UserProfile createUserProfileFromForm() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(realmApp.currentUser().getId());
        EditText email = findViewById(R.id.emailEditText);
        userProfile.setEmail(email.getText().toString());
        EditText firstname = findViewById(R.id.firstNameEditText);
        EditText lastname = findViewById(R.id.lastNameEditText);
        userProfile.setUsername(firstname.getText().toString() + " "+ lastname.getText().toString());
        EditText pass = findViewById(R.id.passwordEditText);
        userProfile.setPassword(pass.getText().toString());

        return userProfile;
    }


}
