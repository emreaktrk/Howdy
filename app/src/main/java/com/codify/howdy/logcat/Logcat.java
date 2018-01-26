package com.codify.howdy.logcat;

import android.os.Handler;
import android.os.Message;

import com.codify.howdy.api.pojo.response.ApiError;

public final class Logcat {

    private static LogQueue sQueue = new LogQueue();

    public static void v(String log) {
        Message message = new Message();
        message.obj = new VerboseLog(log);

        sQueue.sendMessage(message);
    }

    public static void d(String log) {
        Message message = new Message();
        message.obj = new DebugLog(log);

        sQueue.sendMessage(message);
    }

    public static void e(Throwable throwable) {
        Message message = new Message();
        message.obj = new ErrorLog(throwable);

        sQueue.sendMessage(message);
    }

    public static void e(String log) {
        Message message = new Message();
        message.obj = new ErrorLog(log);

        sQueue.sendMessage(message);
    }

    public static void e(ApiError error) {
        Message message = new Message();
        message.obj = new ErrorLog(error);

        sQueue.sendMessage(message);
    }

    private static class LogQueue extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ILog log = (ILog) msg.obj;
            log.display();
        }
    }
}
