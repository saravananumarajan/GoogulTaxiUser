package com.googultaxi.taxiuser;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int START_LEVEL = 1;
    private int mLevel;
    String k;
    Toast toast;
    private Button mNextLevelButton;
    final Handler handler = new Handler();
    private TextView mLevelTextView;
    int[] count1 = {0};
    private GoogleMap mMap;
    Marker now;
    //  GoogleMap googlemap;
    //LatLng sydney;
    //  final
    String latitude;
    String longitude;
    String get;
    String route;
    public float lat;
    public float lang;
    LatLng sydney;
    ImageButton bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        k=getIntent().getExtras().getString("driverno");
        bt=findViewById(R.id.recenter);
        bt.setBackground(getResources().getDrawable(android.R.drawable.btn_default));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void check(final GoogleMap gMap) {
        String url = "http://fundevelopers.tech/Taxi/MapRetrieve.php";
        final boolean[] count = {true};
        final RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);

//Toast.makeText(getApplicationContext(),"route is"+route,Toast.LENGTH_LONG).show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
             //     Toast.makeText(MapsActivity.this,response, Toast.LENGTH_LONG).show();
                get = response;
                String[] request = get.split(":");
                latitude = request[0];
                Log.e("latitude",get);
                longitude = request[1];
                lat = Float.parseFloat(latitude);
                lang = Float.parseFloat(longitude);

                //      Toast.makeText(getApplicationContext(), latitude + "" + longitude, Toast.LENGTH_LONG).show();
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (now != null) {
                            now.remove();
                        }

                        if(lat==0 || lang ==0)
                        {
                            if(count1[0] ==0)
                            {
//                                toast=Toast.makeText(MapsActivity.this,"Driver went to offline",Toast.LENGTH_LONG);
//                                toaststyle();
//                                Intent intent=new Intent(MapsActivity.this, AdminDrawer.class);
//                                startActivity(intent);
//                                count1[0] =1;
                            }

                            //dialog.cancel();
                        }
                        sydney = new LatLng(lat, lang);
                        int height = 100;
                        int width = 100;
                        Drawable drawable=getResources().getDrawable(R.drawable.maprouteicon);
                        Bitmap b = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

                        Canvas canvas = new Canvas(b);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);

                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        now = mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                        //new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.bus1));
                        // now.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus1));
                        if (count1[0]==0) {

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 15.0f));
                            count1[0]++;
                        }

                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 15.0f));
                            }
                        });

                    }

                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(), "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("route_no",k);
                return params;

            }


            //requestQueue.add(stringRequest);

        };
        singleton.getInstance(MapsActivity.this).addtoRequestqueue(stringRequest);
    }

    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

            // GoogleMap googleMap;
            mMap = googleMap;
            double latitude = 20.5937;
            double longitude = 78.9629;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 4.0f));



            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    check(mMap);
                    handler.postDelayed(this, 2000);
                }
            }, 2000);


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            else {
            Log.e("loc","location");
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setMyLocationEnabled(true);
            }
//        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setOnMyLocationClickListener(this);


            // Add a marker in Sydney and move the camera
//Toast.makeText(getApplicationContext(),""+lat,Toast.LENGTH_LONG).show();


        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    handler.removeCallbacksAndMessages(null);
    }

}
