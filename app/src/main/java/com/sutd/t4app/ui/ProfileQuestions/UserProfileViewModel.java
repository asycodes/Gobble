package com.sutd.t4app.ui.ProfileQuestions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.utility.RealmUtility;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.sync.SyncConfiguration;

@HiltViewModel
public class UserProfileViewModel extends ViewModel {
    private MutableLiveData<UserProfile> userProfilesLiveData = new MutableLiveData<>();
    private Realm realm;
    private final App realmApp;

    @Inject
    public UserProfileViewModel(App realmApp) {
        this.realmApp = realmApp;
        initializeRealm();
    }

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        UserProfileViewModel.this.realm = realm;
                        fetchUserProfiles();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                // Handle error, could be logging or setting a LiveData for error messages
            }
        });
    }


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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (realm != null) {
            realm.close();}
    }
}
