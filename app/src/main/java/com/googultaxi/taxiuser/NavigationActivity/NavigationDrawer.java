package com.googultaxi.taxiuser.NavigationActivity;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;

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
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawer extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
View navheader;
    boolean isTouched = false;
SwitchCompat switchCompat;
ActionBarDrawerToggle actionBarDrawerToggle;
ViewGroup view;
TextView textView;
ImageView imageView;
BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        textView=findViewById(R.id.nav_text);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
      imageView=navigationView.getHeaderView(0).findViewById(R.id.profile);
      Picasso.with(getApplicationContext()).load(R.drawable.profile).placeholder(R.drawable.profile).into(imageView);
        //toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

      switchCompat=findViewById(R.id.triptype);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.profile, R.id.trip, R.id.wait,
                R.id.track_another_trip, R.id.remarks, R.id.about_us,R.id.local_trip,R.id.bookings,R.id.long_trip)
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
                    switchCompat.setVisibility(View.VISIBLE);
                    if(!switchCompat.isChecked())
                    {
                        navController.navigate(R.id.local_trip);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
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
