package com.applikeysolutions.library.networks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.applikeysolutions.library.Authentication;
import com.applikeysolutions.library.AuthenticationActivity;
import com.applikeysolutions.library.NetworklUser;
import com.applikeysolutions.library.R;
import com.applikeysolutions.library.Utils;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class GoogleAuthActivity extends AuthenticationActivity /*implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks*/ {

    private static final int RC_SIGN_IN = 1000;
    private GoogleSignInClient googleApiClient;
    private boolean retrySignIn;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, GoogleAuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Utils.getMetaDataValue(this, getString(R.string.vv_com_applikeysolutions_library_googleWebClientId)))
                .requestServerAuthCode(Utils.getMetaDataValue(this, getString(R.string.vv_com_applikeysolutions_library_googleWebClientId)))
                .requestId()
                .requestProfile()
                .requestEmail();

        setupScopes(gsoBuilder);

        /*googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build())
                .build();*/

        googleApiClient = GoogleSignIn.getClient(this, gsoBuilder.build());
        signIn();
    }

//    @Override public void onConnected(@Nullable Bundle bundle) {
      /*  Runnable signIn = () -> {
            retrySignIn = true;
            GoogleAuthActivity.this.signIn();
        };
        if (isGoogleDisconnectRequested()) {
            handleDisconnectRequest(signIn);
        } else if (isGoogleRevokeRequested()) {
            handleRevokeRequest(signIn);
        } else {
            signIn();
        }*/
//    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != RC_SIGN_IN || resultCode != RESULT_OK) {
            return;
        }
        handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));

        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if ((!isGoogleDisconnectRequested() && !isGoogleRevokeRequested()) || retrySignIn) {
            retrySignIn = false;
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

  /*  @Override public void onConnectionSuspended(int i) {
        handleError(new Throwable("connection suspended."));
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Throwable error = new Throwable(connectionResult.getErrorMessage());
        handleError(error);
    }*/

    protected List<String> getAuthScopes() {
        return Authentication.getInstance().getGoogleScopes();
    }

    private boolean isGoogleRevokeRequested() {
        return Authentication.getInstance().isGoogleRevokeRequested();
    }

    private boolean isGoogleDisconnectRequested() {
        return Authentication.getInstance().isGoogleDisconnectRequested();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> result) {
        /*if (result == null) {
            handCancel();
//            Authentication.getInstance().onLoginCancel();
//            finish();
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
                    .tokenId(acct.getIdToken())
                    .build();

            getAccessToken(acct, accessToken -> {
                user.setAccessToken(accessToken);
                //Authentication.getInstance().onLoginSuccess(user);
                handleSuccess(user);
            });
        } else {
            String errorMsg = result.getStatus().getStatusMessage();
            if (errorMsg == null) {
                handCancel();
//                Authentication.getInstance().onLoginCancel();
            } else {
                Throwable error = new Throwable(result.getStatus().getStatusMessage());
                handleError(error);
//                Authentication.getInstance().onLoginError(error.getCause());
            }
        }*/

        try {
            GoogleSignInAccount acct = result.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
             NetworklUser user = NetworklUser.newBuilder()
                    .userId(acct.getId())
                  //  .accessToken(acct.getIdToken())
                    .profilePictureUrl(acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "")
                    .email(acct.getEmail())
                    .fullName(acct.getDisplayName())
                    .tokenId(acct.getIdToken())
                     .serverAuthCode(acct.getServerAuthCode())
                    .build();
            handleSuccess(user);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
          /*  Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);*/
        }
    }

    private void getAccessToken(final GoogleSignInAccount account, final AccessTokenListener listener) {
        showDialog();

        AsyncTask.execute(() -> {
            try {
                if (account.getAccount() == null) {
                    dismissProgress();
                    handleError(new RuntimeException("Account is null"));
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
                handleError(e);
            }
        });
    }

    private String getAccessTokenScope() {
        String scopes = "oauth2:id profile email";
        if (getAuthScopes().size() > 0) {
            scopes = "oauth2:" + TextUtils.join(" ", getAuthScopes());
        }
        return scopes;
    }

  /*  private void handleDisconnectRequest(final Runnable onSignOut) {
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
    }*/

    private void signIn() {
        /*Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);*/

        Intent signInIntent = googleApiClient.getSignInIntent();
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
        for (String str : getAuthScopes()) {
            scopes.add(new Scope(str));
        }
        return scopes;
    }

    private interface AccessTokenListener {

        void onTokenReady(String accessToken);
    }
}
