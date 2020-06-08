package com.zyj.bluetooth.ui.main;


import android.app.Application;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zyj.bluetooth.ui.scan.ScanActivity;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class MainViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> scanEnable = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);

        BluetoothManager bluetoothManager = (BluetoothManager) application.getSystemService(Context.BLUETOOTH_SERVICE);
        scanEnable.setValue(bluetoothManager != null && bluetoothManager.getAdapter() != null);
    }

    public final void onJumpScanClick(View view) {
        // statusText.setValue("正在扫描");
        view.getContext().startActivity(new Intent(view.getContext(), ScanActivity.class));
    }

}
