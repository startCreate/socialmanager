package com.applikeysolutions.library;

import java.util.ArrayList;
import java.util.List;

class AuthenticationData {

    private List<String> scopes;
    private AuthenticationCallback callback;

    AuthenticationData(List<String> scopes, AuthenticationCallback callback) {
        this.scopes = new ArrayList<>(scopes);
        this.callback = callback;
    }

    List<String> getScopes() {
        return scopes;
    }

    AuthenticationCallback getCallback() {
        return callback;
    }

}
