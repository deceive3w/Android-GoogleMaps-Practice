package com.example.root.gmapspractice.services.model.Geocode.GeoDirection;

import com.google.android.gms.maps.model.LatLng;

public class Direction {
    private  LatLng origin;
    private  LatLng destination;
    private String origin_address;
    private String destination_address;

    public String getOrigin_address() {
        return origin_address;
    }

    public void setOrigin_address(String origin_address) {
        this.origin_address = origin_address;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }
}
