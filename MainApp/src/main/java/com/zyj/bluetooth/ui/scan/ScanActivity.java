package com.zyj.bluetooth.ui.scan;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zyj.bluetooth.BaseActivity;
import com.zyj.bluetooth.R;
import com.zyj.bluetooth.databinding.ActivityScanLayoutBinding;

import java.util.Set;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class ScanActivity extends BaseActivity<ActivityScanLayoutBinding, ScanViewModel> {

    private BluetoothListAdapter adapter;

    @Override
    protected void onInnerCreate(@Nullable Bundle savedInstanceState) {
        getViewModel().finish.observe(this, aBoolean -> {
            if (aBoolean) {
                finish();
            }
        });
        getDataBinding().setVm(getViewModel());

        getDataBinding().list.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        getDataBinding().list.setAdapter(adapter = new BluetoothListAdapter());
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        getDataBinding().list.addItemDecoration(decoration);
        getViewModel().bDevices.observe(this, new Observer<Set<ScanViewModel.MyBluetoothDevice>>() {
            @Override
            public void onChanged(Set<ScanViewModel.MyBluetoothDevice> myBluetoothDevices) {
                adapter.setData(myBluetoothDevices);
            }
        });
    }

    @Override protected void onResume() {
        super.onResume();
        getViewModel().onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        getViewModel().onPause();
    }

    @NonNull @Override protected Class<ScanViewModel> getViewModelClass() {
        return ScanViewModel.class;
    }

    @Override protected int getMainLayout() {
        return R.layout.activity_scan_layout;
    }
}
