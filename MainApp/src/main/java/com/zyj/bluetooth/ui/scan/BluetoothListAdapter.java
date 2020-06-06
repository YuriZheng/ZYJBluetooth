package com.zyj.bluetooth.ui.scan;

import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.zyj.bluetooth.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: yuri.zheng<br>
 * Email: zhengyujie@wps.cn<br>
 * Date: 20-6-6<br>
 * Description: NON
 */
public final class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.MyHolder> {

    private final List<ScanViewModel.MyBluetoothDevice> datas = new ArrayList<>();

    public void setData(Set<ScanViewModel.MyBluetoothDevice> d) {
        datas.clear();
        datas.addAll(d);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public BluetoothListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_scan_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothListAdapter.MyHolder holder, int position) {
        ScanViewModel.MyBluetoothDevice device = datas.get(position);
        holder.name.setText(device.device.getName());
        holder.address.setText(device.device.getAddress());
        holder.connect.setVisibility(device.isBonded ? View.VISIBLE : View.GONE);
        holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // 开始自动连接
            }
        });
        holder.itemView.setBackgroundResource(R.drawable.ripple_selector);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        AppCompatTextView name;
        AppCompatTextView address;
        AppCompatButton connect;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            connect = itemView.findViewById(R.id.connect);
        }
    }

}
