package istanbul.codify.muudy.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.ui.givevote.GiveVoteDialog;
import istanbul.codify.muudy.ui.main.MainActivity;

public class GivePointLink extends DeepLink {
    public GivePointLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        if (activity instanceof MainActivity){
            GiveVoteDialog
                    .newInstance(getNotificationMessage(),getItemId())
                    .show(((MainActivity) activity).getSupportFragmentManager(),null);
        }
    }
}
