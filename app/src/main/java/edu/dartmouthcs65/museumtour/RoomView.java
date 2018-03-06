package edu.dartmouthcs65.museumtour;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

public class RoomView extends AppCompatActivity implements View.OnTouchListener {
    int roomNum, lastExIndex;
    StorageReference rmImgRef, rmHitRef;
    ImageView rmView, rmHitView, rmErr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        // Load main imageview
        rmView = (ImageView) findViewById(R.id.rmImg);
        rmHitView = (ImageView) findViewById(R.id.rmHitbox);
        rmErr = (ImageView) findViewById(R.id.err);

        // Get the number of the room to be displayed
        Intent myIntent = getIntent();
        roomNum = myIntent.getIntExtra(Globals.ROOM_NUM_KEY, 0);


        if (roomNum != 0) {

            // If room number is valid, load heatmap and image
            Log.d("Room", "Loading images for room " + String.valueOf(roomNum));
            rmHitRef = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/RoomHitboxes/" + (String.valueOf(roomNum)) + ".png");
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(rmHitRef)
                    .dontAnimate()
                    .into(rmHitView);
            rmImgRef = MainActivity.storage.getReferenceFromUrl("gs://cs65-museumtour.appspot.com/RoomFloorplans/" + (String.valueOf(roomNum)) + ".png");
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(rmImgRef)
                    .into(rmView);
            rmView.setVisibility(View.VISIBLE);

        }
    }


    public boolean onTouch(View myView, MotionEvent myME) {
        Log.d("Touch", "Touch registered");

        int[] colorArray = {3815331, 2201171, 10179040, 15423319};

        int exIndex = 0;  //initialize exhibit Index to default(0, no exhibit)
        if (rmHitView != null) {
            // Convert image to bitmap
            rmHitView.setDrawingCacheEnabled(true);
            Bitmap hitboxBM = Bitmap.createBitmap(rmHitView.getDrawingCache());
            if (hitboxBM != null) {
                // find index of room touched
                exIndex = Hitbox.FindItemClicked(myME, hitboxBM, colorArray);
            } else return false;
        }


        // get the motion event action, and test if up or down press
        final int meAction = myME.getAction();
        if (meAction == MotionEvent.ACTION_DOWN) {
            // Down press, remember exhibit index
            lastExIndex = exIndex;
        } else if (meAction == MotionEvent.ACTION_UP) {
            // Up press, if lastExIndex matches current exIndex, register valid roompress
            if (lastExIndex == exIndex) {
                // TODO: Launch Exhibit Vew
            }
        }
        return true;

    }

}
