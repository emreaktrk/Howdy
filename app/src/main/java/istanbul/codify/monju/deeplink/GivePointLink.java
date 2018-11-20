package istanbul.codify.monju.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.monju.ui.givevote.GiveVoteDialog;
import istanbul.codify.monju.ui.main.MainActivity;

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
