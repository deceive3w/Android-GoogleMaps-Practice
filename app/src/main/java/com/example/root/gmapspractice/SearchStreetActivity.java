package com.example.root.gmapspractice;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.root.gmapspractice.screens.search_street.adapter.ListStreetAdapter;
import com.example.root.gmapspractice.screens.search_street.callback.ListStreetCallback;
import com.example.root.gmapspractice.services.model.Geocode.NetworkResponse;
import com.example.root.gmapspractice.services.model.Geocode.PickedStreet;
import com.example.root.gmapspractice.services.model.Geocode.ResponseGeo;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;
import com.example.root.gmapspractice.viewmodel.SearchStreetViewModel;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class SearchStreetActivity extends AppCompatActivity {

    SearchStreetViewModel searchStreetViewModel;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.rv_list_search_of_query)
    RecyclerView rv_list_search;

    @BindView(R.id.et_search_query_street)
    EditText et_search_street;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_street);
        ButterKnife.bind(this);
        setupRecycler();
        searchStreetViewModel = ViewModelProviders.of(this).get(SearchStreetViewModel.class);
        observeEditText(et_search_street);
//        observeStreet("jln kombas purwokerto");
        searchStreetViewModel.networkResponse.observe(this, networkResponse -> processResponse(networkResponse));
    }
    public void observeStreet(String address){
        searchStreetViewModel.fetchStreet(address);
    }
    private void observeEditText(EditText editText){
        Disposable obs = RxTextView.afterTextChangeEvents(editText).filter(textViewTextChangeEvent -> {
            if(textViewTextChangeEvent.view().getText().length() > 0){
                return true;
            }
            return false;
        }).debounce(400,TimeUnit.MILLISECONDS).subscribe(textViewAfterTextChangeEvent -> {
            Log.d("text view", "accept: "+textViewAfterTextChangeEvent.view().getText().toString());
            observeStreet(textViewAfterTextChangeEvent.view().getText().toString());
        });
        compositeDisposable.add(obs);
    }

    void processResponse(NetworkResponse<ResponseGeo> networkResponse){
        switch (networkResponse.status){
            case ERROR:
                Log.d("NETWORK RESPONSE", "processResponse: error fetch street");
                break;
            case LOADNIG:
                //define your loading animation in this section
                Log.d("NETWORK RESPONSE", "processResponse: on loading");
                break;
            case SUCCESS:
                if(networkResponse.data.getResults().size() > 0){
                    Log.d("NETWORK RESPONSE", "processResponse: on fetch"+networkResponse.data.getResults().get(0).getFormattedAddress());
                    addToRecycler(networkResponse.data.getResults());
                }
                break;
        }
    }
    void setupRecycler(){
        rv_list_search.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
    }
    void addToRecycler(List<ResultsItem> resultsItem){
        rv_list_search.setAdapter(new ListStreetAdapter(resultsItem, new ListStreetCallback() {
            @Override
            public void onItemClick(ResultsItem resultsItem) {
                String key = getIntent().getExtras().getString("key");
                Log.d("search street", "onItemClick: "+key);
                if(key.equals("start")){
//                    searchStreetViewModel.originStreet.setValue(resultsItem);
                    PickedStreet.getStart().onNext(resultsItem);
                    SearchStreetActivity.this.finish();
                }else if(key.equals("finish")){
                    PickedStreet.getEnd().onNext(resultsItem);
//                    searchStreetViewModel.destinationStreet.setValue(resultsItem);
//                    PickedStreet.getEnd().onNext(resultsItem);
                    SearchStreetActivity.this.finish();
                }
            }
        }));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
