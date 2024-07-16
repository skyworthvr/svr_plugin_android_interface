package com.ssnw.androidapitest.function;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.ssnw.androidapitest.BaseActivity;
import com.ssnwt.playertest.R;
import com.ssnwt.vr.androidmanager.AndroidInterface;
import com.ssnwt.vr.androidmanager.bluetooth.BluetoothUtils;
import com.ssnwt.vr.common.L;
import java.util.ArrayList;
import java.util.List;

public class BluetoothActivity extends BaseActivity {
    private static final String TAG = "BluetoothActivity";
    private static final String START_SEARCH = "Start Search";
    private static final String CANCEL_SEARCH = "Cancel Search";
    private AndroidInterface mAndroidInterface;
    private BluetoothUtils mBluetoothUtils;
    private TextView mTextAppInfos;
    private ListView mUnBondedListView;
    private ListView mBondedListView;
    private BluetoothAdapter mUnBondedAdapter;
    private BluetoothAdapter mBondedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        final Button btnScan = findViewById(R.id.btn_scan);
        mAndroidInterface = AndroidInterface.getInstance();
        mBluetoothUtils = mAndroidInterface.getBluetoothUtils();
        mBluetoothUtils.setListener(new BluetoothUtils.BluetoothListener() {
            @Override
            public void onOpened(boolean isOpen) {
                updateNotify(mTextAppInfos, isOpen ? "onOpened" : "onClosed");
            }

            @Override
            public void onConnected(boolean isConnect) {
                runOnUiThread(() -> mBondedAdapter.notifyDataSetChanged());
                updateNotify(mTextAppInfos, isConnect ? "onConnected" : "onDisconnected");
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                updateListView(device);
                updateNotify(mTextAppInfos,
                    "onDeviceFound:" + device.toString() + ", name:" + device.getName());
            }

            @Override
            public void onBondChanged(BluetoothDevice device) {
                updateListView(device);
                updateNotify(mTextAppInfos, "onBondChanged:" + device.toString());
            }

            @Override
            public void onScanStart() {
                runOnUiThread(() -> btnScan.setText(CANCEL_SEARCH));
                updateNotify(mTextAppInfos, "onScanStart");
            }

            @Override
            public void onScanFinish() {
                runOnUiThread(() -> btnScan.setText(START_SEARCH));
                updateNotify(mTextAppInfos, "onScanFinish");
            }

            @Override
            public void onNoSupportBluetooth() {
                updateNotify(mTextAppInfos, "onNoSupportBluetooth");
            }

            @Override
            public void onBondError(int code) {
                updateNotify(mTextAppInfos, "onBondError");
            }

            @Override
            public void onBluetoothUtilsActive(boolean active) {
                if (active) {
                    for (BluetoothDevice device : mBluetoothUtils.getBondedDevices()) {
                        updateListView(device);
                    }
                }
            }
        });

        mBluetoothUtils.setConnectionStateListener(
            (device, state) -> updateNotify(mTextAppInfos,
                "onConnectionStateChanged: device = " + device + " state = " + state));

        mTextAppInfos = findViewById(R.id.text_info);
        findViewById(R.id.btn_open).setOnClickListener(v -> {
            mBluetoothUtils.open();
            updateNotify(mTextAppInfos, "Opening Bluetooth");
        });
        findViewById(R.id.btn_close).setOnClickListener(v -> {
            mBluetoothUtils.close();
            updateNotify(mTextAppInfos, "Close Bluetooth");
        });
        btnScan.setText(START_SEARCH);
        btnScan.setOnClickListener(v -> {
            if (mBluetoothUtils.isOpen()) {
                if (mBluetoothUtils.isSearching()) {
                    mBluetoothUtils.cancelSearch();
                } else {
                    mUnBondedAdapter.clear();
                    mBluetoothUtils.search();
                }
            } else {
                updateNotify(mTextAppInfos, "Please open bluetooth");
            }
        });

        mUnBondedListView = findViewById(R.id.list);
        mUnBondedAdapter = new BluetoothAdapter();
        mUnBondedListView.setAdapter(mUnBondedAdapter);
        mUnBondedListView.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice device = mUnBondedAdapter.getItem(position);
            onDeviceItemClick(device);
        });

        mBondedListView = findViewById(R.id.bonded_list);
        mBondedAdapter = new BluetoothAdapter();
        mBondedListView.setAdapter(mBondedAdapter);
        mBondedListView.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice device = mBondedAdapter.getItem(position);
            onDeviceItemClick(device);
        });
        mBondedListView.setOnItemLongClickListener((parent, view, position, id) -> {
            BluetoothDevice device = mBondedAdapter.getItem(position);
            mBluetoothUtils.unbond(device);
            return false;
        });
    }

    private void updateListView(BluetoothDevice device) {
        if (isBonded(device)) {
            mUnBondedAdapter.removeData(device);
            mBondedAdapter.addData(device);
        } else {
            mUnBondedAdapter.addData(device);
            mBondedAdapter.removeData(device);
        }
    }

    private void onDeviceItemClick(BluetoothDevice device) {
        int bondState = device.getBondState();
        if (mBluetoothUtils.isDeviceConnected(device)) {
            mBluetoothUtils.disconnectDevice(device);
        } else if (bondState == BluetoothDevice.BOND_BONDED) {
            mBluetoothUtils.connectDevice(device);
        } else if (bondState == BluetoothDevice.BOND_NONE) {
            mBluetoothUtils.bond(device);
        }
    }

    @Override
    protected void updateNotify(TextView text, String info) {
        super.updateNotify(text, info);
        L.d(TAG, info);
    }

    private boolean isBonded(BluetoothDevice device) {
        return device.getBondState() == BluetoothDevice.BOND_BONDED;
    }

    private boolean isConnected(BluetoothDevice device) {
        return mBluetoothUtils.isDeviceConnected(device);
    }

    private String getStateStr(BluetoothDevice device) {
        if (isBonded(device)) {
            if (isConnected(device)) {
                return "Connected";
            }
            return "Bonded";
        }
        return "unBonded";
    }

    private String getTypeStr(BluetoothDevice device) {
        String type = "";
        switch (device.getType()) {
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                type = "UNKNOWN";
                break;
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                type = "CLASSIC";
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                type = "LE";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                type = "DUAL";
                break;
            default:
                break;
        }
        return type;
    }

    private class BluetoothAdapter extends BaseAdapter {
        private List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();

        public BluetoothAdapter() {
        }

        public void addData(BluetoothDevice device) {
            if (TextUtils.isEmpty(device.getName()) || mBluetoothDevices.contains(device)) {
                return;
            }
            mBluetoothDevices.add(device);
            runOnUiThread(() -> notifyDataSetChanged());
        }

        public void removeData(BluetoothDevice device) {
            if (TextUtils.isEmpty(device.getName()) || !mBluetoothDevices.contains(device)) {
                return;
            }
            mBluetoothDevices.remove(device);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        public void clear() {
            mBluetoothDevices.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getCount() {
            return mBluetoothDevices.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return mBluetoothDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView =
                    LayoutInflater.from(BluetoothActivity.this).
                        inflate(R.layout.list_item, parent, false);
                holder.title = convertView.findViewById(R.id.text_title);
                holder.subtitle = convertView.findViewById(R.id.text_subtitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BluetoothDevice device = mBluetoothDevices.get(position);
            holder.title.setText(device.getName());
            holder.subtitle.setText(
                device.getAddress() + "-" + getStateStr(device) + "-" + getTypeStr(device));
            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public TextView subtitle;
        }
    }
}
