package me.bingyue.fuckbiliads;

import java.io.File;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class GetBiliiliVersion {
    public static int B(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> parserCls = XposedHelpers.findClass("android.content.pm.PackageParser", lpparam.classLoader);
            Object parser = parserCls.newInstance();
            File apkPath = new File(lpparam.appInfo.sourceDir);
            Object pkg = XposedHelpers.callMethod(parser, "parsePackage", apkPath, 0);
            return XposedHelpers.getIntField(pkg, "mVersionCode");
        } catch (Throwable e) {
            return 0;
        }
    }
}
