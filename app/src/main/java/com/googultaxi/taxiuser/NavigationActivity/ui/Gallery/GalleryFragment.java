package com.googultaxi.taxiuser.NavigationActivity.ui.Gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import java.util.HashMap;
import java.util.Map;


public class GalleryFragment extends Fragment  {

    TextView userid,droplocation,amount,journeytype,vehicletype,driverno;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
             return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url="http://fundevelopers.tech/Taxi/Bookings/bookings.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
Log.e("responsetext",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//Log.e("errrorr",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("mobile_no",getActivity().getIntent().getExtras().getString("mobile_no"));
                return super.getParams();
            }
        };
        singleton.getInstance(getActivity()).addtoRequestqueue(stringRequest);
    }


    }
