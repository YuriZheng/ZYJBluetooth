package com.zyj.bluetooth.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zyj.bluetooth.BaseActivity;
import com.zyj.bluetooth.R;
import com.zyj.bluetooth.databinding.ActivityMainLayoutBinding;

public class MainActivity extends BaseActivity<ActivityMainLayoutBinding, MainViewModel> {

    @Override
    protected void onInnerCreate(@Nullable Bundle savedInstanceState) {
        getDataBinding().setVm(getViewModel());
    }

    @NonNull @Override protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override protected int getMainLayout() {
        return R.layout.activity_main_layout;
    }

}
