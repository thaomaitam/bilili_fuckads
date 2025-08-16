package me.bingyue.fuckbiliads;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import de.robv.android.xposed.XposedBridge;

public class GetBiliiliVersion {
    public static int B(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            XposedBridge.log("获取软件版本号失败：" + e.getMessage());
            return 0;
        }
    }
}
