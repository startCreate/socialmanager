package com.applikeysolutions.library;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationData {

    private List<String> scopes;
    private AuthenticationCallback callback;

    AuthenticationData(List<String> scopes, AuthenticationCallback callback) {
        this.scopes = new ArrayList<>(scopes);
        this.callback = callback;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public AuthenticationCallback getCallback() {
        return callback;
    }
}
