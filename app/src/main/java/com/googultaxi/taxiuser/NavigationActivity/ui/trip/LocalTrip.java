package com.googultaxi.taxiuser.NavigationActivity.ui.trip;


import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.SphericalUtil;
import com.googultaxi.taxiuser.MainActivity;
import com.googultaxi.taxiuser.NavigationActivity.NavigationDrawer;
import com.googultaxi.taxiuser.NavigationActivity.SimpleGeofence;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocalTrip extends Fragment implements OnMapReadyCallback, LocationListener {
BottomSheetDialog bottomSheetDialog;
public static double minibasecost,miniaccost,mininonaccost;
double sedanbasecost,sedanaccost,sedannonaccost;
 double hatchbackbasecost,hatchbackaccost,hatchbacknonaccost;
double suvbasecost,suvaccost,suvnonaccost;
    GoogleMap mMap;
LocationManager locationManager;
AlertDialog alertDialog;
public static String acornonac=null;
public static int cartype=0;
View locationButton;
View navigation_view;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
EditText drop;
ImageButton pickup;
public static String pickup_point,drop_point,trip_type,car_type,distance,cost,mobile_no;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
MarkerOptions pickupmarker,dropmarker;
MapView mMapView;
String pickupsplit;
Button locationbt;
double km;
TextView pickup_text;
    public static String latitude,longitude;

    public LocalTrip() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_local_trip, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        return v;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissions();
        NavigationDrawer.switchCompat.setChecked(false);
        getCost();
        ProgressDialog progressDialog=new ProgressDialog(getContext());
     progressDialog.setTitle("Please wait");
        getCurrentLocation();
       navigation_view=view;
        showBottomSheet(view);
        pickup_text=view.findViewById(R.id.pickup_text);
        locationbt=view.findViewById(R.id.locationbt);
        locationbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationButton.callOnClick();
            }
        });
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        pickup=view.findViewById(R.id.pickup);
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                alertDialog = null;
                // ...Irrelevant code for customizing the buttons and title

                final LayoutInflater inflater = getLayoutInflater();
                Button currentloc,anotherloc;
                final View dialogView= inflater.inflate(R.layout.get_location, null);
               currentloc=dialogView.findViewById(R.id.currentlocation);
               anotherloc=dialogView.findViewById(R.id.anotherloc);
               currentloc.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    //try {
                        pickupsplit = latitude + "," + longitude;
                        alertDialog.dismiss();
                        pickup_text.setText("current location");
                        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Pickup location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                latLng, 15);
                        mMap.animateCamera(location);
                    //}

                    }
             });
             anotherloc.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                     alertDialog.dismiss();
                     // ...Irrelevant code for customizing the buttons and title
                     final LayoutInflater inflater = getLayoutInflater();
                     Button submit;
                     final View dialogView= inflater.inflate(R.layout.get_another_location, null);
                     final EditText nearestloc=dialogView.findViewById(R.id.nearestloc);
                     submit=dialogView.findViewById(R.id.submit);
                     submit.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             pickup_text.setText(nearestloc.getText().toString());
                             String getloc=getLocationFromAddress(nearestloc.getText().toString());
                             String[] split=getloc.split(",");
                             alertDialog.dismiss();
                             LatLng latLng=new LatLng(Double.parseDouble(split[0]),Double.parseDouble(split[1]));
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Pickup location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).draggable(true));
                             CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                     latLng, 15);
                             mMap.animateCamera(location);
                            pickupsplit=split[0]+","+split[1];
                         Toast.makeText(getContext(),"Drag marker to correct location",Toast.LENGTH_LONG).show();
                         }
                     });
                     dialogBuilder.setView(dialogView);
                     alertDialog=dialogBuilder.create();
                     alertDialog.show();
                     alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 }
             });
                dialogBuilder.setView(dialogView);
                alertDialog=dialogBuilder.create();

                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        drop=view.findViewById(R.id.drop);
        mobile_no=getActivity().getIntent().getExtras().getString("mobile_no");
    progressDialog.dismiss();


 //     simpleGeofence.toGeofence();
    }
        private void getCost() {
        String url="http://fundevelopers.tech/Taxi/Booking/get_cost.php";
        //   Log.e("usernamee",username);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responsecost",response);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String mini=jsonObject.getString("mini");
                    String sedan=jsonObject.getString("sedan");
                    String hatchback=jsonObject.getString("hatchback");
                    String suv=jsonObject.getString("suv");
                    String[] minisplit=mini.split(":");
                    String[] sedansplit=sedan.split(":");
                    String[] hatchbacksplit=hatchback.split(":");
                    String[] suvsplit=suv.split(":");
                minibasecost= Double.parseDouble(minisplit[0]);
               miniaccost= Double.parseDouble(minisplit[1]);
                mininonaccost= Double.parseDouble(minisplit[2]);
               sedanbasecost= Double.parseDouble(sedansplit[0]);
                sedanaccost= Double.parseDouble(sedansplit[1]);
                sedannonaccost= Double.parseDouble(sedansplit[2]);
                hatchbackbasecost= Double.parseDouble(hatchbacksplit[0]);
               hatchbackaccost= Double.parseDouble(hatchbacksplit[1]);
                hatchbacknonaccost= Double.parseDouble(hatchbacksplit[2]);
              suvbasecost= Double.parseDouble(suvsplit[0]);
               suvaccost= Double.parseDouble(suvsplit[1]);
            suvnonaccost= Double.parseDouble(suvsplit[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        singleton.getInstance(getActivity()).addtoRequestqueue(stringRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            final double latitude = 20.5937;
            double longitude = 78.9629;
            googleMap.setMyLocationEnabled(true);
        //    drawCircle(new LatLng(latitude,longitude));
//            Circle circle = mMap.addCircle(new CircleOptions()
//                    .center(new LatLng(latitude, longitude))
//                    .radius(1000)
//                    .strokeColor(Color.RED)
//                    .fillColor(Color.BLUE));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 4.0f));
            locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            if(locationButton!=null)
            {
                locationButton.setVisibility(View.INVISIBLE);
            }
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    pickupsplit=marker.getPosition().latitude+","+marker.getPosition().longitude;
                }
            });

        }
        catch (Exception e)
        {

        }
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(getContext(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }
    public String getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            return location.getLatitude()+","+location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void showBottomSheet(View view)
    {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)view. findViewById(R.id.main_content);
// The View with the BottomSheetBehavior
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);

        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                Log.e("onStateChanged", "onStateChanged:" + newState);
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                  behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
                Log.e("onSlide", "onSlide");
            }
        });

        behavior.setPeekHeight(450);
        selectCar(bottomSheet);
    }

    private void selectCar(View view) {
        Button submit;
        final ImageView mini,sedan,hatchback,suv;
        mini=view.findViewById(R.id.mini);
        sedan=view.findViewById(R.id.sedan);
        hatchback=view.findViewById(R.id.hatchback);
        suv=view.findViewById(R.id.suv);
        submit=view.findViewById(R.id.submit);
        mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartype=1;
                mini.setImageResource(R.drawable.car5);
                hatchback.setImageResource(R.drawable.car2);
                suv.setImageResource(R.drawable.car3);
                sedan.setImageResource(R.drawable.car4);
                asktypeofcar();

            }
        });
        sedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartype=2;
            mini.setImageResource(R.drawable.car1);
            hatchback.setImageResource(R.drawable.car3);
            suv.setImageResource(R.drawable.car4);
            sedan.setImageResource(R.drawable.car6);
            asktypeofcar();
            }
        });
        hatchback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartype=3;
                mini.setImageResource(R.drawable.car1);
                hatchback.setImageResource(R.drawable.car7);
                suv.setImageResource(R.drawable.car4);
                sedan.setImageResource(R.drawable.car2);
            asktypeofcar();
            }
        });
        suv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartype=4;
                mini.setImageResource(R.drawable.car1);
                hatchback.setImageResource(R.drawable.car3);
                suv.setImageResource(R.drawable.car8);
                sedan.setImageResource(R.drawable.car2);

            }

        });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(drop.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Please enter the drop location",Toast.LENGTH_LONG).show();
                    return;
                }
                //String pickupsplit = getLocationFromAddress(pickup.getText().toString());
               // String pickupsplit=latitude+","+longitude;
                String dropsplit = getLocationFromAddress(drop.getText().toString());
                String[] split = pickupsplit.split(",");
                String[] split1 = dropsplit.split(",");
                LatLng pickuplatlng = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
                LatLng droplatlng = new LatLng(Double.parseDouble(split1[0]), Double.parseDouble(split1[1]));
                double km = SphericalUtil.computeDistanceBetween(pickuplatlng, droplatlng);
                Log.e("kmmmm",""+km);
                km=km/1000;
                pickup_point=getAddressFromLocation(Double.parseDouble(latitude),Double.parseDouble(longitude));
                drop_point=drop.getText().toString();
                if(cartype==1)
                {

                    double cost=costformini(km);
                    car_type="mini";
                    distance= String.valueOf((int)km);
                finalVerification(cost);
                }
                else if(cartype==2)
                {
                    double cost=costforsedan(km);
                    distance= String.valueOf((int)km);
                    car_type="sedan";
finalVerification(cost);
                    Log.e("costforsedan",""+cost);
                }
                else if(cartype==3)
                {
                    double cost=costforhatchback(km);
                    distance= String.valueOf((int)km);
                    car_type="hatchback";
                finalVerification(cost);
                    Log.e("costforhatchback",""+cost);
                }
                else if(cartype==4)
                {
                   double cost= costforsuv(km);
                    distance= String.valueOf((int)km);
              car_type="suv";
              finalVerification(cost);
                    Log.e("costforsuv",""+cost);
                }

            }
        });
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

    private void finalVerification(final double farecost) {
        TextView fare;
        final int finalfare=(int)farecost;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        alertDialog = null;
        // ...Irrelevant code for customizing the buttons and title

        final LayoutInflater inflater = this.getLayoutInflater();
Button submit;

        final View dialogView= inflater.inflate(R.layout.activity_fare, null);
        dialogBuilder.setView(dialogView);
        fare=dialogView.findViewById(R.id.fare);
        submit=dialogView.findViewById(R.id.submit);
        fare.setText("Amount to pay\n\t\t\t\t\t\t\t\t\t\t\t"+finalfare);
        //final View[] view = new View[1];
        submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(final View v) {
        Random r = new Random();
        final String randomNumber = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));
            cost= String.valueOf(finalfare);
    String url="http://fundevelopers.tech/Taxi/Booking/local_trip.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
