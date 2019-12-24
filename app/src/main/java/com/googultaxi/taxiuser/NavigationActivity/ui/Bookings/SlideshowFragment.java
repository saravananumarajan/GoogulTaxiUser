package com.googultaxi.taxiuser.NavigationActivity.ui.Bookings;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    TextView userid,droplocation,amount,journeytype,vehicletype,driverno,otp;
    ImageButton call;
RelativeLayout nonzero,zero;
Button trackdriver;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userid=view.findViewById(R.id.userid);
        nonzero=view.findViewById(R.id.nonzero);
        trackdriver=view.findViewById(R.id.track_driver);
        zero=view.findViewById(R.id.zero);
        userid.setTextColor(Color.WHITE);
        droplocation=view.findViewById(R.id.drop_location);
        droplocation.setTextColor(Color.WHITE);
        amount=view.findViewById(R.id.amount);
        amount.setTextColor(Color.WHITE);
        journeytype=view.findViewById(R.id.journey_type);
        journeytype.setTextColor(Color.WHITE);
        vehicletype=view.findViewById(R.id.vehicle_type);
        vehicletype.setTextColor(Color.WHITE);
        driverno=view.findViewById(R.id.driver_no);
        otp=view.findViewById(R.id.otp);
        driverno.setTextColor(Color.WHITE);
       call=view.findViewById(R.id.call);
       trackdriver.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               NavController navController= Navigation.findNavController(view);
               navController.navigate(R.id.trackdriverfragment);
           }
       });
       call.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.e("call",driverno.getText().toString());
               String[] split=driverno.getText().toString().split(":");
               Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + split[1]));
               startActivity(intent);
           }
       });

        final String m=getActivity().getIntent().getExtras().getString("mobile_no");
        getBookings(m);
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDriverLocation(m,handler);
         handler.postDelayed(this,5000);
            }
        },2000);

    }

    private void getDriverLocation(final String m, final Handler handler) {
        String url="http://fundevelopers.tech/Taxi/Booking/driver_location.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responsetext",response);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
String latitude=jsonObject.getString("latitude");
if(!latitude.equals("0"))
{
handler.removeCallbacksAndMessages(null);
trackdriver.setVisibility(View.VISIBLE);
}
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("errrorr",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("mobile_no",m);
                return params;
            }
        };
        singleton.getInstance(getActivity()).addtoRequestqueue(stringRequest);

    }

    private void getBookings(final String m) {
        String url="http://fundevelopers.tech/Taxi/Booking/bookings.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responsetext",response);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if(!jsonObject.getString("otp").equals("0")) {
                            nonzero.setVisibility(View.VISIBLE);
                            zero.setVisibility(View.GONE);
                            userid.setText("UserID:" + jsonObject.getString("user_id"));
                            droplocation.setText("droplocation:" + jsonObject.getString("drop_point"));
                            amount.setText("Amount:" + jsonObject.getString("cost"));
                            journeytype.setText("Journey type:" + jsonObject.getString("triptype"));
                            vehicletype.setText("Vehicle type:" + jsonObject.getString("cartype"));
                            driverno.setText("Driver no:" + jsonObject.getString("driver_no"));
                            otp.setText("otp is\n\t" + jsonObject.getString("otp"));
                        }
                        else
                        {
                            zero.setVisibility(View.VISIBLE);
                            nonzero.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.e("errrorr",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("mobile_no",m);
                return params;
            }
        };
        singleton.getInstance(getActivity()).addtoRequestqueue(stringRequest);

    }

}
