package istanbul.codify.muudy.ui.statistic.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.VisibleRegion;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.EmojiLocation;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.List;

interface StatisticMapView extends MvpView {

    void onReady(GoogleMap map);

    void onArea(GoogleMap map, VisibleRegion region);

    void onLoaded(GoogleMap map, List<EmojiLocation> locations);

    void onError(ApiError error);

    void onMarkerClicked(EmojiLocation emojiLocation);
}
