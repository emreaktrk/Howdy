package istanbul.codify.monju.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.monju.ui.main.MainActivity;
import istanbul.codify.monju.ui.postdetail.PostDetailActivity;

public class PostDetailLink extends DeepLink {

    public PostDetailLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        if (activity instanceof MainActivity){
            Long id = getItemId();
            if (id != null){
                PostDetailActivity.start(id);
            }

        }
    }
}
