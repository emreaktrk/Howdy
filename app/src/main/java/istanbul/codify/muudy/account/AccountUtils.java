package istanbul.codify.muudy.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import istanbul.codify.muudy.account.sync.profile.ProfileContract;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import com.google.gson.Gson;

public final class AccountUtils {


    public static void login(Context context, User user) {
        Account account = new Account(user.username, AccountContract.ACCOUNT_TYPE);

        AccountManager manager = AccountManager.get(context);
        manager.addAccountExplicitly(account, null, null);
        manager.setAuthToken(account, AccountContract.FULL_ACCESS, user.tokenstring);
        manager.setUserData(account, AccountManager.KEY_USERDATA, user.toString());
    }

    public static String token(Context context) {
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

    public static String tokenLegacy(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account account = manager.getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
        String json = manager.getUserData(account, AccountManager.KEY_USERDATA);
        return new Gson().fromJson(json, User.class).tokenstring;
    }

    public static boolean has(Context context) {
        return AccountManager.get(context).getAccountsByType(AccountContract.ACCOUNT_TYPE).length == 1;
    }

    public static User me(Context context) {
        Account account = AccountManager.get(context).getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
        return new Gson().fromJson(AccountManager.get(context).getUserData(account, AccountManager.KEY_USERDATA), User.class);
    }

    public static void sync(Context context) {
        Account account = AccountManager.get(context).getAccountsByType(AccountContract.ACCOUNT_TYPE)[0];
        ContentResolver.requestSync(account, ProfileContract.AUTHORITY, Bundle.EMPTY);
    }
}
