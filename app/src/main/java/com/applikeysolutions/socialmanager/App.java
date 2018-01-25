package com.applikeysolutions.socialmanager;


import android.app.Application;

import com.applikeysolutions.library.SimpleAuth;

public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        SimpleAuth.init(getBaseContext());
    }
}
