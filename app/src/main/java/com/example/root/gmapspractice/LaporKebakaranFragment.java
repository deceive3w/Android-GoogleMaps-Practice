package com.example.root.gmapspractice;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.gmapspractice.services.model.Geocode.PickedStreet;
import com.example.root.gmapspractice.services.model.Geocode.RadiusKebakaran;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class LaporKebakaranFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.lapor_et_kebakaran_radius)
    EditText et_radius_kebakaran;
    @BindView(R.id.lapor_tv_kebakaran_alamat)
    TextView tv_alamat_kebakaran;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public LaporKebakaranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.mapEvent.onNext(this);
        View view = inflater.inflate(R.layout.fragment_lapor_kebakaran, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.lapor_tv_kebakaran_alamat)
    public void pickPlaces(){
        Intent intent = new Intent(getContext(),SearchStreetActivity.class);
        intent.putExtra("key","start");
        startActivity(intent);

    }
    Circle circle;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        PickedStreet.getStart().subscribe(new Consumer<ResultsItem>() {
            @Override
            public void accept(ResultsItem resultsItem) throws Exception {
                tv_alamat_kebakaran.setText(resultsItem.getFormattedAddress());
                LatLng latLng  = new LatLng(resultsItem.getGeometry().getLocation().getLat(),resultsItem.getGeometry().getLocation().getLng());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                googleMap.addMarker(new MarkerOptions().title("origin").position(latLng));
                PickedStreet.getStart().onComplete();
            }
        });
        Observable<Integer> radiusObservable = RxTextView.afterTextChangeEvents(et_radius_kebakaran).filter(textViewAfterTextChangeEvent -> {
            if(textViewAfterTextChangeEvent.view().getText().toString().isEmpty()){
                return false;
            }
            return true;
        }).map(new Function<TextViewAfterTextChangeEvent, Integer>() {
            @Override
            public Integer apply(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                return Integer.parseInt(textViewAfterTextChangeEvent.view().getText().toString());
            }
        });
        Disposable drawCircleFromPoint =  Observable.combineLatest(PickedStreet.getStart(), radiusObservable, (BiFunction<ResultsItem, Integer, RadiusKebakaran>) (resultsItem, radius) -> {
//            tv_alamat_kebakaran.setText(resultsItem.getFormattedAddress());
            LatLng position = new LatLng(resultsItem.getGeometry().getLocation().getLat(),resultsItem.getGeometry().getLocation().getLng());

            return new RadiusKebakaran(radius,position);
        }).doOnDispose(() -> googleMap.clear()).subscribe(o -> {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.radius(o.getRadius());
            circleOptions.center(o.getLokasi());
            circleOptions.fillColor(Color.red(5));
            if(circle == null){
                circle = googleMap.addCircle(circleOptions);
            }
            drawCircle(googleMap,o.getRadius(),o.getLokasi());
            Log.d("lapor kebakaran", "onMapReady: lets draw");
        });
        compositeDisposable.addAll(drawCircleFromPoint);


    }
    void drawCircle(GoogleMap googleMap, int radius, LatLng position){
        circle.setCenter(position);
        circle.setRadius(radius);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}
