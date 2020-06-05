package com.zyj.bluetooth.scan;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.zyj.bluetooth.BaseActivity;
import com.zyj.bluetooth.R;
import com.zyj.bluetooth.databinding.ActivityScanLayoutBinding;
import com.zyj.library.P;

import java.util.List;

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
        if (!checkPermissions(P.PERMISSION_ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this).setTitle(R.string.bluetooth_permission_title)
                    .setMessage(R.string.bluetooth_access_fine_location_explain)
                    .setOnDismissListener(dialog -> requestPermissions(new String[]{P.PERMISSION_ACCESS_FINE_LOCATION}, false))
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        } else {
            getViewModel().startScanBluetooth();
        }
    }

    @Override protected void onRequestPermissionDenied(@NonNull List<String> permissions) {
        if (permissions.contains(P.PERMISSION_ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, R.string.bluetooth_access_fine_location_explain, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @NonNull @Override protected Class<ScanViewModel> getViewModelClass() {
        return ScanViewModel.class;
    }

    @Override protected int getMainLayout() {
        return R.layout.activity_scan_layout;
    }
}
