package istanbul.codify.muudy.account.sync.profile;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import istanbul.codify.muudy.model.event.SyncEvent;
import org.greenrobot.eventbus.EventBus;

public class ProfileSyncAdapter extends AbstractThreadedSyncAdapter {

    public ProfileSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        // TODO update profile

        EventBus
                .getDefault()
                .post(new SyncEvent());
    }
}
