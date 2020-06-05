package com.zyj.bluetooth.ui.scan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zyj.bluetooth.BaseActivity;
import com.zyj.bluetooth.R;
import com.zyj.bluetooth.databinding.ActivityScanLayoutBinding;
import com.zyj.library.PBuilder;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class ScanActivity extends BaseActivity<ActivityScanLayoutBinding, ScanViewModel> {

    @Override
    protected void onInnerCreate(@Nullable Bundle savedInstanceState) {
        getDataBinding().setVm(getViewModel());
    }

    @Override protected void onStart() {
        super.onStart();

    }

    @Override protected void onRequestPermissionDenied(@NonNull PBuilder deniedBuilder) {
        super.onRequestPermissionDenied(deniedBuilder);

//        if (permissions.contains(P.PERMISSION_ACCESS_FINE_LOCATION)) {
//            Toast.makeText(this, R.string.bluetooth_access_fine_location_explain, Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }

    @NonNull @Override protected Class<ScanViewModel> getViewModelClass() {
        return ScanViewModel.class;
    }

    @Override protected int getMainLayout() {
        return R.layout.activity_scan_layout;
    }
}