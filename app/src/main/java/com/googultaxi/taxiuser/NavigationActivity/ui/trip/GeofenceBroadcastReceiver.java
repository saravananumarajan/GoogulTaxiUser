package com.googultaxi.taxiuser.NavigationActivity.ui.trip;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;
import com.googultaxi.taxiuser.MainActivity;
import com.googultaxi.taxiuser.R;

import java.util.List;

public class GeofenceBroadcastReceiver extends IntentService {
    private static final String TAG = "GeofenceTransitions";

    public GeofenceBroadcastReceiver() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Goefencing Error " + geofencingEvent.getErrorCode());
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            //   showNotification("Entered", "Entered the Location");
            Log.e("geofence","entered");
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // showNotification("Exited", "Exited the Location");
        } else {
            //showNotification("Error", "Error");
        }


    }
}
