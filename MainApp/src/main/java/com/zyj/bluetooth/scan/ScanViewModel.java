package com.zyj.bluetooth.scan;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zyj.bluetooth.permission.AccessFineLocationLiveData;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class ScanViewModel extends AndroidViewModel {

    public final MutableLiveData<Boolean> scanning = new MutableLiveData<>();
    public final AccessFineLocationLiveData permission = new AccessFineLocationLiveData();

    public ScanViewModel(@NonNull Application application) {
        super(application);
        scanning.setValue(false);
    }

    public final void reScanClick(View view) {

    }

    public final void startScanBluetooth() {
        if (scanning.getValue()) {
            return;
        }

    }
}
