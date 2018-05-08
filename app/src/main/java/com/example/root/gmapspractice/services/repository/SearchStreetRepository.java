package com.example.root.gmapspractice.services.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.root.gmapspractice.services.client.ApiServices;
import com.example.root.gmapspractice.services.client.NetworkClient;
import com.example.root.gmapspractice.services.model.Geocode.NetworkResponse;
import com.example.root.gmapspractice.services.model.Geocode.ResponseGeo;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;
import com.example.root.gmapspractice.viewmodel.SearchStreetViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchStreetRepository {
    static SearchStreetRepository instance;
    NetworkResponse<ResponseGeo> networkResponse;
    public static SearchStreetRepository getInstance(NetworkResponse networkResponse){

        if(instance == null){
            instance = new SearchStreetRepository(networkResponse);
        }
        return instance;
    }
    public SearchStreetRepository(NetworkResponse networkResponse){
        this.networkResponse = networkResponse;
    }


}
