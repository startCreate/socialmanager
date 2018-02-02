package com.applikeysolutions.library;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

abstract class AuthenticationActivity extends AppCompatActivity {

    protected ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    protected void handCancel() {
        getAuthData().getCallback().onCancel();
        finish();
    }

    protected void handleError(Throwable error) {
        getAuthData().getCallback().onError(error);
        finish();
    }

    protected void handleSuccess(NetworklUser user) {
        getAuthData().getCallback().onSuccess(user);
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

    protected abstract AuthenticationData getAuthData();

}
