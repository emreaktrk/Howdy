package istanbul.codify.muudy.ui.places;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Mention;
import istanbul.codify.muudy.model.Place;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.ArrayList;

interface PlacesView extends MvpView {

    void onLoaded(ArrayList<Place> places);

    void onError(ApiError error);

    void onPlaceClicked(Place place);

    void onPlaceSearched(String query);

    void onCloseClicked();

}
