package com.googultaxi.taxiuser.Help;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.googultaxi.taxiuser.R;

public class DeveloperDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_details);
getSupportActionBar().hide();
    }
}
