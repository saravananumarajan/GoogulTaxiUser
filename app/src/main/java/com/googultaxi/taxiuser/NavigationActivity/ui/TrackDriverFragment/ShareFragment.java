package com.googultaxi.taxiuser.NavigationActivity.ui.TrackDriverFragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googultaxi.taxiuser.GetDetails.UserDetails;
import com.googultaxi.taxiuser.NavigationActivity.ui.Bookings.SlideshowFragment;
import com.googultaxi.taxiuser.NavigationActivity.ui.trip.LocalTrip;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ShareFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private ShareViewModel shareViewModel;
    MapView mMapView;
    GoogleMap mMap;
    Button locationBt;
    View locationButton;
    Marker marker;
    CameraUpdate location;
    Button driverlocbt;
    TextView distance;
    LocationManager locationManager;
    Location locationuser;
TextView time;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCurrentLocation();
        locationBt = view.findViewById(R.id.locbt);
        driverlocbt = view.findViewById(R.id.driverlocbt);
        distance = view.findViewById(R.id.distance);
     time=view.findViewById(R.id.time);
        driverlocbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location != null) {
                    mMap.animateCamera(location);
                } else {
                    Toast.makeText(getContext(), "Wait for sometime to animate", Toast.LENGTH_LONG).show();
                }
            }
        });
        locationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationButton.callOnClick();
            }
        });
//SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
//final String mobileno=sharedPreferences.getString("phoneNumber",null);
//Log.e("mobilenotrack",mobileno);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                trackDriver(LocalTrip.mobile_no);
                handler.postDelayed(this, 5000);
            }
        }, 2000);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentLocation() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
          Log.e("not enter","notenter");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

    }

    private void trackDriver(final String m) {
//        Log.e("userdata",m);
        String url = "http://fundevelopers.tech/Taxi/Booking/driver_location.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(String response) {
                Log.e("responsetexttracking", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String latitude = jsonObject.getString("latitude");
                        String longitude = jsonObject.getString("longitude");
                        if (!latitude.equals("0")) {
                            if (marker != null) {
                                marker.remove();
                            }
                            int height = 100;
                            int width = 100;
                            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.logoicon);
                            Bitmap b=bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                           // now = mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title("Driver location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                            location = CameraUpdateFactory.newLatLngZoom(
                                    marker.getPosition(), 15);
                            }
                        }
                    }
                catch (JSONException e) {
                        e.printStackTrace();
                    }
                catch (Exception e)
                {
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
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    private double calculateDistance(LatLng position, Location location) {
        double lon1=position.longitude;
        double lon2=location.getLongitude();
        double lat1=position.latitude;
        double lat2=location.getLatitude();
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.5937,78.9629),
                4.0f));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        if(locationButton!=null)
        {
            locationButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("location",""+location.getLatitude());
        locationuser=location;
        if(marker!=null) {
            distance.setText("Distance\n" + (int) calculateDistance(marker.getPosition(), locationuser) + "km");
            int speedIs10MetersPerMinute = 5;
            Location location1=new Location("");
            location1.setLatitude(marker.getPosition().latitude);
            location1.setLongitude(marker.getPosition().longitude);
            float distanceInMeters=locationuser.distanceTo(location1);
            float estimatedDriveTimeInMinutes = distanceInMeters / speedIs10MetersPerMinute;
            time.setText(""+(int)estimatedDriveTimeInMinutes+"min Approx...");
        }
        }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
Toast.makeText(getContext(),"Please turn on your location",Toast.LENGTH_LONG).show();
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
