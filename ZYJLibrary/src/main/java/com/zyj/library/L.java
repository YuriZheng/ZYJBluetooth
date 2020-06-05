package com.zyj.library;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-5<br>
 * Description: NON
 */
public final class L {

    private static final boolean debug = true;
    private static final String TAG = "zyj_bluetooth";

    public static boolean debug() {
        return debug;
    }

    public static void i(@NonNull String tag, @NonNull Object message) {
        if (debug()) {
            Log.i(TAG, tag + ": " + message);
        }
    }

    public static void d(@NonNull String tag, @NonNull Object message) {
        if (debug()) {
            Log.d(TAG, tag + ": " + message);
        }
    }

    public static void w(@NonNull String tag, @NonNull Object message) {
        if (debug()) {
            Log.w(TAG, tag + ": " + message);
        }
    }

    public static void e(@NonNull String tag, @NonNull Object message) {
        if (debug()) {
            Log.e(TAG, tag + ": " + message);
        }
    }

    public static void s(@NonNull String tag, @NonNull Object message) {
        if (debug()) {
            System.out.println(tag + ": " + message);
        }
    }

}
