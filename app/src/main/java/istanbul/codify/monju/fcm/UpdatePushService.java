package istanbul.codify.monju.fcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.api.pojo.response.ApiError;

public final class UpdatePushService extends IntentService implements FCMView {

    private static final String TAG = UpdatePushService.class.getSimpleName();

    private FCMPresenter mPresenter = new FCMPresenter();

    public UpdatePushService() {
        super(TAG);
    }

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();
        Intent starter = new Intent(context, UpdatePushService.class);
        context.startService(starter);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPresenter.attachView(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mPresenter.update(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTokenUpdated(String token) {
        mPresenter.detachView();
    }

    @Override
    public void onError(ApiError error) {
        // Empty block
    }
}