package com.zyj.bluetooth.ui.main;


import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.zyj.bluetooth.ui.scan.ScanActivity;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class MainViewModel extends AndroidViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public final void onJumpScanClick(View view) {
        // statusText.setValue("正在扫描");
        view.getContext().startActivity(new Intent(view.getContext(), ScanActivity.class));
    }

}
