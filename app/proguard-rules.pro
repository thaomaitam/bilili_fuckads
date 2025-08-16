# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 10
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontoptimize
-dontshrink
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keep public class me.bingyue.fuckbiliads.MainHook
-keep public class me.bingyue.fuckbiliads.GetBiliiliVersion {
public static int *(de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam);
}
-keep public class me.bingyue.fuckbiliads.HookAD {
public static void *(de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam);
}
-keepclassmembers class me.bingyue.fuckbiliads.MainHook {
    public <init>();
    public <init>(...);
}
-keepclassmembers class me.bingyue.fuckbiliads.GetBiliiliVersion {
    public <init>();
    public <init>(...);
}
-keep class * extends de.robv.android.xposed.XC_MethodHook { *; }
-dontwarn javax.lang.model.element.Modifier