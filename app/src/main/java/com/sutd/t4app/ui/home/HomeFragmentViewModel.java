package com.sutd.t4app.ui.home;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dagger.hilt.android.lifecycle.HiltViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import com.sutd.t4app.data.api.TripAdvisorService;
import com.sutd.t4app.data.api.YelpService;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.apiresponses.LocationSearchResponse;
import com.sutd.t4app.data.model.apiresponses.YelpSearchResponse;
import com.sutd.t4app.myApp;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.ui.ProfileQuestions.UserProfile;
import com.sutd.t4app.utility.RealmUtility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;
@HiltViewModel
/*
 * The `HomeFragmentViewModel` class in the Android app handles data retrieval from APIs, Realm
 * database initialization, and restaurant ranking based on user profiles.
 */
public class HomeFragmentViewModel extends ViewModel {
    private MutableLiveData<UserProfile> userProfilesLiveData = new MutableLiveData<>();
    private TripAdvisorService tripAdvisorService;
    private YelpService yelpService;
    private List<YelpSearchResponse.Business> yelpresponse;
    private List<LocationSearchResponse.Location> searchresults;
    private MutableLiveData<List<LocationSearchResponse.Location>> results = new MutableLiveData<>();
    private final App realmApp;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private Realm realm;
    private RealmResults<Restaurant> realmResults;
    private final MutableLiveData<List<Restaurant>> rankedRestaurantsLiveData = new MutableLiveData<>();

    @Inject
    public HomeFragmentViewModel(App realmApp, TripAdvisorService tripadvisorService, YelpService yelpservice) {
        this.tripAdvisorService = tripadvisorService;
        String tripkey = BuildConfig.TRIP_API;
        tripAdvisorService.searchLocation("Al-Azhar", "restaurant", "Singapore","1.343433928826536, 103.77529447421927" ,"en" ,tripkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    this.searchresults = result.getData();
                    Log.d("SEARCH RESULTS", "" + this.searchresults);
                }, throwable -> {
                    // Handle error
                });
        String yelpkey = BuildConfig.YELP_API;
        this.yelpService = yelpservice;
        yelpService.searchBusinesses(yelpkey, "Singapore", 1.3431765075310407,103.77512281284234 ,"Al-Azhar")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d("SEARCH RESULTS", "" + result);
                }, throwable -> {
                    // Handle error
                });
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
                        HomeFragmentViewModel.this.realm = realm;
                        Log.d("HomeFragmentViewModel", "Realm instance has been initialized successfully.");
                        observeRestaurants(); // Observes data and updates LiveData
                        fetchUserProfiles();
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
                    restaurantsLiveData.postValue(realm.copyFromRealm(results));
                }
            });
        }}

    private void fetchUserProfiles() {
        String currentUserId="bshfbefnwoef212100001";
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
    public LiveData<UserProfile> getUserProfilesLiveData() {
        return userProfilesLiveData;
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }
    public void rankAndUpdateRestaurants(UserProfile userProfile) {
        // Use getRestaurantsLiveData().getValue() to get the current list of restaurants
        List<Restaurant> unrankedRestaurants = getRestaurantsLiveData().getValue();
        if (unrankedRestaurants == null) {
            unrankedRestaurants = new ArrayList<>(); // Handle null case, possibly by re-fetching or showing an error
        }

        RestaurantRanking ranking = new RestaurantRanking();
        List<RestaurantScore> scores= ranking.rankRestaurants(unrankedRestaurants,userProfile);
        RestaurantRanker ranker = new RestaurantRanker();
        for (RestaurantScore score : scores) {
            ranker.addRestaurantScore(score);
        }

        // Get the sorted restaurants
        List<Restaurant> rankedRestaurants = ranker.getRankedRestaurants();
        rankedRestaurantsLiveData.postValue(rankedRestaurants);
    }

    protected void cleanUp() {
        if(realm != null) {
            Log.d("CLOSE REALM", "it is closed");
            realm.close();
            realm = null;
        }
    }
}