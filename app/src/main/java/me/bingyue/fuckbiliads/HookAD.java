package me.bingyue.fuckbiliads;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.Toast;

import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindClass;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.matchers.ClassMatcher;
import org.luckypray.dexkit.query.matchers.FieldsMatcher;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.query.matchers.MethodsMatcher;
import org.luckypray.dexkit.result.ClassDataList;
import org.luckypray.dexkit.result.MethodData;
import org.luckypray.dexkit.wrap.DexMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookAD {

    private static XC_MethodHook.Unhook applicationCreateUnhook;
    public static String SplashAdHelp_Method = "";
    public static String SplashPage_Method = "";
    public static String SplashShow_Method = "";

    public static void dexkit_hooker(XC_LoadPackage.LoadPackageParam loadPackageParam) throws NoSuchMethodException {
        String apkPath = loadPackageParam.appInfo.sourceDir;
        DexKitBridge bridge = DexKitBridge.create(apkPath);
        try  {
            SplashAdHelp(bridge, loadPackageParam.classLoader);
            SplashPage(bridge, loadPackageParam.classLoader);
            SplashShow(bridge, loadPackageParam.classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        bridge.close();
    }

    public static void SplashShow(DexKitBridge bridge, ClassLoader classLoader) throws NoSuchMethodException, ClassNotFoundException {
        ClassDataList classdata = bridge.findClass(FindClass.create()
                .searchPackages("tv.danmaku.bili.ui.splash.ad.page")
                .matcher(ClassMatcher.create()
                        .fields(FieldsMatcher.create()
                                .addForType("boolean")
                                .addForType("long")
                                .addForType("tv.danmaku.bili.ui.splash.ad.page.TemporaryReference")
                                )
                )
        );
        MethodData methodData = bridge.findMethod(FindMethod.create()
                .searchInClass(classdata)
                .matcher(MethodMatcher.create()
                        .returnType("void")
                        .paramTypes("tv.danmaku.bili.ui.splash.ad.model.Splash")
                )
        ).single();
        Method method = methodData.getMethodInstance(classLoader);
        SplashShow_Method = methodData.toDexMethod().serialize();
        XposedBridge.hookMethod(method, XC_MethodReplacement.DO_NOTHING);
    }

    public static void SplashPage(DexKitBridge bridge, ClassLoader classLoader) throws NoSuchMethodException {
        ClassDataList classdata = bridge.findClass(FindClass.create()
                .searchPackages("tv.danmaku.bili.ui.splash.ad.page")
                .matcher(ClassMatcher.create()
                        .fields(FieldsMatcher.create()
                                .count(0)
                        )
                        .methods(MethodsMatcher.create()
                                .count(1)
                        )
                )
        );
        MethodData methodData = bridge.findMethod(FindMethod.create()
                .searchInClass(classdata)
                .matcher(MethodMatcher.create()
                        .modifiers(Modifier.PUBLIC)
                        .paramCount(1)
                        .returnType("androidx.fragment.app.Fragment")
                        .paramTypes("tv.danmaku.bili.ui.splash.ad.model.Splash")
                )
        ).single();
        Method method = methodData.getMethodInstance(classLoader);
        SplashPage_Method = methodData.toDexMethod().serialize();
        XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(null));
    }

    public static void SplashAdHelp(DexKitBridge bridge, ClassLoader classLoader) throws NoSuchMethodException {
        MethodData methodData = bridge.findMethod(FindMethod.create()
                .searchPackages("tv.danmaku.bili.ui.splash")
                .matcher(MethodMatcher.create()
                        .modifiers(Modifier.PUBLIC)
                        .paramCount(10)
                        .returnType("void")
                        .usingStrings("[Splash]SplashAdHelper", "stockReport openEvent = ", "hot")
                        .paramTypes("java.lang.String", "boolean")
                )
        ).single();
        Method method = methodData.getMethodInstance(classLoader);
        SplashAdHelp_Method = methodData.toDexMethod().serialize();
        XposedBridge.hookMethod(method, XC_MethodReplacement.DO_NOTHING);
    }


    public static void D(XC_LoadPackage.LoadPackageParam lpparam) throws PackageManager.NameNotFoundException, NoSuchMethodException {
        if(!Objects.equals(SplashShow_Method, "") && !Objects.equals(SplashAdHelp_Method, "") && !Objects.equals(SplashPage_Method, "")){
            XposedBridge.hookMethod(new DexMethod(SplashPage_Method).getMethodInstance(lpparam.classLoader), XC_MethodReplacement.returnConstant(null));
            XposedBridge.hookMethod(new DexMethod(SplashShow_Method).getMethodInstance(lpparam.classLoader), XC_MethodReplacement.DO_NOTHING);
            XposedBridge.hookMethod(new DexMethod(SplashAdHelp_Method).getMethodInstance(lpparam.classLoader), XC_MethodReplacement.DO_NOTHING);
        }else{
            dexkit_hooker(lpparam);
            applicationCreateUnhook = XposedHelpers.findAndHookMethod(
                    "android.app.Application",
                    lpparam.classLoader,
                    "onCreate",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Context hostContext = (Context) param.thisObject;
                            SharedPreferences preferences = hostContext.getSharedPreferences("me_bingyue_bilifuckads", MODE_PRIVATE);
                            preferences.edit().putString("SplashPage_Method", SplashShow_Method)
                                    .putString("SplashShow_Method", SplashShow_Method)
                                    .putString("SplashAdHelp_Method", SplashAdHelp_Method)
                                    .putInt("now_version", GetBiliiliVersion.B(lpparam))
                                    .apply();

                            Toast.makeText(hostContext, "模块搜索完毕，已缓存", Toast.LENGTH_SHORT).show();
                            applicationCreateUnhook.unhook();
                        }
                    }
            );
        }
    }
}
