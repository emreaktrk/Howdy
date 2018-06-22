package istanbul.codify.muudy.deeplink.action;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;

public final class ShareAction extends Action {

    public ShareAction(RemoteMessage message) {
        super(message);
    }

    @NonNull
    @Override
    protected String getAction() {
        return "Share";
    }

    @Override
    public void execute(Context context, Intent intent) {
        mMessage.getData().get("itemid");

        // TODO Request api
    }
}
