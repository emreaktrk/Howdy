package istanbul.codify.muudy.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import istanbul.codify.muudy.account.sync.location.LocationContract;
import istanbul.codify.muudy.account.sync.profile.ProfileContract;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.event.SyncEvent;
import org.greenrobot.eventbus.EventBus;

public final class AccountUtils {


    public synchronized static void login(Context context, User user) {
        Account account = new Account(user.username, AccountContract.ACCOUNT_TYPE);

        AccountManager manager = AccountManager.get(context);
        manager.addAccountExplicitly(account, null, null);
        manager.setAuthToken(account, AccountContract.FULL_ACCESS, user.tokenstring);
        manager.setUserData(account, AccountManager.KEY_USERDATA, user.toString());

        ContentResolver.setSyncAutomatically(account, ProfileContract.AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, ProfileContract.AUTHORITY, Bundle.EMPTY, 60L);

        ContentResolver.setSyncAutomatically(account, LocationContract.AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, LocationContract.AUTHORITY, Bundle.EMPTY, 60L);
    }

    public synchronized static void update(Context context, User user) {
        AccountManager manager = AccountManager.get(context);
        Account account = manager.getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
        manager.setAuthToken(account, AccountContract.FULL_ACCESS, user.tokenstring);
        manager.setUserData(account, AccountManager.KEY_USERDATA, new Gson().toJson(user));

        EventBus
                .getDefault()
                .post(new SyncEvent());
    }

    public synchronized static String token(Context context) {
        try {
            AccountManager manager = AccountManager.get(context);
            Account account = manager.getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
            Bundle result = manager
                    .getAuthToken(account, AccountContract.FULL_ACCESS, null, null, null, null)
                    .getResult();
            return result.getString(AccountManager.KEY_AUTHTOKEN);
        } catch (Exception e) {
            Logcat.e(e);
            return null;
        }
    }

    public synchronized static String tokenLegacy(Context context) {
        if (!has(context)) {
            return "";
        }

        AccountManager manager = AccountManager.get(context);
        Account account = manager.getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
        String json = manager.getUserData(account, AccountManager.KEY_USERDATA);
        return new Gson().fromJson(json, User.class).tokenstring;
    }

    public synchronized static boolean has(Context context) {
        return AccountManager.get(context).getAccountsByType(AccountContract.ACCOUNT_TYPE).length == 1;
    }

    public synchronized static User me(Context context) {
        Account account = AccountManager.get(context).getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
        return new Gson().fromJson(AccountManager.get(context).getUserData(account, AccountManager.KEY_USERDATA), User.class);
    }

    public synchronized static boolean own(Context context, User user) {
        User me = me(context);
        return me.equals(user);
    }

    public synchronized static boolean own(Context context, long userId) {
        User me = me(context);
        return me.equals(userId);
    }

    public synchronized static void sync(Context context) {
        Account account = AccountManager.get(context).getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
        ContentResolver.requestSync(account, ProfileContract.AUTHORITY, Bundle.EMPTY);
    }

    public synchronized static void logout(Context context, @Nullable AccountCallback callback) {
        LoginManager
                .getInstance()
                .logOut();

        Account[] accounts = AccountManager.get(context).getAccountsByType(AccountContract.ACCOUNT_TYPE);
        if (accounts.length == 0) {
            return;
        }

        Account account = accounts[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AccountManager.get(context).removeAccountExplicitly(account);
            if (callback != null) {
                callback.onResult(true);
            }
        } else {
            AccountManager.get(context).removeAccount(account, future -> {
                if (callback != null) {
                    try {
                        Boolean result = future.getResult();
                        callback.onResult(result);
                    } catch (Exception e) {
                        callback.onResult(false);
                    }
                }
            }, null);
        }
    }

    public interface AccountCallback {

        void onResult(boolean result);
    }
}
