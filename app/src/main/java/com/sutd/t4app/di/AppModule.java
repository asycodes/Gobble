package com.sutd.t4app.di;

import dagger.Module;
import dagger.Provides;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    App provideRealmApp() {
        String appId = "devicesync-hdlvd"; // Replace with your actual App ID
        AppConfiguration appConfiguration = new AppConfiguration.Builder(appId).build();
        return new App(appConfiguration);
    }
}