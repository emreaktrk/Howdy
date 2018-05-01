package istanbul.codify.muudy.navigation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public final class Navigator {

    public static Navigation.IController with(@NonNull Fragment fragment) {
        if (fragment == null) {
            throw new NullPointerException("Fragment cannot be null.");
        }

        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("Fragment dont hanve a host. Probably not attached?");
        }

        return (Navigation.IController) activity;
    }
}
