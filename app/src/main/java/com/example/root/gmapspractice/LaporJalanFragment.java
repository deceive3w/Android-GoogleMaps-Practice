package com.example.root.gmapspractice;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.example.root.gmapspractice.services.model.Geocode.GeoDirection.Direction;
import com.example.root.gmapspractice.services.model.Geocode.PickedStreet;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;
import com.example.root.gmapspractice.viewmodel.DirectionStreetViewModel;
import com.example.root.gmapspractice.viewmodel.SearchStreetViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class LaporJalanFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.lapor_et_start_street)
    TextView et_start_street;
    @BindView(R.id.lapor_et_end_street)
    TextView et_end_street;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    DirectionStreetViewModel streetViewModel;
    SearchStreetViewModel searchStreetViewModel;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public LaporJalanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lapor_jalan, container, false);
        ButterKnife.bind(this, view);
        MainActivity.mapEvent.onNext(this);
//        et_start_street = view.findViewById(R.id.lapor_et_start_street);
        streetViewModel = ViewModelProviders.of(this).get(DirectionStreetViewModel.class);
        searchStreetViewModel = ViewModelProviders.of(this).get(SearchStreetViewModel.class);
        // Inflate the layout for this fragment
        return view;
    }
    @OnClick(R.id.lapor_et_start_street)
    void pick_start_street(){
        Intent intent = new Intent(getContext(),SearchStreetActivity.class);
        intent.putExtra("key","start");
        startActivity(intent);
    }
    @OnClick(R.id.lapor_et_end_street)
    void pick_end_street(){
        Intent intent = new Intent(getContext(),SearchStreetActivity.class);
        intent.putExtra("key","finish");
        startActivity(intent);
    }


    private void drawPath(List<LatLng> polyLineList,GoogleMap googleMap){
        googleMap.addPolyline(new PolylineOptions().addAll(polyLineList).color(Color.RED).width(10));
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    GoogleMap googleMap;
    @Override
    public void onDestroyView() {

        Log.d("disposable", "onDestroyView: iam destroyedview");
        if(googleMap != null){
            googleMap.clear();
        }
        compositeDisposable.dispose();
//        streetViewModel.directionLatLang.removeObservers(this);
        PickedStreet.getStart().onComplete();
        PickedStreet.getEnd().onComplete();
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        Log.d("disposable", "onStop: iam destroyedview");

        super.onStop();

    }

    Marker originMarker = null;
    Marker destinationMarker =null;



    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Log.d("fragment", "onMapReady: on fragment");
        streetViewModel.directionLatLang.observe(this,latLngs -> {
            drawPath(latLngs,googleMap);
            Log.d("disposable", "onMapReady: lets find path");
            for(int i = 0;i<latLngs.size();i++){
                Log.d("direction map", "onMapReady: "+latLngs.get(i).longitude);
            }
        });
        Disposable disposableProvideDirection =  Observable.combineLatest(PickedStreet.getStart(), PickedStreet.getEnd(), new BiFunction<ResultsItem, ResultsItem, Direction>() {
            @Override
            public Direction apply(ResultsItem origin, ResultsItem destination) throws Exception {
                Direction direction = new Direction();
                direction.setOrigin_address(origin.getFormattedAddress());
                direction.setDestination_address(destination.getFormattedAddress());
                direction.setOrigin(new LatLng(origin.getGeometry().getLocation().getLat(),origin.getGeometry().getLocation().getLng()));
                direction.setDestination(new LatLng(destination.getGeometry().getLocation().getLat(),destination.getGeometry().getLocation().getLng()));

                return direction;
            }
        }).subscribe(direction -> {
            Log.d("direction", "onMapReady: "+direction.getOrigin());

           streetViewModel.provideDirection(direction.getOrigin(),direction.getDestination());
        });
        Disposable disposableDestination = PickedStreet.getEnd().subscribe(resultsItem -> {
            Log.d("disposable", "onMapReady: end called"+resultsItem.getFormattedAddress());
            if(destinationMarker != null){
                destinationMarker.remove();
            }
            et_end_street.setText(resultsItem.getFormattedAddress());
            LatLng latLng  = new LatLng(resultsItem.getGeometry().getLocation().getLat(),resultsItem.getGeometry().getLocation().getLng());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            destinationMarker = googleMap.addMarker(new MarkerOptions().title("finish").position(latLng));
        });
//        searchStreetViewModel.originStreet.observe(this,resultsItem -> {
//            Log.d("user street", "onMapReady: get item from user"+resultsItem.getFormattedAddress());
//            et_start_street.setText(resultsItem.getFormattedAddress());
//            if(originMarker != null){
//                originMarker.remove();
//            }
//
//            LatLng latLng  = new LatLng(resultsItem.getGeometry().getLocation().getLat(),resultsItem.getGeometry().getLocation().getLng());
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
//            originMarker = googleMap.addMarker(new MarkerOptions().title("origin").position(latLng));
//        });
        Disposable disposableOrigin =  PickedStreet.getStart().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultsItem -> {
            Log.d("disposable", "onMapReady: start called");
            if(originMarker != null){
                originMarker.remove();
            }
            et_start_street.setText(resultsItem.getFormattedAddress());

            LatLng latLng  = new LatLng(resultsItem.getGeometry().getLocation().getLat(),resultsItem.getGeometry().getLocation().getLng());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            originMarker = googleMap.addMarker(new MarkerOptions().title("origin").position(latLng));

//            PickedStreet.getStart().onComplete();
        });
//        compositeDisposable.add(disposableOrigin);
        compositeDisposable.addAll(disposableOrigin,disposableDestination,disposableProvideDirection);
    }
}
