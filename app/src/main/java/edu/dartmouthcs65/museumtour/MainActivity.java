package edu.dartmouthcs65.museumtour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.fabric.sdk.android.Fabric;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String WORK_KEY = "WORK_KEY";
    public static final String IS_ART_KEY = "ART_OR_EXHIBIT";

    // Action bar
    Toolbar myActBr;

    //Fragments
    Fragment mainMap;

    //Fragment manager
    FragmentManager mainFM;

    DatabaseReference dRef;

    public static FirebaseStorage storage;
    MuseumRoom[] rooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(false);
        dRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        setContentView(R.layout.activity_main);

//        myActBr = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(myActBr);

        //Create main map fragment
        mainMap = new MainMapFragment();

        // Instantiate fragment manager
        mainFM = getFragmentManager();

        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                // Includes index because you need a zero for null room
//                rooms = new MuseumRoom[(int) dataSnapshot.getChildrenCount()];
//                for (DataSnapshot room: dataSnapshot.getChildren()) {
//                    Log.d("ROOMNAME: ", room.getKey());
//                    WorkDisplayed[] roomWorks = new WorkDisplayed[(int) room.getChildrenCount()];
//                    roomWorks[0] = null;
//                    for (DataSnapshot work : room.getChildren()){
//                        if (!Objects.equals(work.getKey(), "index") && ){
//                            roomWorks[Math.toIntExact((long) work.child("Index").getValue())] =
//                                    new WorkDisplayed(work.getKey(),
//                                            (String) work.child("artist").getValue(),
//                                            (String) work.child("year").getValue(),
//                                            (String) work.child("description").getValue(),
//                                            (String) work.child("photoURL").getValue());
//                            Log.d("WORKNAME: ", work.getKey());
//                        }
//                    }
//
//                    Long roomIndex = (Long) room.child("index").getValue();
//                    Integer indexRoom = Math.toIntExact(roomIndex);
//                    rooms[indexRoom] =  new MuseumRoom(room.getKey(), roomWorks);
//                }
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

}
