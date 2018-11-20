package istanbul.codify.monju.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.monju.R;
import istanbul.codify.monju.ui.main.MainActivity;
import istanbul.codify.monju.ui.notification.NotificationFragment;

public class GeneralNotificationLink extends DeepLink {

    public GeneralNotificationLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        if (activity instanceof MainActivity) {
            ((MainActivity) activity)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_frame,((MainActivity) activity).mPool.get(NotificationFragment.class))
                    .commit();


        }
    }
}
