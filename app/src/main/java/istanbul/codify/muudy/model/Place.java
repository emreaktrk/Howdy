package istanbul.codify.muudy.model;

import java.io.Serializable;

public final class Place implements Selectable, Serializable {

    public String place_id;
    public String place_name;
    public double place_lat;
    public double place_lng;
    public int userDistance;

    @Override
    public long id() {
        return NO_ID;
    }

    @Override
    public String text() {
        return place_name;
    }
}
