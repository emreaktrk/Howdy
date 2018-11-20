package istanbul.codify.monju.account.sync.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public final class LocationSyncService extends Service {

    private LocationSyncAdapter mAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mAdapter == null){
            mAdapter = new LocationSyncAdapter(this, true, false);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAdapter.getSyncAdapterBinder();
    }
}
