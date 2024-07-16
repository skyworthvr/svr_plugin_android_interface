package com.ssnw.androidapitest.function;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.ssnw.androidapitest.BaseActivity;
import com.ssnwt.playertest.R;
import com.ssnwt.vr.androidmanager.AndroidInterface;
import com.ssnwt.vr.common.AppTaskManager;
import com.ssnwt.vr.common.Utils;
import java.util.ArrayList;

public class AppTasksActivity extends BaseActivity {
    private static final String TAG = "AppTasksActivity";
    private TextView mTextAppInfos;
    private GridView mListView;
    private AppAdapter mAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tasks);

        mTextAppInfos = findViewById(R.id.text_info);

        findViewById(R.id.btn_reload).setOnClickListener(v -> {
            mAppAdapter.clear();
            mAppAdapter.setAppInfos(AppTaskManager.getInstance().
                getRunningProcess(getApplication()));
        });

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            updateNotify(mTextAppInfos, "clearMemory");
            AppTaskManager.getInstance().clearMemory(getApplication());
        });

        mListView = findViewById(R.id.list);
        mAppAdapter = new AppAdapter();
        mListView.setAdapter(mAppAdapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            updateNotify(mTextAppInfos, "kill process " +
                mAppAdapter.getItem(position).getPackageName());
            AppTaskManager.getInstance().killProcess(getApplication(),
                mAppAdapter.getItem(position).getPackageName());
        });
    }

    private class AppAdapter extends BaseAdapter {
        private final Object mSyncObj = new Object();
        private ArrayList<ComponentName> mAppInfos = new ArrayList<>();
        private Bitmap mBmp;

        public AppAdapter() {
        }

        public void setAppInfos(ArrayList<ComponentName> infos) {
            synchronized (mSyncObj) {
                if (infos != null) mAppInfos = infos;
            }
            notifyDataChanged();
        }

        public void clear() {
            synchronized (mSyncObj) {
                mAppInfos.clear();
            }
        }

        public void notifyDataChanged() {
            runOnUiThread(() -> notifyDataSetChanged());
        }

        @Override
        public int getCount() {
            int size = 0;
            synchronized (mSyncObj) {
                size = mAppInfos.size();
            }
            return size;
        }

        @Override
        public ComponentName getItem(int position) {
            return mAppInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ComponentName info = null;
            synchronized (mSyncObj) {
                if (mAppInfos.size() > 0 && mAppInfos.size() > position) {
                    info = mAppInfos.get(position);
                }
            }
            if (info != null) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(AppTasksActivity.this)
                        .inflate(R.layout.grid_item, parent, false);
                    holder.text = convertView.findViewById(R.id.text);
                    holder.image = convertView.findViewById(R.id.img);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                mBmp = Utils.byte2Bitmap(AndroidInterface.getInstance().
                    getAppUtils().getIcon(info.getPackageName(), info.getClassName()));
                if (mBmp != null && !mBmp.isRecycled()) holder.image.setImageBitmap(mBmp);
                holder.text.setText(AndroidInterface.getInstance().
                    getAppUtils().getAppName(info.getPackageName(), info.getClassName()));
            }
            return convertView;
        }

        class ViewHolder {
            public TextView text;
            public ImageView image;
        }
    }
}
