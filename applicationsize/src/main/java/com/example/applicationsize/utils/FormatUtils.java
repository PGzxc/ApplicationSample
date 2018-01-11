package com.example.applicationsize.utils;

import android.content.Context;
import android.text.format.Formatter;

/**
 * Created by admin on 2018/1/11.
 */

public class FormatUtils {
    //系统函数，字符串转换 long -String (kb)
    public static String formateFileSize(Context context,long size){
        return Formatter.formatFileSize(context, size);
    }
}
