package istanbul.codify.monju.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;

import istanbul.codify.monju.ui.response.ResponseActivity;

public class AnswerHiLink extends DeepLink {
    public AnswerHiLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        ResponseActivity.start(getItemId(),true,getAnswerHiWord(),getAnswerHiWordImage(),getNotificationMessage());

    }
}
