package com.example.root.gmapspractice.screens.search_street.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.gmapspractice.R;
import com.example.root.gmapspractice.screens.search_street.callback.ListStreetCallback;
import com.example.root.gmapspractice.services.model.Geocode.ResultsItem;

import java.util.ArrayList;
import java.util.List;

public class ListStreetAdapter extends RecyclerView.Adapter<ListStreetHolder> {

    List<ResultsItem> resultsItemList = new ArrayList<>();
    ListStreetCallback listStreetCallback;
    public ListStreetAdapter(List<ResultsItem> resultsItem,ListStreetCallback listStreetCallback) {
        this.resultsItemList = resultsItem;
        this.listStreetCallback = listStreetCallback;
    }

    @NonNull
    @Override
    public ListStreetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_street_item,parent,false);
        ListStreetHolder holder = new ListStreetHolder(view);
        view.setOnClickListener(v -> {
            listStreetCallback.onItemClick(resultsItemList.get(holder.getAdapterPosition()));
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListStreetHolder holder, int position) {
        holder.bind(resultsItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return resultsItemList.size();
    }
}
