package me.bingyue.fuckbiliads;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;



public class MainHook implements IXposedHookLoadPackage {


    static {
        System.loadLibrary("dexkit");
    }

    private XC_MethodHook.Unhook applicationCreateUnhook;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("tv.danmaku.bili")) return;

        applicationCreateUnhook = XposedHelpers.findAndHookMethod(
                "android.app.Application",
                lpparam.classLoader,
                "onCreate",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Context hostContext = (Context) param.thisObject;
                        int version_code = GetBiliiliVersion.B(hostContext, lpparam.packageName);
                        saveDataToHost(hostContext, version_code, lpparam);
                    }
                }
        );


    }

    private void saveDataToHost(Context hostContext, Integer version_code_, XC_LoadPackage.LoadPackageParam lpparam) throws NoSuchMethodException, PackageManager.NameNotFoundException, ClassNotFoundException {
        SharedPreferences prefs = hostContext.getSharedPreferences(
                "me_bingyue_bilifuckads",
                Context.MODE_PRIVATE
        );

        int version_code = prefs.getInt("now_version", 0);
        if(version_code == 0){
            Toast.makeText(hostContext, "正在初始化去广告模块", Toast.LENGTH_SHORT).show();

        }
        if(version_code != version_code_){
            Toast.makeText(hostContext, "检测到你[升级|降级]了b站", Toast.LENGTH_SHORT).show();
            prefs.edit().putString("BaseSplash", "").apply();
        }
        HookAD.dexkit_hooker(lpparam, hostContext);
        Toast.makeText(hostContext, "模块搜索完毕，已缓存", Toast.LENGTH_SHORT).show();
        applicationCreateUnhook.unhook();
    }
}