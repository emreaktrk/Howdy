package istanbul.codify.monju.helper.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.logcat.Logcat;

import java.security.MessageDigest;

public final class CertificateUtils {

    public static void keyhash() {
        try {
            Context context = Utils.getApp().getApplicationContext();

            PackageInfo info = context
                    .getPackageManager()
                    .getPackageInfo(
                            context.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest digest = MessageDigest.getInstance("SHA");
                digest.update(signature.toByteArray());
                Logcat.d(Base64.encodeToString(digest.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Logcat.e(e);
        }
    }
}
