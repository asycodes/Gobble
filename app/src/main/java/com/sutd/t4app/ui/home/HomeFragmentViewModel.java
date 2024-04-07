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

import java.util.List;

import javax.inject.Inject;

import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;
@HiltViewModel
public class HomeFragmentViewModel extends ViewModel {
    private TripAdvisorService tripAdvisorService;
    private YelpService yelpService;
    private List<YelpSearchResponse.Business> yelpresponse;
    private List<LocationSearchResponse.Location> searchresults;
    private MutableLiveData<List<LocationSearchResponse.Location>> results = new MutableLiveData<>();
    private final App realmApp;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private Realm realm;
    private RealmResults<Restaurant> realmResults;

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
                Realm.getInstanceAsync(RealmUtility.getDefaultSyncConfig(realmApp), new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        HomeFragmentViewModel.this.realm = realm;
                        Log.d("HomeFragmentViewModel", "Realm instance has been initialized successfully.");

                        // Perform your Realm query
                        realmResults = realm.where(Restaurant.class).findAllAsync();
                        Log.d("SyncCheck", "Data synced or updated: " + realmResults);


                        // Attach a listener to update LiveData when Realm results change
                        realmResults.addChangeListener(new RealmChangeListener<RealmResults<Restaurant>>() {
                            @Override
                            public void onChange(RealmResults<Restaurant> results) {
                                // This is automatically called on the main thread when data changes
                                Log.d("SyncCheck", "Data synced or updated: " + results.size());
                                // Detach the results from Realm and update LiveData
                                restaurantsLiveData.postValue(realm.copyFromRealm(results));
                            }
                        });
                    }
                });



    }
    public void getRestaurantsTrip() {
        // Example call

    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(realm != null) {
            realm.close();
            realm = null;
        }
    }
}