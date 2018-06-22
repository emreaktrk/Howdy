package istanbul.codify.muudy.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

import istanbul.codify.muudy.model.Place;
import istanbul.codify.muudy.ui.compose.ComposeActivity;

/**
 * Created by egesert on 22.06.2018.
 */

public class PlaceRecommedationLink extends DeepLink {
    public PlaceRecommedationLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        Place place = new Place();
        place.place_name = getPlaceName();
        ComposeActivity.start(place);
    }
}
