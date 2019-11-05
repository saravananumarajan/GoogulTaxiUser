package com.googultaxi.taxiuser.NavigationActivity.ui.slideshow;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class WaitFragment extends Fragment {

View navigation_view;
    public WaitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wait, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      navigation_view=view;
        final String m=getActivity().getIntent().getExtras().getString("mobile_no");
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.43.83/Taxi/Booking/bookings.php";
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("responsetext",response);
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String driverno=jsonObject.getString("driver_no");
                            if(!driverno.equals("0"))
                            {

                                final NavController navController= Navigation.findNavController(navigation_view);
                                navController.navigate(R.id.bookings);
                            handler.removeCallbacksAndMessages(null);
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
                handler.postDelayed(this,5000);

            }
        },2000);
    }
}
