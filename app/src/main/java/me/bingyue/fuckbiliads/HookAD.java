package me.bingyue.fuckbiliads;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.matchers.MethodMatcher;


import org.luckypray.dexkit.result.MethodData;
import org.luckypray.dexkit.wrap.DexMethod;
import java.lang.reflect.Modifier;


import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookAD {

    public static String BaseSplash;

    public static void dexkit_hooker(XC_LoadPackage.LoadPackageParam loadPackageParam, Context context) throws NoSuchMethodException, ClassNotFoundException, PackageManager.NameNotFoundException {;
        SharedPreferences preferences = context.getSharedPreferences("me_bingyue_bilifuckads", MODE_PRIVATE);
        BaseSplash = preferences.getString("BaseSplash", "");
        if(BaseSplash.isEmpty()){
            String apkPath = loadPackageParam.appInfo.sourceDir;
            DexKitBridge bridge = DexKitBridge.create(apkPath);
            SplashAdType(bridge, preferences);
            bridge.close();
        }
        final Class<?> b = XposedHelpers.findClass("tv.danmaku.bili.ui.splash.ad.model.Splash", loadPackageParam.classLoader);
        XposedBridge.hookMethod(new DexMethod(BaseSplash).getMethodInstance(loadPackageParam.classLoader), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object newSplash = b.newInstance();
                param.args[0] = newSplash;
                super.beforeHookedMethod(param);
            }
        });
        preferences.edit()
                .putInt("now_version", GetBiliiliVersion.B(context, loadPackageParam.packageName))
                .apply();
    }

    public static void SplashAdType(DexKitBridge bridge, SharedPreferences preferences) throws NoSuchMethodException {
        MethodData methodData = bridge.findMethod(FindMethod.create()
                .searchPackages("tv.danmaku.bili.ui.splash.ad.page")
                .matcher(
                        MethodMatcher.create()
                                .declaredClass("tv.danmaku.bili.ui.splash.ad.page.BaseSplash")
                                .modifiers(Modifier.PUBLIC)
                                .paramCount(1)
                                .returnType("void")
                                .paramTypes("tv.danmaku.bili.ui.splash.ad.model.Splash")
                )
        ).single();
        preferences.edit().putString("BaseSplash", methodData.toDexMethod().serialize()).apply();
        BaseSplash = methodData.toDexMethod().serialize();
    }
}
