package com.codify.howdy.api.pojo.request;

import android.location.Location;

public final class GetWallRequest {

    public String token;
    public int page;
    public double lat;
    public double lng;

    public GetWallRequest(Location location) {
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
    }
}
