package com.example.ibstestmvp.network;

import com.example.ibstestmvp.entities.RootFolder;
import io.reactivex.Observable;;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("resources")
    Observable<RootFolder> getAll(@Query("public_key") String publicKey);

    @GET("resources")
    Observable<RootFolder> getAll(@Query("public_key") String publicKey, @Query("path") String path);
}
