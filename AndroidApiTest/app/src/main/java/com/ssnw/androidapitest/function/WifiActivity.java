package com.ssnw.androidapitest.function;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.ssnw.androidapitest.BaseActivity;
import com.ssnwt.playertest.R;
import com.ssnwt.vr.androidmanager.AndroidInterface;
import com.ssnwt.vr.androidmanager.wifi.WifiInfo;
import com.ssnwt.vr.androidmanager.wifi.WifiUtils;
import com.ssnwt.vr.common.L;
import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends BaseActivity {
    private static final String TAG = "WifiActivity";
    int spinnerIndex = -1;
    private AndroidInterface mAndroidInterface;
    private WifiUtils wifiUtils;
    private TextView mTextAppInfos;
    private ListView mListView;
    private WifiAdapter wifiAdapter;
    private int connectStatus = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAndroidInterface = AndroidInterface.getInstance();
        wifiUtils = mAndroidInterface.getWifiUtils();

        wifiUtils.setListener2(new WifiUtils.WifiListener2() {
            @Override
            public void onRssiLevelChanegd(int rssi) {
                updateStatus("update rssi : " + rssi);
            }

            @Override
            public void onOpened(boolean open) {
                if (!open) {
                    connectStatus = -1;
                    wifiAdapter.clear();
                }
                updateStatus(open ? "Wifi is opened" : "Wifi is closed");
            }

            @Override
            public void onConnecting(int status, String ssid) {
                connectStatus = status;
                updateStatus("connect status changed ssid="
                    + ssid
                    + ", status="
                    + wifiUtils.getConnectionStateName(status));
            }

            @Override public void onSearchResult(ArrayList<WifiInfo> wifiList) {
                wifiAdapter.setData(wifiList);
            }
        });

        mTextAppInfos = findViewById(R.id.text_info);
        findViewById(R.id.btn_open_wifi).setOnClickListener(v -> {
                wifiUtils.openWifi();
                updateStatus("Opening Wifi");
            }
        );
        findViewById(R.id.btn_close_wifi).setOnClickListener(v -> {
                wifiUtils.closeWifi();
                updateStatus("Close Wifi");
            }
        );
        findViewById(R.id.btn_scan_wifi).setOnClickListener(v -> {
                if (wifiUtils.isOpenWifi()) {
                    wifiUtils.searchWifi();
                    updateStatus("Start search wifi");
                }
            }
        );
        findViewById(R.id.btn_add_network).setOnClickListener(v -> showAddNetwokDialog());

        mListView = findViewById(R.id.list);
        wifiAdapter = new WifiAdapter();
        mListView.setAdapter(wifiAdapter);
        mListView.setOnItemClickListener(
            (parent, view, position, id) -> show(wifiAdapter.getItem(position)));
    }

    private void updateStatus(String... log) {
        if (log != null && log.length > 0) {
            L.d(TAG, log[0]);
        }
        WifiInfo wifiInfo = wifiUtils.getConnectedWifi2();
        StringBuilder builder = new StringBuilder();
        builder.append("status: " + wifiUtils.isOpenWifi() + "\n")
            .append(
                "connection status: " + wifiUtils.getConnectionStateName(connectStatus) + "\n");
        if (wifiInfo != null) {
            builder.append("current ssid: " + wifiInfo.getSSID());
        }
        updateNotify(mTextAppInfos, builder.toString());
    }

    private void showAddNetwokDialog() {
        View view = getLayoutInflater().inflate(R.layout.add_wifi_network, null);
        EditText edSsid = view.findViewById(R.id.et_ssid);
        EditText edPassword = view.findViewById(R.id.et_password);
        Spinner spinner = view.findViewById(R.id.net_security);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.wifi_security_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerIndex = position;
                L.d(TAG, "spinnerIndex=" + spinnerIndex + ", id=" + id);
                if (spinnerIndex == 0) {
                    edPassword.setVisibility(View.GONE);
                } else {
                    edPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setView(view)
            .setTitle("Add wifi network")
            .setPositiveButton("connect", (dialog, which) -> {
                String ssid = edSsid.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                wifiUtils.addNetwork(ssid, spinnerIndex, password);
            })
            .setNegativeButton("cancel", (dialog, which) -> {
            });
        builder.show();
    }

    private void show(final WifiInfo info) {
        String connectStr = wifiUtils.getConnectedWifi();
        if (connectStr != null) {
            WifiInfo connectInfo = WifiInfo.parseFromJson(connectStr);
            L.d(TAG, "cur ssid: "
                + connectInfo.getSSID()
                + ", click ssid: "
                + info.getSSID()
                + ", bssid: "
                + info.getBSSID()
                + ", netId: "
                + info.getNetworkID());
            if (info.getSSID().equals(connectInfo.getSSID().replace("\"", ""))) {
                new AlertDialog.Builder(this)
                    .setTitle("Disconnect of forget")
                    .setPositiveButton("disconnect", (dialog, which) -> wifiUtils.disconnectWifi())
                    .setNegativeButton("forget", (dialog, which) -> wifiUtils.forget())
                    .show();
                return;
            }
        }

        final EditText mEdit = new EditText(this);
        mEdit.setHint("Please input password");
        new AlertDialog.Builder(this).setTitle("Wifi")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(mEdit)
            .setPositiveButton("Connect", (dialog, which) -> {
                    String input = mEdit.getText().toString();
                    boolean isNewPassword = false;
                    if (input.length() < 8) {
                        updateStatus("Password length need 8.");
                    } else {
                        isNewPassword = true;
                        wifiUtils.connectWifi(info.getSSID(), info.getBSSID(),
                            info.getCapabilities(), input);
                    }

                    if (!isNewPassword) {
                        wifiUtils.connectWifi(info.getNetworkID());
                    }
                }
            )
            .setNegativeButton("Forget", (dialog, which) -> {
                    if (info.getNetworkID() >= 0) {
                        wifiUtils.forget(info.getNetworkID());
                    }
                }
            ).show();
    }

    private class WifiAdapter extends BaseAdapter {
        private List<WifiInfo> wifiInfos = new ArrayList<>();

        public WifiAdapter() {
        }

        public void setData(List<WifiInfo> infos) {
            wifiInfos = infos;
            runOnUiThread(() -> notifyDataSetChanged());
        }

        public void clear() {
            wifiInfos.clear();
            runOnUiThread(() -> notifyDataSetChanged());
        }

        @Override
        public int getCount() {
            return wifiInfos.size();
        }

        @Override
        public WifiInfo getItem(int position) {
            return wifiInfos.get(position);
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
                convertView = LayoutInflater.from(WifiActivity.this).
                    inflate(R.layout.list_item, parent, false);
                holder.title = convertView.findViewById(R.id.text_title);
                holder.subtitle = convertView.findViewById(R.id.text_subtitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            WifiInfo info = wifiInfos.get(position);
            holder.title.setText(info.getSSID());
            String subtitle = info.getRssi() + ":";
            if (info.getWifiStatus() == 1) {
                subtitle += "Saved";
            } else if (info.getWifiStatus() == 2) {
                subtitle += "connected";
            }
            holder.subtitle.setText(subtitle);
            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public TextView subtitle;
        }
    }
}
