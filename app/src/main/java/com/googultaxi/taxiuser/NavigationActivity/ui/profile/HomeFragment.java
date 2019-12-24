package com.googultaxi.taxiuser.NavigationActivity.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.googultaxi.taxiuser.Otp.RequestMobileNo;
import com.googultaxi.taxiuser.R;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
TextView user_id,mobile_no;
Button changeno;
FirebaseAuth mAuth;
SwitchCompat switchCompat;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_id=view.findViewById(R.id.username);
        mobile_no=view.findViewById(R.id.mobile_no);
        changeno=view.findViewById(R.id.change_no);
        String userid=getActivity().getIntent().getExtras().getString("user_id");
        String mobileno=getActivity().getIntent().getExtras().getString("mobile_no");
        user_id.setText(userid);
        mobile_no.setText(mobileno);
        mAuth=FirebaseAuth.getInstance();

        changeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Change Mobile No")
                        .setMessage("Are you sure you want to change the mobile no?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                FirebaseAuth.getInstance().signOut();
                                SharedPreferences prefs = getActivity().getSharedPreferences("USER_PREF",
                                        Context.MODE_PRIVATE);
                                prefs.edit().clear();
                                Toast.makeText(getContext(),"Successfully signed out",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getContext(), RequestMobileNo.class);
                                startActivity(intent);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }
}