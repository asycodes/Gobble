package com.sutd.t4app.di;

import com.sutd.t4app.MainActivity;
import com.sutd.t4app.data.api.TripAdvisorApiModule;
import com.sutd.t4app.data.api.YelpApiModule;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, TripAdvisorApiModule.class, YelpApiModule.class})
public interface AppComponent {
    // whatever here are the consumers of the dependency aka fragments,viewmodels, etc
    void inject(HomeFragmentViewModel viewModel);
    void inject(MainActivity activity);
}