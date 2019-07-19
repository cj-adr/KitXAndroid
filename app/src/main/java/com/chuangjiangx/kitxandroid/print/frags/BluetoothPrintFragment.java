package com.chuangjiangx.kitxandroid.print.frags;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chuangjiangx.kitxandroid.R;
import com.chuangjiangx.print.PrintLogUtils;
import com.chuangjiangx.print.PrintSupport;
import com.chuangjiangx.print.impl.bluetooth.BluetoothPrinter;
import com.chuangjiangx.print.size.Print80Size;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 蓝牙打印
 */
public class BluetoothPrintFragment extends BasePrintFragment {

    // 蓝牙设备列表
    private LinkedHashMap<String, BluetoothDevice> mBluetoothList = new LinkedHashMap<>();
    private BluetoothListAdapter mAdapter;
    private List<BluetoothDevice> mData = new ArrayList<>();
    private boolean isConnect = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        registerReceiver();
    }

    @Override
    public void onDetach() {
        unregisterReceiver();
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_print_bluetooth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_scan).setOnClickListener(v -> findBluetoothDevice());
        view.findViewById(R.id.btn_print).setOnClickListener(v -> {
            if (isConnect) {
                print();
                return;
            }

            Toast.makeText(mContext, "请先连接蓝牙打印机！", Toast.LENGTH_SHORT).show();
        });

        mAdapter = new BluetoothListAdapter(mData, getOnItemClick());
        RecyclerView listView = view.findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(mContext));
        listView.setAdapter(mAdapter);
    }

    private OnItemClickListener getOnItemClick() {
        return this::connectDevice;
    }

    private void registerReceiver() {
        try {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            mContext.registerReceiver(receiver, filter);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    private void unregisterReceiver() {
        try {
            mContext.unregisterReceiver(receiver);

        } catch (Exception e) {
            PrintLogUtils.e(e, "");
        }
    }

    /**
     * 查找蓝牙设备
     */
    private void findBluetoothDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        // 开始搜索
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 广播接收器
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!TextUtils.isEmpty(device.getName()) && !mBluetoothList.containsKey(device.getAddress())) {
                    mBluetoothList.put(device.getAddress(), device);
                    PrintLogUtils.d("找到蓝牙设备：" + device.getName() + " | " + (BluetoothDevice.BOND_BONDED == device.getBondState()) + " | " + device.getAddress());
                    PrintLogUtils.d("蓝牙设备列表数目：" + mBluetoothList.size());
                }

                return;
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                PrintLogUtils.d("蓝牙设备搜索完成！");

                handleBluetoothList();
            }
        }
    };

    /**
     * 处理蓝牙列表
     */
    private void handleBluetoothList() {
        if (null == mBluetoothList || mBluetoothList.isEmpty()) {
            PrintLogUtils.d("没有找到蓝牙设备！");
            return;
        }

        mData.clear();
        for (Map.Entry<String, BluetoothDevice> entry : mBluetoothList.entrySet()) {
            mData.add(entry.getValue());
        }

        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 连接设备
     */
    private void connectDevice(BluetoothDevice device) {
        PrintSupport.getInstance().init(mContext, new BluetoothPrinter(device.getAddress(), getBluetoothConnectListener()), new Print80Size());
    }

    /**
     * 蓝牙连接监听
     */
    private BluetoothPrinter.BluetoothConnectListener getBluetoothConnectListener() {
        return new BluetoothPrinter.BluetoothConnectListener() {

            @Override
            public void onConnectSuccess(String address) {

            }

            @Override
            public void onConnectFail(String address, Throwable e) {

            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(BluetoothDevice device);
    }

    private class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.ViewHolder> {

        List<BluetoothDevice> mData;
        OnItemClickListener mListener;

        BluetoothListAdapter(List<BluetoothDevice> data, OnItemClickListener listener) {
            this.mData = data;
            this.mListener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bluetooth, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BluetoothDevice info = mData.get(position);
            holder.mTvDevice.setText(info.getName());

            holder.itemView.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.onItemClick(info);
                }
            });
        }

        @Override
        public int getItemCount() {
            return null == mData ? 0 : mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTvDevice;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                mTvDevice = itemView.findViewById(R.id.tv_device);
            }

        }
    }

}
