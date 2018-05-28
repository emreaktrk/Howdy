package istanbul.codify.muudy.deeplink;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.ui.chat.ChatActivity;
import istanbul.codify.muudy.ui.main.MainActivity;
import istanbul.codify.muudy.ui.messages.UserMessagesActivity;

public class MessageLink extends DeepLink {

    public MessageLink(@NonNull RemoteMessage message) {
        super(message);
    }

    @Override
    public void navigate(Activity activity) {
        if (activity instanceof MainActivity){
           UserMessagesActivity.start();
        }else if (activity instanceof UserMessagesActivity){
            if (getItemId() != null) {
                ChatActivity.start(getItemId());
            }
        }
    }
}
