package com.applikeysolutions.library;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.List;

public abstract class AuthenticationActivity extends AppCompatActivity {

    protected ProgressDialog dialog;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    protected void handCancel() {
        Authentication.getInstance().onLoginCancel();
     //   getScopes().getCallback().onCancel();
        finish();
    }

    protected void handleError(Throwable error) {
Authentication.getInstance().onLoginError(error);
//        getScopes().getCallback().onError(error);
        finish();
    }

    protected void handleSuccess(NetworklUser user) {
        Authentication.getInstance().onLoginSuccess(user);
//        getScopes().getCallback().onSuccess(user);
        finish();
    }

    protected final void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
            dialog.setCancelable(false);
            dialog.setMessage("Please wait ....");
        }
        dialog.show();
    }

    protected final void dismissProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected abstract List<String> getAuthScopes();
}
