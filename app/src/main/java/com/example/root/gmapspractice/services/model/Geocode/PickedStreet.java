package com.example.root.gmapspractice.services.model.Geocode;

import android.util.Log;

import javax.xml.transform.Result;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class PickedStreet {
    public static ReplaySubject<ResultsItem> start ;
    public static BehaviorSubject<ResultsItem> end;
    public static PickedStreet instance;

    public static PickedStreet getInstance() {
        if(instance == null){
            instance = new PickedStreet();
        }
        return instance;
    }

    public static ReplaySubject<ResultsItem> getStart() {
        if(start == null || start.hasComplete()){
//            Log.d("disposable", "getStart: new instance");
            start = ReplaySubject.create();
        }
        return start;
    }
    public static BehaviorSubject<ResultsItem> getEnd(){
        if(end == null || end.hasComplete()){
            end = BehaviorSubject.create();
        }
        return end;
    }

}
