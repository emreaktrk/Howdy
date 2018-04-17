package istanbul.codify.muudy.logcat;

import android.support.annotation.NonNull;
import android.util.Log;

import istanbul.codify.muudy.api.pojo.response.ApiError;

public class ErrorLog extends AbstractLog {

    ErrorLog(@NonNull String log) {
        super(log);
    }

    ErrorLog(@NonNull Throwable throwable) {
        super(throwable.getMessage());
    }

    ErrorLog(@NonNull ApiError error) {
        super(error.message);
    }

    @Override
    public void display() {
        Log.e(TAG, mLog);
    }
}
