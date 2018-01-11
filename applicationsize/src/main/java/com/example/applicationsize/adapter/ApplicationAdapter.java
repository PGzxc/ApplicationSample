package com.example.applicationsize.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.applicationsize.R;
import com.example.applicationsize.bean.AppInfo;
import com.example.applicationsize.holder.ViewHolder;

import java.util.List;

/**
 * 自定义适配器类，提供给listView的自定义view
 */

public class ApplicationAdapter extends BaseAdapter {

    private List<AppInfo> mListAppInfos = null;
    private LayoutInflater inflater = null;

    public ApplicationAdapter(Context context, List<AppInfo> mListAppInfos) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListAppInfos = mListAppInfos;
    }

    @Override
    public int getCount() {
        return mListAppInfos.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return mListAppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            view = inflater.inflate(R.layout.app_info_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = getItem(position);
        viewHolder.appIcon.setImageDrawable(appInfo.getAppIcon());
        viewHolder.tvAppLabel.setText(appInfo.getAppLabel());
        viewHolder.tvPackageName.setText(appInfo.getPkgName());
        return view;
    }
}
