package com.ssnw.androidapitest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.ssnwt.playertest.R;

public class PermissionActivity extends BaseActivity {
    private static final String TAG = "PermissionActivity";
    private static final int REQUEST_PERMISSION = 1;
    private static final String[] PERMISSIONS = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.INSTALL_PACKAGES,
        Manifest.permission.DELETE_PACKAGES,
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH_PRIVILEGED,
    };
    private boolean isPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        isPermission = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                    requestPermissionForApp();
                    isPermission = false;
                    break;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (isPermission) {
            startActivity(PermissionActivity.this, MainActivity.class);
            finish();
        }
    }

    private void requestPermissionForApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            isPermission = true;
        }
    }

    @Override
    public void setSystemUiState() {
        setSystemUIFull();
    }
}
