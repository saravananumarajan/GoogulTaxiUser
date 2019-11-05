package com.googultaxi.taxiuser.NavigationActivity.ui.trip;


import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.SphericalUtil;
import com.googultaxi.taxiuser.MainActivity;
import com.googultaxi.taxiuser.NavigationActivity.NavigationDrawer;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocalTrip extends Fragment implements OnMapReadyCallback {
BottomSheetDialog bottomSheetDialog;
GoogleMap mMap;
AlertDialog alertDialog;
public static String acornonac=null;
public static int cartype=0;
View navigation_view;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
EditText pickup,drop;
public static String pickup_point,drop_point,trip_type,car_type,distance,cost,mobile_no;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
MarkerOptions pickupmarker,dropmarker;
double km;
    public LocalTrip() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
navigation_view=view;
        checkPermissions();
        showBottomSheet(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pickup=view.findViewById(R.id.pickup);
        drop=view.findViewById(R.id.drop);
        mobile_no=getActivity().getIntent().getExtras().getString("mobile_no");


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

            // GoogleMap googleMap;
            mMap = googleMap;
            final double latitude = 20.5937;
            double longitude = 78.9629;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 4.0f));

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            final int[] count = {0};
            pickup.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_NEXT) {
                        String location=getLocationFromAddress(pickup.getText().toString());
                 if(location!=null) {
                     String[] split = location.split(",");
                     double latitude = Double.parseDouble(split[0]);
                     double longitude = Double.parseDouble(split[1]);
                     pickupmarker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(pickup.getText().toString());
                     mMap.addMarker(pickupmarker);
                     pickup_point=pickup.getText().toString();
                 }
                    }
                    return false;
                }
            });
            drop.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_NEXT) {
                        String location=getLocationFromAddress(drop.getText().toString());
                        String[] split=location.split(",");
                        double latitude= Double.parseDouble(split[0]);
                        double longitude=Double.parseDouble(split[1]);
                        dropmarker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(drop.getText().toString());
                        mMap.addMarker(dropmarker);
     String distance= String.valueOf(SphericalUtil.computeDistanceBetween(pickupmarker.getPosition(),dropmarker.getPosition()));
 km=Double.parseDouble(distance)*0.001;
Log.e("distancee",""+km);
drop_point=drop.getText().toString();
                    }
                    return false;
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

        behavior.setPeekHeight(430);
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

            cost= String.valueOf(finalfare);
    String url="http://192.168.43.83/Taxi/Booking/local_trip.php";
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
        alertDialog.show();

    }

    public double costforsedan(double km) {
        double base_cost=195;
        if(acornonac!=null)
        {
            if(acornonac.equals("ac"))
            {
                double km_cost=km*11.0;
                double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                double km_cost=km*10.0;
                double final_cost_non_ac=base_cost+km_cost;
                return final_cost_non_ac;
            }

        }
        else
        {
            Toast.makeText(getContext(),"select ac or non ac",Toast.LENGTH_LONG).show();
        }
        return 0.0;

    }

    public double costformini(double km) {
double base_cost=165;
if(acornonac!=null)
{
    if(acornonac.equals("ac"))
    {
        double km_cost=km*10.0;
        double final_cost=base_cost+km_cost;
        return final_cost;
    }
    else if(acornonac.equals("nonac"))
    {
        double km_cost=km*9.0;
        double final_cost_non_ac=base_cost+km_cost;
        return final_cost_non_ac;
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
        double base_cost=250;
        if(acornonac!=null)
        {
            if(acornonac.equals("ac"))
            {
                double km_cost=km*12.50;
                double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                double km_cost=km*11.50;
                double final_cost_non_ac=base_cost+km_cost;
                return final_cost_non_ac;
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
        double base_cost=500;
                double km_cost=km*14.0;
                double final_cost=base_cost+km_cost;
                return final_cost;



    }

}
