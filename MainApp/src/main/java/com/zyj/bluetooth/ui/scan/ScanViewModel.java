package com.zyj.bluetooth.ui.scan;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zyj.bluetooth.R;
import com.zyj.library.L;
import com.zyj.library.P;
import com.zyj.library.PBuilder;
import com.zyj.library.PUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class ScanViewModel extends AndroidViewModel {

    public final MutableLiveData<Boolean> finish = new MutableLiveData<>();
    public final MutableLiveData<Boolean> scaning = new MutableLiveData<>();
    public final MutableLiveData<Integer> mode = new MutableLiveData<>();
    public final MutableLiveData<Boolean> modeEnable = new MutableLiveData<>();
    public final MutableLiveData<Set<MyBluetoothDevice>> bDevices = new MutableLiveData<>();
    private final BluetoothAdapter bluetoothAdapter;
    private final int TIME = 120; // 秒
    private final Handler handler = new Handler();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                scaning.postValue(bluetoothAdapter.isDiscovering());
                bDevices.getValue().add(new MyBluetoothDevice(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE),
                        false));
                bDevices.postValue(bDevices.getValue());
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                int oldMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
                int newMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, -1);
                if (newMode == -1) {
                    return;
                }
                setScanMode(newMode);
            }
        }
    };

    private final Runnable stopScanTask = new Runnable() {
        @Override public void run() {
            stopScanBluetooth();
        }
    };

    public ScanViewModel(@NonNull Application application) {
        super(application);
        finish.setValue(false);
        bDevices.setValue(new HashSet<>());
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setScanMode(bluetoothAdapter.getScanMode());
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        application.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override protected void onCleared() {
        super.onCleared();
        stopScanBluetooth();
        getApplication().unregisterReceiver(receiver);
        if (L.debug()) {
            L.d(ScanViewModel.class.getSimpleName(), "onCleared...");
        }
    }

    final void onResume() {
        scaning.setValue(bluetoothAdapter != null && bluetoothAdapter.isDiscovering());
    }

    final void onPause() {
        stopScanBluetooth();
    }

    public final void scanClick(View view) {
        startScanBluetooth((AppCompatActivity) view.getContext());
    }

    public final void stopScanClick(View view) {
        stopScanBluetooth();
    }

    public final void openDiscoveryClick(View view) {
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, TIME);
        view.getContext().startActivity(discoverableIntent);
    }

    private void setScanMode(int newMode) {
        if (newMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            mode.postValue(R.string.bluetooth_scan_mode_connectable_discoverable);
            modeEnable.setValue(false);
        } else if (newMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE) {
            mode.postValue(R.string.bluetooth_scan_mode_scan_mode_connectable);
            modeEnable.setValue(false);
        } else if (newMode == BluetoothAdapter.SCAN_MODE_NONE) {
            mode.postValue(R.string.bluetooth_scan_mode_none);
            modeEnable.setValue(true);
        }
    }

    private boolean isDiscovering() {
        return bluetoothAdapter != null && bluetoothAdapter.isDiscovering()
                && scaning.getValue() != null && scaning.getValue();
    }

    private void startScanBluetooth(AppCompatActivity activity) {
        bDevices.getValue().clear();
        if (isDiscovering()) {
            return;
        }
        if (!PUtil.checkPermissions(activity, P.PERMISSION_ACCESS_FINE_LOCATION)) {
            requestPermission(activity);
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            new AlertDialog.Builder(activity).setMessage(R.string.bluetooth_open_title)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activity.startActivityForResult(enableBtIntent, 1);
                    })
                    .create().show();
            return;
        }
        scaning.postValue(true);
        // 开始真正的扫描
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            bDevices.getValue().add(new MyBluetoothDevice(device, true));
        }
        bDevices.postValue(bDevices.getValue());
        bluetoothAdapter.startDiscovery();
        handler.removeCallbacks(stopScanTask);
        handler.postDelayed(stopScanTask, TIME / 10 * 1000);
    }

    private void stopScanBluetooth() {
        handler.removeCallbacks(stopScanTask);
        if (isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        scaning.postValue(false);
    }

    private void requestPermission(Activity activity) {
        PBuilder builder = PBuilder.newBuilder(
                new String[]{P.PERMISSION_ACCESS_FINE_LOCATION},
                new int[]{R.string.bluetooth_access_fine_location_explain},
                new int[]{R.string.bluetooth_access_fine_location_denied});
        PUtil.requestPermissions(activity, builder, false);
    }

    public static class MyBluetoothDevice {

        public final BluetoothDevice device;
        public final boolean isBonded;

        public MyBluetoothDevice(BluetoothDevice device, boolean isBonded) {
            this.device = device;
            this.isBonded = isBonded;
        }

        @Override public int hashCode() {
            return device.hashCode();
        }

        @Override public boolean equals(@Nullable Object obj) {
            if (obj instanceof MyBluetoothDevice) {
                return this.device.equals(((MyBluetoothDevice) obj).device);
            }
            return false;
        }
    }
}
