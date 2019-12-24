package com.googultaxi.taxiuser.NavigationActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.florent37.tutoshowcase.TutoShowcase;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.googultaxi.taxiuser.R;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.Slide;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;


public class NavigationDrawer extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
View navheader;
    boolean isTouched = false;
public static SwitchCompat switchCompat;
ActionBarDrawerToggle actionBarDrawerToggle;
ViewGroup view;
View v;
TextView textView;
ImageView imageView;
BottomSheetDialog bottomSheetDialog;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
    //    ActionBar actionBar=getActionBar();
      //  actionBar.setDisplayHomeAsUpEnabled(false);
SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
boolean switchvalue=sharedPreferences.getBoolean("switch",false);
if(!switchvalue) {
    showCase();
}
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        textView=findViewById(R.id.nav_text);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
      imageView=navigationView.getHeaderView(0).findViewById(R.id.profile);
     // Picasso.with(getApplicationContext()).load(R.drawable.logoicon).placeholder(R.drawable.logoicon).into(imageView);
        //toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // This callback will only be called when MyFragment is at least Started.

      switchCompat=findViewById(R.id.triptype);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.profile, R.id.local_trip,
                 R.id.trackdriverfragment, R.id.about_us,R.id.contact_us)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull final NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if(destination.getId()==R.id.profile)
                {
                    switchCompat.setVisibility(View.GONE);
                    textView.setText("Profile");
                }
                else if(destination.getId()==R.id.trip)
                {

                    textView.setText("Trip");
                    switchCompat.setChecked(false);
                    switchCompat.setVisibility(View.VISIBLE);
                    if(!switchCompat.isChecked())
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                navController.navigate(R.id.local_trip);
                            }
                        },100);

                    }
                    else
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                navController.navigate(R.id.long_trip);
                            }
                        },100);
                    }
                    switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                switchCompat.setText("Long");
                                navController.navigate(R.id.long_trip);
                            } else {
                                switchCompat.setText("Local");
                                navController.navigate(R.id.local_trip);
                            }
                        }

                    });
                }
                else if(destination.getId()==R.id.bookings)
                {
                    switchCompat.setVisibility(View.GONE);
                    textView.setText("Bookings");
                }
            }
        });
    }
public void showCase()
{
    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor=sharedPreferences.edit();
    editor.putBoolean("switch",true);
    editor.apply();
    new GuideView.Builder(this)
            .setTitle("Trip")
            .setContentText("Click switch for getting the long trip")
            .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
            .setTargetView(findViewById(R.id.triptype))
            .setContentTextSize(12)//optional
            .setTitleTextSize(14)//optional
            .build()
            .show();
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.local_trip:
                switchCompat.setChecked(false);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

public void changeFragment(Fragment fragment)
{
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.frame, fragment);
    fragmentTransaction.commit();
}
}
