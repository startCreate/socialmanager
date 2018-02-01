package com.applikeysolutions.library.callback;

import com.applikeysolutions.library.NetworklUser;

public interface AuthenticationCallback {

    void onSuccess(NetworklUser socialUser);

    void onError(Throwable error);

    void onCancel();

}
