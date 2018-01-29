package com.codify.howdy.logcat;

import android.os.Handler;
import android.os.Looper;
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

    public static void method(String method) {
        Message message = new Message();
        message.obj = new MethodLog(method);

        sQueue.sendMessage(message);
    }

    public static void request(String json) {
        Message message = new Message();
        message.obj = new RequestLog(json);

        sQueue.sendMessage(message);
    }

    public static void response(String json) {
        Message message = new Message();
        message.obj = new ResponseLog(json);

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

        private LogQueue() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ILog log = (ILog) msg.obj;
            log.display();
        }
    }
}
