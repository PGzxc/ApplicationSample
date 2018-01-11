package com.example.applicationsample.bean;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Model类，用来存储应用程序信息
 */

public class AppInfo {

    private String appLabel; //应用程序标签
    private Drawable appIcon; //应用程序图像
    private Intent intent; //启动应用程序的Intent
    private String packageName;

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
