package com.sutd.t4app.data.api;

import com.sutd.t4app.data.model.apiresponses.LocationSearchResponse;
import com.sutd.t4app.data.model.apiresponses.ReviewLocationResponse;
import com.sutd.t4app.data.model.apiresponses.YelpSearchResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YelpService {
    @GET("search")
    Observable<YelpSearchResponse> searchBusinesses(
            @Header("Authorization") String authToken,
            @Query("location") String location,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("term") String term
    );
}
