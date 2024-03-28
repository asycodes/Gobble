package com.sutd.t4app.ui.home;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.myApp;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

import java.util.List;

import javax.inject.Inject;

import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;
@HiltViewModel
public class HomeFragmentViewModel extends ViewModel {

    private final App realmApp;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private Realm realm;
    private RealmResults<Restaurant> realmResults;

    @Inject
    public HomeFragmentViewModel(App realmApp) {
        myApp.getAppComponent().inject(this);
        this.realmApp = realmApp;
        Credentials credentials = Credentials.anonymous();

        this.realmApp.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                User user = this.realmApp.currentUser();
                Log.v("QUICKSTART", "Successfully authenticated anonymously.");
                SyncConfiguration config2 = new SyncConfiguration.Builder(
                        user).initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                            @Override
                            public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                // add a subscription with a name
                                boolean ressubscriptionExists = false;
                                for (Subscription existingSubscription : subscriptions) {
                                    if ("restaurantsSubscription".equals(existingSubscription.getName())) {
                                        ressubscriptionExists = true;
                                        break;
                                    }
                                }

                                if(!ressubscriptionExists){
                                    subscriptions.add(Subscription.create("restaurantsSubscription",
                                            realm.where(Restaurant.class)));
                                }

                            }
                        })
                        .build();

                // realmApp is the same app from myApp.
                // now we create a new configuration


                // Initialize the Realm instance asynchronously
                Realm.getInstanceAsync(config2, new Realm.Callback() {
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


            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
            }
        });

    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Remove the change listener
        if (realmResults != null) {
            realmResults.removeAllChangeListeners();
        }
        // Close the Realm instance
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}