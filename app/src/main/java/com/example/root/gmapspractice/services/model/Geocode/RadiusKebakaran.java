package com.example.root.gmapspractice.services.model.Geocode;

import com.google.android.gms.maps.model.LatLng;

public class RadiusKebakaran {
    private int radius;
    private LatLng lokasi;

    public RadiusKebakaran(int radius, LatLng lokasi) {
        this.radius = radius;
        this.lokasi = lokasi;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public LatLng getLokasi() {
        return lokasi;
    }

    public void setLokasi(LatLng lokasi) {
        this.lokasi = lokasi;
    }
}
