package com.example.applicationsample.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.applicationsample.R;

/**
 * Created by admin on 2018/1/11.
 */

public class ViewHolder {

    public ImageView appIcon;
    public TextView tvAppLabel;
    public TextView tvPackageName;

    public ViewHolder(View view) {
        this.appIcon = view.findViewById(R.id.imgApp);
        this.tvAppLabel = view.findViewById(R.id.tvAppLabel);
        this.tvPackageName = view.findViewById(R.id.tvPkgName);
    }
}
