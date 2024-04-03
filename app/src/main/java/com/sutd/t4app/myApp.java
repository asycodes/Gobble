package com.sutd.t4app;

import android.app.Application;

import com.sutd.t4app.di.AppComponent;
import com.sutd.t4app.di.DaggerAppComponent;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;


@HiltAndroidApp
public class myApp extends Application {
    private static AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2) // Set to a version higher than the current on-device version.

                //as off 3/04 it its at version 2-ANGIE
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        RealmSchema schema = realm.getSchema();

                        // Migration from version 1 to version 2
                        if (oldVersion == 1) {
                            RealmObjectSchema restaurantSchema = schema.get("Restaurant");
                            // Migrate the schema as per the required changes
                            // Example: removing a field, adding new fields
                            if (restaurantSchema != null) {
                                // Remove 'Status' if it exists
                                if (restaurantSchema.hasField("Status")) {
                                    restaurantSchema.removeField("Status");
                                }
                                // Add new fields
                                restaurantSchema
//                                        .addField("TotalReviews", String.class)
//                                        .addField("TopMenu1", String.class)
//                                        .addField("TopMenu2", String.class)
//                                        .addField("TopMenu3", String.class)
//                                        .addField("TopMenu4", String.class)
//                                        .addField("PriceRange", String.class)
//                                        .addField("FoodRating", Double.class)
//                                        .addField("ServiceRating", Double.class)
//                                        .addField("AmbienceRating", Double.class)
//                                        .addField("lat", String.class)
//                                        .addField("lng", String.class)
//                                        .addField("UserId1", String.class)
//                                        .addField("Review1", String.class)
//                                        .addField("ReviewRating1", Double.class)
//                                        .addField("UserId2", String.class)
//                                        .addField("Review2", String.class)
//                                        .addField("ReviewRating2", Double.class)
//                                        .addField("Description", String.class)
                                        .addField("DietaryOptions", String.class);;

                            }
                            oldVersion++;
                        }
                        // Handle other migration steps if there are any further schema changes
                    }
                })
                .build();

        // Set this configuration as the default for this application
        Realm.setDefaultConfiguration(config);
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
