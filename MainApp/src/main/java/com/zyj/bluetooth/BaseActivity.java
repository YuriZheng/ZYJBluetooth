package com.zyj.bluetooth;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zyj.library.PBuilder;
import com.zyj.library.PUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zyj.library.PUtil.REQUEST_CODE_PERMISSION;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public abstract class BaseActivity<T extends ViewDataBinding, D extends AndroidViewModel> extends AppCompatActivity {

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

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PBuilder builder = PUtil.getBuilder(this);
        if (builder == null) {
            return;
        }
        List<Integer> deniedList = new ArrayList<>(permissions.length);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedList.add(i);
                }
            }
        }
        String[] deniedPermissions = new String[deniedList.size()];
        int[] explain = new int[deniedList.size()];
        int[] denied = new int[deniedList.size()];

        for (int i = 0; i < deniedList.size(); i++) {
            String p = permissions[deniedList.get(i)];
            int index = -1;
            for (int j = 0; j < builder.getRequestPermissions().length; j++) {
                if (builder.getRequestPermissions()[j].equals(p)) {
                    index = j;
                    break;
                }
            }
            if (index < 0) {
                throw new IllegalArgumentException("Can't find the permissions, check the request");
            }
            deniedPermissions[i] = builder.getRequestPermissions()[index];
            explain[i] = builder.getExplain()[i];
            denied[i] = builder.getDenied()[i];
        }

        onRequestPermissionDenied(PBuilder.newBuilder(deniedPermissions, explain, denied));
    }

    /**
     * 被用户拒绝的权限集合
     */
    protected void onRequestPermissionDenied(@NonNull PBuilder deniedBuilder) {
        // Do nothing
    }

    protected abstract void onInnerCreate(@Nullable Bundle savedInstanceState);

    protected abstract @NonNull Class<D> getViewModelClass();

    protected abstract @LayoutRes int getMainLayout();

}
