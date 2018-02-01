package com.applikeysolutions.socialmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.applikeysolutions.library.Authentication;
import com.applikeysolutions.library.NetworklUser;
import com.applikeysolutions.library.callback.AuthenticationCallback;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String FACEBOOK = "FACEBOOK";
    public static final String GOOGLE = "GOOGLE";
    public static final String TWITTER = "TWITTER";
    public static final String INSTAGRAM = "INSTAGRAM";
    private static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_facebook)
    void connectFacebook() {
        List<String> scopes = Arrays.asList("user_birthday", "user_friends");

        Authentication.getInstance().connectFacebook(scopes, new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, FACEBOOK, socialUser);
                Log.e(TAG, "onSuccess: " + socialUser.getFullName() + " " + socialUser.getEmail());
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
                Log.e(TAG, "onError: " + error.getMessage());

            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });
    }

    @OnClick(R.id.button_google)
    void connectGoogle() {
        List<String> scopes = Arrays.asList(
                "https://www.googleapis.com/auth/youtube",
                "https://www.googleapis.com/auth/youtube.upload"
        );

        Authentication.getInstance().connectGoogle(scopes, new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, GOOGLE, socialUser);
                Log.e(TAG, "onSuccess: " + socialUser.getFullName() + " " + socialUser.getEmail());
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
                Log.e(TAG, "onError: " + error.getMessage());
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });
    }

    @OnClick(R.id.button_twitter)
    void connectTwitter() {
        Authentication.getInstance().connectTwitter(new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, TWITTER, socialUser);
                Log.e(TAG, "onSuccess: " + socialUser.getFullName() + " " + socialUser.getEmail());
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });
    }

    @OnClick(R.id.button_instagram)
    void connectInstagram() {
        List<String> scopes = Arrays.asList("follower_list", "likes");

        Authentication.getInstance().connectInstagram(scopes, new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, INSTAGRAM, socialUser);
                Log.e(TAG, "onSuccess: " + socialUser.getFullName() + " " + socialUser.getEmail());
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

}
