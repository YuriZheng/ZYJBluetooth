package com.zyj.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>CREATED ON: 2020/6/5 22:36<br>
 * Author: Yuri.Zheng<br>
 * Email: 497393102@qq.com<br>
 * Description: 权限工具类，将权限申请从{@link android.app.Activity}中脱离开来
 */
public final class PUtil {

    public static final int REQUEST_CODE_PERMISSION = 1024;

    private static final String REQUEST_DATA_PERMISSION_BUNDLE = "_r_d_permission_bundle";
    private static final String REQUEST_DATA_PERMISSION_KEY = "_r_d_permission_key";

    /**
     * 检测是否有权限
     */
    public static boolean checkPermissions(@NonNull Context context, @NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 获取请求权限时的数据
     */
    public static @Nullable PBuilder getBuilder(@NonNull Activity activity) {
        Intent intent = activity.getIntent();
        if (intent == null) {
            return null;
        }
        Bundle bundle = intent.getBundleExtra(REQUEST_DATA_PERMISSION_BUNDLE);
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle.getParcelable(REQUEST_DATA_PERMISSION_KEY);
    }

    /**
     * 请求权限
     */
    public static void requestPermissions(@NonNull Activity activity,
                                          final @NonNull PBuilder permissionBuilder,
                                          final boolean force) {
        showToastDialog(activity, permissionBuilder, force);
    }

    /**
     * 将字符串资源数组组合成字符串信息
     */
    public static String getPermissionMessage(@NonNull Context context, @NonNull @StringRes int[] messages) {
        if (messages.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messages.length; i++) {
            sb.append(context.getResources().getString(messages[i]));
            if (i != messages.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static void showToastDialog(@NonNull Activity activity,
                                        final @NonNull PBuilder permissionBuilder,
                                        final boolean force) {
        if (permissionBuilder.getRequestPermissions().length > 0) {
            new AlertDialog.Builder(activity).setTitle(R.string.bluetooth_permission_title)
                    .setMessage(getPermissionMessage(activity, permissionBuilder.getExplain()))
                    .setOnDismissListener(dialog ->
                            realRequestPermissions(activity, permissionBuilder, force))
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        }
    }

    private static void realRequestPermissions(@NonNull Activity activity,
                                               final @NonNull PBuilder permissionBuilder,
                                               final boolean force) {
        String[] oldPermission = permissionBuilder.getRequestPermissions();
        List<Integer> insertList = new ArrayList<>(oldPermission.length);
        for (int i = 0; i < oldPermission.length; i++) {
            if (!checkPermissions(activity, oldPermission[i])) {
                // 用户没有拒绝过且是强制申请
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, oldPermission[i])) {
                    insertList.add(i);
                } else {
                    // 用户拒绝过了
                    if (!force) {
                        // 应用程序强制申请
                        insertList.add(i);
                    } else {
                        L.d(activity.getClass().getSimpleName(), oldPermission[i] + " ignore");
                    }
                }
            }
        }
        String[] newPermissions = new String[insertList.size()];
        int[] explain = new int[newPermissions.length];
        int[] denied = new int[newPermissions.length];
        for (int i = 0; i < insertList.size(); i++) {
            int index = insertList.get(i);
            newPermissions[i] = oldPermission[index];
            explain[i] = permissionBuilder.getExplain()[index];
            denied[i] = permissionBuilder.getDenied()[index];
        }
        permissionBuilder.setRequestPermissions(newPermissions, explain, denied);
        Intent intent = activity.getIntent();
        if (intent != null) {
            intent.putExtra(REQUEST_DATA_PERMISSION_BUNDLE,
                    insertPermissions(intent.getBundleExtra(REQUEST_DATA_PERMISSION_BUNDLE),
                            permissionBuilder));
        }
        activity.setIntent(intent);
        ActivityCompat.requestPermissions(activity, permissionBuilder.getRequestPermissions(),
                REQUEST_CODE_PERMISSION);
    }

    private static @NonNull Bundle insertPermissions(Bundle bundle,
                                                     PBuilder insertPermissionBuilder) {
        // 将之前已经存在的和现在添加的数据进行合并处理
        if (bundle == null) {
            bundle = new Bundle();
        }
        PBuilder builder = bundle.getParcelable(REQUEST_DATA_PERMISSION_KEY);
        if (builder == null) {
            builder = insertPermissionBuilder;
        } else {
            List<String> existPermissions = Arrays.asList(builder.getRequestPermissions());
            List<Integer> insert = new ArrayList<>();
            for (int i = 0; i < insertPermissionBuilder.getRequestPermissions().length; i++) {
                if (!existPermissions.contains(insertPermissionBuilder.getRequestPermissions()[i])) {
                    insert.add(i);
                }
            }
            int length = builder.getRequestPermissions().length;
            String[] newPermissions = new String[existPermissions.size() + insert.size()];
            System.arraycopy(builder.getRequestPermissions(), 0, newPermissions, 0, length);
            int[] explain = new int[newPermissions.length];
            System.arraycopy(builder.getExplain(), 0, explain, 0, length);
            int[] denied = new int[newPermissions.length];
            System.arraycopy(builder.getDenied(), 0, denied, 0, length);
            for (int i = length; i < newPermissions.length; i++) {
                int index = insert.get(length + i);
                newPermissions[i] = insertPermissionBuilder.getRequestPermissions()[index];
                explain[i] = insertPermissionBuilder.getExplain()[index];
                denied[i] = insertPermissionBuilder.getDenied()[index];
            }
            builder.setRequestPermissions(newPermissions, explain, denied);
        }
        bundle.putParcelable(REQUEST_DATA_PERMISSION_KEY, builder);
        return bundle;
    }

}
