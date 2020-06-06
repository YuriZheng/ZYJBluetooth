package com.zyj.bluetooth.ui.scan;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zyj.bluetooth.R;
import com.zyj.library.P;
import com.zyj.library.PBuilder;
import com.zyj.library.PUtil;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class ScanViewModel extends AndroidViewModel {

    public final MutableLiveData<Boolean> scanning = new MutableLiveData<>();
    private final BluetoothAdapter bluetoothAdapter;

    public ScanViewModel(@NonNull Application application) {
        super(application);
        scanning.setValue(false);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public final void reScanClick(View view) {
        stopScanBluetooth();
        startScanBluetooth((AppCompatActivity) view.getContext());
    }

    public final void startScanBluetooth(AppCompatActivity activity) {
        if (scanning.getValue()) {
            return;
        }
        if (!PUtil.checkPermissions(activity, P.PERMISSION_ACCESS_FINE_LOCATION)) {
            requestPermission(activity);
            scanning.setValue(false);
            return;
        }

        if (!this.bluetoothAdapter.isEnabled()) {
            new AlertDialog.Builder(activity).setMessage(R.string.bluetooth_open_title)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activity.startActivityForResult(enableBtIntent, 1);
                    })
                    .create().show();
            scanning.setValue(false);
            return;
        }
        // 开始真正的扫描

    }

    private void stopScanBluetooth() {
        if (!scanning.getValue()) {
            return;
        }
    }

    private void requestPermission(Activity activity) {
        if (!PUtil.checkPermissions(activity, P.PERMISSION_ACCESS_FINE_LOCATION)) {

            PBuilder builder = PBuilder.newBuilder(
                    new String[]{P.PERMISSION_ACCESS_FINE_LOCATION},
                    new int[]{R.string.bluetooth_access_fine_location_explain},
                    new int[]{R.string.bluetooth_access_fine_location_denied});
            PUtil.requestPermissions(activity, builder, false);
        } else {
//            getViewModel().startScanBluetooth();
        }
    }
}
