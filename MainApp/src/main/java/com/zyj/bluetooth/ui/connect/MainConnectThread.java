package com.zyj.bluetooth.ui.connect;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.zyj.library.PUtil;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-8<br>
 * Description: NON
 */
public final class MainConnectThread implements Handler.Callback {

    public final static MainConnectThread instance = new MainConnectThread();

    private final ConnectedThread connectedThread = new ConnectedThread(this);
    private final boolean isClient = PUtil.isClient();

    private MainConnectThread() {
    }

    private static class ConnectedThread extends HandlerThread {

        private Handler handler;
        private Handler.Callback callback;

        private ConnectedThread(@NonNull Handler.Callback callback) {
            super("connect_thread");
            this.callback = callback;
            start();
        }

        @Override protected void onLooperPrepared() {
            super.onLooperPrepared();
            handler = new Handler(getLooper(), callback);
        }

        public @NonNull Handler getThreadHandler() {
            return handler;
        }
    }

    @Override public boolean handleMessage(@NonNull Message msg) {
        return false;
    }




}
