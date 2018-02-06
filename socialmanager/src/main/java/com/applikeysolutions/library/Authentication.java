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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.applikeysolutions.library.Network.FACEBOOK;
import static com.applikeysolutions.library.Network.GOOGLE;
import static com.applikeysolutions.library.Network.INSTAGRAM;
import static com.applikeysolutions.library.Network.TWITTER;
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
    private PublishSubject<NetworklUser> userEmitter;
    private Network network;
    List<String> scopes = new ArrayList<>();

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

    public Observable<NetworklUser> login() {
        userEmitter = PublishSubject.create();
        /*appContext.startActivity(*/getIntent()/*)*/;
        return userEmitter;
    }


    public void onLoginSuccess(NetworklUser socialUser) {
        if (userEmitter != null) {
         //   NetworklUser copy = new NetworklUser(socialUser);
            userEmitter.onNext(socialUser);
            userEmitter.onComplete();
        }
    }

    public void onLoginError(Throwable throwable) {
        if (userEmitter != null) {
            Throwable copy = new Throwable(throwable);
            userEmitter.onError(copy);
        }
    }

    public void onLoginCancel() {
        if (userEmitter != null) {
            userEmitter.onComplete();
        }
    }

    public void getIntent() {
        if (network == FACEBOOK) {
            FacebookAuthActivity.start(appContext);
           /* Intent intent = new Intent(appContext, FbLoginHiddenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;*/
        } else if (network == GOOGLE) {
            GoogleAuthActivity.start(appContext);
           /* Intent intent = new Intent(appContext, GoogleLoginHiddenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(GoogleLoginHiddenActivity.EXTRA_CLIENT_ID, igClientId);
            return intent;*/
        } else if (network == INSTAGRAM) {
            InstagramAuthActivity.start(appContext);
            /*Intent intent = new Intent(appContext, IgLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(IgLoginActivity.EXTRA_CLIENT_ID, igClientId);
            intent.putExtra(IgLoginActivity.EXTRA_CLIENT_SECRET, igCleintSecret);
            intent.putExtra(IgLoginActivity.EXTRA_REDIRECT_URL, igRedirectUrl);
            return intent;*/
        }else if (network == TWITTER){
            TwitterAuthActivity.start(appContext);
        } else {
            throw new RuntimeException("No such network");
        }
    }

    public Authentication connectFacebook(@Nullable List<String> scopes,/* @NonNull */AuthenticationCallback listener) {
        facebookAuthData = new AuthenticationData(scopes, listener);
      //  FacebookAuthActivity.start(appContext);
       network = FACEBOOK;
       //this.scopes = scopes;

       return this;
    }

    public void connectFacebook(@NonNull AuthenticationCallback listener) {
        connectFacebook(Collections.<String>emptyList(), listener);
    }

    public void disconnectFacebook() {
        facebookAuthData = null;
        LoginManager.getInstance().logOut();
    }

    public Authentication connectGoogle(@Nullable List<String> scopes,/* @NonNull */AuthenticationCallback listener) {
        googleAuthData = new AuthenticationData(scopes, listener);
        network = GOOGLE;
        return this;
        //GoogleAuthActivity.start(appContext);
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

    public Authentication connectTwitter(@NonNull AuthenticationCallback listener) {
        twitterAuthData = new AuthenticationData(Collections.<String>emptyList(), listener);
     //   TwitterAuthActivity.start(appContext);
        network = TWITTER;
        return this;
    }

    public void disconnectTwitter() {
        twitterAuthData = null;
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        clearCookies();
    }

    public Authentication connectInstagram(@Nullable List<String> scopes, @NonNull AuthenticationCallback listener) {
        instagramAuthData = new AuthenticationData(scopes, listener);
       // InstagramAuthActivity.start(appContext);
        network = INSTAGRAM;
        return this;
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
