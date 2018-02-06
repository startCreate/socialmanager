package com.applikeysolutions.library.networks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.applikeysolutions.library.Authentication;
import com.applikeysolutions.library.AuthenticationActivity;
import com.applikeysolutions.library.AuthenticationData;
import com.applikeysolutions.library.NetworklUser;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import java.util.ArrayList;
import java.util.List;

public class GoogleAuthActivity extends AuthenticationActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 1000;
    private GoogleApiClient googleApiClient;
    private boolean retrySignIn;

    public static void start(Context context) {
        Intent intent = new Intent(context, GoogleAuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestProfile()
                .requestEmail();

        setupScopes(gsoBuilder);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build())
                .build();
    }

    @Override public void onConnected(@Nullable Bundle bundle) {
        Runnable signIn = () -> {
            retrySignIn = true;
            GoogleAuthActivity.this.startSignInFlows();
        };
        if (isGoogleDisconnectRequested()) {
            handleDisconnectRequest(signIn);
        } else if (isGoogleRevokeRequested()) {
            handleRevokeRequest(signIn);
        } else {
            startSignInFlows();
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != RC_SIGN_IN || resultCode != RESULT_OK) {
            return;
        }
        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if ((!isGoogleDisconnectRequested() && !isGoogleRevokeRequested()) || retrySignIn) {
            retrySignIn = false;
            handleSignInResult(signInResult);
        }
    }

    @Override public void onConnectionSuspended(int i) {
        handleError(new Throwable("connection suspended."));
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Throwable error = new Throwable(connectionResult.getErrorMessage());
        handleError(error);
    }

    @Override protected AuthenticationData getAuthenticationData() {
        return Authentication.getInstance().getGoogleAuthData();
    }

    private boolean isGoogleRevokeRequested() {
        return Authentication.getInstance().isGoogleRevokeRequested();
    }

    private boolean isGoogleDisconnectRequested() {
        return Authentication.getInstance().isGoogleDisconnectRequested();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result == null) {
            //handCancel();
            Authentication.getInstance().onLoginCancel();
            finish();
            return;
        }
        if (result.isSuccess() && result.getSignInAccount() != null) {
            final GoogleSignInAccount acct = result.getSignInAccount();
            final NetworklUser user = NetworklUser.newBuilder()
                    .userId(acct.getId())
                    .accessToken(acct.getIdToken())
                    .profilePictureUrl(acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "")
                    .email(acct.getEmail())
                    .fullName(acct.getDisplayName())
                    .build();

            getAccessToken(acct, accessToken -> {
                user.setAccessToken(accessToken);
                Authentication.getInstance().onLoginSuccess(user);
        //        GoogleAuthActivity.this.handleSuccess(user);
            });
        } else {
            String errorMsg = result.getStatus().getStatusMessage();
            if (errorMsg == null) {
               // handCancel();
                Authentication.getInstance().onLoginCancel();
            } else {
                Throwable error = new Throwable(result.getStatus().getStatusMessage());
               // handleError(error);
                Authentication.getInstance().onLoginError(error.getCause());
            }
        }
        finish();
    }

    private void getAccessToken(final GoogleSignInAccount account, final AccessTokenListener listener) {
        showDialog();

        AsyncTask.execute(() -> {
            try {
                if (account.getAccount() == null) {
                    dismissProgress();
                    GoogleAuthActivity.this.handleError(new RuntimeException("Account is null"));
                } else {
                    dismissProgress();
                    Authentication.getInstance().setGoogleDisconnectRequested(false);
                    Authentication.getInstance().setGoogleRevokeRequested(false);
                    String token = GoogleAuthUtil.getToken(GoogleAuthActivity.this.getApplicationContext(), account.getAccount().name, GoogleAuthActivity.this.getAccessTokenScope());
                    listener.onTokenReady(token);
                }
            } catch (Exception e) {
                e.printStackTrace();
                dismissProgress();
                GoogleAuthActivity.this.handleError(e);
            }
        });
    }

    private String getAccessTokenScope() {
        String scopes = "oauth2:id profile email";
        if (getAuthenticationData().getScopes().size() > 0) {
            scopes = "oauth2:" + TextUtils.join(" ", getAuthenticationData().getScopes());
        }
        return scopes;
    }

    private void handleDisconnectRequest(final Runnable onSignOut) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
            onSignOut.run();
            Authentication.getInstance().setGoogleDisconnectRequested(false);
        });
    }

    private void handleRevokeRequest(final Runnable onRevoke) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(status -> {
            onRevoke.run();
            Authentication.getInstance().setGoogleRevokeRequested(false);
        });
    }

    private void startSignInFlows() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void setupScopes(GoogleSignInOptions.Builder builder) {
        List<Scope> scopes = getScopes();
        if (scopes.size() == 1) {
            builder.requestScopes(scopes.get(0));
        } else if (scopes.size() > 1) {
            List<Scope> restScopes = scopes.subList(1, scopes.size());
            Scope[] restScopesArray = new Scope[restScopes.size()];
            restScopesArray = scopes.toArray(restScopesArray);
            builder.requestScopes(scopes.get(0), restScopesArray);
        }
    }

    private List<Scope> getScopes() {
        List<Scope> scopes = new ArrayList<>();
        for (String str : getAuthenticationData().getScopes()) {
            scopes.add(new Scope(str));
        }
        return scopes;
    }

    private interface AccessTokenListener {

        void onTokenReady(String accessToken);
    }
}
