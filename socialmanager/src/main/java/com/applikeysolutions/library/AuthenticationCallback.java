package com.applikeysolutions.library;

public interface AuthenticationCallback {

    void onSuccess(NetworklUser socialUser);

    void onError(Throwable error);

    void onCancel();


}
