package istanbul.codify.muudy.ui.statistic.map;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetTopEmojisOnMapRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetTopEmojisOnMapResponse;
import istanbul.codify.muudy.helper.rx.map.RxGoogleMap;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.EmojiLocation;
import istanbul.codify.muudy.ui.MarkerCustomPopup.MarkerCustomPopupFragmentDialog;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

final class StatisticMapPresenter extends BasePresenter<StatisticMapView> {

    @Override
    public void attachView(StatisticMapView view, View root) {
        super.attachView(view, root);

        map().getMapAsync(map -> mView.onReady(map));
    }

    void bind(GoogleMap map) {
        LatLng center = new LatLng(39.2d, 34.75d);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(center, 4.7f);
        map.animateCamera(update);
        BigDecimal threshold = new BigDecimal(1.365772219865448E-5);
        map.getUiSettings().setMapToolbarEnabled(false);
        mDisposables.add(
                RxGoogleMap
                        .cameraIdles(map)
                        .distinctUntilChanged((previous, next) -> {
                            BigDecimal from = new BigDecimal(previous.latLngBounds.northeast.latitude);
                            BigDecimal to = new BigDecimal(next.latLngBounds.northeast.latitude);
                            BigDecimal diff = from.subtract(to);
                            return diff.compareTo(threshold) < 0;
                        })
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .subscribe(region -> mView.onArea(map, region)));


    }

    MapView map() {
        return findViewById(R.id.statistic_map_view, MapView.class);
    }

    void getEmojis(GoogleMap map, VisibleRegion region) {
        GetTopEmojisOnMapRequest request = new GetTopEmojisOnMapRequest();
        request.lat1 = region.latLngBounds.northeast.latitude;
        request.lng1 = region.latLngBounds.northeast.longitude;
        request.lat2 = region.latLngBounds.southwest.latitude;
        request.lng2 = region.latLngBounds.southwest.longitude;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getTopEmojisOnMapRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetTopEmojisOnMapResponse>() {
                            @Override
                            protected void success(GetTopEmojisOnMapResponse response) {
                                mView.onLoaded(map, response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void icons(GoogleMap map, List<EmojiLocation> locations) {
        map.clear();

        for (int i = 0; i < locations.size(); i++) {

            Marker temp = map.addMarker(
                    new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
                            .position(new LatLng(locations.get(i).lat, locations.get(i).lng)));
            temp.setTag(i);
        }


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mView.onMarkerClicked(locations.get(Integer.parseInt(String.valueOf(marker.getTag()))));
                return false;
            }
        });

    }
}
