package istanbul.codify.monju.logcat;

import android.support.annotation.NonNull;
import android.util.Log;

public class DebugLog extends AbstractLog {

    DebugLog(@NonNull String log) {
        super(log);
    }

    @Override
    public void display() {
        Log.d(TAG, mLog);
    }
}
