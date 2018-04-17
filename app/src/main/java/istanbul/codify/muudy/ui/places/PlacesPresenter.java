package istanbul.codify.muudy.ui.places;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.SearchPlacesRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.SearchPlacesResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Place;
import istanbul.codify.muudy.ui.base.BasePresenter;
import com.google.android.gms.location.LocationServices;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

final class PlacesPresenter extends BasePresenter<PlacesView> {

    @Override
    public void attachView(PlacesView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.places_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.places_search))
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .skip(1)
                        .subscribe(word -> {
                            Logcat.v("Places searched : " + findViewById(R.id.places_search, AppCompatEditText.class).getText().toString());

                            view.onPlaceSearched(word.toString());
                        }));

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider));
        findViewById(R.id.places_recycler, RecyclerView.class).addItemDecoration(divider);
        findViewById(R.id.places_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void search(String query) {
        // TODO Remote search
    }

    void bind(List<Place> places) {
        PlacesAdapter adapter = new PlacesAdapter(places);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(place -> {
                            Logcat.v("Place clicked");

                            mView.onPlaceClicked(place);
                        }));
        findViewById(R.id.places_recycler, RecyclerView.class).setAdapter(adapter);
    }

    @SuppressLint("MissingPermission")
    void getPlaces() {
        LocationServices
                .getFusedLocationProviderClient(getContext())
                .getLastLocation()
                .continueWith(task -> {
                    Location result = task.getResult();
                    if (result == null) {
                        Location location = new Location("default");
                        location.setLatitude(40.991955);
                        location.setLatitude(28.712913);
                        return location;
                    }

                    return result;
                })
                .addOnSuccessListener(location -> {
                    SearchPlacesRequest request = new SearchPlacesRequest();
                    request.token = AccountUtils.tokenLegacy(getContext());
                    request.lat = location.getLatitude();
                    request.lng = location.getLongitude();

                    mDisposables.add(
                            ApiManager
                                    .getInstance()
                                    .searchPlaces(request)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ServiceConsumer<SearchPlacesResponse>() {
                                        @Override
                                        protected void success(SearchPlacesResponse response) {
                                            mView.onLoaded(response.data);
                                        }

                                        @Override
                                        protected void error(ApiError error) {
                                            Logcat.e(error);

                                            mView.onError(error);
                                        }
                                    }));
                })
                .addOnFailureListener(Logcat::e);
    }

    void getPlaces(String query) {
        SearchPlacesRequest request = new SearchPlacesRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.text = query;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .searchPlaces(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SearchPlacesResponse>() {
                            @Override
                            protected void success(SearchPlacesResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
