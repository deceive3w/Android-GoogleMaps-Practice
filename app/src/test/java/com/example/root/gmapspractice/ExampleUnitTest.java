package com.example.root.gmapspractice;

import android.arch.lifecycle.ViewModelProviders;
import android.util.Log;

import com.example.root.gmapspractice.services.client.ApiServices;
import com.example.root.gmapspractice.services.client.NetworkClient;
import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.Direction;
import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.ResponseDirection;
import com.example.root.gmapspractice.services.model.Geocode.ResponseGeo;
import com.example.root.gmapspractice.viewmodel.SearchStreetViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    ResponseGeo lastest_searched;
    @Test
    public void getGeo() throws IOException {

        PublishSubject<String> behaviorSubject1 = PublishSubject.create();
        BehaviorSubject<String> behaviorSubject2 = BehaviorSubject.create();
        behaviorSubject1.onNext("asd");
        behaviorSubject1.onNext("asd 2");
        behaviorSubject1.subscribe(s -> {
           System.out.println(s);
        });
        behaviorSubject1.subscribe(s -> {
            System.out.println("sub2"+s);
        });
        behaviorSubject1.onNext("asd 3 ");
        behaviorSubject1.onNext("asd 4");
        behaviorSubject1.onNext("asd 5");

    }
}