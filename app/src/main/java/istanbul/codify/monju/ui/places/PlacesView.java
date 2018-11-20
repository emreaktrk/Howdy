package istanbul.codify.monju.ui.places;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Place;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.ArrayList;

interface PlacesView extends MvpView {

    void onLoaded(ArrayList<Place> places);

    void onError(ApiError error);

    void onPlaceClicked(Place place);

    void onPlaceSearched(String query);

    void onCloseClicked();

}
