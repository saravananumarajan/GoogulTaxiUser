package com.googultaxi.taxiuser.NavigationActivity.ui.trip;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.googultaxi.taxiuser.R;
import com.googultaxi.taxiuser.singleton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LongTrip extends Fragment {
    EditText pickup_point, drop_point, pickup_date, pickup_time;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigation_view=view;
        mobile_no=getActivity().getIntent().getExtras().getString("mobile_no");
        pickup_point = view.findViewById(R.id.pickup);
        drop_point = view.findViewById(R.id.drop);
        pickup_date = view.findViewById(R.id.pickup_date);
        pickup_time = view.findViewById(R.id.pickup_time);
selectCar(view);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pickupsplit = getLocationFromAddress(pickup_point.getText().toString());
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

                    double cost=localTrip.costformini(km);
                    car_type="mini";
                    distance= String.valueOf((int)km);
                    finalVerification(cost);
                }
                else if(cartype==2)
                {
                    double cost=localTrip.costforsedan(km);
                    distance= String.valueOf((int)km);
                    car_type="sedan";
                    finalVerification(cost);
                    Log.e("costforsedan",""+cost);
                }
                else if(cartype==3)
                {
                    double cost=localTrip.costforhatchback(km);
                    distance= String.valueOf((int)km);
                    car_type="hatchback";
                    finalVerification(cost);
                    Log.e("costforhatchback",""+cost);
                }
                else if(cartype==4)
                {
                    double cost= localTrip.costforsuv(km);
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
        alertDialog.show();

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
                String url="http://192.168.43.83/Taxi/Booking/long_trip.php";
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
                        params.put("trip_type","Long");
                        params.put("car_type",car_type);
                        params.put("distance",distance);
                        params.put("cost",cost);
                        params.put("pickup_point",pickup_point.getText().toString());
                        params.put("drop_point",drop_point.getText().toString());
                        params.put("mobile_no",mobile_no);
                        params.put("date",pickup_date.getText().toString());
                        params.put("time",pickup_time.getText().toString());
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

}
