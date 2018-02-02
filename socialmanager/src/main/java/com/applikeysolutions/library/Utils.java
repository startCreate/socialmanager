package com.applikeysolutions.library;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class Utils {

    private Utils() {
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public static String getMetaDataValue(Context context, String name) {
        ApplicationInfo app;
        try {
            app = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException var4) {
            return null;
        }
        return (String) app.metaData.get(name);
    }

    public static boolean isFacebookInstalled(Context context) {
        Intent facebook = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
        return facebook != null;
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(key, false);
    }

}

