package istanbul.codify.muudy.deeplink;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.firebase.messaging.RemoteMessage;
import android.app.Activity;

import java.io.Serializable;

public abstract class DeepLink implements Serializable {

    protected RemoteMessage message;

    DeepLink(@NonNull RemoteMessage message) {
        this.message = message;
    }

    public abstract void navigate(Activity activity);

    final @Nullable
    Long getItemId() {
        String itemId = message.getData().get("itemid");
        return TextUtils.isEmpty(itemId) ? null : Long.valueOf(itemId);
    }
}
