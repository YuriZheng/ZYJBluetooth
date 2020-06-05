package com.zyj.bluetooth;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zyj.library.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public abstract class BaseActivity<T extends ViewDataBinding, D extends AndroidViewModel> extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION = 1024;

    private T dataBinding;
    private D viewModel;

    @Override protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getMainLayout());
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                .create(getViewModelClass());
        onInnerCreate(savedInstanceState);
        dataBinding.setLifecycleOwner(this);
    }

    public final @NonNull D getViewModel() {
        return viewModel;
    }

    public final @NonNull T getDataBinding() {
        return dataBinding;
    }

    /**
     * 检测是否有权限
     */
    protected final boolean checkPermissions(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求权限
     */
    protected final void requestPermissions(final @NonNull String[] permissions, final boolean force) {
        List<String> newPermission = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (!checkPermissions(permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    newPermission.add(permission);
                } else {
                    // 用户拒绝过了
                    if (!force) {
                        // 应用程序强制申请
                        newPermission.add(permission);
                    } else {
                        L.d(BaseActivity.class.getSimpleName(), permission + " ignore");
                    }
                }
            }
        }
        String[] s = new String[newPermission.size()];
        for (int i = 0; i < newPermission.size(); i++) {
            s[i] = newPermission.get(i);
        }
        ActivityCompat.requestPermissions(this, s, REQUEST_CODE_PERMISSION);
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> deniedPermissions = new ArrayList<>(permissions.length);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
        }
        onRequestPermissionDenied(deniedPermissions);
    }

    /**
     * 被用户拒绝的权限集合
     */
    protected void onRequestPermissionDenied(@NonNull List<String> permissions) {
        // Do nothing
    }

    protected abstract void onInnerCreate(@Nullable Bundle savedInstanceState);

    protected abstract @NonNull Class<D> getViewModelClass();

    protected abstract @LayoutRes int getMainLayout();

}
