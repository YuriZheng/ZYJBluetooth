package com.zyj.library;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;

/**
 * <p>CREATED ON: 2020/6/6 00:00<br>
 * Author: Yuri.Zheng<br>
 * Email: 497393102@qq.com<br>
 * Description: NON
 */
public final class PBuilder implements Parcelable {


    private String[] requestPermissions;
    private @StringRes int[] explain;
    private @StringRes int[] denied;

    public final String[] getRequestPermissions() {
        return requestPermissions;
    }

    public static PBuilder newBuilder(String[] requestPermissions,
                                      int[] explain, int[] denied) {
        PBuilder builder = new PBuilder();
        builder.setRequestPermissions(requestPermissions, explain, denied);
        return builder;
    }

    public final PBuilder setRequestPermissions(String[] requestPermissions,
                                                int[] explain, int[] denied) {
        if (requestPermissions.length != explain.length ||
                requestPermissions.length != denied.length) {
            throw new IllegalArgumentException("Permissions request length error: " +
                    requestPermissions.length + "-" + explain.length + "-" + denied.length);
        }
        this.requestPermissions = requestPermissions;
        this.explain = explain;
        this.denied = denied;
        return this;
    }

    public final int[] getExplain() {
        return explain;
    }

    public final int[] getDenied() {
        return denied;
    }

    private PBuilder() {
    }

    private PBuilder(Parcel in) {
        in.readStringArray(requestPermissions);
        in.readIntArray(explain);
        in.readIntArray(denied);
    }

    public static final Creator<PBuilder> CREATOR = new Creator<PBuilder>() {
        @Override
        public PBuilder createFromParcel(Parcel in) {
            return new PBuilder(in);
        }

        @Override
        public PBuilder[] newArray(int size) {
            return new PBuilder[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(requestPermissions);
        dest.writeIntArray(explain);
        dest.writeIntArray(denied);
    }

}
