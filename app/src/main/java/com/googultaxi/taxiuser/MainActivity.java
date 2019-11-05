package com.googultaxi.taxiuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    EditText driverno,userid;
Button track;
ImageButton help;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     checkPermissions();
        getSupportActionBar().hide();
    driverno=findViewById(R.id.routeno);
    track=findViewById(R.id.trackbt);
    help=findViewById(R.id.help);
   userid=findViewById(R.id.userid);
   help.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
Intent intent=new Intent(getApplicationContext(),HelpActivity.class);
startActivity(intent);
       }
   });
    track.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        verify(driverno.getText().toString(),userid.getText().toString());
        }
    });
    }

    private void verify(final String driver, final String user) {
String url="http://fundevelopers.tech/Taxi/verifyuser.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("present"))
                {
                    Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                    intent.putExtra("driverno",driverno.getText().toString());
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Enter the correct user id", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String,String>();
                params.put("user",user);
                params.put("driver",driver);
                return params;

            }
        };
        singleton.getInstance(getApplicationContext()).addtoRequestqueue(stringRequest);
   }
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(MainActivity.this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }
}
