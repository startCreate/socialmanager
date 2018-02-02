package com.applikeysolutions.library;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public abstract class AuthenticationActivity extends AppCompatActivity {

    protected ProgressDialog dialog;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    protected void handCancel() {
        getAuthenticationData().getCallback().onCancel();
        finish();
    }

    protected void handleError(Throwable error) {
        getAuthenticationData().getCallback().onError(error);
        finish();
    }

    protected void handleSuccess(NetworklUser user) {
        getAuthenticationData().getCallback().onSuccess(user);
        finish();
    }

    protected void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
            dialog.setCancelable(false);
            dialog.setMessage("Please wait ....");
        }
        dialog.show();
    }

    protected abstract AuthenticationData getAuthenticationData();

}
