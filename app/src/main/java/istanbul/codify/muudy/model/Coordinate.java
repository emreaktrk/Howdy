package istanbul.codify.muudy.model;

import android.location.Location;

public final class Coordinate {

    public Double lat;
    public Double lng;

    public Coordinate(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public static Coordinate from(Location location) {
        return new Coordinate(location.getLatitude(), location.getLongitude());
    }
}
