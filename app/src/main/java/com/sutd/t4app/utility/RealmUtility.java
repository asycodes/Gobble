package com.sutd.t4app.utility;

import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.ui.ProfileQuestions.UserProfile;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;


public class RealmUtility {
    private static SyncConfiguration defaultSyncConfig = null;

    public interface ConfigCallback {
        void onConfigReady(SyncConfiguration configuration);
        void onError(Exception e);
    }

    public static void getDefaultSyncConfig(App realmApp, ConfigCallback callback) {
        if (defaultSyncConfig != null) {
            callback.onConfigReady(defaultSyncConfig);
            return;
        }
        realmApp.loginAsync(Credentials.anonymous(), result -> {
            if (result.isSuccess()) {
                User user = realmApp.currentUser();
                defaultSyncConfig = new SyncConfiguration.Builder(user)
                        .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                            @Override
                            public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                // add a subscription with a name
                                boolean ressubscriptionExists = false;
                                boolean usersubscriptionExists = false;
                                for (Subscription existingSubscription : subscriptions) {
                                    if ("restaurantsSubscription".equals(existingSubscription.getName())) {
                                        ressubscriptionExists = true;
                                    }
                                    if ("usersSubscription".equals(existingSubscription.getName())) {
                                        usersubscriptionExists = true;
                                    }
                                }

                                if(!ressubscriptionExists){
                                    subscriptions.add(Subscription.create("restaurantsSubscription",
                                            realm.where(Restaurant.class)));
                                }

                                if(!usersubscriptionExists){
                                    subscriptions.add(Subscription.create("usersSubscription",
                                            realm.where(UserProfile.class)));
                                }

                            }

                        })
                        .build();

                callback.onConfigReady(defaultSyncConfig);
            } else {
                callback.onError(new Exception("Login failed"));
            }
        });
    }
}

