package com.applikeysolutions.library;

public interface AuthenticationCallback {

    void onSuccess(NetworklUser user);

    void onError(Throwable error);

    void onCancel();
}
