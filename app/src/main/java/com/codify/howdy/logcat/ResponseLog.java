package com.codify.howdy.logcat;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseLog extends AbstractLog {

    ResponseLog(@NonNull String log) {
        super(log);
    }

    @Override
    public void display() {
        try {
            JSONObject json = new JSONObject(mLog);
            Log.i(TAG, "RESPONSE START");
            String formatted = json.toString(3);
            if (formatted.length() > 4000) {
                for (String line : formatted.split("\n", 4000)) {
                    Log.d(TAG, line);
                }
            } else {
                Log.i(TAG, formatted);
            }
            Log.i(TAG, "RESPONSE END");
        } catch (JSONException exception) {
            Logcat.e(exception);
        }
    }
}
