package istanbul.codify.monju.account.sync.profile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public final class ProfileSyncService extends Service {

    private ProfileSyncAdapter mAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mAdapter == null){
            mAdapter = new ProfileSyncAdapter(this, true, false);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAdapter.getSyncAdapterBinder();
    }
}
