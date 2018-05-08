package com.example.root.gmapspractice.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.root.gmapspractice.services.client.ApiServices;
import com.example.root.gmapspractice.services.client.NetworkClient;
import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.Direction;
import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.LegsItem;
import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.ResponseDirection;
import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.StepsItem;
import com.example.root.gmapspractice.services.model.Geocode.ResponseGeo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.*;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionStreetViewModel extends AndroidViewModel implements OnMapReadyCallback {

    public  MutableLiveData<List<LatLng>> directionLatLang = new MutableLiveData<>();

    public DirectionStreetViewModel(@NonNull Application application) {
        super(application);
    }

    public void provideDirection(LatLng origin,LatLng destination){
        if(origin == null || destination == null){
            return;
        }
//        if(directionLatLang.getValue() != null){
//            directionLatLang.getValue().clear();
//        }
        String parsedOrigin = origin.latitude+","+origin.longitude;
        String parsedDestination = destination.latitude+","+destination.longitude;
        NetworkClient.apiServices().getDirResponse(parsedOrigin,parsedDestination, ApiServices.api_dir_key).enqueue(new Callback<ResponseDirection>() {
            @Override
            public void onResponse(Call<ResponseDirection> call, Response<ResponseDirection> response) {
                List<LatLng> decodedPathList = new ArrayList<>();
                for(int j = 0 ; j < response.body().getRoutes().get(0).getLegs().size() ; j++){
                    LegsItem legsItem = response.body().getRoutes().get(0).getLegs().get(j);
                    for(int k = 0; k < legsItem.getSteps().size(); k++){
                        StepsItem stepsItem = response.body().getRoutes().get(0).getLegs().get(j).getSteps().get(k);
                        List<LatLng> list = decodePath(stepsItem.getPolyline().getPoints());
                        for (LatLng latlng:list
                             ) {
                            decodedPathList.add(latlng);
                        }
                    }
                }
                directionLatLang.postValue(decodedPathList);
            }

            @Override
            public void onFailure(Call<ResponseDirection> call, Throwable t) {

            }
        });
    }

    private List<LatLng> decodePath(String encodedPolyline){
        return PolyUtil.decode(encodedPolyline);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
