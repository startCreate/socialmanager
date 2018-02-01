package com.applikeysolutions.library;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import io.reactivex.Single;
import retrofit2.Call;

public class Utils {
    public Utils() {
    }

    static ProgressDialog createLoadingDialog(Context context) {
        ProgressDialog loadingDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Loading....");
        return loadingDialog;
    }

    @SuppressLint("WrongConstant") @Nullable
    public static String getMetaDataValue(Context context, String name) {
        ApplicationInfo ai;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException var4) {
            return null;
        }

        return (String) ai.metaData.get(name);
    }

    public static boolean isFacebookInstalled(Context context) {
        Intent facebook = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
        return facebook != null;
    }

    public static <T> Single<Object> modifyCall(Call<T> callTweets/*, Resources resources*/) {
        return Single.create(e -> callTweets
                .enqueue(new Callback<T>() {
                    @Override public void success(Result<T> result) {
                        e.onSuccess(result.data);
                    }

                    @Override public void failure(TwitterException exception) {
                        if (!e.isDisposed()) {
                            e.onError(exception);
                        }
                    }
                }));
    }
    //    .onErrorResumeNext(throwable -> Single.error(ExceptionTransformer.transform(resources, throwable)));
}

