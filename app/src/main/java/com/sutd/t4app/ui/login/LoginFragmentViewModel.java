package com.sutd.t4app.ui.login;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.ui.signup.SignUpFragmentViewModel;
import com.sutd.t4app.utility.RealmUtility;

import org.bson.types.ObjectId;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;


@HiltViewModel
public class LoginFragmentViewModel extends ViewModel {
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> resultLauncher;
    private final App realmApp;
    Realm realm;
    private MutableLiveData<String> navigationTrigger = new MutableLiveData<>();

    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();


    @Inject
    public LoginFragmentViewModel(App realmApp) {
        this.realmApp = realmApp;
    }

    public LiveData<String> getNavigationTrigger() {
        return navigationTrigger;
    }

    private void loginProcess(String email, String password){
        // do a loginasync with the credentials provided by the user , email and pass, if error means doesnt exist or smth
        // if manage to login means that we can go next
        Credentials credentials = Credentials.emailPassword(email, password);
        realmApp.loginAsync(credentials, loginResult -> {
            if (loginResult.isSuccess()) {
                // The user is successfully logged in
                Log.d("Login Info","SUCCESS");
                initializeRealm();
            } else {
                Log.d("Login Info","FAILED" +  loginResult.getError().getErrorMessage());
            }

    });}

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {

                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {

                    @Override
                    public void onSuccess(Realm realm) {
                        LoginFragmentViewModel.this.realm = realm;
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
        RealmQuery<UserProfile> query = realm.where(com.sutd.t4app.data.model.UserProfile.class).equalTo("userId", user.getId());
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
                navigationTrigger.postValue("Home"); // need to go MAIN ACTIVITY
                // PROCEED TO BE QUESTIONED
            }, error -> {
                // Transaction failed and was automatically canceled.
                Log.e("UserProfile", "Error saving user profile", error);
            });
        }


    }

    private UserProfile createUserProfileFromForm() {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(email.getValue());
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
