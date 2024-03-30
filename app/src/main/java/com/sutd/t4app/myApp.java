package com.sutd.t4app;

import android.app.Application;

import com.sutd.t4app.di.AppComponent;
import com.sutd.t4app.di.DaggerAppComponent;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;


@HiltAndroidApp
public class myApp extends Application {
    private static AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}


/*
package com.sutd.t4app;

        import android.app.Application;


        import io.realm.Realm;
        import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class myApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm
        Realm.init(this);
    }


    public getAppComponent(){
        return

    }
}
*/
