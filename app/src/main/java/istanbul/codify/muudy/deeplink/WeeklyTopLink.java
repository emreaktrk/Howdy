package istanbul.codify.muudy.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.ui.main.MainActivity;
import istanbul.codify.muudy.ui.weeklytopuser.WeeklyTopUsersActivity;

public final class WeeklyTopLink extends DeepLink {

    public WeeklyTopLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        if (activity instanceof MainActivity) {
            WeeklyTopUsersActivity.start();
        }
    }
}
