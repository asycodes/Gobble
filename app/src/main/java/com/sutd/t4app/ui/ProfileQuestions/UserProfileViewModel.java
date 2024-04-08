package com.sutd.t4app.ui.ProfileQuestions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserProfileViewModel extends ViewModel {
    private MutableLiveData<List<UserProfile>> userProfilesLiveData = new MutableLiveData<>();
    private Realm realm;

    public UserProfileViewModel() {
        realm = Realm.getDefaultInstance();
        fetchUserProfiles();
    }

    // Fetch all user profiles from Realm and post to LiveData
    private void fetchUserProfiles() {
        realm.executeTransactionAsync(realm -> {
            RealmResults<UserProfile> results = realm.where(UserProfile.class).findAll();
            // Since RealmResults are live, they will automatically update.
            // We need to create a detached copy to use outside of Realm.
            List<UserProfile> detachedResults = realm.copyFromRealm(results);
            userProfilesLiveData.postValue(detachedResults);
        });
    }

    public LiveData<List<UserProfile>> getUserProfilesLiveData() {
        return userProfilesLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();  // Don't forget to close the Realm instance
    }
}
