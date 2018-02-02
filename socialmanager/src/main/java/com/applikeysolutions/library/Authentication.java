package com.applikeysolutions.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.applikeysolutions.library.networks.FacebookAuthActivity;
import com.applikeysolutions.library.networks.GoogleAuthActivity;
import com.applikeysolutions.library.networks.InstagramAuthActivity;
import com.applikeysolutions.library.networks.TwitterAuthActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Collections;
import java.util.List;

import static com.twitter.sdk.android.core.TwitterConfig.Builder;

public class Authentication {

    private static final String KEY_IS_GOOGLE_DISCONNECT_REQUESTED = Authentication.class.getName() + "KEY_IS_GOOGLE_DISCONNECT_REQUESTED";
    private static final String KEY_IS_GOOGLE_REVOKE_REQUESTED = Authentication.class.getName() + "KEY_IS_GOOGLE_REVOKE_REQUESTED";

    @SuppressLint("StaticFieldLeak")
    private static Authentication instance;
    private Context appContext;
    private AuthenticationData facebookAuthData;
    private AuthenticationData googleAuthData;
    private AuthenticationData twitterAuthData;
    private AuthenticationData instagramAuthData;

    private Authentication() {
    }

    public static Authentication getInstance() {
        if (instance == null) {
            synchronized (Authentication.class) {
                if (instance == null) {
                    instance = new Authentication();
                }
            }
        }
        return instance;
    }

    public static void init(Context context) {
        Context appContext = context.getApplicationContext();
        getInstance().appContext = appContext;
        getInstance().initFacebook(appContext);
        getInstance().initTwitter(appContext);
    }

    public void connectFacebook(@Nullable List<String> scopes, @NonNull AuthenticationCallback listener) {
        facebookAuthData = new AuthenticationData(scopes, listener);
        FacebookAuthActivity.start(appContext);
    }

    public void connectFacebook(@NonNull AuthenticationCallback listener) {
        connectFacebook(Collections.<String>emptyList(), listener);
    }

    public void disconnectFacebook() {
        facebookAuthData = null;
        LoginManager.getInstance().logOut();
    }

    public void connectGoogle(@Nullable List<String> scopes, @NonNull AuthenticationCallback listener) {
        googleAuthData = new AuthenticationData(scopes, listener);
        GoogleAuthActivity.start(appContext);
    }

    public void connectGoogle(@NonNull AuthenticationCallback listener) {
        connectGoogle(Collections.<String>emptyList(), listener);
    }

    public void disconnectGoogle() {
        googleAuthData = null;
        setGoogleDisconnectRequested(true);
    }

    public void revokeGoogle() {
        googleAuthData = null;
        setGoogleRevokeRequested(true);
    }

    public void connectTwitter(@NonNull AuthenticationCallback listener) {
        twitterAuthData = new AuthenticationData(Collections.<String>emptyList(), listener);
        TwitterAuthActivity.start(appContext);
    }

    public void disconnectTwitter() {
        twitterAuthData = null;
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        clearCookies();
    }

    public void connectInstagram(@Nullable List<String> scopes, @NonNull AuthenticationCallback listener) {
        instagramAuthData = new AuthenticationData(scopes, listener);
        InstagramAuthActivity.start(appContext);
    }

    public void connectInstagram(@NonNull AuthenticationCallback listener) {
        connectInstagram(Collections.<String>emptyList(), listener);
    }

    public void disconnectInstagram() {
        instagramAuthData = null;
        clearCookies();
    }

    public AuthenticationData getFacebookAuthData() {
        return facebookAuthData;
    }

    public AuthenticationData getGoogleAuthData() {
        return googleAuthData;
    }

    public AuthenticationData getTwitterAuthData() {
        return twitterAuthData;
    }

    public AuthenticationData getInstagramAuthData() {
        return instagramAuthData;
    }

    public boolean isGoogleDisconnectRequested() {
        return Utils.getBoolean(appContext, KEY_IS_GOOGLE_DISCONNECT_REQUESTED);
    }

    public void setGoogleDisconnectRequested(boolean isRequested) {
        Utils.saveBoolean(appContext, KEY_IS_GOOGLE_DISCONNECT_REQUESTED, isRequested);
    }

    public boolean isGoogleRevokeRequested() {
        return Utils.getBoolean(appContext, KEY_IS_GOOGLE_REVOKE_REQUESTED);
    }

    public void setGoogleRevokeRequested(boolean isRequested) {
        Utils.saveBoolean(appContext, KEY_IS_GOOGLE_REVOKE_REQUESTED, isRequested);
    }

    private void initFacebook(Context appContext) {
        String fbAppId = Utils.getMetaDataValue(appContext, appContext.getString(R.string.vv_com_applikeysolutions_socialmanager_facebookAppId));
        if (!TextUtils.isEmpty(fbAppId)) {
            FacebookSdk.setApplicationId(fbAppId);
            FacebookSdk.sdkInitialize(appContext);
        }
    }

    private void initTwitter(Context appContext) {
        String consumerKey = Utils.getMetaDataValue(appContext, appContext.getString(R.string.vv_com_applikeysolutions_library_twitterConsumerKey));
        String consumerSecret = Utils.getMetaDataValue(appContext, appContext.getString(R.string.vv_com_applikeysolutions_library_twitterConsumerSecret));

        if (consumerKey != null && consumerSecret != null) {
            TwitterConfig twitterConfig = new Builder(appContext)
                    .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                    .build();
            Twitter.initialize(twitterConfig);
        }
    }

    private void clearCookies() {
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieSyncManager.createInstance(appContext);
            CookieManager.getInstance().removeAllCookie();
        }
    }
}
