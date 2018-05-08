package com.example.root.gmapspractice.screens.search_street.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.root.gmapspractice.R;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListStreetHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_search_street_name)
    TextView street_name;

    public ListStreetHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
    void bind(ResultsItem resultsItem){
        street_name.setText(resultsItem.getFormattedAddress());
    }

}
