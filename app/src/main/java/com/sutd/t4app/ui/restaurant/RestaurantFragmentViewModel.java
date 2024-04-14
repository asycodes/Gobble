package com.sutd.t4app.ui.restaurant;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.data.api.TripAdvisorService;
import com.sutd.t4app.data.api.YelpService;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.data.model.apiresponses.LocationSearchResponse;
import com.sutd.t4app.data.model.apiresponses.YelpSearchResponse;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.utility.RealmUtility;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.sync.SyncConfiguration;

@HiltViewModel
public class RestaurantFragmentViewModel extends ViewModel {
    private final App realmApp;
    private final MutableLiveData<List<Review>> reviewsLiveData = new MutableLiveData<>();
    private Restaurant currRes;
    private Realm realm;
    private TripAdvisorService tripAdvisorService;
    private YelpService yelpService;
    private LocationSearchResponse.Location Tripsearchresults;
    private YelpSearchResponse.Business Yelpsearchresults;

    @Inject
    public RestaurantFragmentViewModel(App realmApp, TripAdvisorService tripadvisorService, YelpService yelpservice) {
        this.tripAdvisorService = tripadvisorService;
        this.yelpService = yelpservice;
        this.realmApp = realmApp;

    }

    public void setcurrRes(Restaurant res){
        this.currRes = res;
        initializeRealm();

    }

    public LiveData<List<Review>> getreviewsLiveData(){
        return reviewsLiveData;
    }

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        RestaurantFragmentViewModel.this.realm = realm;
                        Log.d("RestaurantFragmentViewModel", "Realm instance has been initialized successfully.");
                        gatherreviews(); // Observes data and updates LiveData
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

    public void gatherreviews(){
        // after initialisation check if there are any existing reviews from realm of the particular res
        if (realm != null ) {
            RealmResults<Review> reviews = realm.where(Review.class).equalTo("RestaurantId", this.currRes.getRestaurantId()).findAllAsync();

            if (reviews != null) {
                 this.reviewsLiveData.postValue(realm.copyFromRealm(reviews));

            }
            // after showing OR if null, gather new reviews from Yelp or Tripadvisor,

            // but first need to know if the res has the id from both yelp and tripadvisor
            String tripID = this.currRes.getTripAdvisorId();
            String yelpID = this.currRes.getYelpId();
            if(tripID == ""){
                String tripkey = BuildConfig.TRIP_API;
                String latlong = this.currRes.getLat() + "," +  this.currRes.getLng();
                tripAdvisorService.searchLocation(this.currRes.getName(), "restaurant", "Singapore",""+ latlong ,"en" ,tripkey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            this.Tripsearchresults = result.getData().get(0);
                            String tripid = this.Tripsearchresults.getLocation_id();
                            this.currRes.setTripAdvisorId(tripid);
                            // now we do a realm transaction to update the id
                            realm.executeTransactionAsync(r -> {
                            Restaurant restaurant = r.where(Restaurant.class).equalTo("RestaurantId", this.currRes.getId()).findFirst();
                            if (restaurant != null) {
                                restaurant.setTripAdvisorId(tripid);
                            }
                        }, () -> {
                            Log.d("Realm", "Trip ID updated successfully!");
                        }, error -> {
                            Log.e("Realm Error", "Failed to update Trip ID: " + error.getMessage());
                        });

                            Log.d("FIRST TRIP SEARCH RESULTS", "" + this.Tripsearchresults);
                        }, throwable -> {
                            // Handle error
                            Log.d("TripAdvisor Error", "Error occurred: " + throwable.getMessage());
                        });
            }
            if(yelpID == ""){
                String yelpkey = BuildConfig.YELP_API;
                double latitude = Double.parseDouble(this.currRes.getLat());
                double longti = Double.parseDouble(this.currRes.getLng());
                yelpService.searchBusinesses(yelpkey, "Singapore", latitude,longti ,this.currRes.getName())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            this.Yelpsearchresults = result.getBusinesses().get(0);
                            String yelpid = this.Yelpsearchresults.getId();
                            this.currRes.setYelpId(yelpid);
                            realm.executeTransactionAsync(r -> {
                                Restaurant restaurant = r.where(Restaurant.class).equalTo("RestaurantId", this.currRes.getId()).findFirst();
                                if (restaurant != null) {
                                    restaurant.setYelpId(yelpid);
                                }
                            }, () -> {
                                Log.d("Realm", "Yelp ID updated successfully!");
                            }, error -> {
                                Log.e("Realm Error", "Failed to update Yelp ID: " + error.getMessage());
                            });
                            Log.d("FIRST YELP SEARCH RESULTS", "" + Yelpsearchresults);
                        }, throwable -> {
                            // Handle error
                        });
            }
            // once they have their own IDs, we find the reviews from each service

            try {
                int tripAdvisorId = Integer.parseInt(this.currRes.getTripAdvisorId());
                tripAdvisorService.getReviews(tripAdvisorId,)
            } catch (NumberFormatException e) {
                System.out.println("Error converting to integer: " + e.getMessage());
            }



        }


        // if STILL NULL, then ok, just show a "empty reviews"
    }
    }
