package com.gigaweb.mytrucks;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapApiInterface {

    @GET("maps/api/distancematrix/json")
    Single<Result> getDistace(
            @Query("key") String key,
            @Query("origins") String origin,
            @Query("destination") String destination
                        );
}
