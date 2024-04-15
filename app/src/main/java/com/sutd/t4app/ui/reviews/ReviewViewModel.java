package com.sutd.t4app.ui.reviews;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.utility.RealmUtility;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.sync.SyncConfiguration;


@HiltViewModel
public class ReviewViewModel extends ViewModel {
    private final App realmApp;
    Realm realm;
    private MutableLiveData<String> searchText = new MutableLiveData<>();

    private MutableLiveData<Restaurant> restaurantSelected;
    private MutableLiveData<Integer> foodRating = new MutableLiveData<>(3);
    private MutableLiveData<Integer> serviceRating = new MutableLiveData<>(3);
    private MutableLiveData<Integer> atmosphereRating = new MutableLiveData<>(3);

    @Inject
    public ReviewViewModel(App realmApp) {
        this.realmApp = realmApp;
        initializeRealm();
    }

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        ReviewViewModel.this.realm = realm;
                        Log.d("HomeFragmentViewModel", "Realm instance has been initialized successfully.");
                        observeRestaurants(); // retrieve the exact restaurant based on the user selection
                        fetchUserProfiles(); // retrieve the exact userprofile based on the current login
                        // after retrieving both things, we upload the review to Review
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

    private void observeRestaurants() {
        if (realm != null) {
            // Perform your Realm query
            realmResults = realm.where(Restaurant.class).findAllAsync();
            realmResults.addChangeListener(new RealmChangeListener<RealmResults<Restaurant>>() {
                @Override
                public void onChange(RealmResults<Restaurant> results) {
                    // This is automatically called on the main thread when data changes
                    Log.d("SyncCheck", "Data synced or updated: " + results.size());
                    // Detach the results from Realm and update LiveData
                    if (realm != null && !realm.isClosed()) {
                        restaurantsLiveData.postValue(realm.copyFromRealm(results));
                    }
                }
            });
        }}

    private void fetchUserProfiles() {
        String currentUserId=realmApp.currentUser().getId();
        if (realm != null && currentUserId != null) {
            UserProfile userProfile = realm.where(UserProfile.class).equalTo("userId", currentUserId)
                    .findFirst();

            if (userProfile != null) {
                UserProfile detachedUserProfile = realm.copyFromRealm(userProfile);

                userProfilesLiveData.postValue(detachedUserProfile);
            } else {
                // Handle the case where the user profile is not found, e.g., post null or a default UserProfile object
                userProfilesLiveData.postValue(null);
            }
        }
    }


    public void updateSearchText(String newText) {
        searchText.setValue(newText);
    }

    public LiveData<Integer> getFoodRating() {
        return foodRating;
    }

    public LiveData<Integer> getServiceRating() {
        return serviceRating;
    }

    public LiveData<Integer> getAtmosphereRating() {
        return atmosphereRating;
    }

    public void setFoodRating(int rating) {
        foodRating.setValue(rating);
    }

    public void setServiceRating(int rating) {
        serviceRating.setValue(rating);
    }

    public void setAtmosphereRating(int rating) {
        atmosphereRating.setValue(rating);
    }
}