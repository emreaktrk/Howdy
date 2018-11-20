package istanbul.codify.monju.logcat;

import android.support.annotation.NonNull;
import android.util.Log;

public class MethodLog extends AbstractLog {

    MethodLog(@NonNull String log) {
        super(log);
    }

    @Override
    public void display() {
        Log.i(TAG, mLog);
    }
}
