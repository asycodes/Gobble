package com.sutd.t4app.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sutd.t4app.data.model.Restaurant;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.realm.Realm;
import io.realm.mongodb.App;


@HiltViewModel
public class ReviewViewModel extends ViewModel {
    private final App realmApp;
    Realm realm;

    private MutableLiveData<Restaurant> restaurantSelected;
    private MutableLiveData<Integer> foodRating = new MutableLiveData<>(3);
    private MutableLiveData<Integer> serviceRating = new MutableLiveData<>(3);
    private MutableLiveData<Integer> atmosphereRating = new MutableLiveData<>(3);

    @Inject
    public ReviewViewModel(App realmApp) {
        this.realmApp = realmApp;

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