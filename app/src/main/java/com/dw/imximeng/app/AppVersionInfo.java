package com.dw.imximeng.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author hjd
 * @time 2017-11-06 14:14
 */

public class AppVersionInfo {
    /**
     * 获取当前应用版本名
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context){
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert packInfo != null;
        return packInfo.versionName;
    }
    /**
     * 获取当前应用版本号
     * @return
     * @throws Exception
     */
    public static int getVersionCode(Context context){
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert packInfo != null;
        return packInfo.versionCode;
    }
}
