package com.example.root.gmapspractice.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import com.example.root.gmapspractice.services.client.ApiServices;
import com.example.root.gmapspractice.services.client.NetworkClient;
import com.example.root.gmapspractice.services.model.Geocode.NetworkResponse;
import com.example.root.gmapspractice.services.model.Geocode.ResponseGeo;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;
import com.example.root.gmapspractice.services.repository.SearchStreetRepository;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class SearchStreetViewModel extends AndroidViewModel {
    public static int REQUEST_CODE = 23;
    public MutableLiveData<NetworkResponse<ResponseGeo>> networkResponse = new MutableLiveData<>();
    MutableLiveData<ResponseGeo> mutableLiveData = new MutableLiveData<>();
    MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    MutableLiveData<Integer> loadingLiveData = new MutableLiveData<>();
    public static MutableLiveData<ResultsItem> originStreet = new MutableLiveData<>();
    public MutableLiveData<ResultsItem> destinationStreet = new MutableLiveData<>();
    CompositeDisposable  compositeDisposable = new CompositeDisposable();
    public SearchStreetViewModel(@NonNull Application application) {
        super(application);
    }
    public void fetchStreet(String address){
        Disposable disposableFetchStreet =  NetworkClient.apiServices().getGeoResponse(address,"id",ApiServices.api_key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(disposable -> {
            Log.d("composite street", "fetchStreet: "+compositeDisposable.size());
            compositeDisposable.clear();
            networkResponse.postValue(NetworkResponse.loading());
        }).filter(responseGeo -> {
            if(responseGeo == null){
                Throwable throwable = new Throwable("data tidak ad");
                networkResponse.postValue(NetworkResponse.error(throwable));
                return false;
            }
            return true;
        }).subscribe(responseGeo -> networkResponse.postValue(NetworkResponse.success(responseGeo)),throwable -> networkResponse.postValue(NetworkResponse.error((Throwable) throwable)));
        compositeDisposable.add(disposableFetchStreet);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
