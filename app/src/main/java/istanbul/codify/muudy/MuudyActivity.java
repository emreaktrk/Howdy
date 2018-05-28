package istanbul.codify.muudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.account.sync.SyncListener;
import istanbul.codify.muudy.model.NotificationActionType;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.event.SyncEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;


public abstract class MuudyActivity extends AppCompatActivity {

    public static final int NO_ID = -1;

    abstract protected @LayoutRes
    int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutResId() != NO_ID) {
            setContentView(getLayoutResId());
        }

        if (this instanceof EventSupport) {
            EventBus
                    .getDefault()
                    .register(this);
        }

        if (this instanceof KeyboardSupport) {
            KeyboardVisibilityEvent.setEventListener(this, ((KeyboardSupport) MuudyActivity.this)::onKeyboard);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this instanceof EventSupport) {
            EventBus
                    .getDefault()
                    .unregister(this);
        }
    }

    protected final <T extends Serializable> T getSerializable(Class<? extends T> clazz) {
        String key = clazz.getSimpleName();
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(key)) {
            return clazz.cast(getIntent().getExtras().getSerializable(key));
        }

        return null;
    }

    protected final <T extends Serializable> T getSerializable(Class<? extends T> clazz, String key) {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(key)) {
            return clazz.cast(getIntent().getExtras().getSerializable(key));
        }

        return null;
    }

    protected final RemoteMessage getSerializableNotification(Class<RemoteMessage> clazz){
        String key = clazz.getSimpleName();
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(key)) {
            return clazz.cast(getIntent().getExtras().getSerializable(key));
        }

        return null;
    }

    protected final long[] getLongArray() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(long[].class.getSimpleName())) {
            return getIntent().getExtras().getLongArray(long[].class.getSimpleName());
        }

        return null;
    }

    protected final void setResult(int resultCode, Serializable data) {
        Intent intent = new Intent();
        intent.putExtra(data.getClass().getSimpleName(), data);
        setResult(resultCode, intent);
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T extends Serializable> T resolveResult(int requestCode, int resultCode, Intent data, Class<? extends T> clazz, int waitingRequestCode) {
        if (requestCode != waitingRequestCode ||
                data == null ||
                resultCode == RESULT_CANCELED ||
                data.getExtras() == null ||
                !data.getExtras().containsKey(clazz.getSimpleName())) {
            return null;
        }

        return (T) data.getExtras().getSerializable(clazz.getSimpleName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(SyncEvent event) {
        if (this instanceof SyncListener) {
            User me = AccountUtils.me(this);

            SyncListener listener = (SyncListener) this;
            listener.onSync(me);
        }
    }

    @Nullable
    public NotificationActionType getNotificationActionType(RemoteMessage message) {
        return NotificationActionType.value(message.getData().get("actiontype"));
    }


    public NotificationActionType getNotificationActionType(int ordinal) {
        return NotificationActionType.values()[ordinal];
    }

}
