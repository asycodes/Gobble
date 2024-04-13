package com.sutd.t4app.ui.signup;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sutd.t4app.MainActivity;
import com.sutd.t4app.R;
import com.sutd.t4app.SignUpActivity;
import com.sutd.t4app.data.api.TripAdvisorService;
import com.sutd.t4app.data.api.YelpService;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.utility.FormValidation;
import com.sutd.t4app.utility.RealmUtility;

import org.bson.types.ObjectId;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import com.sutd.t4app.data.model.UserProfile;
import io.realm.mongodb.sync.SyncConfiguration;

// this viewmodel will focus on realm transactions, as well as verification


// Create the mutable live data varibales for all the user details
// then send setters and getters.
// once the signUpFragment does the onclick, we do the setters for each variables
// within the setters we do validation
@HiltViewModel
public class SignUpFragmentViewModel extends ViewModel {
    private MutableLiveData<String> firstName = new MutableLiveData<>();
    private MutableLiveData<String> lastName = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();


    // All the error variables
    private MutableLiveData<String> emailError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<String> firstNameError = new MutableLiveData<>();
    private MutableLiveData<String> lastnameError = new MutableLiveData<>();
    private MutableLiveData<UserProfile> userDetails = new MutableLiveData<>();

    private MutableLiveData<String> navigationTrigger = new MutableLiveData<>();
    private final App realmApp;
    Realm realm;
    @Inject
    public SignUpFragmentViewModel(App realmApp) {
        this.realmApp = realmApp;

    }
    public LiveData<String> getEmailError() { return emailError; }
    public LiveData<String> getPasswordError() { return passwordError; }
    public LiveData<String> getFirstNameError() { return firstNameError; }
    public LiveData<String> getLasttNameError() { return lastnameError; }
    public LiveData<UserProfile> getUserDetails() { return userDetails; }
    public LiveData<String> getNavigationTrigger() {
        return navigationTrigger;
    }

    public void validateAndSave(String firstname, String lastname, String email, String password){
        boolean isValid = true;
        String lastNameValidationResult = FormValidation.validateFirstName(firstname);
        String firstNameValidationResult = FormValidation.validateLastName(lastname);
        String emailValidationResult = FormValidation.validateEmail(email);
        String passwordValidationResult = FormValidation.validatePass(password);

        firstNameError.setValue(firstNameValidationResult);
        lastnameError.setValue(lastNameValidationResult);
        emailError.setValue(emailValidationResult);
        passwordError.setValue(passwordValidationResult);

        if (firstNameError != null || emailValidationResult != null || passwordValidationResult != null || lastnameError!= null) {
            isValid = false;
        }

        if (isValid) {
            realmApp.getEmailPassword().registerUserAsync(email, password, result -> {
                if (result.isSuccess()) {
                    Log.d("Register Info","SUCCESS");
                    Credentials credentials = Credentials.emailPassword(email, password);
                    realmApp.loginAsync(credentials, loginResult -> {
                        if (loginResult.isSuccess()) {
                            // The user is successfully logged in
                            Log.d("Login Info","SUCCESS");
                            initializeRealm();
                        } else {
                            Log.d("Login Info","FAILED" +  loginResult.getError().getErrorMessage());
                        }
                    });
                } else {
                    Log.d("Register Info","FAILED" +  result.getError().getErrorMessage());

                }
            });
        }
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
                        SignUpFragmentViewModel.this.realm = realm;
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
        RealmQuery<com.sutd.t4app.data.model.UserProfile> query = realm.where(com.sutd.t4app.data.model.UserProfile.class).equalTo("userId", user.getId());
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

                    com.sutd.t4app.data.model.UserProfile item = realm.createObject(com.sutd.t4app.data.model.UserProfile.class,new ObjectId());
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
                realm.close();
                navigationTrigger.postValue("Profile");
                // PROCEED TO BE QUESTIONED
            }, error -> {
                // Transaction failed and was automatically canceled.
                Log.e("UserProfile", "Error saving user profile", error);
            });
        }


    }

    private UserProfile createUserProfileFromForm() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(realmApp.currentUser().getId());
        userProfile.setEmail(email.getValue());
        userProfile.setUsername(firstName.getValue() + lastName.getValue());
        userProfile.setPassword(password.getValue());
        return userProfile;
    }

    protected void cleanUp() {
        if(realm != null) {
            Log.d("CLOSE REALM", "it is closed");
            realm.close();
            realm = null;
        }
    }


}
