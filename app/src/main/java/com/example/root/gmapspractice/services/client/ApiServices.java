package com.example.root.gmapspractice.services.client;

import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.ResponseDirection;
import com.example.root.gmapspractice.services.model.Geocode.ResponseGeo;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    String api_key = "AIzaSyB3muaOhGiuFnXa9JGT13czJ3dXzftIiMI";
    String api_dir_key = "AIzaSyB3muaOhGiuFnXa9JGT13czJ3dXzftIiMI";
    @GET("geocode/json")
    Observable<ResponseGeo> getGeoResponse(@Query("address")String address,@Query("region") String region_id  , @Query("key") String api_key);

    @GET("directions/json")
    Call<ResponseDirection> getDirResponse(@Query("origin")String origin,@Query("destination") String destination, @Query("key") String api_dir_key);
}
