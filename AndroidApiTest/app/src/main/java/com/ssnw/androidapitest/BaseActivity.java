package com.ssnw.androidapitest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class BaseActivity extends AppCompatActivity {
    private static final int MSG_SYSTEM_UI_HIDE_NAVIGATION = 1;
    protected static final int MSG_UPDATE_INFO = 2;
    private String mLastTextInfo;
    private BaseHandler mHandler = new BaseHandler(this);
    private Toast mToast;

    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    private static class BaseHandler extends Handler {
        WeakReference<BaseActivity> mWeakActivity;

        public BaseHandler(BaseActivity activity) {
            mWeakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SYSTEM_UI_HIDE_NAVIGATION:
                    if (mWeakActivity.get() != null) mWeakActivity.get().setSystemUiState();
                    break;
                case MSG_UPDATE_INFO:
                    if (mWeakActivity.get() != null) {
                        if (msg.obj != null && msg.obj instanceof TextView) {
                            ((TextView) msg.obj).setText(msg.getData().getString("info"));
                        } else {
                            Toast.makeText(mWeakActivity.get(), msg.getData().getString("info"),
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }

    protected void updateNotify(TextView text, String info) {
        if (info == null || info.equalsIgnoreCase(mLastTextInfo)) return;
        Message msg = mHandler.obtainMessage();
        msg.obj = text;
        msg.what = MSG_UPDATE_INFO;
        StringBuilder sb = new StringBuilder();
        if (mLastTextInfo != null) {
            sb.append(mLastTextInfo);
            sb.append("\n\n");
        }
        sb.append(info);
        msg.getData().putString("info", sb.toString());
        mLastTextInfo = info;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemUiState();
    }

    protected void startActivity(Context packageContext, Class<?> cls) {
        this.startActivity(new Intent(packageContext, cls));
    }

    public void setSystemUiState() {
        setSystemUINormal();
    }

    protected void setSystemUIFull() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        getWindow().getDecorView()
            .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if (visibility == View.VISIBLE) {
                        hideNavigation(3000);
                    }
                }
            });
    }

    protected void setSystemUINormal() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    private void hideNavigation(int delay) {
        mHandler.removeMessages(MSG_SYSTEM_UI_HIDE_NAVIGATION);
        mHandler.sendEmptyMessageDelayed(MSG_SYSTEM_UI_HIDE_NAVIGATION, delay);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
