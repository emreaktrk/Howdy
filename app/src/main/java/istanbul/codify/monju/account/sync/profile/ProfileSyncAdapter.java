package istanbul.codify.monju.account.sync.profile;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import com.blankj.utilcode.util.StringUtils;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.request.GetUserProfileRequest;
import istanbul.codify.monju.api.pojo.response.GetUserProfileResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.User;

public final class ProfileSyncAdapter extends AbstractThreadedSyncAdapter {

    public ProfileSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Logcat.v("Performing sync");

        User me = AccountUtils.me(getContext());

        GetUserProfileRequest request = new GetUserProfileRequest(me.iduser);
        request.token = AccountUtils.token(getContext());

        try {
            GetUserProfileResponse response = ApiManager
                    .getInstance()
                    .me(request)
                    .execute()
                    .body();

            if (response != null) {
                if (StringUtils.isEmpty(response.err) && StringUtils.isEmpty(response.errMes)) {
                    AccountUtils.update(getContext(), response.data.user);
                } else {
                    Logcat.e("Sync has error");
                }
            }
        } catch (Exception exception) {
            Logcat.e(exception);
        }
    }
}
