package com.sutd.t4app.di;

import android.text.TextUtils;
import android.util.Log;

import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.utility.RealmUtility;

import dagger.Module;
import dagger.Provides;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.realm.mongodb.sync.SyncConfiguration;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    String apiKey = BuildConfig.PLACES_API_KEY;

    @Provides
    @Singleton
    App provideRealmApp() {
        String appId = apiKey; // Replace with your actual App ID
        AppConfiguration appConfiguration = new AppConfiguration.Builder(appId).build();
        return new App(appConfiguration);
    }

}