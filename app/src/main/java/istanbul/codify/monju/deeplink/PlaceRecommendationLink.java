package istanbul.codify.monju.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.monju.model.Place;
import istanbul.codify.monju.ui.compose.ComposeActivity;

public class PlaceRecommendationLink extends DeepLink {

    public PlaceRecommendationLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        Place place = new Place();
        place.place_name = getPlaceName();
        ComposeActivity.start(place);
    }
}
