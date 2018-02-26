package edu.dartmouthcs65.museumtour;

import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    // Action bar
    Toolbar myActBr;

    //Fragments
    Fragment mainMap;

    //Fragment manager
    FragmentManager mainFM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myActBr = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myActBr);

        //Create main map fragment
        mainMap = new MainMapFragment();

        // Instantiate fragment manager
        mainFM = getFragmentManager();


        FragmentTransaction initialTrans = mainFM.beginTransaction();
        initialTrans.replace(R.id.main_fragment, mainMap);
        initialTrans.commit();

    }


    public boolean onTouch(View myView, MotionEvent myME) {

        /* IN PROGRESS -- Andrew
        // On down touch, ensure hitbox image is brought to front.
        // On up touch, test color of hit location. If color matches hitbox of a room, select that
        // room.

        // Get x and y click location
        final int xCoord = (int) myME.getX();
        final int yCoord = (int) myME.getY();

        // get the motion event action, and test if up or down press
        final int meAction = myME.getAction();
        if (meAction == MotionEvent.ACTION_DOWN){

        } else if (meAction == MotionEvent.ACTION_UP) {

        }
        */

        return true;
    }

}
