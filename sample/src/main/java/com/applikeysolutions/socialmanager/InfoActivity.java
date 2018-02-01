package com.applikeysolutions.socialmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.applikeysolutions.library.Authentication;
import com.applikeysolutions.library.NetworklUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoActivity extends AppCompatActivity {

    private static final String USER_DATA = "USER_DATA";
    private static final String TYPE = "TYPE";

    @BindView(R.id.text_user)
    TextView userInfo;
    @BindView(R.id.button_disconnect)
    Button buttonDisconnect;
    private String type;

    public static void start(Context context, String type, NetworklUser socialUser) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(USER_DATA, socialUser);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        NetworklUser socialUser = getIntent().getParcelableExtra(USER_DATA);
        userInfo.setText(socialUser.toString());
        type = getIntent().getStringExtra(TYPE);
    }

    @OnClick(R.id.button_disconnect)
    void disconnect() {
        if (MainActivity.FACEBOOK.equals(type)) {
            Authentication.getInstance().disconnectFacebook();
        } else if (MainActivity.GOOGLE.equals(type)) {
            Authentication.getInstance().disconnectGoogle();
        } else if (MainActivity.TWITTER.equals(type)) {
            Authentication.getInstance().disconnectTwitter();
        } else if (MainActivity.INSTAGRAM.equals(type)) {
            Authentication.getInstance().disconnectInstagram();
        }
        finish();
    }
}
