package istanbul.codify.monju.deeplink.action;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.blankj.utilcode.util.StringUtils;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.monju.fcm.FCMListenerService;

public abstract class Action extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (StringUtils.equals(getAction(), intent.getAction())) {
            RemoteMessage message = intent.getParcelableExtra(RemoteMessage.class.getSimpleName());
            execute(context, message);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancel(FCMListenerService.NOTIFICATION_ID);
            }
        }
    }

    protected abstract @NonNull
    String getAction();

    public abstract void execute(Context context, RemoteMessage message);

    public final PendingIntent getPendingIntent(@NonNull RemoteMessage message, @NonNull Context context) {
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra(RemoteMessage.class.getSimpleName(), message);
        intent.setAction(getAction());
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
