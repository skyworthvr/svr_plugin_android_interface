package com.ssnw.androidapitest.function;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.ssnw.androidapitest.BaseActivity;
import com.ssnwt.playertest.R;
import com.ssnwt.vr.androidmanager.AndroidInterface;
import com.ssnwt.vr.androidmanager.BatteryUtils;
import com.ssnwt.vr.androidmanager.DeviceUtils;
import com.ssnwt.vr.androidmanager.FotaUtils;
import com.ssnwt.vr.androidmanager.ProximitySensorUtils;
import com.ssnwt.vr.androidmanager.StorageUtils;
import com.ssnwt.vr.androidmanager.WakeLockUtils;
import java.io.File;

public class OtherAndroidInterfaceActivity extends BaseActivity {
    private TextView mTextAppInfos;
    private MediaPlayer mMediaPlayer;
    private WakeLockUtils mWakeLockUtils;
    private PowerManager.WakeLock mWakeLock;

    private ProximitySensorUtils.SensorListener mProximitySensorListener =
        new ProximitySensorUtils.SensorListener() {

            @Override
            public void onDistanceFar(boolean far) {
                updateNotify(mTextAppInfos, "距离在5cm外： " + far);
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_android_interface);
        mTextAppInfos = findViewById(R.id.text_info);
        mWakeLockUtils = new WakeLockUtils(getApplication());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mMediaPlayer = MediaPlayer.create(this, R.raw.test);
        mMediaPlayer.setLooping(true);
        AndroidInterface.getInstance().getDeviceUtils().setMouseListener(
            isAttached -> {
                updateNotify(mTextAppInfos, "鼠标连接状态 = " + isAttached);
                //Toast.makeText(OtherAndroidInterfaceActivity.this, "鼠标已 " + isAttached , Toast.LENGTH_SHORT)
                //    .show();
                Log.d("jie", "鼠标已 " + isAttached);
            });
        AndroidInterface.getInstance().getVolumeUtils().setListener(
            () -> updateNotify(mTextAppInfos, "current volume " +
                AndroidInterface.getInstance().getVolumeUtils().getCurrentVolume()));
        findViewById(R.id.btn_brightness_up).setOnClickListener(v -> {
            int curr =
                AndroidInterface.getInstance().getBrightnessUtils().getCurrentBrightness() + 5;
            if (curr > AndroidInterface.getInstance().getBrightnessUtils().getMaxBrightness()) {
                curr = AndroidInterface.getInstance().getBrightnessUtils().getMaxBrightness();
            }
            updateNotify(mTextAppInfos, "brightness = " + curr);
            AndroidInterface.getInstance().getBrightnessUtils().setBrightness(curr);
        });
        findViewById(R.id.btn_brightness_down).setOnClickListener(v -> {
            int curr =
                AndroidInterface.getInstance().getBrightnessUtils().getCurrentBrightness() - 5;
            if (curr < 0) {
                curr = 0;
            }
            updateNotify(mTextAppInfos, "brightness = " + curr);
            AndroidInterface.getInstance().getBrightnessUtils().setBrightness(curr);
        });
        findViewById(R.id.btn_volume_up).setOnClickListener(v -> {
            int curr = AndroidInterface.getInstance().getVolumeUtils().getCurrentVolume() + 1;
            if (curr > AndroidInterface.getInstance().getVolumeUtils().getMaxVolume()) {
                curr = AndroidInterface.getInstance().getVolumeUtils().getMaxVolume();
            }
            updateNotify(mTextAppInfos, "volume = " + curr);
            AndroidInterface.getInstance().getVolumeUtils().setVolume(curr);
        });
        findViewById(R.id.btn_volume_down).setOnClickListener(v -> {
            int curr = AndroidInterface.getInstance().getVolumeUtils().getCurrentVolume() - 1;
            if (curr < 0) {
                curr = 0;
            }
            updateNotify(mTextAppInfos, "volume = " + curr);
            AndroidInterface.getInstance().getVolumeUtils().setVolume(curr);
        });
        findViewById(R.id.btn_battery_status).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("curr battery:");
            sb.append(AndroidInterface.getInstance().getBatteryUtils().getCurrentBattery());
            sb.append("\nmax battery:");
            sb.append(AndroidInterface.getInstance().getBatteryUtils().getMaxBattery());
            sb.append("\nstatus battery:");
            sb.append(AndroidInterface.getInstance().getBatteryUtils().getBatteryStatus());
            updateNotify(mTextAppInfos, sb.toString());
        });
        findViewById(R.id.btn_fota).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("hasNewVersion:");
            sb.append(AndroidInterface.getInstance().getFotaUtils().hasNewVersion());
            sb.append("\nisDownloading:");
            sb.append(AndroidInterface.getInstance().getFotaUtils().isDownloading());
            sb.append("\nisDownloadingFinished:");
            sb.append(AndroidInterface.getInstance().getFotaUtils().isDownloadingFinished());
            updateNotify(mTextAppInfos, sb.toString());
        });
        findViewById(R.id.btn_force_update).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            String filePath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/update.zip";
            if (!new File(filePath).exists()) {
                sb.append("File ").append(filePath).append(" not exist!");
            } else {
                sb.append("Force Update!");
                AndroidInterface.getInstance().getFotaUtils().goToFotaForceUpdate(filePath);
            }
            updateNotify(mTextAppInfos, sb.toString());
        });
        findViewById(R.id.btn_storage_size).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("total size:");
            sb.append(AndroidInterface.getInstance().getStorageUtils().getTotalStorageSize());
            sb.append("\nused size:");
            sb.append(AndroidInterface.getInstance().getStorageUtils().getUsedStorageSize());
            updateNotify(mTextAppInfos, sb.toString());
        });
        findViewById(R.id.btn_eye_production).setOnClickListener(v -> {
            boolean open =
                AndroidInterface.getInstance().getDeviceUtils().isOpenEyeProtectionMode();
            AndroidInterface.getInstance().getDeviceUtils().openEyeProtectionMode(!open);
            StringBuilder sb = new StringBuilder();
            sb.append("open eye production-");
            sb.append(!open);
            updateNotify(mTextAppInfos, sb.toString());
        });
        findViewById(R.id.btn_add_proximity_listener).setOnClickListener(
            v -> AndroidInterface.getInstance()
                .getProximitySensorUtils()
                .addSensorListener(mProximitySensorListener));

        findViewById(R.id.btn_get_display_size).setOnClickListener(
            v -> {
                int width = AndroidInterface.getInstance().getDeviceUtils().getDisplayWidthPixels();
                int height =
                    AndroidInterface.getInstance().getDeviceUtils().getDisplayHeightPixels();
                updateNotify(mTextAppInfos, width + "x" + height);
            }
        );

        findViewById(R.id.btn_is_mouse_attached).setOnClickListener(
            v -> {
                updateNotify(mTextAppInfos,
                    "鼠标当前状态：" + AndroidInterface.getInstance().getDeviceUtils().isMouseAttached());
            }
        );

        AndroidInterface.getInstance()
            .getStorageUtils()
            .setListener(new StorageUtils.StorageListener() {
                @Override
                public void onSDCardInsert(String path) {
                    updateNotify(mTextAppInfos, "onSDCardInsert:" + path);
                }

                @Override
                public void onSDCardRemove() {
                    updateNotify(mTextAppInfos, "onSDCardInsert");
                }
            });
        AndroidInterface.getInstance().getFotaUtils().setListener(new FotaUtils.FotaListener() {

            @Override
            public void onVersion(boolean hasNewVersion, boolean forceUpdate, String pkgVersion,
                String description) {
                updateNotify(mTextAppInfos, "onVersion:" + hasNewVersion +
                    "\nforceUpdate:" + forceUpdate + "\npkgVersion:" + pkgVersion);
            }

            @Override public void onVersionError(boolean b, int i) {

            }

            @Override
            public void onDownloadProgress(int progress) {
                updateNotify(mTextAppInfos, "onDownLoadProgress:" + progress);
            }

            @Override
            public void onDownloadError(int errCode) {
                updateNotify(mTextAppInfos, "onDownLoadError:" + errCode);
            }

            @Override
            public void onDownloadFinish() {
                updateNotify(mTextAppInfos, "onDownLoadFinish");
            }

            @Override public void onInstallProgress(int i) {

            }

            @Override public void onInstallError(int i) {

            }

            @Override public void onInstallFinish() {

            }
        });
        AndroidInterface.getInstance().getBatteryUtils().setListener(new BatteryUtils.Listener() {
            @Override
            public void onBatteryChanged(int battery) {
                updateNotify(mTextAppInfos, "battery-" + battery);
            }

            @Override
            public void onBatteryStatusChanged(int status) {
                updateNotify(mTextAppInfos, "battery status-" + status);
            }
        });
        AndroidInterface.getInstance().getFotaUtils().checkUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
        AndroidInterface.getInstance()
            .getProximitySensorUtils()
            .removeSensorListener(mProximitySensorListener);
    }
}
