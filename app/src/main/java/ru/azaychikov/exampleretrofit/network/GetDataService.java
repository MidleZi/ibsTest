package ru.azaychikov.exampleretrofit.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.azaychikov.exampleretrofit.model.Root;

public interface GetDataService {

    @GET("resources")
    Call<Root> getAll(@Query("public_key") String publicKey);

    @GET("resources")
    Call<Root> getAll(@Query("public_key") String publicKey, @Query("path") String path);
}