Log.e("responsebookings",response);
if(response.equals("success"))
{
    final LayoutInflater factory = getLayoutInflater();

    final View textEntryView = factory.inflate(R.layout.content_navigation_drawer, null);

   final NavController navController= Navigation.findNavController(navigation_view);
navController.navigate(R.id.wait);
    Toast.makeText(getContext(),"Successfully Booked",Toast.LENGTH_LONG).show();
    alertDialog.dismiss();


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
                params.put("trip_type","Local");
                params.put("car_type",car_type);
                params.put("distance",distance);
                params.put("cost",cost);
                params.put("pickup_point",pickup_point);
                params.put("drop_point",drop_point);
                params.put("mobile_no",mobile_no);
                params.put("otp",randomNumber);
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                return params;
            }
        };
        singleton.getInstance(getActivity()).addtoRequestqueue(stringRequest);
    }
});
        alertDialog=dialogBuilder.create();

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
public String getAddressFromLocation(double latitude,double longitude){
    Geocoder geocoder;
    List<Address> addresses = null;
    geocoder = new Geocoder(getContext(), Locale.getDefault());

    try {
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
    } catch (IOException e) {
        e.printStackTrace();
    }

    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    String city = addresses.get(0).getLocality();
    String state = addresses.get(0).getAdminArea();
    String country = addresses.get(0).getCountryName();
    String postalCode = addresses.get(0).getPostalCode();
    String knownName = addresses.get(0).getFeatureName();
return city;
    }
    private void asktypeofcar() {
        TextView ac,nonac;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        alertDialog = null;
        // ...Irrelevant code for customizing the buttons and title

        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView= inflater.inflate(R.layout.typeofcar, null);
        dialogBuilder.setView(dialogView);
        ac=dialogView.findViewById(R.id.ac);
        nonac=dialogView.findViewById(R.id.nonac);

        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acornonac="ac";
if(alertDialog!=null)
{
    alertDialog.dismiss();
}
            }
        });
        nonac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acornonac="nonac";
                alertDialog.dismiss();
            }
        });
        alertDialog=dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    public double costforsedan(double km) {
        double base_cost=sedanbasecost;
        double final_cost=0;
        if(acornonac!=null)
        {
            if(acornonac.equals("ac"))
            {
                if(km<=3)
                {
                    final_cost= base_cost;
                }
                else if(km>3)
                {
                    final_cost=base_cost+((km-3)*10)+sedanaccost;
                }
                //double km_cost=km*12.50;
                //double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                if(km<=3)
                {
                    final_cost= base_cost;
                }
                else if(km>3)
                {
                    final_cost=(base_cost+((km-3)*10))+sedannonaccost;
                }
                //       double km_cost=km*11.50;
                //     double final_cost_non_ac=base_cost+km_cost;
                return final_cost;
            }

        }
        else
        {
            Toast.makeText(getContext(),"select ac or non ac",Toast.LENGTH_LONG).show();
        }
        return 0.0;

    }

    public double costformini(double km) {
//double base_cost=165;
        double base_cost=minibasecost;
        double final_cost=0;
        if(acornonac!=null)
        {
            if(acornonac.equals("ac"))
            {
                if(km<=3)
                {
                    final_cost= base_cost;
                }
                else if(km>3)
                {
                    final_cost=(base_cost+((km-3)*10))+miniaccost;
                }
                //double km_cost=km*12.50;
                //double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                if(km<=3)
                {
                    final_cost= base_cost;
                }
                else if(km>3)
                {
                    final_cost=(base_cost+((km-3)*10))+mininonaccost;
                }
                //       double km_cost=km*11.50;
                //     double final_cost_non_ac=base_cost+km_cost;
                return final_cost;
            }

        }
        else
        {
            Toast.makeText(getContext(),"select ac or non ac",Toast.LENGTH_LONG).show();
        }
        return 0.0;
    }
    public double costforhatchback(double km)
    {
        double base_cost=hatchbackbasecost;
        double final_cost=0;
        if(acornonac!=null)
        {
            if(acornonac.equals("ac"))
            {
                if(km<=3)
                {
              final_cost= base_cost;
                }
                else if(km>3)
                {
                    final_cost=(base_cost+((km-3)*10))+hatchbackaccost;
                }
                //double km_cost=km*12.50;
                //double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                if(km<=3)
                {
                    final_cost= base_cost;
                }
                else if(km>3)
                {
                    final_cost=base_cost+((km-3)*10)+hatchbacknonaccost;
                }
         //       double km_cost=km*11.50;
           //     double final_cost_non_ac=base_cost+km_cost;
                return final_cost;
            }

        }
        else
        {
            Toast.makeText(getContext(),"select ac or non ac",Toast.LENGTH_LONG).show();
        }
        return 0.0;

    }
    public double costforsuv(double km)
    {
        double base_cost=suvbasecost;
double final_cost=0;
        if(km<3)
{
    final_cost=base_cost;
}
        else if(km>3)
        {
            final_cost=(base_cost+((km-3)*10))+suvaccost;
        }

                return final_cost;
    }

    @Override
    public void onLocationChanged(Location location) {
latitude= String.valueOf(location.getLatitude());
longitude= String.valueOf(location.getLongitude());
double lat= Double.parseDouble(latitude);
       double lang= Double.parseDouble(longitude);
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 14.0f));
//Toast.makeText(getContext(),"Location updated",Toast.LENGTH_LONG).show();
    locationManager.removeUpdates(this);
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


}
