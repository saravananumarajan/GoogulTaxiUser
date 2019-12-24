package com.googultaxi.taxiuser.GetDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.googultaxi.taxiuser.NavigationActivity.NavigationDrawer;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import java.util.HashMap;
import java.util.Map;

public class UserDetails extends AppCompatActivity {
public static String phoneNo;
EditText user_id;
Button submit;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
String value=sharedPreferences.getString("user_id",null);
String ph=sharedPreferences.getString("mobile_no",null);
if(value!=null && ph!=null)
{
    Intent intent=new Intent(UserDetails.this, NavigationDrawer.class);
    intent.putExtra("user_id",value);
    intent.putExtra("mobile_no",ph);
    startActivity(intent);
}
else {
    setContentView(R.layout.activity_user_details);
    getSupportActionBar().hide();
    user_id = findViewById(R.id.userdetais);
    submit = findViewById(R.id.submit);
    phoneNo = getIntent().getExtras().getString("mobile_no");
    Log.e("phoneno", phoneNo);
    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateValue(user_id.getText().toString(), phoneNo);
        }
    });
}
    }

    private void updateValue(final String user_id, final String phoneNo) {
   String url="http://fundevelopers.tech/Taxi/Booking/add_user_details.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")||(response.contains("present")))
                {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("user_id",user_id);
                    editor.putString("mobile_no",phoneNo);
                    editor.apply();
                    Intent intent=new Intent(UserDetails.this, NavigationDrawer.class);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("mobile_no",phoneNo);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<String,String>();
                hashMap.put("user_id",user_id);
                hashMap.put("mobile_no",phoneNo);
            return hashMap;
            }
        };
        singleton.getInstance(getApplicationContext()).addtoRequestqueue(stringRequest);
}
}
