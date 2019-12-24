package com.googultaxi.taxiuser.NavigationActivity.ui.trip;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class LongTrip extends Fragment implements OnMapReadyCallback {
    EditText  drop_point, pickup_date, pickup_time;
   ImageButton pickup_point;
    LatLng pickup, drop;
    String cost;
    String car_type=null;
String pickup_point1,drop_point1;
    String distance;
    Button submit;
    AlertDialog alertDialog;
public static int cartype;
    public static String acornonac;
String mobile_no;
View navigation_view;
GoogleMap mMap;
String pickupsplit;
TextView pickup_text;
Bundle saved;
MapView mMapView;
    double minibasecost,miniaccost,mininonaccost;
    double sedanbasecost,sedanaccost,sedannonaccost;
    double hatchbackbasecost,hatchbackaccost,hatchbacknonaccost;
    double suvbasecost,suvaccost,suvnonaccost;

  public LongTrip() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_long_trip, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
getCost();
       NavController navController = Navigation.findNavController(view);
saved=savedInstanceState;
        navigation_view=view;
        mobile_no=getActivity().getIntent().getExtras().getString("mobile_no");
        pickup_point = view.findViewById(R.id.pickup);
        drop_point = view.findViewById(R.id.drop);
        pickup_date = view.findViewById(R.id.pickup_date);
        pickup_time = view.findViewById(R.id.pickup_time);
        pickup_text=view.findViewById(R.id.pickup_text);
selectCar(view);
pickup_point.setOnClickListener(new View.OnClickListener() {
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
                pickupsplit=LocalTrip.latitude+","+LocalTrip.longitude;
                alertDialog.dismiss();
                pickup_text.setText("current location");
            }
        });
        anotherloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                alertDialog.dismiss();
                final LayoutInflater inflater = getLayoutInflater();
                Button submit,close;
                final View dialogView= inflater.inflate(R.layout.get_another_location_long, null);
               close=dialogView.findViewById(R.id.close);
               close.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       alertDialog.dismiss();
                   }
               });
                mMapView = (MapView) dialogView.findViewById(R.id.mapView);
                mMapView.onCreate(saved);
                mMapView.onResume(); // needed to get the map to display immediately
                try {
                    MapsInitializer.initialize(getActivity().getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mMapView.getMapAsync((new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        final double latitude = 20.5937;
                        double longitude = 78.9629;
                        //googleMap.setMyLocationEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 4.0f));

                    }
                }));

                final EditText nearestloc=dialogView.findViewById(R.id.nearestloc);
                submit=dialogView.findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickup_text.setText(nearestloc.getText().toString());
                        String getloc=getLocationFromAddress(nearestloc.getText().toString());
                        String[] split=getloc.split(",");
                        //alertDialog.dismiss();
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
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String pickupsplit = getLocationFromAddress(pickup_point.getText().toString());
                 pickupsplit=LocalTrip.latitude+","+LocalTrip.longitude;
                String dropsplit = getLocationFromAddress(drop_point.getText().toString());
                String[] split = pickupsplit.split(",");
                String[] split1 = dropsplit.split(",");
                LatLng pickuplatlng = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
                LatLng droplatlng = new LatLng(Double.parseDouble(split1[0]), Double.parseDouble(split1[1]));
                double km = SphericalUtil.computeDistanceBetween(pickuplatlng, droplatlng);
                Log.e("kmmmm",""+km);
                km=km/1000;
                LocalTrip localTrip=new LocalTrip();
                if(cartype==1)
                {

                    double cost=costformini(km);
               Log.e("costt",""+cost);
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
        pickup_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    final int months = calendar.get(Calendar.MONTH);
                    final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    if (day >= dayOfMonth && months>=month) {
                                        pickup_date.setText(day + ":" + month + ":" + year);
                                    } else {
                                        Toast.makeText(getContext(), "Select the correct date", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, year, months, dayOfMonth);
                    datePickerDialog.setTitle("Select the date");
                    datePickerDialog.show();
                }
            }
        });
        pickup_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            pickup_time.setText(hourOfDay + ":" + minute);
                        }
                    }, hour, minute, true);
                    timePickerDialog.setTitle("Select the time");
                    timePickerDialog.show();
                }
            }
        });

    }

    public String getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            return location.getLatitude() + "," + location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void selectCar(View view) {
        Button submit;
        final ImageView mini, sedan, hatchback, suv;
        mini = view.findViewById(R.id.mini);
        sedan = view.findViewById(R.id.sedan);
        hatchback = view.findViewById(R.id.hatchback);
        suv = view.findViewById(R.id.suv);
        mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartype = 1;
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
                cartype = 2;
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
                cartype = 3;
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
                cartype = 4;
                mini.setImageResource(R.drawable.car1);
                hatchback.setImageResource(R.drawable.car3);
                suv.setImageResource(R.drawable.car8);
                sedan.setImageResource(R.drawable.car2);
            }

        });

    }
    private void asktypeofcar ()
    {
        TextView ac, nonac;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        alertDialog = null;
        // ...Irrelevant code for customizing the buttons and title

        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.typeofcar, null);
        dialogBuilder.setView(dialogView);
        ac = dialogView.findViewById(R.id.ac);
        nonac = dialogView.findViewById(R.id.nonac);

        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acornonac="ac";
                LocalTrip.acornonac = "ac";
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
        nonac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acornonac="nonac";
                LocalTrip.acornonac = "nonac";
                alertDialog.dismiss();
            }
        });

        alertDialog = dialogBuilder.create();
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
                final_cost=km*sedanaccost+base_cost;
                //double km_cost=km*12.50;
                //double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                final_cost=km*sedannonaccost+base_cost;
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
               final_cost=km*miniaccost+base_cost;
                //double km_cost=km*12.50;
                //double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                final_cost=km*mininonaccost+base_cost;
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
                final_cost=km*hatchbackaccost+base_cost;
                //double km_cost=km*12.50;
                //double final_cost=base_cost+km_cost;
                return final_cost;
            }
            else if(acornonac.equals("nonac"))
            {
                final_cost=km*hatchbacknonaccost+base_cost;
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
        final_cost=km*suvaccost+base_cost;
        return final_cost;
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
        fare.setText("Amount to pay\n\t\t\t\t\t\t\t\t\t\t\t"+(finalfare*2));
        //final View[] view = new View[1];
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProgressDialog progressDialog=new ProgressDialog(getContext());
                progressDialog.setTitle("Please Wait");
                progressDialog.show();
                Random r = new Random();
                final String randomNumber = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));

                cost= String.valueOf(finalfare);
                String url="http://fundevelopers.tech/Taxi/Booking/long_trip.php";
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("responsebookings",response);
                        if(response.equals("success"))
                        {
                            Toast.makeText(getActivity(),"Successfully Booked",Toast.LENGTH_LONG).show();
                            final LayoutInflater factory = getLayoutInflater();

                            final View textEntryView = factory.inflate(R.layout.content_navigation_drawer, null);

                            final NavController navController= Navigation.findNavController(navigation_view);
                            navController.navigate(R.id.wait);
                            Toast.makeText(getContext(),"Successfully Booked",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
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
                        params.put("trip_type","Long");
                        params.put("car_type",car_type);
                        params.put("distance",distance);
                        params.put("cost",cost);
                        params.put("pickup_point", getAddressFromLocation(Double.parseDouble(LocalTrip.latitude),Double.parseDouble(LocalTrip.longitude)));
                        params.put("drop_point",drop_point.getText().toString());
                        params.put("mobile_no",mobile_no);
                        params.put("date",pickup_date.getText().toString());
                        params.put("time",pickup_time.getText().toString());
                        params.put("otp",randomNumber);
                        params.put("latitude",LocalTrip.latitude);
                        params.put("longitude",LocalTrip.longitude);
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
    private void getCost() {
        String url="http://fundevelopers.tech/Taxi/Booking/get_long_cost.php";
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

}
