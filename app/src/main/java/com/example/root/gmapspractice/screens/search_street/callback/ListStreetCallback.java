package com.example.root.gmapspractice.screens.search_street.callback;

import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;

public interface ListStreetCallback {
    void onItemClick(ResultsItem resultsItem);
}
