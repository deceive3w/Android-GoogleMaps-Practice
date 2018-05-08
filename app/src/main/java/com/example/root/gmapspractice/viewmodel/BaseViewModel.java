package com.example.root.gmapspractice.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class BaseViewModel<T> extends ViewModel{
    MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();
    MutableLiveData<Integer> loadingStateLiveData = new MutableLiveData<>();
    Observer<Throwable> errorObserver;
    Observer<Integer> loadingObserver;

    public MediatorLiveData<T> fromPublisher(Publisher<T> publisher){
        final MediatorLiveData<T> mainLiveData = new MediatorLiveData();
        mainLiveData.addSource(errorLiveData,errorObserver);
        mainLiveData.addSource(loadingStateLiveData,loadingObserver);
        publisher.subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
                loadingStateLiveData.postValue(LoadingState.LOADING);
            }

            @Override
            public void onNext(T t) {
                mainLiveData.postValue(t);
            }

            @Override
            public void onError(Throwable t) {
                errorLiveData.postValue(t);
            }

            @Override
            public void onComplete() {
                loadingStateLiveData.postValue(LoadingState.NOT_LOADING);
            }
        });
        return mainLiveData;
    }

    public static class LoadingState {
        static int LOADING = 1;
        static int NOT_LOADING = 0;
    }
}
