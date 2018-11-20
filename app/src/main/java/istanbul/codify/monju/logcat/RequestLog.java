package istanbul.codify.monju.logcat;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestLog extends AbstractLog {

    RequestLog(@NonNull String log) {
        super(log);
    }

    @Override
    public void display() {
        try {
            JSONObject json = new JSONObject(mLog);
            Log.i(TAG, "REQUEST START");
            String formatted = json.toString(3);
            if (formatted.length() > 4000) {
                for (String line : formatted.split("\n", 4000)) {
                    Log.i(TAG, line);
                }
            } else {
                Log.i(TAG, formatted);
            }
            Log.i(TAG, "REQUEST END");
        } catch (JSONException exception) {
            Logcat.e(exception);
        }
    }
}
