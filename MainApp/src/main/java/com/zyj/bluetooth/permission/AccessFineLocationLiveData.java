package com.zyj.bluetooth.permission;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import com.zyj.bluetooth.R;
import com.zyj.library.P;
import com.zyj.library.PUtil;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class AccessFineLocationLiveData extends LiveData<Boolean> {

    private void requestPermission(Activity activity) {
        if (!PUtil.checkPermissions(activity, P.PERMISSION_ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(activity).setTitle(R.string.bluetooth_permission_title)
                    .setMessage(R.string.bluetooth_access_fine_location_explain)
                    .setOnDismissListener(dialog -> {
                        PUtil.PermissionBuilder builder = PUtil.PermissionBuilder.newBuilder(
                                new String[]{P.PERMISSION_ACCESS_FINE_LOCATION},
                                new int[]{R.string.bluetooth_access_fine_location_explain},
                                new int[]{R.string.bluetooth_access_fine_location_denied});
                        PUtil.requestPermissions(activity, builder, false);
                    })
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        } else {
//            getViewModel().startScanBluetooth();
        }
    }

}
