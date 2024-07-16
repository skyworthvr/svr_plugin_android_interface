package com.ssnw.androidapitest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import com.ssnw.androidapitest.function.AppManagerActivity;
import com.ssnw.androidapitest.function.AppTasksActivity;
import com.ssnw.androidapitest.function.BluetoothActivity;
import com.ssnw.androidapitest.function.ControllerActivity;
import com.ssnw.androidapitest.function.OtherAndroidInterfaceActivity;
import com.ssnw.androidapitest.function.WifiActivity;
import com.ssnwt.playertest.R;
import com.ssnwt.vr.androidmanager.AndroidInterface;
import com.ssnwt.vr.common.L;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private static final String TAG = "SSNWT_MainActivity";
    private static final Object[][] Items = {
        {"App Manager", AppManagerActivity.class},
        {"App Task", AppTasksActivity.class},
        {"Bluetooth", BluetoothActivity.class},
        {"Wifi Manager", WifiActivity.class},
        //{"Controller", ControllerActivity.class},
        {"Other api", OtherAndroidInterfaceActivity.class},
    };
    private List<Map<String, Object>> mMapList = new ArrayList<>();
    private GridView mGridView;
    private SimpleAdapter mSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (Object[] item : Items) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", item[0]);
            map.put("value", item[1]);
            mMapList.add(map);
        }

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String[] from = {"key"};
        int[] to = {R.id.text};
        mSimpleAdapter = new SimpleAdapter(this, mMapList, R.layout.grid_item_text, from, to);
        mGridView = findViewById(R.id.grid_view);

        mGridView.setAdapter(mSimpleAdapter);
        mGridView.setOnItemClickListener(MainActivity.this::onItemClick);

        AndroidInterface.getInstance().init(getApplication(), new AndroidInterface.InitListener() {
            @Override public void onInitialized() {
                L.d(TAG, "onInitialized");
            }

            @Override public void onReleased() {
                L.d(TAG, "onReleased");
            }

            @Override public void onInitError() {
                L.d(TAG, "onInitError");
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        AndroidInterface.getInstance().release();
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (AndroidInterface.getInstance().isInitialized()) {
            startActivity(new Intent(MainActivity.this, (Class<?>) Items[position][1]));
        } else {
            L.d(TAG, "AndroidInterface.getInstance().isInitialized() failed");
        }
    }
}
