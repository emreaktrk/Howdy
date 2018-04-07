package com.codify.howdy.ui.places;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.model.Mention;
import com.codify.howdy.model.Place;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.MvpView;

import java.util.ArrayList;

interface PlacesView extends MvpView {

    void onLoaded(ArrayList<Place> places);

    void onError(ApiError error);

    void onPlaceClicked(Place place);

    void onPlaceSearched(String query);

    void onCloseClicked();

}
