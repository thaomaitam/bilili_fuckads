package me.bingyue.fuckbiliads;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;



public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.bstar.intl")) return;
        XposedHelpers.findAndHookMethod("com.bstar.intl.ui.splash.ad.model.Splash", lpparam.classLoader, "isValid", XC_MethodReplacement.returnConstant(false));
    }
}
