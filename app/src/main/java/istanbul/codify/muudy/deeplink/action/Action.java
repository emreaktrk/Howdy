package istanbul.codify.muudy.deeplink.action;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.blankj.utilcode.util.StringUtils;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.fcm.FCMListenerService;

public abstract class Action extends BroadcastReceiver {

    private RemoteMessage mMessage;

    public Action() {
        // Empty block
    }

    public Action(RemoteMessage message) {
        this.mMessage = message;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (StringUtils.equals(getAction(), intent.getAction())) {
            execute(context, mMessage);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancel(FCMListenerService.NOTIFICATION_ID);
            }
        }
    }

    protected abstract @NonNull
    String getAction();

    public abstract void execute(Context context, RemoteMessage message);

    public final PendingIntent getPendingIntent(@NonNull Context context) {
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra("Test", "Hello");
        intent.setAction(getAction());
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}