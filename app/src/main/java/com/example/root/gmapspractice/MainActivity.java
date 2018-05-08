package com.example.root.gmapspractice;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.root.gmapspractice.services.model.Geocode.PickedStreet;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    @BindView(R.id.bottom_sheet1)
    View bottom_sheet;
    @BindView(R.id.tap_action_layout)
    View tap_action;

    BottomSheetBehavior sheetBehavior;

    public static ReplaySubject<OnMapReadyCallback> mapEvent = ReplaySubject.create();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,MapsActivity.class);
//        startActivity(intent);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        setupMapInteractor(mapFragment);

        ButterKnife.bind(this);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setPeekHeight(120);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        tap_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("map", "onMapReady: on main activity");
    }

    @OnClick(R.id.bottom_btn_lapor_jalan)
    void OpenLaporJalanFragment(){
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.bottom_fragment_container,new LaporJalanFragment()).commit();
    }

    @OnClick(R.id.bottom_btn_lapor_kebakaran)
    void OpenLaporKebakaranFragment(){
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.bottom_fragment_container,new LaporKebakaranFragment()).commit();
    }
    void setupMapInteractor(SupportMapFragment mapFragment){
        mapEvent.onNext(this);
        Disposable disposable =  mapEvent.subscribe(onMapReadyCallback -> mapFragment.getMapAsync(onMapReadyCallback));
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onStop() {
        Log.d("main activity", "onStop: main activity stoped");
        super.onStop();
//        compositeDisposable.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("main activity", "onDestroy: i am destroy");
        compositeDisposable.dispose();

    }


}
