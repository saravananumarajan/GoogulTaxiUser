package com.googultaxi.taxiuser.Otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.googultaxi.taxiuser.GetDetails.UserDetails;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import java.util.Random;

public class RequestMobileNo extends AppCompatActivity {
EditText mobile_no;
Button submit;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_mobile_no);
        getSupportActionBar().hide();
    mobile_no=findViewById(R.id.mobile_no);
    submit=findViewById(R.id.submit);
    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(RequestMobileNo.this,OtpActivity.class);
            intent.putExtra("mobile_no","+91"+mobile_no.getText().toString());
            startActivity(intent);
        }
    });
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, UserDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("mobile_no",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            startActivity(intent);
        }
    }
}
