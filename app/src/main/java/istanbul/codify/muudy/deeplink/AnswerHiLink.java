package istanbul.codify.muudy.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.ui.main.MainActivity;
import istanbul.codify.muudy.ui.response.ResponseActivity;

public class AnswerHiLink extends DeepLink {
    public AnswerHiLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        ResponseActivity.start(getItemId(),true,getAnswerHiWord(),getAnswerHiWordImage(),getNotificationMessage());

    }
}
