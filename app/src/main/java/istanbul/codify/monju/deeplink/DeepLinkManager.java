package istanbul.codify.monju.deeplink;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class DeepLinkManager {

    private static DeepLinkManager sInstance;

    private DeepLink mPending;

    public static DeepLinkManager getInstance() {
        if (sInstance == null) {
            sInstance = new DeepLinkManager();
        }

        return sInstance;
    }

    public @Nullable
    DeepLink getPending() {
        return mPending;
    }

    public void setPending(@Nullable DeepLink pending) {
        mPending = pending;
    }

    public void nullifyIf(@NonNull Class clazz) {
        if (mPending != null && mPending.getClass().equals(clazz)) {
            sInstance = null;
        }
    }

    public void nullify() {
        sInstance = null;
    }
}
