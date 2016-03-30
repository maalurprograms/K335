package com.example.bcosaj.k335;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserSettings extends AppCompatActivity {

    private SharedPreferences PREFS;
    private String PERSON;
    private String YOUTUBE_ACCOUNT;
    private String TWITTER_ACCOUNT;
    private String FACEBOOK_ACCOUNT;
    private String INSTAGRAM_ACCOUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        PREFS = PreferenceManager.getDefaultSharedPreferences(this);
        PERSON = PREFS.getString("PERSON", "");
        YOUTUBE_ACCOUNT = PREFS.getString(PERSON, "").split(":")[0];
        TWITTER_ACCOUNT = PREFS.getString(PERSON, "").split(":")[1];
        FACEBOOK_ACCOUNT = PREFS.getString(PERSON, "").split(":")[2];
        INSTAGRAM_ACCOUNT = PREFS.getString(PERSON, "").split(":")[3];

        final EditText app_username = (EditText) findViewById(R.id.app_username);
        app_username.setText(PERSON);

        final EditText t_username = (EditText) findViewById(R.id.t_username);
        t_username.setText(TWITTER_ACCOUNT);

        final EditText i_username = (EditText) findViewById(R.id.i_username);
        i_username.setText(INSTAGRAM_ACCOUNT);

        final EditText yt_username = (EditText) findViewById(R.id.yt_username);
        yt_username.setText(YOUTUBE_ACCOUNT);

        final EditText f_username = (EditText) findViewById(R.id.f_username);
        f_username.setText(FACEBOOK_ACCOUNT);

        ImageView imageView = (ImageView)findViewById(R.id.settings_save);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PREFS.edit().putString("PERSON", app_username.getText().toString()).commit();
                PREFS.edit().putString(app_username.getText().toString(), yt_username.getText().toString()+":"+t_username.getText().toString()+":"+f_username.getText().toString()+":"+i_username.getText().toString()).commit();
                finish();
            }
        });

    }
}
