package com.applikeysolutions.socialmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.applikeysolutions.library.Authentication;

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

       /* Authentication.getInstance().connectFacebook(scopes, new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, FACEBOOK, socialUser);
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });*/

       Authentication.getInstance().connectFacebook(scopes,null).login()
       .subscribe(user ->
               InfoActivity.start(MainActivity.this, GOOGLE, user));

    }

    @OnClick(R.id.button_google)
    void connectGoogle() {
        List<String> scopes = Arrays.asList(
                "https://www.googleapis.com/auth/youtube",
                "https://www.googleapis.com/auth/youtube.upload"
        );

       /* Authentication.getInstance().connectGoogle(scopes, new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, GOOGLE, socialUser);
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });*/

       Authentication.getInstance().connectGoogle(scopes,null).login().subscribe(networklUser -> InfoActivity.start(MainActivity.this, GOOGLE, networklUser));
    }

    @OnClick(R.id.button_twitter)
    void connectTwitter() {
       /* Authentication.getInstance().connectTwitter(new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, TWITTER, socialUser);
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });*/
       Authentication.getInstance().connectTwitter(null).login().subscribe(networklUser -> InfoActivity.start(MainActivity.this,TWITTER, networklUser));
    }

    @OnClick(R.id.button_instagram)
    void connectInstagram() {
        List<String> scopes = Arrays.asList("follower_list", "likes");

       /* Authentication.getInstance().connectInstagram(scopes, new AuthenticationCallback() {
            @Override
            public void onSuccess(NetworklUser socialUser) {
                InfoActivity.start(MainActivity.this, INSTAGRAM, socialUser);
            }

            @Override
            public void onError(Throwable error) {
                showToast(error.getMessage());
            }

            @Override
            public void onCancel() {
                showToast("Canceled");
            }
        });*/
       Authentication.getInstance().connectInstagram(scopes,null).login().subscribe(networklUser -> InfoActivity.start(this, INSTAGRAM,networklUser));
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
