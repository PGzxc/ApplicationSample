package com.example.applicationsample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.applicationsample.adapter.ApplicationAdapter;
import com.example.applicationsample.bean.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listview = null;
    private List<AppInfo> mlistAppInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        listview = findViewById(R.id.listView_App);

    }

    private void initData() {
        mlistAppInfo = new ArrayList<>();
        queryAppInfo(); // 查询所有应用程序信息
        listview.setAdapter(new ApplicationAdapter(this, mlistAppInfo));
    }

    //获得所有启动Activity的信息
    private void queryAppInfo() {
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        if (mlistAppInfo != null) {
            mlistAppInfo.clear();
            for (ResolveInfo resolveInfo : resolveInfos) {
                String activityName = resolveInfo.activityInfo.name;// 获得该应用程序的启动Activity的name
                String packageName = resolveInfo.activityInfo.packageName;// 获得应用程序的包名
                String appLabel = (String) resolveInfo.loadLabel(pm);// 获得应用程序的Label
                Drawable icon = resolveInfo.loadIcon(pm);// 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(packageName, activityName));
                // 创建一个AppInfo对象，并赋值
                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setAppIcon(icon);
                appInfo.setPackageName(packageName);
                appInfo.setIntent(launchIntent);
                mlistAppInfo.add(appInfo);
            }
        }
    }

    private void setListener() {
        listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = mlistAppInfo.get(position).getIntent();
            startActivity(intent);
        });
    }

}
