package istanbul.codify.muudy.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.ui.main.MainActivity;
import istanbul.codify.muudy.ui.response.ResponseActivity;

public final class SayHiLink extends DeepLink {

    public SayHiLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        // TODO Get user from remote message
        // TODO Start Response activity
        if(activity instanceof MainActivity){

            ResponseActivity.start(getItemId(),getNotificationMessage());
        }
    }
}
