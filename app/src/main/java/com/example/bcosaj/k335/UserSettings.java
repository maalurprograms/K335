package com.example.bcosaj.k335;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserSettings extends AppCompatActivity {

    private ArrayList<String> PERSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        PERSON = (ArrayList)getIntent().getCharSequenceArrayListExtra("PERSON");
    }
}
