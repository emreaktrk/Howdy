package istanbul.codify.monju.api.pojo.request;

import android.support.annotation.Nullable;

public final class SearchPlacesRequest {

    public String token;
    public double lat;
    public double lng;
    public String text;

    public SearchPlacesRequest() {
    }

    public SearchPlacesRequest(String token, double lat, double lng, @Nullable String text) {
        this.token = token;
        this.lat = lat;
        this.lng = lng;
        this.text = text;
    }
}
