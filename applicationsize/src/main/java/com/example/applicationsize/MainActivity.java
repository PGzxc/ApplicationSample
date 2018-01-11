package com.example.applicationsize;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.applicationsize.adapter.ApplicationAdapter;
import com.example.applicationsize.bean.AppInfo;
import com.example.applicationsize.utils.FormatUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "APP_SIZE";

    private ListView listview = null;
    private List<AppInfo> mlistAppInfo = null;
    LayoutInflater infater = null;
    //全局变量，保存当前查询包得信息
    private long cachesize; //缓存大小
    private long datasize;  //数据大小
    private long codesize;  //应用程序大小
    private long totalsize; //总大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listView_App);
    }

    private void initData() {
        mlistAppInfo = new ArrayList<AppInfo>();
        queryAppInfo(); // 查询所有应用程序信息
        listview.setAdapter(new ApplicationAdapter(this,mlistAppInfo));

    }

    // 获得所有启动Activity的信息，类似于Launch界面
    public void queryAppInfo() {
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        if (mlistAppInfo != null) {
            mlistAppInfo.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName, activityName));
                // 创建一个AppInfo对象，并赋值
                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                appInfo.setIntent(launchIntent);
                mlistAppInfo.add(appInfo); // 添加至列表中
            }
        }
    }

    //aidl文件形成的Bindler机制服务类
    public class PkgSizeObserver extends IPackageStatsObserver.Stub {
        /*** 回调函数，
         * @param pStats ,返回数据封装在PackageStats对象中
         * @param succeeded  代表回调成功
         */
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException {
            // TODO Auto-generated method stub
            cachesize = pStats.cacheSize; //缓存大小
            datasize = pStats.dataSize;  //数据大小
            codesize = pStats.codeSize;  //应用程序大小
            totalsize = cachesize + datasize + codesize;
            Log.i(TAG, "cachesize--->" + cachesize + " datasize---->" + datasize + " codeSize---->" + codesize);
        }
    }

    private void setListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //更新显示当前包得大小信息
                try {
                    queryPacakgeSize(mlistAppInfo.get(position).getPkgName());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                infater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialog = infater.inflate(R.layout.dialog_app_size, null) ;
                TextView tvcachesize =(TextView) dialog.findViewById(R.id.tvcachesize) ; //缓存大小
                TextView tvdatasize = (TextView) dialog.findViewById(R.id.tvdatasize)  ; //数据大小
                TextView tvcodesize = (TextView) dialog.findViewById(R.id.tvcodesize) ; // 应用程序大小
                TextView tvtotalsize = (TextView) dialog.findViewById(R.id.tvtotalsize) ; //总大小
                //类型转换并赋值
                tvcachesize.setText(FormatUtils.formateFileSize(MainActivity.this,cachesize));
                tvdatasize.setText(FormatUtils.formateFileSize(MainActivity.this,datasize)) ;
                tvcodesize.setText(FormatUtils.formateFileSize(MainActivity.this,codesize)) ;
                tvtotalsize.setText(FormatUtils.formateFileSize(MainActivity.this,totalsize)) ;
                //显示自定义对话框
                AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this) ;
                builder.setView(dialog) ;
                builder.setTitle(mlistAppInfo.get(position).getAppLabel()+"的大小信息为：") ;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel() ;  // 取消显示对话框
                    }

                });
                builder.create().show() ;
            }
        });
    }
    public void  queryPacakgeSize(String pkgName) throws Exception{
        if ( pkgName != null){
            //使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
            PackageManager pm = getPackageManager();  //得到pm对象
            try {
                //通过反射机制获得该隐藏函数
                Method getPackageSizeInfo = pm.getClass().getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
                //调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
                getPackageSizeInfo.invoke(pm, pkgName,new PkgSizeObserver());
            }
            catch(Exception ex){
                Log.e(TAG, "NoSuchMethodException") ;
                ex.printStackTrace() ;
                throw ex ;  // 抛出异常
            }
        }
    }
}
