package edu.dartmouthcs65.museumtour;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.dartmouthcs65.museumtour.RoomKit.BeaconTracker;
import edu.dartmouthcs65.museumtour.RoomKit.Classifier;
import edu.dartmouthcs65.museumtour.RoomKit.RoomKit;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements Classifier.Listener {

    // Action bar
    Toolbar myActBr;

    //Fragments
    Fragment mainMap;

    //Fragment manager
    FragmentManager mainFM;

    DatabaseReference dRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

//        myActBr = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(myActBr);

        //Create main map fragment
        mainMap = new MainMapFragment();

        // Instantiate fragment manager
        mainFM = getFragmentManager();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        dRef = FirebaseDatabase.getInstance().getReference();

        dRef.child("kemeny").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Update to load on startup

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        FragmentTransaction initialTrans = mainFM.beginTransaction();
        initialTrans.replace(R.id.main_fragment, mainMap);
        initialTrans.commit();

        Boolean hasCompletedIntro = getSharedPreferences(Globals.SHARED_PREF, 0)
                .getBoolean(Globals.INTRO_COMPLETED_KEY, false);
        // If user has not seen intro, show it
        Log.d("hasCompletedIntro", hasCompletedIntro ? "YES": "NO");
        if (!hasCompletedIntro) {
            Intent introIntent = new Intent(this, IntroductionActivity.class);
            startActivity(introIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (RoomKit.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            // Handled
        }else{
            // Not handled
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RoomKit.checkPermissions(this);
        BeaconTracker tracker = BeaconTracker.getInstance(this);
        tracker.setOnClassifyListener(this);
        tracker.setRangeFrequency(60); // Hz
        tracker.start();
    }

    @Override
    public void onClassify(Integer roomIndex, String room) {
        Log.d("classify", room);
        Toast.makeText(this, "CLASSIFIED ROOM: " + room, Toast.LENGTH_SHORT).show();
    }
}
