package com.ssnw.androidapitest.function;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.packageinstaller.permission.aidl.PermissionGroupInfo;
import com.ssnw.androidapitest.BaseActivity;
import com.ssnwt.playertest.R;
import com.ssnwt.vr.androidmanager.AndroidInterface;
import com.ssnwt.vr.androidmanager.PermissionUtils;
import com.ssnwt.vr.androidmanager.app.JAppInfo;
import com.ssnwt.vr.common.L;
import com.ssnwt.vr.common.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppManagerActivity extends BaseActivity {
    private static final String TAG = "AppManagerActivity";
    private AndroidInterface androidInterface;
    private TextView textAppInfos;
    private int index = 0;
    private GridView listView;
    private AppAdapter appAdapter;
    private List<JAppInfo> appInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        textAppInfos = findViewById(R.id.text_info);

        androidInterface = AndroidInterface.getInstance();
        androidInterface.init(getApplication());
        androidInterface.getAppUtils().scanApps();
        androidInterface.getAppUtils().setListener(
            count -> updateNotify(textAppInfos, "have " + count + " apps."));
        androidInterface.getApkUtils().setListener((eventCode, msg, info) -> {
            if (info == null) {
                updateNotify(textAppInfos, eventCode + ":" + msg);
            } else {
                updateNotify(textAppInfos, eventCode + ":" + msg + ":" + info.toString());
            }
        });

        listView = findViewById(R.id.list);
        appAdapter = new AppAdapter();
        listView.setAdapter(appAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please choose your opt!");
            builder.setMessage("Please choose your opt!");
            builder.setPositiveButton("start app",
                (dialog, which) -> androidInterface.getAppUtils().startApp(
                    appInfos.get(position).getPackageName(),
                    appInfos.get(position).getClassName()));
            builder.setNegativeButton("uninstall", (dialog, which) -> androidInterface.getApkUtils()
                .uninstallApk(appInfos.get(position).getPackageName()));
            builder.setNeutralButton("cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            managerPermissions(appInfos.get(position).getPackageName());
            return true;
        });

        findViewById(R.id.btn_get_app_infos).setOnClickListener(v -> {
            String text = "";
            updateNotify(textAppInfos, text);
            if (appInfos != null) {
                int lastLength = appInfos.size();
                if (index + lastLength >= androidInterface.getAppUtils().getAppCount()) {
                    index = 0;
                } else {
                    index += 15;
                }
            }
            String jsonAppinfos = androidInterface.getAppUtils().getAppInfos(index, 15);
            appInfos = Utils.jsonToList(jsonAppinfos, JAppInfo.class);
            if (appInfos != null) {
                L.d(TAG, "size:"
                    + appInfos.size()
                    + ", index="
                    + index
                    + ", total="
                    + androidInterface.getAppUtils().getAppCount());
                for (JAppInfo ai : appInfos) {
                    text = text + ai.toString() + "\n";
                }
                updateNotify(textAppInfos, text);
                appAdapter.setData(appInfos);
            }
        });

        findViewById(R.id.btn_open_browser).setOnClickListener(
            v -> androidInterface.getAppUtils().openBrowser("http://www.baidu.com"));

        findViewById(R.id.btn_clear_memory).setOnClickListener(
            v -> androidInterface.getAppUtils().cleanMemory());

        findViewById(R.id.btn_device_info).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("sn:");
            sb.append(androidInterface.getDeviceUtils().getSerialNumber());
            sb.append("\nhardware:");
            sb.append(androidInterface.getDeviceUtils().getHardwareVersion());
            sb.append("\nsoftware:");
            sb.append(androidInterface.getDeviceUtils().getSoftwareVersion());
            sb.append("\ngetAndroidVersion:");
            sb.append(androidInterface.getDeviceUtils().getAndroidVersion());
            sb.append("\nwifi mac:");
            sb.append(androidInterface.getDeviceUtils().getWifiMac());
            sb.append("\ngetManufacturer:");
            sb.append(androidInterface.getDeviceUtils().getManufacturer());
            sb.append("\ngetProductModel:");
            sb.append(androidInterface.getDeviceUtils().getProductModel());
            sb.append("\ngetBoardModel:");
            sb.append(androidInterface.getDeviceUtils().getBoardModel());
            sb.append("\ngetBrand:");
            sb.append(androidInterface.getDeviceUtils().getBrand());
            sb.append("\ngetCPUModel:");
            sb.append(androidInterface.getDeviceUtils().getCPUModel());
            sb.append("\ngetGPUModel:");
            sb.append(androidInterface.getDeviceUtils().getGPUModel());
            sb.append("\ngetResolution:");
            sb.append(androidInterface.getDeviceUtils().getResolution());
            updateNotify(textAppInfos, sb.toString());
            L.d(TAG, sb.toString());
        });
    }

    private void managerPermissions(final String packageName) {
        androidInterface.getPermissionUtils().bindAIDLService();
        androidInterface.getPermissionUtils()
            .setServiceConnectionListener(new PermissionUtils.ServiceConnectionListener() {
                @Override
                public void onServiceConnected() {
                    androidInterface.getPermissionUtils().init(packageName);
                    String string = androidInterface.getPermissionUtils().getPermissionGroups();
                    List<PermissionGroupInfo> list =
                        string != null ? Arrays.asList(Utils.jsonToPermissionGroupInfo(string))
                            : null;
                    initPermissionDialog(list);
                }

                @Override
                public void onServiceDisconnected() {

                }
            });
    }

    private void initPermissionDialog(List<PermissionGroupInfo> list) {
        PermissionAdapter adapter = new PermissionAdapter();
        AlertDialog alertDialog = new AlertDialog
            .Builder(this)
            .setSingleChoiceItems(adapter, 0, (dialog, which) -> dialog.dismiss()).create();
        alertDialog.show();
        adapter.setData(list);

        alertDialog.setOnCancelListener(
            dialog -> androidInterface.getPermissionUtils().unbindService());
    }

    private class AppAdapter extends BaseAdapter {
        private List<JAppInfo> appInfos = new ArrayList<>();

        public AppAdapter() {
        }

        public void setData(List<JAppInfo> infos) {
            appInfos = infos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public JAppInfo getItem(int position) {
            return appInfos.get(position);
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
                convertView = LayoutInflater.from(AppManagerActivity.this)
                    .inflate(R.layout.grid_item, parent, false);
                holder.text = convertView.findViewById(R.id.text);
                holder.image = convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            JAppInfo info = appInfos.get(position);
            holder.image.setImageDrawable(androidInterface.getAppUtils()
                ._getIcon(info.getPackageName(), info.getClassName()));
            holder.text.setText(info.getAppName());
            return convertView;
        }

        class ViewHolder {
            public TextView text;
            public ImageView image;
        }
    }

    private class PermissionAdapter extends BaseAdapter {
        List<PermissionGroupInfo> list = new ArrayList<>();

        public PermissionAdapter() {
        }

        public void setData(List<PermissionGroupInfo> list) {
            if (list != null) {
                this.list = list;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public PermissionGroupInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AppManagerActivity.this)
                    .inflate(R.layout.item_permission, parent, false);
                holder.checkBox = convertView.findViewById(R.id.cbPermissionState);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PermissionGroupInfo info = list.get(position);
            holder.checkBox.setText(info.getLabel());
            holder.checkBox.setChecked(info.isState());
            holder.checkBox.setOnClickListener(v -> {
                boolean result = androidInterface.getPermissionUtils()
                    .togglePermission(list.get(position).getName());
                L.e(TAG, "result = " + result);
            });

            return convertView;
        }

        class ViewHolder {
            public CheckBox checkBox;
        }
    }
}
