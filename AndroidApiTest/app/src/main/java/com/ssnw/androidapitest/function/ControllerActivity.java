package com.ssnw.androidapitest.function;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import com.ssnw.androidapitest.BaseActivity;
import com.ssnwt.playertest.R;
import com.ssnwt.vr.common.L;
import com.ssnwt.vr.svrcontroller.ControllerManager;
import com.ssnwt.vr.svrcontroller.PairManager;

public class ControllerActivity extends BaseActivity {
    private static final String TAG = "Controller:activity";
    private ControllerManager mControllerManager;
    private float[] data = new float[ControllerManager.GROUP_DATA_SIZE *
        ControllerManager.Handness.Max.getValue()];

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mControllerManager.getData(data);
            if (data[0] == 3) {
                L.d(TAG, "Controller is paired");
            }
            mHandler.sendEmptyMessageDelayed(0, 13);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        mControllerManager = ControllerManager.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mControllerManager.startService(getApplicationContext());

        findViewById(R.id.btn_controller_start).setOnClickListener(
            v -> L.d(TAG, "Connect result : " + mControllerManager.connect()));
        findViewById(R.id.btn_controller_finish).setOnClickListener(
            v -> L.d(TAG, "Disconnect result : " + mControllerManager.disconnect()));
        findViewById(R.id.btn_search).setOnClickListener(
            v -> {
                PairManager.getInstance().enablePair(this);
                PairManager.getInstance().search(this);
            });
        findViewById(R.id.btn_stop_search).setOnClickListener(
            v -> {
                PairManager.getInstance().stopSearch(this);
                PairManager.getInstance().disablePair(this);
            });
        findViewById(R.id.btn_cancel_paired).setOnClickListener(
            v -> PairManager.getInstance().cancelPaired(this));
        findViewById(R.id.btn_disconnect).setOnClickListener(
            v -> PairManager.getInstance().disconnect(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d(TAG, "Connect result : " + mControllerManager.connect());
        mHandler.sendEmptyMessageDelayed(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.d(TAG, "Disconnect result : " + mControllerManager.disconnect());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}
