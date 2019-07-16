package com.example.autonomoussystemthesis;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;


// Starting an App in the Background
// The app launches itself when it first sees an beacon region.
// In order for this to work, the app must have been launched by the user at least once

// We create class that extends Application and then we declare this in our AndroidManifest.xml,
// where we declares a custom Application class,
// and a background launch activity marked as “singleInstance

// This class launch the MainActivity as soon as any beacon is seen
public class AutonomousSystemApp extends Application implements BootstrapNotifier {
    private static final String TAG = "AutonomousSystemApp";
    private RegionBootstrap regionBootstrap;

    //             Auto Battery Saving
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MainActivity monitoringActivity = null;
    private String cumulativeLog = "";

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AutonomousSystemApp started up");
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().clear();

//         If we have a proprietary beacons, we find "setBeaconLayout" and get the proper expression.
//        beaconManager.getBeaconParsers().add(new BeaconParser()
//            .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
//        in our case MacBook is served as Beacon, so for iBeacons it is 0215
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        BeaconManager.setDebug(true);

//        wake up the app when any beacon is seen
//        TODO: Region("backgroundRegion",
        Region region = new Region(".boostrapRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

//         enables auto battery saving of about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);


//      When run first time -> MainActivity
//      When second.. -> ShowSavedData
//      run app only once for the fist time
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity
            startActivity(new Intent(AutonomousSystemApp.this, MainActivity.class));
            Toast.makeText(AutonomousSystemApp.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        } else {
            startActivity(new Intent(AutonomousSystemApp.this, ShowAllDistances.class));
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

    }

//    public void disableMonitoring() {
//        if (regionBootstrap != null) {
//            regionBootstrap.disable();
//            regionBootstrap = null;
//        }
//    }

//    public void enableMonitoring() {
////        Region region = new Region("backgroundRegion",
////                null, null, null);
//        Region region = new Region("backgroundRegion",
//                Identifier.parse("CE:3C:09:30:99:B0"),
//                Identifier.parse("C8:CB:EC:A6:32:55"),
//                Identifier.parse("F2:E4:35:BA:FB:D9"));
//        regionBootstrap = new RegionBootstrap(this, region);
//    }


    @Override
    public void didEnterRegion(Region arg0) {
        Log.d(TAG, "Got a didEnterRegion call");
//         This call to disable will make it so the activity below only gets launched the first
//         time a beacon is seen (until the next time the app is launched)
//         if you want the Activity to launch every single time beacons come into view, remove this call.
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "automatically launch MainActivity");
            Intent intent = new Intent(this, MainActivity.class);
//            In the AndroidManifest.xml definition of this activity,
//            we have set android:launchMode="singleInstance" otherwise we will get two instances
//            created when a user launches the activity manually and it gets launched from here.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (monitoringActivity != null) {
                logToDisplay("I see a beacon again");
            } else {
                Log.d(TAG, "Sending notification.");
                sendNotification();
            }
        }
    }

    @Override
    public void didExitRegion(Region region) {
        logToDisplay("I no longer see a beacon.");
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        logToDisplay("Current region state is: " + (state == 1 ? "INSIDE" : "OUTSIDE (" + state + ")"));
    }

    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "")
                        .setContentTitle("Beacon Reference Application")
                        .setContentText("An beacon is nearby.")
                        .setSmallIcon(R.drawable.ic_launcher_background);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setMonitoringActivity(MainActivity activity) {
        this.monitoringActivity = activity;
    }

    private void logToDisplay(String line) {
        cumulativeLog += (line + "\n");
    }

//    public String getLog() {
//        return cumulativeLog;
//    }

}
