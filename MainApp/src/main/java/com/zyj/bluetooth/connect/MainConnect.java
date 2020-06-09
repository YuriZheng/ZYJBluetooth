package com.zyj.bluetooth.connect;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.zyj.library.L;
import com.zyj.library.PUtil;

import java.io.IOException;
import java.util.UUID;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-8<br>
 * Description: NON
 */
public final class MainConnect implements Handler.Callback {

    public final static MainConnect instance = new MainConnect();

    private final UUID CONN_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final ConnectedThread connectedThread = new ConnectedThread(this);
    private final boolean isClient = PUtil.isClient();

    private BluetoothSocket socket;

    private boolean destroy = false;

    private MainConnect() {
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

        @NonNull Handler getThreadHandler() {
            return handler;
        }
    }

    private Handler getHandler() {
        return connectedThread.getThreadHandler();
    }

    public final void startConnection(BluetoothManager bluetoothManager, BluetoothDevice device) {
        if (isClient) {
            sendMessage(MSG_CODE_INIT_CLIENT, device);
        } else {
            sendMessage(MSG_CODE_INIT_SERVER, bluetoothManager);
        }
    }

    public final void destroy() {
        sendMessage(MSG_CODE_DESTROY, null);
        destroy = true;
    }

    private void sendMessage(int what, Object obj) {
        if (destroy) {
            L.w(MainConnect.class.getSimpleName(), "Destroy...");
            return;
        }
        Message.obtain(getHandler(), what, obj).sendToTarget();
    }

    private void initializationServer(BluetoothManager bluetoothManager) {
        try {
            //加密传输，Android强制执行配对，弹窗显示配对码
            BluetoothServerSocket serverSocket = bluetoothManager.getAdapter().listenUsingRfcommWithServiceRecord("", CONN_UUID);
            socket = serverSocket.accept(1000 * 60);
            serverSocket.close();
            sendMessage(MSG_CODE_SOCKET, null);
            // 明文传输(不安全)，无需配对
            // serverSocket = bluetoothManager.getAdapter().listenUsingInsecureRfcommWithServiceRecord("", CONN_UUID);
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(MSG_CODE_DESTROY, null);
        }
    }

    private void initializationClient(BluetoothDevice device) {
        try {
            // 加密传输，Android强制执行配对，弹窗显示配对码
            socket = device.createRfcommSocketToServiceRecord(CONN_UUID);
            sendMessage(MSG_CODE_SOCKET, null);
            //明文传输(不安全)，无需配对
            // clientSocket = device.createInsecureRfcommSocketToServiceRecord(CONN_UUID);
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(MSG_CODE_DESTROY, null);
        }
    }

    private final int MSG_CODE_INIT_CLIENT = 1;
    private final int MSG_CODE_INIT_SERVER = 2;
    private final int MSG_CODE_SOCKET = 3;

    private final int MSG_CODE_DESTROY = Integer.MAX_VALUE;

    @Override public final boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MSG_CODE_INIT_CLIENT:
                initializationClient((BluetoothDevice) msg.obj);
                break;
            case MSG_CODE_INIT_SERVER:
                initializationServer((BluetoothManager) msg.obj);
                break;
            case MSG_CODE_SOCKET:
                break;
            case MSG_CODE_DESTROY:
                getHandler().removeCallbacksAndMessages(null);
                connectedThread.quitSafely();
                break;
            default:

        }
        return true;
    }

}
